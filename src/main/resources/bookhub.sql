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

 Date: 25/10/2023 21:22:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for adminactions
-- ----------------------------
DROP TABLE IF EXISTS `adminactions`;
CREATE TABLE `adminactions`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `actionType` enum('Add','Edit','Delete') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型',
  `adminUserId` int NULL DEFAULT NULL COMMENT '用户id',
  `bookId` int NULL DEFAULT NULL COMMENT '图书id',
  `actionDate` datetime NULL DEFAULT NULL COMMENT '操作日期',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `adminUserId`(`adminUserId` ASC) USING BTREE,
  INDEX `bookId`(`bookId` ASC) USING BTREE,
  CONSTRAINT `adminactions_ibfk_1` FOREIGN KEY (`adminUserId`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `adminactions_ibfk_2` FOREIGN KEY (`bookId`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adminactions
-- ----------------------------

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `book_id` int NOT NULL AUTO_INCREMENT COMMENT '图书id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书名',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图书作者',
  `ISBN` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国际标准书号',
  `publicationDate` date NULL DEFAULT NULL COMMENT '出版日期',
  `status` enum('Available','Borrowed') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态：有库存1，无库存0',
  `stock` int NULL DEFAULT NULL COMMENT '库存',
  `reservationCount` int NULL DEFAULT 0 COMMENT '预定数量',
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------

-- ----------------------------
-- Table structure for borrowedbook
-- ----------------------------
DROP TABLE IF EXISTS `borrowedbook`;
CREATE TABLE `borrowedbook`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `userId` int NULL DEFAULT NULL COMMENT '用户id',
  `bookId` int NULL DEFAULT NULL COMMENT '图书id',
  `borrowDate` date NULL DEFAULT NULL COMMENT '借阅时间',
  `returnDate` date NULL DEFAULT NULL COMMENT '归还时间',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `userID`(`userId` ASC) USING BTREE,
  INDEX `bookID`(`bookId` ASC) USING BTREE,
  CONSTRAINT `borrowedbook_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrowedbook_ibfk_2` FOREIGN KEY (`bookId`) REFERENCES `book` (`book_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrowedbook
-- ----------------------------

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `notification_id` int NOT NULL AUTO_INCREMENT COMMENT '通知id',
  `userId` int NULL DEFAULT NULL COMMENT '用户id',
  `notificationType` enum('ReturnReminder','ReservationReminder','OverdueNotice') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知类型',
  `notificationContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '通知内容',
  `sentDate` datetime NULL DEFAULT NULL COMMENT '发送日期',
  PRIMARY KEY (`notification_id`) USING BTREE,
  INDEX `userId`(`userId` ASC) USING BTREE,
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notifications
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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'PlanB', '$2a$10$yUPSooryTpThUJtqDH5TeO2ShJR3bOlqi8magQoolpC2UiguoS/Ae', '潘常浩', '2741718884@qq.com', 'ADMIN');
INSERT INTO `user` VALUES (2, 'PlanC', '$2a$10$yUPSooryTpThUJtqDH5TeO2ShJR3bOlqi8magQoolpC2UiguoS/Ae', '邱楷彬', 'adfjiasdfhka4@qq.com', 'USER');

SET FOREIGN_KEY_CHECKS = 1;
