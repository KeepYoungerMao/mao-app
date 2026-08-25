-- ------------------------------------------------
-- version 8.0.18
-- DATABASE mao-mms
-- ------------------------------------------------


-- ------------------------------------------------
-- 系统 sys 部分
-- ------------------------------------------------

-- 字典
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    `description` VARCHAR(256) COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 字典项
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `pid` INT NOT NULL COMMENT '父主键',
    `name` VARCHAR(100) NOT NULL COMMENT '名称',
    `status` INT NOT NULL DEFAULT 1 COMMENT '状态，1：启用，0：禁用',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY(`pid`, `name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 省市区字典
DROP TABLE IF EXISTS `sys_province_city_district`;
CREATE TABLE `sys_province_city_district`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `pid` INT NOT NULL COMMENT '父主键',
    `code` VARCHAR(100) NOT NULL COMMENT '编码',
    `name` VARCHAR(100) NOT NULL COMMENT '名称',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 行业字典-2017版
DROP TABLE IF EXISTS `sys_industry_2017`;
CREATE TABLE `sys_industry_2017`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `pid` INT NOT NULL DEFAULT 0 COMMENT '父主键',
    `code` VARCHAR(20) NOT NULL COMMENT '编码',
    `name` VARCHAR(200) NOT NULL COMMENT '名称',
    `description` VARCHAR(500) COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 系统操作日志
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(20) NOT NULL COMMENT '操作人',
    `scope` VARCHAR(20) NOT NULL COMMENT '操作域',
    `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
    `operation` VARCHAR(50) NOT NULL COMMENT '操作项',
    `description` VARCHAR(200) COMMENT '操作描述',
    `method` VARCHAR(20) COMMENT 'HTTP METHOD',
    `ip` VARCHAR(50) COMMENT '操作人IP',
    `success` TINYINT NOT NULL DEFAULT 1 COMMENT '是否成功',
    `error_message` TEXT COMMENT '错误信息',
    `operation_time` DATETIME(3) NOT NULL COMMENT '操作时间',
    `cost` BIGINT NOT NULL DEFAULT 0 COMMENT '接口耗时',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 系统服务指标
DROP TABLE IF EXISTS `sys_server_metric`;
CREATE TABLE `sys_server_metric`
(
    `id` BIGINT NOT NULL COMMENT '主键',
    `minute_start` DATETIME NOT NULL COMMENT '记录分钟数',
    `total_requests` BIGINT NOT NULL COMMENT '分钟内总请求数',
    `success_requests` BIGINT NOT NULL COMMENT '分钟内成功请求数',
    `error_requests` BIGINT NOT NULL COMMENT '分钟内失败请求数',
    `total_response_time_millis` BIGINT NOT NULL COMMENT '耗时总和',
    `avg_response_time_millis` BIGINT NOT NULL COMMENT '平均响应时间',
    `online_users` INT NOT NULL COMMENT '在线人数',
    `login_users` INT NOT NULL COMMENT '今日总登陆人数',
    `created_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建日期',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`minute_start`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 用户
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(20) COMMENT '用户名',
    `password` VARCHAR(300) COMMENT '密码',
    `avatar` VARCHAR(300) COMMENT '头像',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(32) COMMENT '邮箱',
    `enabled` TINYINT DEFAULT 1 COMMENT '是否可使用',
    `expired` TINYINT DEFAULT 0 COMMENT '是否过期',
    `locked` TINYINT DEFAULT 0 COMMENT '是否锁定',
    `expire_time` DATETIME(3) COMMENT '过期时间',
    `last_login_time` DATETIME(3) COMMENT '上次登陆时间',
    `password_status` INT COMMENT '密码状态，0：正常，1：首次需要更改密码，2：密码已更改，3：密码已重置',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`username`),
    UNIQUE KEY (`phone`),
    UNIQUE KEY (`email`)
) ENGINE = InnoDB AUTO_INCREMENT=10000001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 角色
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(20) COMMENT '角色名称',
    `description` VARCHAR(20) COMMENT '角色描述',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 用户-角色关系表
DROP TABLE IF EXISTS `sys_user_role_ref`;
CREATE TABLE `sys_user_role_ref`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` INT NOT NULL COMMENT '用户id',
    `role_id` INT NOT NULL COMMENT '角色id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 权限
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `pid` INT NOT NULL DEFAULT 0 COMMENT '父类主键',
    `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `api` varchar(200) NOT NULL COMMENT '接口api',
    `order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 角色-权限关系表
DROP TABLE IF EXISTS `sys_role_permission_ref`;
CREATE TABLE `sys_role_permission_ref`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` INT NOT NULL COMMENT '角色id',
    `permission_id` INT NOT NULL COMMENT '权限id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 用户资料信息
-- 基本信息 sys_user_profile
DROP TABLE IF EXISTS `sys_user_profile`;
CREATE TABLE `sys_user_profile`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` INT NOT NULL COMMENT '用户id',
    `user_code` VARCHAR(10) NOT NULL COMMENT '用户编号',
    `real_name` VARCHAR(20) NOT NULL COMMENT '姓名',
    `sex_id` INT COMMENT '性别ID',
    `entry_date` DATE COMMENT '入职日期',
    `id_card_num` VARCHAR(18) NOT NULL COMMENT '身份证号',
    `blood_type_id` INT COMMENT '血型ID',
    `high` DECIMAL(5,2) COMMENT '身高，单位CM',
    `weight` DECIMAL(5,2) COMMENT '体重，单位KG',
    `province_id` INT COMMENT '省ID',
    `city_id` INT COMMENT '市ID',
    `district_id` INT COMMENT '区ID',
    `address` VARCHAR(200) COMMENT '详细地址',
    `birthday` DATE COMMENT '出生日期',
    `nation_id` INT COMMENT '民族ID',
    `country_id` INT COMMENT '国籍ID',
    `marital_id` INT COMMENT '婚姻状况ID',
    `political_id` INT COMMENT '政治面貌ID',
    `education_id` INT COMMENT '学历ID',
    `major` VARCHAR(50) COMMENT '专业',
    `origin_province_id` INT COMMENT '籍贯省ID',
    `origin_city_id` INT COMMENT '籍贯市ID',
    `origin_district_id` INT COMMENT '籍贯区ID',
    `origin_address` VARCHAR(200) COMMENT '籍贯详细地址',
    `family_phone` varchar(20) COMMENT '家庭电话',
    `hobby` VARCHAR(100) COMMENT '兴趣爱好',
    `remark` VARCHAR(200) COMMENT '备注',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY user_id(`user_id`),
    KEY user_code(`user_code`),
    UNIQUE KEY (`id_card_num`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- 教育经历 sys_user_profile_education
DROP TABLE IF EXISTS `sys_user_profile_education`;
CREATE TABLE `sys_user_profile_education`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` INT NOT NULL COMMENT '用户id',
    `institution_name` VARCHAR(50) NOT NULL COMMENT '学校/教育机构名称',
    `degree` VARCHAR(30) COMMENT '获得的学位',
    `major` VARCHAR(50) COMMENT '专业名称',
    `start_date` DATE COMMENT '入学日期',
    `end_date` DATE COMMENT '毕业日期',
    `additional_info` TEXT COMMENT '其他教育相关经历',
    `order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY user_id(`user_id`),
    UNIQUE KEY(`user_id`, `institution_name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- 工作经历 sys_user_profile_work
DROP TABLE IF EXISTS `sys_user_profile_work`;
CREATE TABLE `sys_user_profile_work`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` INT NOT NULL COMMENT '用户id',
    `company_name` VARCHAR(50) NOT NULL COMMENT '公司/单位名称',
    `job_title` VARCHAR(30) COMMENT '职位名称',
    `industry` VARCHAR(50) COMMENT '所在行业',
    `start_date` DATE COMMENT '入职日期',
    `end_date` DATE COMMENT '离职日期',
    `responsibilities` TEXT COMMENT '工作职责和主要成就',
    `current_employment` TINYINT DEFAULT 0 COMMENT '是否在职',
    `order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY user_id(`user_id`),
    UNIQUE KEY(`user_id`, `company_name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- 人员关系 sys_user_profile_relationship
DROP TABLE IF EXISTS `sys_user_profile_relationship`;
CREATE TABLE `sys_user_profile_relationship`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` INT NOT NULL COMMENT '用户id',
    `real_name` VARCHAR(20) NOT NULL COMMENT '姓名',
    `relationship_id` INT NOT NULL COMMENT '人员关系ID',
    `id_card_num` VARCHAR(18) COMMENT '身份证号',
    `phone` VARCHAR(20) COMMENT '联系方式',
    `remark` VARCHAR(300) COMMENT '备注',
    `order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY user_id(`user_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- 补充材料 sys_user_profile_material
DROP TABLE IF EXISTS `sys_user_profile_material`;
CREATE TABLE `sys_user_profile_material`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` INT NOT NULL COMMENT '用户id',
    `material_name` VARCHAR(50) NOT NULL COMMENT '材料名称',
    `file_path` VARCHAR(1000) NOT NULL COMMENT '文件存储路径，包含文件名',
    `upload_time` DATETIME(3) NOT NULL COMMENT '文件上传时间',
    `description` TEXT COMMENT '补充说明',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY user_id(`user_id`),
    UNIQUE KEY(`user_id`, `material_name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 清理旧版多公司表
DROP TABLE IF EXISTS `sys_company`;

-- 部门表
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `pid` INT NOT NULL COMMENT '父类id',
    `department_name` VARCHAR(100) NOT NULL COMMENT '部门名称',
    `description` VARCHAR(300) COMMENT '描述',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`department_name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 用户 - 部门关系表
DROP TABLE IF EXISTS `sys_user_department_ref`;
CREATE TABLE `sys_user_department_ref`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` INT NOT NULL COMMENT '用户id',
    `department_id` INT NOT NULL COMMENT '部门id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


-- ------------------------------------------------
-- 系统 sys 部分
-- ------------------------------------------------


-- 图片
DROP TABLE IF EXISTS `data_picture`;
CREATE TABLE `data_picture` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(200) NOT NULL COMMENT '标题',
    `type_id` INT NOT NULL COMMENT '父类型id',
    `sub_type_id` INT NOT NULL COMMENT '子类型id',
    `image_url` VARCHAR(100) COMMENT '图片',
    `origin_image_url` VARCHAR(100) COMMENT '原始图片',
    `keyword` VARCHAR(1000) COMMENT '标签',
    `image_type` VARCHAR(5) COMMENT '图片类型',
    `width` INT NOT NULL COMMENT '宽度',
    `height` INT NOT NULL COMMENT '长度',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    FULLTEXT INDEX `picture_text_index`(`name`, `keyword`) WITH PARSER `ngram`
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 图片分类
DROP TABLE IF EXISTS `data_picture_type`;
CREATE TABLE `data_picture_type` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `pid` INT NOT NULL DEFAULT 0 COMMENT '父类id',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY(`name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 古代书籍
-- 古代书籍类型[1-18]：传记、武侠、历史演义、军事、史记、神魔、地理、诗词、笔记、医学、小传、音乐、算术、评书、百科、佛教、风水、农业
-- 朝代[1-15]：先秦、秦朝、汉朝、三国时期、东西晋、南北朝、隋朝、唐朝、五代十国、宋朝、金朝、元朝、明朝、清朝、近代
DROP TABLE IF EXISTS `data_ancient_book`;
CREATE TABLE `data_ancient_book` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    `author` VARCHAR(50) NOT NULL COMMENT '作者',
    `editor` VARCHAR(50) NOT NULL COMMENT '编辑人/出版人',
    `image_url` VARCHAR(100) COMMENT '图片',
    `origin_image_url` VARCHAR(100) COMMENT '原始图片',
    `publication_time` VARCHAR(100) COMMENT '初版时间',
    `summer` TEXT COMMENT '简介',
    `score` DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '评分',
    `comments` INT NOT NULL DEFAULT 0 COMMENT '评论人次',
    `book_status_id` INT NOT NULL DEFAULT 0 COMMENT '0:正常,1:下架2:推荐3:热卖',
    `fonts` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '字数',
    `ancient_book_type_id` INT NOT NULL DEFAULT 0 COMMENT '古代书籍类型id',
    `dynasty_id` INT NOT NULL DEFAULT 0 COMMENT '朝代id',
    `keyword` VARCHAR(1000) COMMENT '关键词',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 古代书籍-章节
DROP TABLE IF EXISTS `data_ancient_book_chapter`;
CREATE TABLE `data_ancient_book_chapter` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `book_id` INT NOT NULL COMMENT '书籍id',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    `order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `content` MEDIUMTEXT COMMENT '章节内容',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY (`book_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- LOL-英雄
DROP TABLE IF EXISTS `data_lol_hero`;
CREATE TABLE `data_lol_hero`
(
    `hero_id` INT NOT NULL COMMENT '英雄id',
    `name` varchar(50) COMMENT '名称',
    `title` varchar(50) COMMENT '标题',
    `alias` varchar(50) COMMENT '英文名称',
    `roles` varchar(100) COMMENT '角色',
    `introduce` varchar(2000) COMMENT '介绍',
    `short_bio` varchar(2000) COMMENT '短介绍',
    `attack` INT COMMENT '攻击指数',
    `defense` INT COMMENT '防御指数',
    `magic` INT COMMENT '魔法指数',
    `difficulty` INT COMMENT '操作难度',
    `hp` DECIMAL(9, 4) COMMENT '血量',
    `hp_per_level` DECIMAL(9, 4) COMMENT '每级血量成长',
    `mp` DECIMAL(9, 4) COMMENT '法力',
    `mp_per_level` DECIMAL(9, 4) COMMENT '每级法力成长',
    `move_speed` DECIMAL(9, 4) COMMENT '移动速度',
    `armor` DECIMAL(9, 4) COMMENT '护甲',
    `armor_per_level` DECIMAL(9, 4) COMMENT '每级护甲成长',
    `spell_block` DECIMAL(9, 4) COMMENT '魔抗',
    `spell_block_per_level` DECIMAL(9, 4) COMMENT '每级魔抗成长',
    `attack_range` DECIMAL(9, 4) COMMENT '攻击距离',
    `hp_regen` DECIMAL(9, 4) COMMENT '生命回复',
    `hp_regen_per_level` DECIMAL(9, 4) COMMENT '每级生命回复成长',
    `mp_regen` DECIMAL(9, 4) COMMENT '法力回复',
    `mp_regen_per_level` DECIMAL(9, 4) COMMENT '每级法力回复成长',
    `crit` DECIMAL(9, 4) COMMENT '暴击',
    `crit_per_level` DECIMAL(9, 4) COMMENT '每级暴击成长',
    `attack_damage` DECIMAL(9, 4) COMMENT '攻击力',
    `attack_damage_per_level` DECIMAL(9, 4) COMMENT '每级攻击力成长',
    `attack_speed` DECIMAL(9, 4) COMMENT '攻击速度',
    `attack_speed_per_level` DECIMAL(9, 4) COMMENT '每级攻击速度成长',
    `ally_tips` varchar(2000) COMMENT '队友建议',
    `enemy_tips` varchar(2000) COMMENT '对手建议',
    `damage_type` VARCHAR(30) COMMENT '伤害类型',
    `style` INT COMMENT '伤害风格',
    `difficulty_level` INT COMMENT '操作难度',
    `damage` INT COMMENT '伤害系数',
    `durability` INT COMMENT '抗伤系数',
    `crowd_control` INT COMMENT '群体控制',
    `mobility` INT COMMENT '移动系数',
    `utility` INT COMMENT '实用系数',
    `select_audio` varchar(200) COMMENT '英雄选择语音',
    `ban_audio` varchar(200) COMMENT '英雄禁掉语音',
    `gold_price` int(9) COMMENT '金币购买价格',
    `coupon_price` int(9) COMMENT '券购买价格',
    `keywords` varchar(200) COMMENT '关键词',
    `avatar` varchar(200) COMMENT '头像',
    `loading_image` varchar(200) COMMENT '原皮肤',
    `guide_image` varchar(200) COMMENT '指南图片',
    `position` varchar(100) COMMENT '位置',
    PRIMARY KEY (`hero_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- LOL-英雄-皮肤
DROP TABLE IF EXISTS `data_lol_hero_skin`;
CREATE TABLE `data_lol_hero_skin`
(
    `skin_id` INT COMMENT '皮肤id',
    `hero_id` INT COMMENT '英雄id',
    `hero_name` varchar(50) COMMENT '英雄名称',
    `hero_title` varchar(50) COMMENT '英雄标题',
    `name` varchar(50) COMMENT '皮肤名称',
    `chroma` TINYINT COMMENT '是否是炫彩皮肤',
    `chroma_belong_id` INT COMMENT '属于哪个皮肤',
    `base` TINYINT COMMENT '是否为基础皮肤',
    `emblems_name` varchar(50),
    `description` varchar(500) COMMENT '描述',
    `main_img` varchar(200) COMMENT '主图',
    `icon_img` varchar(200) COMMENT '图标',
    `loading_img` varchar(200) COMMENT '加载皮肤',
    `video_img` varchar(200) COMMENT '视频图片',
    `source_img` varchar(200) COMMENT '原图片',
    `chroma_img` varchar(200) COMMENT '炫彩皮肤图片',
    PRIMARY KEY (`skin_id`),
    KEY hero_id(`hero_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- LOL-英雄-技能
DROP TABLE IF EXISTS `data_lol_hero_spell`;
CREATE TABLE `data_lol_hero_spell`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `hero_id` INT COMMENT '英雄id',
    `spell_key` varchar(10) COMMENT '按键',
    `name` varchar(50) COMMENT '技能名称',
    `description` varchar(2000) COMMENT '技能描述',
    `ability_icon` varchar(300) COMMENT '技能图标',
    `ability_video` varchar(300) COMMENT '技能演示',
    `dynamic_description` varchar(2000) COMMENT '动态描述',
    `cost` varchar(200) COMMENT '消耗',
    `cost_burn` varchar(200) COMMENT '消耗描述',
    `cool_down` varchar(200) COMMENT '冷却',
    `cool_down_burn` varchar(200) COMMENT '冷却描述',
    `spell_range` varchar(200) COMMENT '施法范围',
    PRIMARY KEY (`id`),
    KEY hero_id(`hero_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- LOL-英雄-装备
DROP TABLE IF EXISTS `data_lol_item`;
CREATE TABLE `data_lol_item`
(
    `item_id` INT COMMENT '物品id',
    `name` VARCHAR(20) COMMENT '名称',
    `icon_path` VARCHAR(200) COMMENT '图标',
    `price` INT COMMENT '价格',
    `description` VARCHAR(2000) COMMENT '描述',
    `maps` VARCHAR(200) COMMENT '所在地图',
    `plain_text` VARCHAR(2000) COMMENT '简要描述',
    `sell` INT COMMENT '售价',
    `total` INT COMMENT '总价格',
    `make_into` VARCHAR(500) COMMENT '可合成',
    `make_from` VARCHAR(500) COMMENT '由什么合成',
    `suit_hero_ids` VARCHAR(500) COMMENT '适合英雄',
    `types` VARCHAR(500) COMMENT '物品类型',
    `keywords` VARCHAR(500) COMMENT '关键词',
    `item_desc` VARCHAR(2000) COMMENT '装备描述',
    PRIMARY KEY (`item_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- LOL-英雄-召唤师技能
DROP TABLE IF EXISTS `data_lol_summoner`;
CREATE TABLE `data_lol_summoner`
(
    `id` INT COMMENT '技能id',
    `name` VARCHAR(20) COMMENT '名称',
    `icon_path` VARCHAR(200) COMMENT '图标',
    `description` VARCHAR(200) COMMENT '描述',
    `summoner_level` INT COMMENT '召唤师等级',
    `cool_down` INT COMMENT '冷却时间',
    `game_mode` VARCHAR(200) COMMENT '游戏模式',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- LOL-英雄-召唤师技能
DROP TABLE IF EXISTS `data_lol_rune`;
CREATE TABLE `data_lol_rune`
(
    `id` INT COMMENT '技能id',
    `name` VARCHAR(50) COMMENT '名称',
    `icon_path` VARCHAR(200) COMMENT '图标',
    `key` VARCHAR(50) COMMENT 'key',
    `tooltip` VARCHAR(2000) COMMENT '提示',
    `short_desc` VARCHAR(2000) COMMENT '短描述',
    `long_desc` VARCHAR(2000) COMMENT '长描述',
    `slot_label` VARCHAR(50) COMMENT '组标签',
    `style_name` VARCHAR(50) COMMENT '风格标签',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 中国姓氏
DROP TABLE IF EXISTS `data_chinese_surname`;
CREATE TABLE `data_chinese_surname`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name` VARCHAR(20) COMMENT '名称',
    `traditional_name` VARCHAR(20) COMMENT '繁体名称',
    `pinyin` VARCHAR(100) COMMENT '拼音',
    `char_order` VARCHAR(1) COMMENT '字符排序',
    `order` INT COMMENT '排名',
    `images` VARCHAR(50) COMMENT '图片地址',
    `people_count` VARCHAR(30) COMMENT '人数',
    `origin` TEXT COMMENT '起源',
    `distribution` TEXT COMMENT '分布',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 诗词
DROP TABLE IF EXISTS `data_poem`;
CREATE TABLE `data_poem`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `poet` VARCHAR(20) COMMENT '诗人',
    `poet_id` INT COMMENT '关联诗人id',
    `dynasty_id` INT COMMENT '朝代id',
    `content` TEXT COMMENT '内容',
    `remark` VARCHAR(100) COMMENT '备注',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 诗词作者
DROP TABLE IF EXISTS `data_poet`;
CREATE TABLE `data_poet`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name` VARCHAR(100) NOT NULL COMMENT '名称',
    `dynasty_id` INT COMMENT '朝代id',
    `avatar` VARCHAR(200) COMMENT '头像地址',
    `intro` TEXT COMMENT '介绍',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 网络节目
DROP TABLE IF EXISTS `data_live`;
CREATE TABLE `data_live`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    `group_title` VARCHAR(50) COMMENT '分组标题',
    `live_type_id` INT NOT NULL COMMENT '网络节目类型',
    `image_url` VARCHAR(200) COMMENT '图片地址',
    `live_url` VARCHAR(300) COMMENT '网络节目地址',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 中草药
DROP TABLE IF EXISTS `data_crude_drug`;
CREATE TABLE `data_crude_drug`
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name` VARCHAR(20) NOT NULL COMMENT '名称',
    `title` VARCHAR(50) COMMENT '标题',
    `alias` VARCHAR(200) COMMENT '别名',
    `en_name` VARCHAR(50) COMMENT '英文名',
    `image_url` VARCHAR(100) COMMENT '图片地址',
    `medicinal_part` VARCHAR(1000) COMMENT '药用部位',
    `plant_shape` VARCHAR(1000) COMMENT '植物形态',
    `origin_distribution` VARCHAR(1000) COMMENT '产地分布',
    `collect_process` VARCHAR(1000) COMMENT '收集加工',
    `trait` VARCHAR(1000) COMMENT '药材性状',
    `channel_tropism` VARCHAR(50) COMMENT '性味归经',
    `efficacy` VARCHAR(1000) COMMENT '功效与作用',
    `clinical_practice` VARCHAR(1000) COMMENT '临床研究',
    `pharmacological_research` VARCHAR(1000) COMMENT '药理研究',
    `chemical_composition` VARCHAR(1000) COMMENT '化学成分',
    `use_taboo` VARCHAR(1000) COMMENT '使用禁忌',
    `compatibility_prescription` VARCHAR(1000) COMMENT '药方配伍',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标识',
    `creator` VARCHAR(20) COMMENT '创建用户',
    `create_time` DATETIME(3) COMMENT '创建时间',
    `updater` VARCHAR(20) COMMENT '更新用户',
    `update_time` DATETIME(3) COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
