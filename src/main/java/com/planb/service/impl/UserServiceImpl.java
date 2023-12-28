package com.planb.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.planb.constant.UserConstants;
import com.planb.dao.UserMapper;
import com.planb.security.LoginUser;
import com.planb.entity.User;
import com.planb.service.IUserService;
import com.planb.utils.JwtUtil;
import com.planb.utils.Result;
import com.planb.vo.AdminGetPageVo;
import com.planb.vo.UserLoginVo;
import com.planb.vo.UserRegisterVo;
import com.planb.vo.UserUpdateVo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService, UserDetailsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result register(UserRegisterVo userRegisterVo) {
        if (userRegisterVo == null || userRegisterVo.getUsername() == null || userRegisterVo.getPassword() == null) {
            return Result.fail(UserConstants.USERNAME_OR_PASSWORD_EMPTY);
        }
        //判断用户名是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userRegisterVo.getUsername());
        User u = this.getOne(wrapper);
        if (u != null) {
            return Result.fail(UserConstants.USER_ALREADY_EXISTS);
        }
        User user = new User();
        String password = userRegisterVo.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        user.setName(userRegisterVo.getName());
        user.setUsername(userRegisterVo.getUsername());
        user.setPassword(password);
        user.setRole(UserConstants.ROLE_USER);
        user.setEmail(userRegisterVo.getEmail());
        //注册
        this.save(user);
        return Result.ok(UserConstants.REGISTRATION_SUCCESS);
    }

    @Override
    public boolean authorize(Integer userId) {
        User user = this.getById(userId);
        if (user.getRole().equals(UserConstants.ROLE_ADMIN)) {
            return false;
        }
        user.setRole(UserConstants.ROLE_ADMIN);
        this.updateById(user);
        return true;
    }

    @Override
    public String login(UserLoginVo userLoginVo) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginVo.getUsername(), userLoginVo.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException(UserConstants.INVALID_USERNAME_OR_PASSWORD);
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        User user = loginUser.getUser();
        String userId = String.valueOf(user.getUserId());
        String token = JwtUtil.createJWT(userId);

        String loginUserJson = JSONUtil.toJsonStr(loginUser);
        //authenticate存入redis
        String key = UserConstants.LOGIN_PREFIX + userId;
        stringRedisTemplate.opsForValue().set(key, loginUserJson);
        stringRedisTemplate.expire(key, UserConstants.TIME_OUT, TimeUnit.MINUTES);
        //把token响应给前端
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = getOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException(UserConstants.USER_NOT_FOUND);
        }
        // 根据用户查询权限信息 添加到LoginUser中
        String role = user.getRole();
        List<String> list = new ArrayList<>();
        list.add(role);
        return new LoginUser(user, list);
    }


    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = Long.valueOf(loginUser.getUser().getUserId());
        stringRedisTemplate.delete(UserConstants.LOGIN_PREFIX + userId);
    }

    @Override
    public User check() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser();
    }

    @Override
    public void update(UserUpdateVo userUpdateVo) {
        String key = UserConstants.LOGIN_PREFIX + getUserId();
        String loginUserJson = stringRedisTemplate.opsForValue().get(key);
        LoginUser loginUser = JSONUtil.toBean(loginUserJson, LoginUser.class);
        User loginUserUser = loginUser.getUser();
        User user = getById(getUserId());
        if (userUpdateVo.getPassword() != null && !userUpdateVo.getPassword().isEmpty()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bCryptPasswordEncoder.encode(userUpdateVo.getPassword());
            user.setPassword(password);
            loginUserUser.setPassword(password);
        }
        if (userUpdateVo.getName() != null && !userUpdateVo.getName().isEmpty()) {
            user.setName(userUpdateVo.getName());
            loginUserUser.setName(userUpdateVo.getName());
        }
        if (userUpdateVo.getEmail() != null && !userUpdateVo.getEmail().isEmpty()) {
            user.setEmail(userUpdateVo.getEmail());
            loginUserUser.setEmail(userUpdateVo.getEmail());
        }
        if (userUpdateVo.getAvatar() != null && !userUpdateVo.getAvatar().isEmpty()) {
            user.setAvatar(userUpdateVo.getAvatar());
            loginUserUser.setAvatar(userUpdateVo.getAvatar());
        }
        updateById(user);
        loginUser.setUser(loginUserUser);
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        stringRedisTemplate.opsForValue().set(key, jsonStr);
    }

    @Override
    public IPage<User> getUserPage(AdminGetPageVo adminGetPageVo) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(adminGetPageVo.getUsername()), User::getUsername, "%" + adminGetPageVo.getUsername() + "%")
                .like(Strings.isNotEmpty(adminGetPageVo.getName()), User::getName, "%" + adminGetPageVo.getName() + "%")
                .like(Strings.isNotEmpty(adminGetPageVo.getEmail()), User::getEmail, "%" + adminGetPageVo.getEmail() + "%")
                .like(Strings.isNotEmpty(adminGetPageVo.getRole()), User::getRole, "%" + adminGetPageVo.getRole() + "%");
        IPage<User> page = new Page<>(adminGetPageVo.getCurrentPage(), adminGetPageVo.getPageSize());
        userMapper.selectPage(page, lqw);
        return page;
    }

    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser().getUserId();
    }
}
