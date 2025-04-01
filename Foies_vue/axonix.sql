/*
 Navicat Premium Dump SQL

 Source Server         : MySQL 8.4
 Source Server Type    : MySQL
 Source Server Version : 80404 (8.4.4)
 Source Host           : localhost:3306
 Source Schema         : axonix

 Target Server Type    : MySQL
 Target Server Version : 80404 (8.4.4)
 File Encoding         : 65001

 Date: 01/04/2025 20:04:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '手机号',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin1', 'adminpass1', '98765432101', '2025-03-30 20:46:15', '2025-03-30 20:46:15');
INSERT INTO `admin` VALUES (2, '123', '123', '12333', '2025-03-11 20:46:32', '2025-03-30 20:46:36');

-- ----------------------------
-- Table structure for ai_log
-- ----------------------------
DROP TABLE IF EXISTS `ai_log`;
CREATE TABLE `ai_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始使用时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `ai_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_log
-- ----------------------------
INSERT INTO `ai_log` VALUES (1, 1, '2025-03-30 20:46:15');
INSERT INTO `ai_log` VALUES (2, 2, '2025-03-30 20:46:15');

-- ----------------------------
-- Table structure for emergency_contact
-- ----------------------------
DROP TABLE IF EXISTS `emergency_contact`;
CREATE TABLE `emergency_contact`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '紧急联系人记录ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `contact_user_id` int NOT NULL COMMENT '紧急联系人ID',
  `contact_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '紧急联系人手机号',
  `relationship` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '关系',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `contact_user_id`(`contact_user_id` ASC) USING BTREE,
  CONSTRAINT `emergency_contact_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `emergency_contact_ibfk_2` FOREIGN KEY (`contact_user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_contact
-- ----------------------------
INSERT INTO `emergency_contact` VALUES (2, 1, 2, '1234567891', '家人', '2025-03-30 20:46:15', '2025-03-30 20:46:15');
INSERT INTO `emergency_contact` VALUES (8, 1, 2, '3', '4', NULL, NULL);
INSERT INTO `emergency_contact` VALUES (10, 1, 1, '11', '1', NULL, NULL);
INSERT INTO `emergency_contact` VALUES (11, 1, 2, '3', '42', '2025-03-31 10:20:30', '2025-03-31 10:20:39');

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `issue` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '反馈问题',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 用户提交, 1: 处理中, 2: 已完成, 3: 异常）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES (1, 1, '应用有时会崩溃', 0, '2025-03-30 20:46:15');
INSERT INTO `feedback` VALUES (2, 2, '界面优化建议', 1, '2025-03-30 20:46:15');

-- ----------------------------
-- Table structure for forum_comment
-- ----------------------------
DROP TABLE IF EXISTS `forum_comment`;
CREATE TABLE `forum_comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `post_id` int NOT NULL COMMENT '帖子ID',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '内容',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 审核中, 1: 驳回, 2: 异常, 3: 已发布, 4: 私密）',
  `likes` int NULL DEFAULT 0 COMMENT '喜欢数',
  `dislikes` int NULL DEFAULT 0 COMMENT '不喜欢数',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `post_id`(`post_id` ASC) USING BTREE,
  CONSTRAINT `forum_comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `forum_comment_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `forum_post` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of forum_comment
-- ----------------------------
INSERT INTO `forum_comment` VALUES (1, 1, 1, '这是第一篇帖子的第一条评论', 3, 0, 0, '2025-03-30 20:46:15');
INSERT INTO `forum_comment` VALUES (2, 2, 2, '这是第二篇帖子的第一条评论', 3, 0, 0, '2025-03-30 20:46:15');

-- ----------------------------
-- Table structure for forum_post
-- ----------------------------
DROP TABLE IF EXISTS `forum_post`;
CREATE TABLE `forum_post`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '内容',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 审核中, 1: 驳回, 2: 异常, 3: 已发布, 4: 私密）',
  `likes` int NULL DEFAULT 0 COMMENT '喜欢数',
  `dislikes` int NULL DEFAULT 0 COMMENT '不喜欢数',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `forum_post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of forum_post
-- ----------------------------
INSERT INTO `forum_post` VALUES (1, 1, '第一篇帖子', '这是第一篇帖子内容', 3, 0, 0, '2025-03-30 20:46:15', '2025-03-30 20:46:15');
INSERT INTO `forum_post` VALUES (2, 2, '第二篇帖子', '这是第二篇帖子内容', 3, 0, 0, '2025-03-30 20:46:15', '2025-03-30 20:46:15');

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
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `function_stat_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of function_stat
-- ----------------------------
INSERT INTO `function_stat` VALUES (1, 1, 3, 0, 0, 0, 5, 0, 2, 0, 0, 0, 0);
INSERT INTO `function_stat` VALUES (2, 2, 2, 0, 0, 0, 3, 0, 1, 0, 0, 0, 0);

-- ----------------------------
-- Table structure for help_request
-- ----------------------------
DROP TABLE IF EXISTS `help_request`;
CREATE TABLE `help_request`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '求助记录ID',
  `requester_id` int NOT NULL COMMENT '求助者ID',
  `helper_id` int NULL DEFAULT NULL COMMENT '帮助者ID',
  `requester_lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '求助者经度',
  `requester_lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '求助者纬度',
  `helper_lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '帮助者经度',
  `helper_lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '帮助者纬度',
  `status` int NULL DEFAULT NULL COMMENT '状态（0: 正在发布, 1: 匹配中, 2: 处理中, 3: 完成, 4: 特殊情况）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `requester_id`(`requester_id` ASC) USING BTREE,
  INDEX `helper_id`(`helper_id` ASC) USING BTREE,
  CONSTRAINT `help_request_ibfk_1` FOREIGN KEY (`requester_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `help_request_ibfk_2` FOREIGN KEY (`helper_id`) REFERENCES `emergency_contact` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of help_request
-- ----------------------------
INSERT INTO `help_request` VALUES (1, 1, NULL, NULL, NULL, NULL, NULL, 0, '2025-03-30 20:46:15', '2025-03-30 20:46:15');
INSERT INTO `help_request` VALUES (2, 2, NULL, NULL, NULL, NULL, NULL, 1, '2025-03-30 20:46:15', '2025-03-30 20:46:15');

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `admin_id` int NOT NULL COMMENT '管理员ID',
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '内容',
  `target_type` int NULL DEFAULT NULL COMMENT '发布对象（0: 全部, 1: 视障, 2: 听障, 3: 其他障碍）',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `admin_id`(`admin_id` ASC) USING BTREE,
  CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (1, 1, '系统维护公告', '系统将于明日凌晨进行维护', 0, NULL, '2025-03-30 20:46:15');

-- ----------------------------
-- Table structure for sos_notification
-- ----------------------------
DROP TABLE IF EXISTS `sos_notification`;
CREATE TABLE `sos_notification`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'SOS通知ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '内容',
  `contact_id` int NOT NULL COMMENT '紧急联系人ID',
  `lng` decimal(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `lat` decimal(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `contact_id`(`contact_id` ASC) USING BTREE,
  CONSTRAINT `sos_notification_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sos_notification_ibfk_2` FOREIGN KEY (`contact_id`) REFERENCES `emergency_contact` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sos_notification
-- ----------------------------
INSERT INTO `sos_notification` VALUES (1, 1, '紧急求助', '用户1需要帮助', 2, NULL, NULL, '2025-03-30 20:46:15', NULL);

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
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `traffic_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of traffic_log
-- ----------------------------
INSERT INTO `traffic_log` VALUES (1, 1, 116.397000, 39.907000, '2025-03-30 20:46:15', '2025-03-30 20:46:15');
INSERT INTO `traffic_log` VALUES (2, 2, 121.473700, 31.230400, '2025-03-30 20:46:15', '2025-03-30 20:46:15');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '头像',
  `gender` int NULL DEFAULT NULL COMMENT '性别（0: 未知, 1: 男, 2: 女）',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `province` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '市',
  `county` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '县',
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '详细地址',
  `email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '手机号',
  `disability_type` int NULL DEFAULT NULL COMMENT '残障类型（0: 无, 1: 视障, 2: 听障, 3: 其他障碍）',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `is_vip` int NULL DEFAULT 0 COMMENT '是否VIP（0: 否, 1: 是）',
  `device_token` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '安卓设备推送令牌',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'user1', 'pass1', NULL, 1, 25, '浙江', '海宁', '12', '12', 'user1@example.com', '123456789011', 1, '2025-03-05 20:47:26', 1, NULL, '2025-03-30 20:46:15', '2025-03-31 09:25:47');
INSERT INTO `user` VALUES (2, 'user2', 'pass2', NULL, 1, 25, '上海', '浦东', '1', '1', 'user2@example.com', '12345678901', 2, '2025-02-27 20:47:29', 1, NULL, '2025-03-30 20:46:15', '2025-03-31 09:25:53');
INSERT INTO `user` VALUES (10, '1', '1', NULL, 1, 24, '1', '1', '1', '1', '1@11.com', '1', 2, NULL, 1, NULL, '2025-03-31 09:29:50', '2025-03-31 09:30:07');
INSERT INTO `user` VALUES (11, '2', '1', NULL, 2, 2, '1', '1', '1', '1', '1@qq.com', '1', 1, NULL, 1, NULL, '2025-03-31 09:30:19', '2025-03-31 09:30:33');
INSERT INTO `user` VALUES (12, '3', '1', NULL, 1, 0, 'q', 'q', 'q', 'q', '2@qqq.com', 'q', 3, NULL, 1, NULL, '2025-03-31 09:30:35', '2025-03-31 09:30:48');

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
  `total_payment` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计支付金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_end_time`(`end_time` ASC) USING BTREE,
  CONSTRAINT `vip_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vip
-- ----------------------------
INSERT INTO `vip` VALUES (1, 2, 2, '2025-03-30 20:46:15', '2026-03-30 20:46:15', 199.99);
INSERT INTO `vip` VALUES (3, 2, 0, '2025-04-24 16:00:00', '2025-05-06 16:00:00', 3.00);
INSERT INTO `vip` VALUES (4, 1, 2, '2025-03-31 16:00:00', '2025-04-15 16:00:00', 2.00);

SET FOREIGN_KEY_CHECKS = 1;
