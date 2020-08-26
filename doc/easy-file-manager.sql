/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : easy-file-manager

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 26/08/2020 17:02:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件id',
  `file_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '源文件名',
  `server_local_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器生成的文件名',
  `server_local_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器储存路径',
  `folder_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录id',
  `file_md5` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件md5值',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `file_suffix` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
  `file_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `is_merge` int(2) NULL DEFAULT 0 COMMENT '是否合并 0否 1是',
  `is_zone` int(2) NULL DEFAULT 0 COMMENT '是否分片 0否 1是',
  `zone_total` int(11) NULL DEFAULT NULL COMMENT '分片总数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES ('03e02ba21bae5b1a7711eaea62bbcaf8', 'sbshurafajjb.zip', '853266f04a1666d066ab24fccd356236.zip', 'C:\\/fileSystem/admin/853266f04a1666d066ab24fccd356236.zip', '1', '853266f04a1666d066ab24fccd356236', 30776771, '.zip', 'application/zip', 1, 0, 1, '2020-08-26 15:48:26', '2020-08-26 15:48:26', 'admin');
INSERT INTO `sys_file` VALUES ('06c11eeddfa5cfc47f6e225a631e70f1', '新建 MindMaster.Document.emmx', '9c0f163042b55ac478e675daa9f47edf.emmx', 'C:\\/fileSystem/admin/9c0f163042b55ac478e675daa9f47edf.emmx', '1', '9c0f163042b55ac478e675daa9f47edf', 7738, '.emmx', 'application/zip', 1, 0, 1, '2020-08-26 16:02:07', '2020-08-26 16:02:07', 'admin');
INSERT INTO `sys_file` VALUES ('5f75827b65a16fa15698dffe4e07d15c', 'ubuntu-16.04.6-desktop-amd64.iso', 'dc2b6b5956423782518ecf0356f13e55.iso', 'C:\\/fileSystem/admin/dc2b6b5956423782518ecf0356f13e55.iso', '1', 'dc2b6b5956423782518ecf0356f13e55', 1664614400, '.iso', 'application/x-iso9660-image', 1, 0, 32, '2020-08-26 15:56:16', '2020-08-26 16:02:17', 'admin');
INSERT INTO `sys_file` VALUES ('83d132986e6b66e83bd8c233188b79f7', 'settings.zip', '38e8cea8a30959609c1559f786131340.zip', 'C:\\/fileSystem/admin/38e8cea8a30959609c1559f786131340.zip', '1', '38e8cea8a30959609c1559f786131340', 21606, '.zip', 'application/zip', 1, 0, 1, '2020-08-26 15:45:15', '2020-08-26 15:55:43', 'admin');
INSERT INTO `sys_file` VALUES ('8fa1ad8ddec59bc9ccb1fda86194cae4', 'README.md', '8da1ea17304ee22777192837ebf6c829.md', 'C:\\/fileSystem/admin/8da1ea17304ee22777192837ebf6c829.md', '1', '8da1ea17304ee22777192837ebf6c829', 3825, '.md', 'text/plain', 1, 0, 1, '2020-08-26 15:55:43', '2020-08-26 15:55:43', 'admin');
INSERT INTO `sys_file` VALUES ('9ee3cb862003f4c042aa9cbc035bbff6', '新建 Microsoft PowerPoint 2007 幻灯片.pptx', 'b63aae5667ce56815f56405568315aad.pptx', 'C:\\/fileSystem/admin/b63aae5667ce56815f56405568315aad.pptx', '1', 'b63aae5667ce56815f56405568315aad', 29251, '.pptx', 'application/x-tika-ooxml', 1, 0, 1, '2020-08-26 16:02:05', '2020-08-26 16:02:06', 'admin');
INSERT INTO `sys_file` VALUES ('b7fe3b60a0a9a44357347f7ab8cdc09c', 'ANIME-PICTURES.NET_-_446704-1920x1080-original-yuko-san-highres-wide+image-sky-cloud+(clouds).jpg', 'd76323eb86909b1e674dc6b2a5c5fef7.jpg', 'C:\\/fileSystem/admin/图片/d76323eb86909b1e674dc6b2a5c5fef7.jpg', '660aedd79adba6a197a701c6f12b3c21', 'd76323eb86909b1e674dc6b2a5c5fef7', 1669656, '.jpg', 'image/jpeg', 1, 0, 1, '2020-08-26 16:48:39', '2020-08-26 16:48:39', 'admin');
INSERT INTO `sys_file` VALUES ('c530166052cbe0ab5280b1456fccc422', 'ANIME-PICTURES.NET_-_241154-1920x1080-original-apofiss-looking+at+viewer-highres-blue+eyes-wide+image.jpg', 'e99198d8efcd05b82e62d2641e92286a.jpg', 'C:\\/fileSystem/admin/图片/美铝/e99198d8efcd05b82e62d2641e92286a.jpg', '074bba033bde43ff09ddbb724ae893b1', 'e99198d8efcd05b82e62d2641e92286a', 820314, '.jpg', 'image/jpeg', 1, 0, 1, '2020-08-26 16:57:10', '2020-08-26 16:57:10', 'admin');
INSERT INTO `sys_file` VALUES ('e417e687e0fe439b725e18bc9f5f969f', '新建 Microsoft Word 文档.docx', '00a450b508a48ada927d6c579e871df3.docx', 'C:\\/fileSystem/admin/00a450b508a48ada927d6c579e871df3.docx', '1', '00a450b508a48ada927d6c579e871df3', 9216, '.docx', 'application/msword', 1, 0, 1, '2020-08-26 16:02:06', '2020-08-26 16:02:07', 'admin');
INSERT INTO `sys_file` VALUES ('f3cb23b68617fc005d76b97c2e5d539c', '说明.txt', 'f24505f6dbafa157a4f7bc4b06efba9d.txt', 'C:\\/fileSystem/admin/f24505f6dbafa157a4f7bc4b06efba9d.txt', '1', 'f24505f6dbafa157a4f7bc4b06efba9d', 7958, '.txt', 'text/plain', 1, 0, 1, '2020-08-26 16:02:04', '2020-08-26 16:02:04', 'admin');
INSERT INTO `sys_file` VALUES ('f672bd4d73202f022a410ddea6d52d3f', '新建 Microsoft Excel 97-2003 工作表.xls', '789aa9bf3ee313ab2b98d8210c875c28.xls', 'C:\\/fileSystem/admin/789aa9bf3ee313ab2b98d8210c875c28.xls', '1', '789aa9bf3ee313ab2b98d8210c875c28', 6656, '.xls', 'application/vnd.ms-excel', 1, 0, 1, '2020-08-26 16:02:05', '2020-08-26 16:02:05', 'admin');

-- ----------------------------
-- Table structure for sys_file_zone
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_zone`;
CREATE TABLE `sys_file_zone`  (
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分片ID',
  `file_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件ID',
  `zone_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分片名称',
  `zone_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分片路径',
  `zone_md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分片md5值',
  `zone_total_count` int(11) NULL DEFAULT NULL COMMENT '分片总数',
  `file_md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分片前文件的md5值',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `zone_index` int(11) NULL DEFAULT NULL COMMENT '当前分片索引',
  `zone_start_size` bigint(20) NULL DEFAULT NULL COMMENT '分片开始位置',
  `zone_end_size` bigint(20) NULL DEFAULT NULL COMMENT '分片结束位置',
  `relative_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件相对路径',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件分片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_zone
-- ----------------------------
INSERT INTO `sys_file_zone` VALUES ('5829627228eacb2e2a287b1efe64d1f7', 'c530166052cbe0ab5280b1456fccc422', 'e99198d8efcd05b82e62d2641e92286a.jpg.temp', 'C:\\/fileSystem/temp/admin/e99198d8efcd05b82e62d2641e92286a', 'e99198d8efcd05b82e62d2641e92286a', 1, 'e99198d8efcd05b82e62d2641e92286a', 820314, 0, 0, 820314, '图片/美铝/', '2020-08-26 16:57:10', 'admin');

-- ----------------------------
-- Table structure for sys_folder
-- ----------------------------
DROP TABLE IF EXISTS `sys_folder`;
CREATE TABLE `sys_folder`  (
  `folder_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录id',
  `folder_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '目录名称',
  `folder_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录路径',
  `folder_parent_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级目录id',
  `level` int(11) NOT NULL COMMENT '层级数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`folder_id`) USING BTREE,
  INDEX `folder_parent_id`(`folder_parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '目录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_folder
-- ----------------------------
INSERT INTO `sys_folder` VALUES ('074bba033bde43ff09ddbb724ae893b1', '美铝', '图片/美铝', '660aedd79adba6a197a701c6f12b3c21', 2, '2020-08-26 16:51:30', '2020-08-26 16:51:30', 'admin');
INSERT INTO `sys_folder` VALUES ('1', 'ROOT', '', '-1', 0, '2020-08-26 05:39:47', NULL, 'admin');
INSERT INTO `sys_folder` VALUES ('660aedd79adba6a197a701c6f12b3c21', '图片', '图片', '1', 1, '2020-08-26 16:48:39', '2020-08-26 16:48:39', 'admin');
INSERT INTO `sys_folder` VALUES ('be7e18ac81449648a9d4c7711cca9453', '新建文件夹', '新建文件夹', '1', 1, '2020-08-26 15:37:07', '2020-08-26 15:37:07', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
