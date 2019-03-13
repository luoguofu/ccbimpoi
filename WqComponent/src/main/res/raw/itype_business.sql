/*
Navicat SQLite Data Transfer

Source Server         : weiqiadb
Source Server Version : 30714
Source Host           : :0

Target Server Type    : SQLite
Target Server Version : 30714
File Encoding         : 65001

Date: 2015-07-16 16:17:44
*/

PRAGMA foreign_keys = OFF;

-- ----------------------------
-- Table structure for itype_business
-- ----------------------------
DROP TABLE IF EXISTS "main"."itype_business";
CREATE TABLE "itype_business" (
"itype"  INTEGER NOT NULL,
"business_type"  INTEGER,
"op"  INTEGER,
"type"  INTEGER,
"description"  TEXT,
PRIMARY KEY ("itype" ASC)
);

-- ----------------------------
-- Records of itype_business
-- ----------------------------
INSERT INTO "main"."itype_business" VALUES (404, 4, 1, 1, '公告发布');
INSERT INTO "main"."itype_business" VALUES (405, 4, 3, 1, '删除公告');
INSERT INTO "main"."itype_business" VALUES (601, 7, 1, 10, '聊天新增');
INSERT INTO "main"."itype_business" VALUES (704, 1, 1, 2, '任务发布');
INSERT INTO "main"."itype_business" VALUES (705, 1, 2, 2, '任务修改');
INSERT INTO "main"."itype_business" VALUES (706, 1, 1, 3, '任务进展添加');
INSERT INTO "main"."itype_business" VALUES (707, 1, 2, 2, '任务完成');
INSERT INTO "main"."itype_business" VALUES (708, 1, 3, 2, '任务删除');
INSERT INTO "main"."itype_business" VALUES (709, 1, 3, 3, '任务进展删除');
INSERT INTO "main"."itype_business" VALUES (710, 1, 2, 2, '任务重启');
INSERT INTO "main"."itype_business" VALUES (725, 1, 1, 4, '任务参与人添加');
INSERT INTO "main"."itype_business" VALUES (726, 1, 3, 4, '任务参与人移除');
INSERT INTO "main"."itype_business" VALUES (727, 1, 2, 2, '任务负责人转移');
INSERT INTO "main"."itype_business" VALUES (806, 2, 1, 5, '项目发布');
INSERT INTO "main"."itype_business" VALUES (809, 2, 2, 5, '项目修改');
INSERT INTO "main"."itype_business" VALUES (811, 2, 3, 5, '项目删除');
INSERT INTO "main"."itype_business" VALUES (813, 2, 2, 5, '项目完成');
INSERT INTO "main"."itype_business" VALUES (814, 2, 2, 5, '项目重启');
INSERT INTO "main"."itype_business" VALUES (815, 2, 1, 4, '项目参与人添加');
INSERT INTO "main"."itype_business" VALUES (816, 2, 3, 4, '项目参与人删除');
INSERT INTO "main"."itype_business" VALUES (818, 2, 2, 5, '项目负责人转移');
INSERT INTO "main"."itype_business" VALUES (820, 2, 1, 6, '项目动态回复');
INSERT INTO "main"."itype_business" VALUES (821, 2, 3, 6, '项目动态删除');
INSERT INTO "main"."itype_business" VALUES (904, 8, 1, 7, '会议发布');
INSERT INTO "main"."itype_business" VALUES (905, 8, 2, 7, '会议修改');
INSERT INTO "main"."itype_business" VALUES (906, 8, 1, 8, '会议进展新增');
INSERT INTO "main"."itype_business" VALUES (907, 8, 3, 7, '会议删除');
INSERT INTO "main"."itype_business" VALUES (908, 8, 3, 8, '会议进展删除');
INSERT INTO "main"."itype_business" VALUES (909, 8, 2, 7, '会议完成');
INSERT INTO "main"."itype_business" VALUES (910, 8, 2, 7, '会议重启');
INSERT INTO "main"."itype_business" VALUES (916, 8, 1, 7, '会议参与人添加');
INSERT INTO "main"."itype_business" VALUES (917, 8, 3, 7, '会议参与人移除');
INSERT INTO "main"."itype_business" VALUES (931, 8, 2, 7, '会议负责人转交');
INSERT INTO "main"."itype_business" VALUES (1008, 3, 1, 4, 'C项目参与人添加');
INSERT INTO "main"."itype_business" VALUES (1009, 3, 2, 9, 'C项目修改');
INSERT INTO "main"."itype_business" VALUES (1010, 3, 3, 9, 'C项目删除');
INSERT INTO "main"."itype_business" VALUES (1011, 3, 3, 4, 'C项目参与人移除');
INSERT INTO "main"."itype_business" VALUES (1012, 3, 3, 6, 'C项目动态删除');
INSERT INTO "main"."itype_business" VALUES (1013, 3, 2, 9, 'C项目完成');
INSERT INTO "main"."itype_business" VALUES (1014, 3, 1, 9, 'C项目发布');
INSERT INTO "main"."itype_business" VALUES (1015, 3, 1, 6, 'C项目动态回复');
INSERT INTO "main"."itype_business" VALUES (1016, 3, 2, 9, 'C项目重启');
INSERT INTO "main"."itype_business" VALUES (1017, 3, 2, 9, 'C项目负责人转移');
