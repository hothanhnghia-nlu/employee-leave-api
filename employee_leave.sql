/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 100424
 Source Host           : localhost:3306
 Source Schema         : employee_leave

 Target Server Type    : MySQL
 Target Server Version : 100424
 File Encoding         : 65001

 Date: 13/04/2024 12:27:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `day_off_remaining` int NULL DEFAULT NULL,
  `first_day_of_work` datetime NULL DEFAULT NULL,
  `boss_id` int NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKi77y1j3uq9xk27nbsrc2xn0hk`(`boss_id` ASC) USING BTREE,
  CONSTRAINT `FKi77y1j3uq9xk27nbsrc2xn0hk` FOREIGN KEY (`boss_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of employees
-- ----------------------------
INSERT INTO `employees` VALUES (1, 'ngoan', '12345678', 'Hoàng Thanh Ngoan', '20130335@st.hcmuaf.edu.vn', 'PO', 115, '2024-04-02 09:51:11', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for leave_application
-- ----------------------------
DROP TABLE IF EXISTS `leave_application`;
CREATE TABLE `leave_application`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NULL DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `start_date` date NULL DEFAULT NULL,
  `end_date` date NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `reason_reject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `handle_by` int NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT NULL,
  `updated_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKdrfuhqvl3xkiiewe9blflqdji`(`employee_id` ASC) USING BTREE,
  INDEX `FK7lertavnxq8kjm56okfkh4i7t`(`handle_by` ASC) USING BTREE,
  CONSTRAINT `FK7lertavnxq8kjm56okfkh4i7t` FOREIGN KEY (`handle_by`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKdrfuhqvl3xkiiewe9blflqdji` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

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
