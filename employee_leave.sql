/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 100432
 Source Host           : localhost:3306
 Source Schema         : employee_leave

 Target Server Type    : MySQL
 Target Server Version : 100432
 File Encoding         : 65001

 Date: 11/07/2024 23:08:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`  (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `day_off_remaining` int NULL DEFAULT NULL,
    `first_day_of_work` datetime(6) NULL DEFAULT NULL,
    `boss_id` int NULL DEFAULT NULL,
    `created_at` datetime(6) NULL DEFAULT NULL,
    `updated_at` datetime(6) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FKi77y1j3uq9xk27nbsrc2xn0hk`(`boss_id` ASC) USING BTREE,
    CONSTRAINT `FKi77y1j3uq9xk27nbsrc2xn0hk` FOREIGN KEY (`boss_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employees
-- ----------------------------
INSERT INTO `employees` VALUES (1, 'chan', '12345678', 'Hoàng Thanh Ngoan', '20130335@st.hcmuaf.edu.vn', 'Leader', 8, '2024-05-20 09:00:00.000000', NULL, '2024-07-05 03:09:53.000000', NULL);
INSERT INTO `employees` VALUES (2, 'nghia', '98765412', 'Hồ Thanh Nghĩa', '20130333@st.hcmuaf.edu.vn', 'Dev', 12, '2024-01-25 09:00:00.000000', 1, '2024-07-05 03:13:22.000000', NULL);
INSERT INTO `employees` VALUES (3, 'mai', '12378945', 'Nguyễn Thị Trúc Mai', '20130321@st.hcmuaf.edu.vn', 'Tester', 2, '2023-11-06 09:00:00.000000', 1, '2024-07-05 03:15:14.000000', NULL);
INSERT INTO `employees` VALUES (4, 'hieu', '11224455', 'Trần Trung Hiếu', '20130170@st.hcmuaf.edu.vn', 'Dev', 10, '2024-03-24 09:00:00.000000', 1, '2024-07-05 03:21:08.000000', NULL);

-- ----------------------------
-- Table structure for forgot_password
-- ----------------------------
DROP TABLE IF EXISTS `forgot_password`;
CREATE TABLE `forgot_password`  (
    `id` int NOT NULL AUTO_INCREMENT,
    `otp` int NOT NULL,
    `employee_id` int NULL DEFAULT NULL,
    `created_at` datetime(6) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK4tglef0qrhw0cg5uepmwsmwd4`(`employee_id` ASC) USING BTREE,
    CONSTRAINT `FK4tglef0qrhw0cg5uepmwsmwd4` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forgot_password
-- ----------------------------

-- ----------------------------
-- Table structure for leave_application
-- ----------------------------
    
DROP TABLE IF EXISTS `leave_application`;
CREATE TABLE `leave_application`  (
    `id` int NOT NULL AUTO_INCREMENT,
    `employee_id` int NULL DEFAULT NULL,
    `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `start_date` date NULL DEFAULT NULL,
    `end_date` date NULL DEFAULT NULL,
    `status` int NULL DEFAULT NULL,
    `reason_reject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `handle_by` int NULL DEFAULT NULL,
    `created_at` datetime(6) NULL DEFAULT NULL,
    `updated_at` datetime(6) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FKdrfuhqvl3xkiiewe9blflqdji`(`employee_id` ASC) USING BTREE,
    INDEX `FK7lertavnxq8kjm56okfkh4i7t`(`handle_by` ASC) USING BTREE,
    CONSTRAINT `FK7lertavnxq8kjm56okfkh4i7t` FOREIGN KEY (`handle_by`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FKdrfuhqvl3xkiiewe9blflqdji` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leave_application
-- ----------------------------

-- ----------------------------
-- Procedure structure for update_day_off_remaining
-- ----------------------------
DROP PROCEDURE IF EXISTS `update_day_off_remaining`;
delimiter ;;
CREATE PROCEDURE `update_day_off_remaining`()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE _id INT;
  DECLARE _first_day_of_work DATE;
  DECLARE _day_off_remaining INT;
  DECLARE months_worked INT;
  DECLARE years_worked INT;
  DECLARE new_day_off_remaining INT;
  DECLARE cur CURSOR FOR SELECT id, first_day_of_work, day_off_remaining FROM employees;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN cur;

read_loop: LOOP
    FETCH cur INTO _id, _first_day_of_work, _day_off_remaining;
    IF done THEN
      LEAVE read_loop;
END IF;

    SET months_worked = MONTH(_first_day_of_work);
    SET years_worked = YEAR(NOW()) - YEAR(_first_day_of_work);

    IF years_worked = 0 THEN
      -- Trong năm đầu tiên làm việc, số ngày nghỉ phép = 12 - số tháng đã làm việc + 1
      SET new_day_off_remaining = 12 - months_worked + 1;
    ELSEIF years_worked >= 1 THEN
      SET new_day_off_remaining = 12;
ELSE
      SET new_day_off_remaining = months_worked + 1;
END IF;

    IF years_worked >= 5 THEN
      SET new_day_off_remaining = new_day_off_remaining + 1;
END IF;

    -- Cộng dồn số ngày nghỉ phép còn lại từ năm trước
    SET new_day_off_remaining = new_day_off_remaining + _day_off_remaining;

UPDATE employees SET day_off_remaining = new_day_off_remaining WHERE id = _id;
END LOOP;

CLOSE cur;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table employees
-- ----------------------------
DROP TRIGGER IF EXISTS `before_employee_insert`;
delimiter ;;
CREATE TRIGGER `before_employee_insert` BEFORE INSERT ON `employees` FOR EACH ROW BEGIN
    DECLARE months_worked INT;

    SET months_worked = MONTH(NEW.first_day_of_work);

    -- Tính toán số ngày nghỉ phép mới
    SET NEW.day_off_remaining = 12 - months_worked + 1;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
