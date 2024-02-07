package com.planb.service.impl;

import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.planb.constant.UserConstants;
import com.planb.dao.UserMapper;
import com.planb.dto.UserLoginDto;
import com.planb.dto.UserRegisterDto;
import com.planb.dto.UserUpdateDto;
import com.planb.security.LoginUser;
import com.planb.entity.User;
import com.planb.service.IUserService;
import com.planb.utils.JwtUtil;
import com.planb.vo.Result;
import com.planb.dto.AdminGetPageDto;
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

import static com.planb.entity.table.UserTableDef.USER;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result register(UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null || userRegisterDto.getUsername() == null || userRegisterDto.getPassword() == null) {
            return Result.fail(UserConstants.USERNAME_OR_PASSWORD_EMPTY);
        }
        //判断用户名是否存在
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, userRegisterDto.getUsername());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(USER.USERNAME.eq(userRegisterDto.getUsername()));
        User u = userMapper.selectOneByQuery(queryWrapper);
//        User u = this.getOne(wrapper);
        if (u != null) {
            return Result.fail(UserConstants.USER_ALREADY_EXISTS);
        }
        User user = new User();
        String password = userRegisterDto.getPassword();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        user.setName(userRegisterDto.getName());
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(password);
        user.setRole(UserConstants.ROLE_USER);
        user.setEmail(userRegisterDto.getEmail());
        //注册
        userMapper.insert(user);
        //返回注册成功信息
        return Result.ok(UserConstants.REGISTRATION_SUCCESS);
    }

    @Override
    public boolean authorize(Integer userId) {
        User user = userMapper.selectOneById(userId);
        if (user.getRole().equals(UserConstants.ROLE_ADMIN)) {
            return false;
        }
        user.setRole(UserConstants.ROLE_ADMIN);
        userMapper.update(user);
        return true;
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword());
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
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.where(USER.USERNAME.eq(username));
        User user = userMapper.selectOneByQuery(wrapper);
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
    public void update(UserUpdateDto userUpdateDto) {
        String key = UserConstants.LOGIN_PREFIX + getUserId();
        String loginUserJson = stringRedisTemplate.opsForValue().get(key);
        LoginUser loginUser = JSONUtil.toBean(loginUserJson, LoginUser.class);
        User loginUserUser = loginUser.getUser();
        User user = userMapper.selectOneById(getUserId());
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bCryptPasswordEncoder.encode(userUpdateDto.getPassword());
            user.setPassword(password);
            loginUserUser.setPassword(password);
        }
        if (userUpdateDto.getName() != null && !userUpdateDto.getName().isEmpty()) {
            user.setName(userUpdateDto.getName());
            loginUserUser.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isEmpty()) {
            user.setEmail(userUpdateDto.getEmail());
            loginUserUser.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getAvatar() != null && !userUpdateDto.getAvatar().isEmpty()) {
            user.setAvatar(userUpdateDto.getAvatar());
            loginUserUser.setAvatar(userUpdateDto.getAvatar());
        }
        userMapper.update(user);
        loginUser.setUser(loginUserUser);
        String jsonStr = JSONUtil.toJsonStr(loginUser);
        stringRedisTemplate.opsForValue().set(key, jsonStr);
    }

    @Override
    public Page<User> getUserPage(AdminGetPageDto adminGetPageVo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(USER.USERNAME.like(adminGetPageVo.getUsername(),Strings.isNotEmpty(adminGetPageVo.getUsername())))
                .where(USER.NAME.like(adminGetPageVo.getName(),Strings.isNotEmpty(adminGetPageVo.getName())))
                .where(USER.EMAIL.like(adminGetPageVo.getEmail(),Strings.isNotEmpty(adminGetPageVo.getEmail())))
                .where(USER.ROLE.like(adminGetPageVo.getRole(),Strings.isNotEmpty(adminGetPageVo.getRole())));
        Page<User> page = userMapper.paginate(adminGetPageVo.getCurrentPage(), adminGetPageVo.getPageSize(), queryWrapper);
        return page;
    }

    private Integer getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser().getUserId();
    }
}
