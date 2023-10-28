/*
 Navicat Premium Data Transfer

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : bookhub

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 28/10/2023 22:37:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adminactions
-- ----------------------------
DROP TABLE IF EXISTS `adminactions`;
CREATE TABLE `adminactions`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `action_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `admin_user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `book_id` int NULL DEFAULT NULL COMMENT '图书id',
  `action_date` datetime NULL DEFAULT NULL COMMENT '操作日期',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `adminUserId`(`admin_user_id` ASC) USING BTREE,
  INDEX `bookId`(`book_id` ASC) USING BTREE,
  CONSTRAINT `adminactions_ibfk_1` FOREIGN KEY (`admin_user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `adminactions_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adminactions
-- ----------------------------
INSERT INTO `adminactions` VALUES (1, 'INSERT', 1, NULL, '2023-10-27 14:59:32');
INSERT INTO `adminactions` VALUES (2, 'INSERT', 1, 2, '2023-10-27 15:03:27');
INSERT INTO `adminactions` VALUES (3, 'INSERT', 1, 3, '2023-10-27 15:05:10');
INSERT INTO `adminactions` VALUES (4, 'INSERT', 1, 4, '2023-10-27 15:06:17');
INSERT INTO `adminactions` VALUES (5, 'INSERT', 1, 5, '2023-10-27 15:09:08');
INSERT INTO `adminactions` VALUES (6, 'INSERT', 1, 6, '2023-10-27 15:15:06');
INSERT INTO `adminactions` VALUES (7, 'INSERT', 1, 7, '2023-10-27 15:16:25');
INSERT INTO `adminactions` VALUES (8, 'INSERT', 1, 8, '2023-10-27 15:18:05');
INSERT INTO `adminactions` VALUES (9, 'INSERT', 1, 9, '2023-10-27 15:19:54');
INSERT INTO `adminactions` VALUES (10, 'UPDATE', 1, NULL, '2023-10-27 15:23:57');
INSERT INTO `adminactions` VALUES (11, 'UPDATE', 1, NULL, '2023-10-27 15:24:23');
INSERT INTO `adminactions` VALUES (12, 'UPDATE', 1, 7, '2023-10-27 15:26:22');
INSERT INTO `adminactions` VALUES (13, 'DELETE', 1, 5, '2023-10-27 15:27:53');
INSERT INTO `adminactions` VALUES (14, 'DELETE', 1, 5, '2023-10-27 15:42:31');
INSERT INTO `adminactions` VALUES (15, 'DELETE', 1, 6, '2023-10-27 17:51:45');
INSERT INTO `adminactions` VALUES (16, 'INSERT', 1, 10, '2023-10-27 18:29:32');
INSERT INTO `adminactions` VALUES (17, 'INSERT', 1, 11, '2023-10-28 18:43:55');
INSERT INTO `adminactions` VALUES (18, 'INSERT', 1, 12, '2023-10-28 18:44:15');
INSERT INTO `adminactions` VALUES (19, 'INSERT', 1, 13, '2023-10-28 20:09:11');
INSERT INTO `adminactions` VALUES (20, 'DELETE', 1, 8, '2023-10-28 20:38:40');
INSERT INTO `adminactions` VALUES (21, 'UPDATE', 1, 4, '2023-10-28 20:41:02');
INSERT INTO `adminactions` VALUES (22, 'UPDATE', 1, 3, '2023-10-28 20:42:42');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `book_id` int NOT NULL AUTO_INCREMENT COMMENT '图书id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书名',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图书作者',
  `ISBN` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国际标准书号',
  `publication_date` date NULL DEFAULT NULL COMMENT '出版日期',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态：有库存1，无库存0',
  `stock` int NULL DEFAULT NULL COMMENT '库存',
  `reservation_count` int NULL DEFAULT 0 COMMENT '预定数量',
  `is_deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '逻辑删除：未删除1，删除0',
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, 'Java课程设计', '黑马程序员', '724839579023', '2023-10-27', '1', 10, 0, '1');
INSERT INTO `book` VALUES (2, 'Java课程设计Ⅱ', '牛马程序员', '74895790237450', '2023-10-27', '1', 10, 1, '1');
INSERT INTO `book` VALUES (3, 'Oracle进阶', '动力节点', '4508023949089034580-', '1979-09-30', '1', 34, 456, '1');
INSERT INTO `book` VALUES (4, 'mybatisplus从入门到入土', '尚硅谷', '450802394580-', '1979-09-11', '1', 30, 123, '1');
INSERT INTO `book` VALUES (5, 'Python入门到起飞', '黑马程序员', '123783489347895', '2023-10-27', '1', 10, 0, '0');
INSERT INTO `book` VALUES (6, '计算机组成原理', 'bbb', '238945971945792347', '2023-10-27', '1', 10, 0, '0');
INSERT INTO `book` VALUES (7, 'Python入门到入土', '黑马程序员', '75932477934873294', '2023-10-27', '1', 10, 0, '1');
INSERT INTO `book` VALUES (8, '算法导论', 'ccc', '48921739047124', '2023-10-27', '1', 8, 4, '0');
INSERT INTO `book` VALUES (9, '网络是怎样连接的', '新华出版社', '75932475973294', '2023-10-27', '1', 10, 0, '1');
INSERT INTO `book` VALUES (10, 'C++primer', 'GDUFE', '75932475973294', '2023-10-27', '1', 10, 1, '1');
INSERT INTO `book` VALUES (11, '深入理解Java虚拟机', 'aaa', '503840589204238', '2023-09-09', '1', 10, 0, '1');
INSERT INTO `book` VALUES (12, '深入理解Java虚拟机Ⅱ', 'aaa', '503840589204238', '2023-09-09', '1', 10, 0, '1');
INSERT INTO `book` VALUES (13, 'Java并发编程的艺术', 'bbb', '503840589204238', '2023-09-09', '1', 10, 0, '1');

-- ----------------------------
-- Table structure for borrowedbook
-- ----------------------------
DROP TABLE IF EXISTS `borrowedbook`;
CREATE TABLE `borrowedbook`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `book_id` int NULL DEFAULT NULL COMMENT '图书id',
  `borrow_date` date NULL DEFAULT NULL COMMENT '借阅时间',
  `return_date` date NULL DEFAULT NULL COMMENT '归还时间',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `userID`(`user_id` ASC) USING BTREE,
  INDEX `bookID`(`book_id` ASC) USING BTREE,
  CONSTRAINT `borrowedbook_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrowedbook_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrowedbook
-- ----------------------------
INSERT INTO `borrowedbook` VALUES (1, 2, 2, '2023-10-27', '2023-10-27');
INSERT INTO `borrowedbook` VALUES (2, 2, 8, '2023-09-15', NULL);
INSERT INTO `borrowedbook` VALUES (3, 2, 10, '2023-10-28', '2023-10-28');

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `notification_id` int NOT NULL AUTO_INCREMENT COMMENT '通知id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `notification_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知类型',
  `notification_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '通知内容',
  `sent_date` datetime NULL DEFAULT NULL COMMENT '发送日期',
  PRIMARY KEY (`notification_id`) USING BTREE,
  INDEX `userId`(`user_id` ASC) USING BTREE,
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notifications
-- ----------------------------
INSERT INTO `notifications` VALUES (1, 2, 'BOOKING', '您预订的书籍已在库，当前可进行借阅', '2023-10-27 22:53:08');
INSERT INTO `notifications` VALUES (2, 2, 'RETURN', '您所借阅的书籍即将到期，请按时归还', '2023-10-27 23:00:11');
INSERT INTO `notifications` VALUES (3, 2, 'OVERDUE', '您所借阅的书籍已逾期归还，请及时归还', '2023-10-27 23:04:21');

-- ----------------------------
-- Table structure for preorderbook
-- ----------------------------
DROP TABLE IF EXISTS `preorderbook`;
CREATE TABLE `preorderbook`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `user_id` int NOT NULL COMMENT '用户id',
  `book_id` int NOT NULL COMMENT '图书id',
  `booking_date` datetime NOT NULL COMMENT '预订时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of preorderbook
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'PlanB', '$2a$10$yUPSooryTpThUJtqDH5TeO2ShJR3bOlqi8magQoolpC2UiguoS/Ae', '潘常浩', 'planbbbbb@qq.com', 'ADMIN');
INSERT INTO `user` VALUES (2, 'PlanC', '$2a$10$yUPSooryTpThUJtqDH5TeO2ShJR3bOlqi8magQoolpC2UiguoS/Ae', '邱楷彬', '2625989772@qq.com', 'USER');
INSERT INTO `user` VALUES (3, 'pucci', '$2a$10$27zX1KtNpJeYD02v8.1Z8uYOSQszGKOEqIAhLXdM9XA4oShvYKYnO', '潘启浩', '1985753103@qq.com', 'USER');
INSERT INTO `user` VALUES (4, 'winghau', '$2a$10$Ak1rJ3rySy3qUe6te6IESeBYE/o2GRQUi02bssEkgKAUvMVa3ofFK', '缪永恒', '1239406018@qq.com', 'USER');

SET FOREIGN_KEY_CHECKS = 1;
