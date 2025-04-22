/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : axonix

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 19/04/2025 16:20:26
*/

DROP DATABASE IF EXISTS axonix;
CREATE DATABASE axonix;
USE axonix;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ai_log
-- ----------------------------
DROP TABLE IF EXISTS `ai_log`;
CREATE TABLE `ai_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始使用时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `ai_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for emergency_contact
-- ----------------------------
DROP TABLE IF EXISTS `emergency_contact`;
CREATE TABLE `emergency_contact`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '紧急联系人记录ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `contact_user_id` int NOT NULL COMMENT '紧急联系人ID',
  `contact_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '紧急联系人手机号',
  `relationship` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '关系',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `contact_user_id`(`contact_user_id`) USING BTREE,
  CONSTRAINT `emergency_contact_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `emergency_contact_ibfk_2` FOREIGN KEY (`contact_user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `issue` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '反馈问题',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 用户提交, 1: 处理中, 2: 已完成, 3: 异常）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for forum_comment
-- ----------------------------
DROP TABLE IF EXISTS `forum_comment`;
CREATE TABLE `forum_comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `post_id` int NOT NULL COMMENT '帖子ID',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 审核中, 1: 驳回, 2: 异常, 3: 已发布, 4: 私密）',
  `likes` int NULL DEFAULT 0 COMMENT '喜欢数',
  `dislikes` int NULL DEFAULT 0 COMMENT '不喜欢数',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `post_id`(`post_id`) USING BTREE,
  CONSTRAINT `forum_comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `forum_comment_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `forum_post` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for forum_post
-- ----------------------------
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE `forum_post`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 审核中, 1: 驳回, 2: 异常, 3: 已发布, 4: 私密）',
  `likes` int NULL DEFAULT 0 COMMENT '喜欢数',
  `dislikes` int NULL DEFAULT 0 COMMENT '不喜欢数',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `forum_post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for function_stat
-- ----------------------------
DROP TABLE IF EXISTS `function_stat`;
CREATE TABLE `function_stat`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `help_request` int NULL DEFAULT 0 COMMENT '求助匹配次数',
  `help_match` int NULL DEFAULT 0 COMMENT '帮助匹配次数',
  `help_request_success` int NULL DEFAULT 0 COMMENT '求助匹配成功次数',
  `help_match_success` int NULL DEFAULT 0 COMMENT '帮助匹配成功次数',
  `navigation` int NULL DEFAULT 0 COMMENT '导航使用次数',
  `traffic_recognition` int NULL DEFAULT 0 COMMENT '路况识别次数',
  `ai_assistant` int NULL DEFAULT 0 COMMENT 'AI助理使用次数',
  `post_publish` int NULL DEFAULT 0 COMMENT '论坛帖子发布次数',
  `comment` int NULL DEFAULT 0 COMMENT '评论次数',
  `feedback` int NULL DEFAULT 0 COMMENT '问题反馈次数',
  `sos` int NULL DEFAULT 0 COMMENT '紧急SOS次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `function_stat_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for help_request
-- ----------------------------
DROP TABLE IF EXISTS `help_request`;
CREATE TABLE `help_request`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '求助记录ID',
  `requester_id` int NULL DEFAULT NULL COMMENT '求助者ID',
  `helper_id` int NULL DEFAULT NULL COMMENT '帮助者ID',
  `requester_lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '求助者经度',
  `requester_lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '求助者纬度',
  `helper_lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '帮助者经度',
  `helper_lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '帮助者纬度',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 正在发布, 1: 匹配中, 2: 处理中, 3: 完成, 4: 特殊情况）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `requester_id`(`requester_id`) USING BTREE,
  INDEX `help_request_ibfk_2`(`helper_id`) USING BTREE,
  CONSTRAINT `help_request_ibfk_1` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `help_request_ibfk_2` FOREIGN KEY (`helper_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `admin_id` int NOT NULL COMMENT '管理员ID',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `target_type` int NULL DEFAULT NULL COMMENT '发布对象（0: 全部, 1: 视障, 2: 听障, 3: 其他障碍）',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `admin_id`(`admin_id`) USING BTREE,
  CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sos_notification
-- ----------------------------
DROP TABLE IF EXISTS `sos_notification`;
CREATE TABLE `sos_notification`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'SOS通知ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `contact_id` int NOT NULL COMMENT '紧急联系人ID',
  `lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `contact_id`(`contact_id`) USING BTREE,
  CONSTRAINT `sos_notification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 93 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for traffic_log
-- ----------------------------
DROP TABLE IF EXISTS `traffic_log`;
CREATE TABLE `traffic_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `last_lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '最后经度',
  `last_lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '最后纬度',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始使用时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `traffic_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像',
  `gender` int NULL DEFAULT NULL COMMENT '性别（0: 未知, 1: 男, 2: 女）',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `province` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '市',
  `county` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '县',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '详细地址',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `disability_type` int NULL DEFAULT NULL COMMENT '残障类型（0: 无, 1: 视障, 2: 听障, 3: 其他障碍）',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `is_vip` int NULL DEFAULT 0 COMMENT '是否VIP（0: 否, 1: 是）',
  `device_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '安卓设备推送令牌',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for vip
-- ----------------------------
DROP TABLE IF EXISTS `vip`;
CREATE TABLE `vip`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'VIP记录ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `levels` int NULL DEFAULT NULL COMMENT 'Vip等级',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开通时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `total_payment` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '累计支付金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `idx_end_time`(`end_time`) USING BTREE,
  CONSTRAINT `vip_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Triggers structure for table forum_post
-- ----------------------------
DROP TRIGGER IF EXISTS `before_delete_forum_post`;
delimiter ;;
CREATE TRIGGER `before_delete_forum_post` BEFORE DELETE ON `forum_post` FOR EACH ROW BEGIN
    DELETE FROM forum_comment WHERE post_id = OLD.id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `createFunctionStat`;
delimiter ;;
CREATE TRIGGER `createFunctionStat` AFTER INSERT ON `user` FOR EACH ROW BEGIN
    INSERT INTO function_stat (user_id)
    VALUES (NEW.id);
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `createAiLog`;
delimiter ;;
CREATE TRIGGER `createAiLog` AFTER INSERT ON `user` FOR EACH ROW BEGIN
    INSERT INTO ai_log (user_id)
    VALUES (NEW.id);
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `createTrafficLog`;
delimiter ;;
CREATE TRIGGER `createTrafficLog` AFTER INSERT ON `user` FOR EACH ROW BEGIN
    INSERT INTO traffic_log (user_id)
    VALUES (NEW.id);
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
