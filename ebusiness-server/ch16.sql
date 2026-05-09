/*
 Navicat Premium Data Transfer

 Source Server         : MyDB
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : localhost:3306
 Source Schema         : ch16

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 11/04/2024 05:55:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ausertable
-- ----------------------------
DROP TABLE IF EXISTS `ausertable`;
CREATE TABLE `ausertable`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `aname` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `apwd` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ausertable
-- ----------------------------
INSERT INTO `ausertable` VALUES (1, 'admin', 'admin');

-- ----------------------------
-- Table structure for busertable
-- ----------------------------
DROP TABLE IF EXISTS `busertable`;
CREATE TABLE `busertable`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `bemail` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `bpwd` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of busertable
-- ----------------------------

-- ----------------------------
-- Table structure for carttable
-- ----------------------------
DROP TABLE IF EXISTS `carttable`;
CREATE TABLE `carttable`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `busertable_id` int NOT NULL,
  `goodstable_id` int NOT NULL,
  `shoppingnum` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bid`(`busertable_id` ASC) USING BTREE,
  INDEX `gno`(`goodstable_id` ASC) USING BTREE,
  CONSTRAINT `bid` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `gno` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of carttable
-- ----------------------------

-- ----------------------------
-- Table structure for focustable
-- ----------------------------
DROP TABLE IF EXISTS `focustable`;
CREATE TABLE `focustable`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `goodstable_id` int NOT NULL,
  `busertable_id` int NOT NULL,
  `focustime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `gno1`(`goodstable_id` ASC) USING BTREE,
  INDEX `bid1`(`busertable_id` ASC) USING BTREE,
  CONSTRAINT `bid1` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `gno1` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of focustable
-- ----------------------------

-- ---------------------
--
---------
-- Table structure for goodstable
-- ----------------------------
DROP TABLE IF EXISTS `goodstable`;
CREATE TABLE `goodstable`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `gname` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `goprice` double NULL DEFAULT NULL,
  `grprice` double NULL DEFAULT NULL,
  `gstore` int NULL DEFAULT NULL,
  `gpicture` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `is_advertisement` tinyint NULL DEFAULT NULL,
  `goodstype_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `typeid`(`goodstype_id` ASC) USING BTREE,
  CONSTRAINT `typeid` FOREIGN KEY (`goodstype_id`) REFERENCES `goodstype` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goodstable
-- ----------------------------

-- ----------------------------
-- Table structure for goodstype
-- ----------------------------
DROP TABLE IF EXISTS `goodstype`;
CREATE TABLE `goodstype`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `typename` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goodstype
-- ----------------------------

-- ----------------------------
-- Table structure for orderbasetable
-- ----------------------------
DROP TABLE IF EXISTS `orderbasetable`;
CREATE TABLE `orderbasetable`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `busertable_id` int NOT NULL,
  `amount` double NOT NULL,
  `status` tinyint NOT NULL,
  `orderdate` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bid2`(`busertable_id` ASC) USING BTREE,
  CONSTRAINT `bid2` FOREIGN KEY (`busertable_id`) REFERENCES `busertable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orderbasetable
-- ----------------------------

-- ----------------------------
-- Table structure for orderdetail
-- ----------------------------
DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE `orderdetail`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `orderbasetable_id` int NOT NULL,
  `goodstable_id` int NOT NULL,
  `shoppingnum` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `odsn`(`orderbasetable_id` ASC) USING BTREE,
  INDEX `gno3`(`goodstable_id` ASC) USING BTREE,
  CONSTRAINT `gno3` FOREIGN KEY (`goodstable_id`) REFERENCES `goodstable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `odsn` FOREIGN KEY (`orderbasetable_id`) REFERENCES `orderbasetable` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orderdetail
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
