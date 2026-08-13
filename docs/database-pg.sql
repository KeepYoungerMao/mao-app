-- ------------------------------------------------
-- version xxx
-- DATABASE mao_mms
-- mao / maomao
-- ------------------------------------------------

-- ------------------------------------------------
-- 安装中文分词扩展、三元组索引
-- ------------------------------------------------
CREATE EXTENSION IF NOT EXISTS zhparser;
CREATE TEXT SEARCH CONFIGURATION chinese (PARSER = zhparser);
ALTER TEXT SEARCH CONFIGURATION chinese ADD MAPPING FOR n,v,a,i,e,l WITH simple;

CREATE EXTENSION IF NOT EXISTS pg_trgm;


-- ------------------------------------------------
-- 系统 sys 部分
-- ------------------------------------------------

-- 字典
-- 字典表
DROP TABLE IF EXISTS sys_dict;
CREATE TABLE sys_dict (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(256)
);
COMMENT ON TABLE sys_dict IS '字典表';
COMMENT ON COLUMN sys_dict.id IS '主键';
COMMENT ON COLUMN sys_dict.name IS '名称';
COMMENT ON COLUMN sys_dict.description IS '描述';

-- 字典项表
DROP TABLE IF EXISTS sys_dict_item;
CREATE TABLE sys_dict_item (
   id SERIAL PRIMARY KEY,
   pid INTEGER NOT NULL,
   name VARCHAR(100) NOT NULL,
   creator VARCHAR(20),
   create_time TIMESTAMP(3),
   updater VARCHAR(20),
   update_time TIMESTAMP(3)
);
COMMENT ON TABLE sys_dict_item IS '字典项表';
COMMENT ON COLUMN sys_dict_item.id IS '主键';
COMMENT ON COLUMN sys_dict_item.pid IS '父主键';
COMMENT ON COLUMN sys_dict_item.name IS '名称';
COMMENT ON COLUMN sys_dict_item.creator IS '创建用户';
COMMENT ON COLUMN sys_dict_item.create_time IS '创建时间';
COMMENT ON COLUMN sys_dict_item.updater IS '更新用户';
COMMENT ON COLUMN sys_dict_item.update_time IS '更新时间';


-- 省市区字典表
DROP TABLE IF EXISTS sys_province_city_district;
CREATE TABLE sys_province_city_district (
    id SERIAL PRIMARY KEY,
    pid INTEGER NOT NULL,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE sys_province_city_district IS '省市区字典表';
COMMENT ON COLUMN sys_province_city_district.id IS '主键';
COMMENT ON COLUMN sys_province_city_district.pid IS '父主键';
COMMENT ON COLUMN sys_province_city_district.code IS '编码';
COMMENT ON COLUMN sys_province_city_district.name IS '名称';
COMMENT ON COLUMN sys_province_city_district.creator IS '创建用户';
COMMENT ON COLUMN sys_province_city_district.create_time IS '创建时间';
COMMENT ON COLUMN sys_province_city_district.updater IS '更新用户';
COMMENT ON COLUMN sys_province_city_district.update_time IS '更新时间';

-- 行业字典-2017版
DROP TABLE IF EXISTS sys_industry_2017;
CREATE TABLE sys_industry_2017 (
    id SERIAL PRIMARY KEY,
    pid INTEGER NOT NULL DEFAULT 0,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500)
);
COMMENT ON TABLE sys_industry_2017 IS '行业字典-2017版';
COMMENT ON COLUMN sys_industry_2017.id IS '主键';
COMMENT ON COLUMN sys_industry_2017.pid IS '父主键';
COMMENT ON COLUMN sys_industry_2017.code IS '编码';
COMMENT ON COLUMN sys_industry_2017.name IS '名称';
COMMENT ON COLUMN sys_industry_2017.description IS '描述';

-- 系统操作日志
-- 操作日志表
DROP TABLE IF EXISTS sys_operate_log;
CREATE TABLE sys_operate_log (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    scope VARCHAR(20) NOT NULL,
    module VARCHAR(50) NOT NULL,
    operation VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    method VARCHAR(20),
    ip VARCHAR(50),
    success BOOLEAN DEFAULT TRUE,
    error_message TEXT,
    operation_time TIMESTAMP(3) NOT NULL,
    cost BIGINT NOT NULL DEFAULT 0
);
COMMENT ON TABLE sys_operate_log IS '操作日志表';
COMMENT ON COLUMN sys_operate_log.id IS '主键';
COMMENT ON COLUMN sys_operate_log.username IS '操作人';
COMMENT ON COLUMN sys_operate_log.scope IS '操作域';
COMMENT ON COLUMN sys_operate_log.module IS '操作模块';
COMMENT ON COLUMN sys_operate_log.operation IS '操作项';
COMMENT ON COLUMN sys_operate_log.description IS '操作描述';
COMMENT ON COLUMN sys_operate_log.method IS 'HTTP METHOD';
COMMENT ON COLUMN sys_operate_log.ip IS '操作人IP';
COMMENT ON COLUMN sys_operate_log.success IS '是否成功';
COMMENT ON COLUMN sys_operate_log.error_message IS '错误信息';
COMMENT ON COLUMN sys_operate_log.operation_time IS '操作时间';
COMMENT ON COLUMN sys_operate_log.cost IS '接口耗时';

-- 系统服务指标
DROP TABLE IF EXISTS sys_server_metric;
CREATE TABLE sys_server_metric (
    id              BIGINT        NOT NULL,
    minute_start    TIMESTAMP     NOT NULL,
    total_requests  BIGINT        NOT NULL,
    success_requests BIGINT       NOT NULL,
    error_requests  BIGINT        NOT NULL,
    total_response_time_millis BIGINT NOT NULL,
    avg_response_time_millis  BIGINT NOT NULL,
    online_users    INTEGER       NOT NULL,
    login_users     INTEGER       NOT NULL,
    created_time    TIMESTAMP(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (minute_start)
);
COMMENT ON TABLE  sys_server_metric IS '系统服务指标';
COMMENT ON COLUMN sys_server_metric.id IS '主键';
COMMENT ON COLUMN sys_server_metric.minute_start IS '记录分钟数';
COMMENT ON COLUMN sys_server_metric.total_requests IS '分钟内总请求数';
COMMENT ON COLUMN sys_server_metric.success_requests IS '分钟内成功请求数';
COMMENT ON COLUMN sys_server_metric.error_requests IS '分钟内失败请求数';
COMMENT ON COLUMN sys_server_metric.total_response_time_millis IS '耗时总和';
COMMENT ON COLUMN sys_server_metric.avg_response_time_millis IS '平均响应时间';
COMMENT ON COLUMN sys_server_metric.online_users IS '在线人数';
COMMENT ON COLUMN sys_server_metric.login_users IS '今日总登陆人数';
COMMENT ON COLUMN sys_server_metric.created_time IS '创建日期';

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20),
    password VARCHAR(300),
    avatar VARCHAR(300),
    phone VARCHAR(20),
    email VARCHAR(32),
    enabled BOOLEAN DEFAULT TRUE,
    expired BOOLEAN DEFAULT FALSE,
    locked BOOLEAN DEFAULT FALSE,
    expire_time TIMESTAMP(3),
    last_login_time TIMESTAMP(3),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE sys_user IS '用户表';
COMMENT ON COLUMN sys_user.id IS '主键';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.avatar IS '头像';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.enabled IS '是否可使用（true=可用，false=不可用）';
COMMENT ON COLUMN sys_user.expired IS '是否过期（true=已过期，false=未过期）';
COMMENT ON COLUMN sys_user.locked IS '是否锁定（true=已锁定，false=未锁定）';
COMMENT ON COLUMN sys_user.expire_time IS '过期时间';
COMMENT ON COLUMN sys_user.last_login_time IS '上次登陆时间';
COMMENT ON COLUMN sys_user.creator IS '创建用户';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.updater IS '更新用户';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
-- 当你使用 SERIAL 或 BIGSERIAL 创建表时，PostgreSQL 会自动创建一个序列（SEQUENCE），序列的命名规则是：表名_字段名_seq
SELECT setval('sys_user_id_seq', 10000000, false);
CREATE INDEX idx_sys_user_username_trgm ON sys_user USING GIN (username gin_trgm_ops);
CREATE INDEX idx_sys_user_phone_trgm ON sys_user USING GIN (phone gin_trgm_ops);
CREATE INDEX idx_sys_user_email_trgm ON sys_user USING GIN (email gin_trgm_ops);

-- 角色表
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    description VARCHAR(20),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '主键';
COMMENT ON COLUMN sys_role.name IS '角色名称';
COMMENT ON COLUMN sys_role.description IS '角色描述';
COMMENT ON COLUMN sys_role.creator IS '创建用户';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.updater IS '更新用户';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
CREATE INDEX idx_sys_role_name_trgm ON sys_role USING GIN (name gin_trgm_ops);

-- 用户-角色关系表
DROP TABLE IF EXISTS sys_user_role_ref;
CREATE TABLE sys_user_role_ref (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL
);
COMMENT ON TABLE sys_user_role_ref IS '用户-角色关系表';
COMMENT ON COLUMN sys_user_role_ref.id IS '主键';
COMMENT ON COLUMN sys_user_role_ref.user_id IS '用户id';
COMMENT ON COLUMN sys_user_role_ref.role_id IS '角色id';

-- 权限表
DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
    id SERIAL PRIMARY KEY,
    pid INTEGER NOT NULL DEFAULT 0,
    name VARCHAR(50) NOT NULL,
    api VARCHAR(200) NOT NULL,
    "order" INTEGER NOT NULL DEFAULT 0
);
COMMENT ON TABLE sys_permission IS '权限表';
COMMENT ON COLUMN sys_permission.id IS '主键';
COMMENT ON COLUMN sys_permission.pid IS '父类主键';
COMMENT ON COLUMN sys_permission.name IS '权限名称';
COMMENT ON COLUMN sys_permission.api IS '接口api';
COMMENT ON COLUMN sys_permission."order" IS '排序';

-- 角色-权限关系表
DROP TABLE IF EXISTS sys_role_permission_ref;
CREATE TABLE sys_role_permission_ref (
    id BIGSERIAL PRIMARY KEY,
    role_id INTEGER NOT NULL,
    permission_id INTEGER NOT NULL
);
COMMENT ON TABLE sys_role_permission_ref IS '角色-权限关系表';
COMMENT ON COLUMN sys_role_permission_ref.id IS '主键';
COMMENT ON COLUMN sys_role_permission_ref.role_id IS '角色id';
COMMENT ON COLUMN sys_role_permission_ref.permission_id IS '权限id';

-- 用户资料信息表
DROP TABLE IF EXISTS sys_user_profile;
CREATE TABLE sys_user_profile (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    user_code VARCHAR(10) NOT NULL,
    real_name VARCHAR(20) NOT NULL,
    sex_id INTEGER,
    entry_time DATE,
    id_card_num VARCHAR(18) NOT NULL,
    blood_type_id INTEGER,
    high DECIMAL(5,2),
    weight DECIMAL(5,2),
    province_id INTEGER,
    city_id INTEGER,
    district_id INTEGER,
    address VARCHAR(200),
    birthday DATE,
    nation_id INTEGER,
    country_id INTEGER,
    marital_id INTEGER,
    political_id INTEGER,
    education_id INTEGER,
    major VARCHAR(50),
    origin_province_id INTEGER,
    origin_city_id INTEGER,
    origin_district_id INTEGER,
    origin_address VARCHAR(200),
    family_phone VARCHAR(20),
    hobby VARCHAR(100),
    remark VARCHAR(200),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE sys_user_profile IS '用户资料信息表';
COMMENT ON COLUMN sys_user_profile.id IS '主键';
COMMENT ON COLUMN sys_user_profile.user_id IS '用户id';
COMMENT ON COLUMN sys_user_profile.user_code IS '用户编号';
COMMENT ON COLUMN sys_user_profile.real_name IS '姓名';
COMMENT ON COLUMN sys_user_profile.sex_id IS '性别ID';
COMMENT ON COLUMN sys_user_profile.entry_time IS '入职时间';
COMMENT ON COLUMN sys_user_profile.id_card_num IS '身份证号';
COMMENT ON COLUMN sys_user_profile.blood_type_id IS '血型ID';
COMMENT ON COLUMN sys_user_profile.high IS '身高，单位CM';
COMMENT ON COLUMN sys_user_profile.weight IS '体重，单位KG';
COMMENT ON COLUMN sys_user_profile.province_id IS '省ID';
COMMENT ON COLUMN sys_user_profile.city_id IS '市ID';
COMMENT ON COLUMN sys_user_profile.district_id IS '区ID';
COMMENT ON COLUMN sys_user_profile.address IS '详细地址';
COMMENT ON COLUMN sys_user_profile.birthday IS '出生日期';
COMMENT ON COLUMN sys_user_profile.nation_id IS '民族ID';
COMMENT ON COLUMN sys_user_profile.country_id IS '国籍ID';
COMMENT ON COLUMN sys_user_profile.marital_id IS '婚姻状况ID';
COMMENT ON COLUMN sys_user_profile.political_id IS '政治面貌ID';
COMMENT ON COLUMN sys_user_profile.education_id IS '学历ID';
COMMENT ON COLUMN sys_user_profile.major IS '专业';
COMMENT ON COLUMN sys_user_profile.origin_province_id IS '籍贯省ID';
COMMENT ON COLUMN sys_user_profile.origin_city_id IS '籍贯市ID';
COMMENT ON COLUMN sys_user_profile.origin_district_id IS '籍贯区ID';
COMMENT ON COLUMN sys_user_profile.origin_address IS '籍贯详细地址';
COMMENT ON COLUMN sys_user_profile.family_phone IS '家庭电话';
COMMENT ON COLUMN sys_user_profile.hobby IS '兴趣爱好';
COMMENT ON COLUMN sys_user_profile.remark IS '备注';
COMMENT ON COLUMN sys_user_profile.creator IS '创建用户';
COMMENT ON COLUMN sys_user_profile.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_profile.updater IS '更新用户';
COMMENT ON COLUMN sys_user_profile.update_time IS '更新时间';
CREATE INDEX idx_user_profile_user_id ON sys_user_profile (user_id);
CREATE INDEX idx_user_profile_user_code ON sys_user_profile (user_code);
CREATE INDEX idx_user_profile_id_card_num_trgm ON sys_user_profile USING GIN (id_card_num gin_trgm_ops);

-- 教育经历表
DROP TABLE IF EXISTS sys_user_profile_education;
CREATE TABLE sys_user_profile_education (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    institution_name VARCHAR(50) NOT NULL,
    degree VARCHAR(30),
    major VARCHAR(50),
    start_date DATE,
    end_date DATE,
    additional_info TEXT,
    "order" INTEGER NOT NULL DEFAULT 0,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3),
    CONSTRAINT uk_user_education_unique UNIQUE (user_id, institution_name)
);
COMMENT ON TABLE sys_user_profile_education IS '教育经历表';
COMMENT ON COLUMN sys_user_profile_education.id IS '主键';
COMMENT ON COLUMN sys_user_profile_education.user_id IS '用户id';
COMMENT ON COLUMN sys_user_profile_education.institution_name IS '学校/教育机构名称';
COMMENT ON COLUMN sys_user_profile_education.degree IS '获得的学位';
COMMENT ON COLUMN sys_user_profile_education.major IS '专业名称';
COMMENT ON COLUMN sys_user_profile_education.start_date IS '入学日期';
COMMENT ON COLUMN sys_user_profile_education.end_date IS '毕业日期';
COMMENT ON COLUMN sys_user_profile_education.additional_info IS '其他教育相关经历';
COMMENT ON COLUMN sys_user_profile_education."order" IS '排序';
COMMENT ON COLUMN sys_user_profile_education.creator IS '创建用户';
COMMENT ON COLUMN sys_user_profile_education.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_profile_education.updater IS '更新用户';
COMMENT ON COLUMN sys_user_profile_education.update_time IS '更新时间';
CREATE INDEX idx_user_education_user_id ON sys_user_profile_education (user_id);

-- 工作经历表
DROP TABLE IF EXISTS sys_user_profile_work;
CREATE TABLE sys_user_profile_work (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    company_name VARCHAR(50) NOT NULL,
    job_title VARCHAR(30),
    industry VARCHAR(50),
    start_date DATE,
    end_date DATE,
    responsibilities TEXT,
    current_employment BOOLEAN DEFAULT FALSE,
    "order" INTEGER NOT NULL DEFAULT 0,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3),
    CONSTRAINT uk_user_work_unique UNIQUE (user_id, company_name)
);
COMMENT ON TABLE sys_user_profile_work IS '工作经历表';
COMMENT ON COLUMN sys_user_profile_work.id IS '主键';
COMMENT ON COLUMN sys_user_profile_work.user_id IS '用户id';
COMMENT ON COLUMN sys_user_profile_work.company_name IS '公司/单位名称';
COMMENT ON COLUMN sys_user_profile_work.job_title IS '职位名称';
COMMENT ON COLUMN sys_user_profile_work.industry IS '所在行业';
COMMENT ON COLUMN sys_user_profile_work.start_date IS '入职日期';
COMMENT ON COLUMN sys_user_profile_work.end_date IS '离职日期';
COMMENT ON COLUMN sys_user_profile_work.responsibilities IS '工作职责和主要成就';
COMMENT ON COLUMN sys_user_profile_work.current_employment IS '是否在职（true=在职，false=已离职）';
COMMENT ON COLUMN sys_user_profile_work."order" IS '排序';
COMMENT ON COLUMN sys_user_profile_work.creator IS '创建用户';
COMMENT ON COLUMN sys_user_profile_work.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_profile_work.updater IS '更新用户';
COMMENT ON COLUMN sys_user_profile_work.update_time IS '更新时间';
CREATE INDEX idx_user_work_user_id ON sys_user_profile_work (user_id);

-- 人员关系表
DROP TABLE IF EXISTS sys_user_profile_relationship;
CREATE TABLE sys_user_profile_relationship (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    real_name VARCHAR(20) NOT NULL,
    relationship_id INTEGER NOT NULL,
    id_card_num VARCHAR(18),
    phone VARCHAR(20),
    remark VARCHAR(300),
    "order" INTEGER NOT NULL DEFAULT 0,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE sys_user_profile_relationship IS '人员关系表';
COMMENT ON COLUMN sys_user_profile_relationship.id IS '主键';
COMMENT ON COLUMN sys_user_profile_relationship.user_id IS '用户id';
COMMENT ON COLUMN sys_user_profile_relationship.real_name IS '姓名';
COMMENT ON COLUMN sys_user_profile_relationship.relationship_id IS '人员关系ID（关联字典表）';
COMMENT ON COLUMN sys_user_profile_relationship.id_card_num IS '身份证号';
COMMENT ON COLUMN sys_user_profile_relationship.phone IS '联系方式';
COMMENT ON COLUMN sys_user_profile_relationship.remark IS '备注';
COMMENT ON COLUMN sys_user_profile_relationship."order" IS '排序';
COMMENT ON COLUMN sys_user_profile_relationship.creator IS '创建用户';
COMMENT ON COLUMN sys_user_profile_relationship.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_profile_relationship.updater IS '更新用户';
COMMENT ON COLUMN sys_user_profile_relationship.update_time IS '更新时间';
CREATE INDEX idx_user_relationship_user_id ON sys_user_profile_relationship (user_id);

-- 补充材料表
DROP TABLE IF EXISTS sys_user_profile_material;
CREATE TABLE sys_user_profile_material (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    material_name VARCHAR(50) NOT NULL,
    file_path VARCHAR(1000) NOT NULL,
    upload_time TIMESTAMP(3) NOT NULL,
    description TEXT,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE sys_user_profile_material IS '补充材料表';
COMMENT ON COLUMN sys_user_profile_material.id IS '主键';
COMMENT ON COLUMN sys_user_profile_material.user_id IS '用户id';
COMMENT ON COLUMN sys_user_profile_material.material_name IS '材料名称';
COMMENT ON COLUMN sys_user_profile_material.file_path IS '文件存储路径，包含文件名';
COMMENT ON COLUMN sys_user_profile_material.upload_time IS '文件上传时间';
COMMENT ON COLUMN sys_user_profile_material.description IS '补充说明';
COMMENT ON COLUMN sys_user_profile_material.creator IS '创建用户';
COMMENT ON COLUMN sys_user_profile_material.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_profile_material.updater IS '更新用户';
COMMENT ON COLUMN sys_user_profile_material.update_time IS '更新时间';
CREATE INDEX idx_user_material_user_id ON sys_user_profile_material (user_id);

-- 公司表
-- 公司类型：国有企业、私营企业、外资企业、合资企业
-- 所属行业：
-- 经营状况：正常运营、停业、注销
-- 公司表
DROP TABLE IF EXISTS sys_company;
CREATE TABLE sys_company (
    id SERIAL PRIMARY KEY,
    company_code VARCHAR(100) NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    unified_social_credit_code VARCHAR(18),
    company_type_id INTEGER,
    legal_representative VARCHAR(100),
    founded_date DATE,
    registered_capital VARCHAR(50),
    business_scope VARCHAR(500),
    registered_address VARCHAR(300),
    business_address VARCHAR(300),
    phone VARCHAR(50),
    email VARCHAR(50),
    official_website VARCHAR(100),
    enterprise_size VARCHAR(100),
    industry_id INTEGER,
    tax_registration_certificate_no VARCHAR(18),
    deposit_bank_and_account VARCHAR(200),
    business_condition_id INTEGER,
    description TEXT,
    company_logo_filepath VARCHAR(300),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3),
    CONSTRAINT uk_company_name UNIQUE (company_name),
    CONSTRAINT uk_company_unified_social_credit_code UNIQUE (unified_social_credit_code)
);
COMMENT ON TABLE sys_company IS '公司表';
COMMENT ON COLUMN sys_company.id IS '主键';
COMMENT ON COLUMN sys_company.company_code IS '公司编号';
COMMENT ON COLUMN sys_company.company_name IS '公司名称';
COMMENT ON COLUMN sys_company.unified_social_credit_code IS '统一社会信用代码/注册号';
COMMENT ON COLUMN sys_company.company_type_id IS '公司类型id（关联字典表）';
COMMENT ON COLUMN sys_company.legal_representative IS '法定代表人';
COMMENT ON COLUMN sys_company.founded_date IS '成立日期';
COMMENT ON COLUMN sys_company.registered_capital IS '注册资本';
COMMENT ON COLUMN sys_company.business_scope IS '经营范围';
COMMENT ON COLUMN sys_company.registered_address IS '注册地址';
COMMENT ON COLUMN sys_company.business_address IS '办公地址';
COMMENT ON COLUMN sys_company.phone IS '联系电话/传真';
COMMENT ON COLUMN sys_company.email IS '邮箱';
COMMENT ON COLUMN sys_company.official_website IS '官方网站';
COMMENT ON COLUMN sys_company.enterprise_size IS '企业规模';
COMMENT ON COLUMN sys_company.industry_id IS '所属行业id（关联sys_industry_2017表）';
COMMENT ON COLUMN sys_company.tax_registration_certificate_no IS '税务登记证号';
COMMENT ON COLUMN sys_company.deposit_bank_and_account IS '开户行及帐号';
COMMENT ON COLUMN sys_company.business_condition_id IS '经营状态id（关联字典表）';
COMMENT ON COLUMN sys_company.description IS '公司简介';
COMMENT ON COLUMN sys_company.company_logo_filepath IS '公司logo文件地址';
COMMENT ON COLUMN sys_company.creator IS '创建用户';
COMMENT ON COLUMN sys_company.create_time IS '创建时间';
COMMENT ON COLUMN sys_company.updater IS '更新用户';
COMMENT ON COLUMN sys_company.update_time IS '更新时间';

-- 部门表
DROP TABLE IF EXISTS sys_department;
CREATE TABLE sys_department (
    id SERIAL PRIMARY KEY,
    pid INTEGER NOT NULL,
    company_id INTEGER NOT NULL,
    department_name VARCHAR(100) NOT NULL,
    description VARCHAR(300),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3),
    CONSTRAINT uk_department_company_name UNIQUE (company_id, department_name)
);
COMMENT ON TABLE sys_department IS '部门表';
COMMENT ON COLUMN sys_department.id IS '主键';
COMMENT ON COLUMN sys_department.pid IS '父类id';
COMMENT ON COLUMN sys_department.company_id IS '所属公司id';
COMMENT ON COLUMN sys_department.department_name IS '部门名称';
COMMENT ON COLUMN sys_department.description IS '描述';
COMMENT ON COLUMN sys_department.creator IS '创建用户';
COMMENT ON COLUMN sys_department.create_time IS '创建时间';
COMMENT ON COLUMN sys_department.updater IS '更新用户';
COMMENT ON COLUMN sys_department.update_time IS '更新时间';
CREATE INDEX idx_department_company_id ON sys_department (company_id);
CREATE INDEX idx_department_pid ON sys_department (pid);

-- 用户-部门关系表
DROP TABLE IF EXISTS sys_user_department_ref;
CREATE TABLE sys_user_department_ref (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    company_id INTEGER NOT NULL,
    department_id INTEGER NOT NULL
);
COMMENT ON TABLE sys_user_department_ref IS '用户-部门关系表';
COMMENT ON COLUMN sys_user_department_ref.id IS '主键';
COMMENT ON COLUMN sys_user_department_ref.user_id IS '用户id';
COMMENT ON COLUMN sys_user_department_ref.company_id IS '公司id';
COMMENT ON COLUMN sys_user_department_ref.department_id IS '部门id';


-- ------------------------------------------------
-- 数据 data 部分
-- ------------------------------------------------


-- 图片表
DROP TABLE IF EXISTS data_picture;
CREATE TABLE data_picture (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type_id INTEGER NOT NULL,
    sub_type_id INTEGER NOT NULL,
    image_url VARCHAR(100),
    origin_image_url VARCHAR(100),
    keyword VARCHAR(1000),
    image_type VARCHAR(5),
    width INTEGER NOT NULL,
    height INTEGER NOT NULL,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_picture IS '图片表';
COMMENT ON COLUMN data_picture.id IS '主键';
COMMENT ON COLUMN data_picture.name IS '标题';
COMMENT ON COLUMN data_picture.type_id IS '父类型id';
COMMENT ON COLUMN data_picture.sub_type_id IS '子类型id';
COMMENT ON COLUMN data_picture.image_url IS '图片';
COMMENT ON COLUMN data_picture.origin_image_url IS '原始图片';
COMMENT ON COLUMN data_picture.keyword IS '标签';
COMMENT ON COLUMN data_picture.image_type IS '图片类型';
COMMENT ON COLUMN data_picture.width IS '宽度';
COMMENT ON COLUMN data_picture.height IS '长度';
COMMENT ON COLUMN data_picture.creator IS '创建用户';
COMMENT ON COLUMN data_picture.create_time IS '创建时间';
COMMENT ON COLUMN data_picture.updater IS '更新用户';
COMMENT ON COLUMN data_picture.update_time IS '更新时间';
CREATE INDEX idx_picture_type_id ON data_picture (type_id);
CREATE INDEX idx_picture_sub_type_id ON data_picture (sub_type_id);
-- 创建全文索引（PostgreSQL使用GIN索引实现全文搜索）
CREATE INDEX idx_picture_name_keyword_zh ON data_picture
    USING GIN (to_tsvector('chinese', COALESCE(name, '') || ' ' || COALESCE(keyword, '')));

-- 图片分类表
DROP TABLE IF EXISTS data_picture_type;
CREATE TABLE data_picture_type (
    id SERIAL PRIMARY KEY,
    pid INTEGER NOT NULL DEFAULT 0,
    name VARCHAR(50) NOT NULL,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3),
    CONSTRAINT uk_picture_type_name UNIQUE (name)
);
COMMENT ON TABLE data_picture_type IS '图片分类表';
COMMENT ON COLUMN data_picture_type.id IS '主键';
COMMENT ON COLUMN data_picture_type.pid IS '父类id';
COMMENT ON COLUMN data_picture_type.name IS '名称';
COMMENT ON COLUMN data_picture_type.creator IS '创建用户';
COMMENT ON COLUMN data_picture_type.create_time IS '创建时间';
COMMENT ON COLUMN data_picture_type.updater IS '更新用户';
COMMENT ON COLUMN data_picture_type.update_time IS '更新时间';

-- 古代书籍
-- 古代书籍类型[1-18]：传记、武侠、历史演义、军事、史记、神魔、地理、诗词、笔记、医学、小传、音乐、算术、评书、百科、佛教、风水、农业
-- 朝代[1-15]：先秦、秦朝、汉朝、三国时期、东西晋、南北朝、隋朝、唐朝、五代十国、宋朝、金朝、元朝、明朝、清朝、近代
-- 古书籍表
DROP TABLE IF EXISTS data_ancient_book;
CREATE TABLE data_ancient_book (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    author VARCHAR(50) NOT NULL,
    editor VARCHAR(50) NOT NULL,
    image_url VARCHAR(100),
    origin_image_url VARCHAR(100),
    publication_time VARCHAR(100),
    summary TEXT,
    score DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    comments INTEGER NOT NULL DEFAULT 0,
    book_status_id INTEGER NOT NULL DEFAULT 0,
    fonts DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    ancient_book_type_id INTEGER NOT NULL DEFAULT 0,
    dynasty_id INTEGER NOT NULL DEFAULT 0,
    keyword VARCHAR(1000),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_ancient_book IS '古书籍表';
COMMENT ON COLUMN data_ancient_book.id IS '主键';
COMMENT ON COLUMN data_ancient_book.name IS '名称';
COMMENT ON COLUMN data_ancient_book.author IS '作者';
COMMENT ON COLUMN data_ancient_book.editor IS '编辑人/出版人';
COMMENT ON COLUMN data_ancient_book.image_url IS '图片';
COMMENT ON COLUMN data_ancient_book.origin_image_url IS '原始图片';
COMMENT ON COLUMN data_ancient_book.publication_time IS '初版时间';
COMMENT ON COLUMN data_ancient_book.summary IS '简介';
COMMENT ON COLUMN data_ancient_book.score IS '评分';
COMMENT ON COLUMN data_ancient_book.comments IS '评论人次';
COMMENT ON COLUMN data_ancient_book.book_status_id IS '书籍状态（0:正常,1:下架,2:推荐,3:热卖）';
COMMENT ON COLUMN data_ancient_book.fonts IS '字数';
COMMENT ON COLUMN data_ancient_book.ancient_book_type_id IS '古代书籍类型id';
COMMENT ON COLUMN data_ancient_book.dynasty_id IS '朝代id';
COMMENT ON COLUMN data_ancient_book.keyword IS '关键词';
COMMENT ON COLUMN data_ancient_book.creator IS '创建用户';
COMMENT ON COLUMN data_ancient_book.create_time IS '创建时间';
COMMENT ON COLUMN data_ancient_book.updater IS '更新用户';
COMMENT ON COLUMN data_ancient_book.update_time IS '更新时间';
-- 三元组索引
CREATE INDEX idx_ancient_book_name_trgm ON data_ancient_book USING GIN (name gin_trgm_ops);
CREATE INDEX idx_ancient_book_author_trgm ON data_ancient_book USING GIN (author gin_trgm_ops);

-- 古代书籍-章节
DROP TABLE IF EXISTS data_ancient_book_chapter;
CREATE TABLE data_ancient_book_chapter (
    id SERIAL PRIMARY KEY,
    book_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    "order" INTEGER NOT NULL DEFAULT 0,
    content TEXT,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_ancient_book_chapter IS '古书籍章节表';
COMMENT ON COLUMN data_ancient_book_chapter.id IS '主键';
COMMENT ON COLUMN data_ancient_book_chapter.book_id IS '书籍id';
COMMENT ON COLUMN data_ancient_book_chapter.name IS '名称';
COMMENT ON COLUMN data_ancient_book_chapter."order" IS '排序';
COMMENT ON COLUMN data_ancient_book_chapter.content IS '章节内容';
COMMENT ON COLUMN data_ancient_book_chapter.creator IS '创建用户';
COMMENT ON COLUMN data_ancient_book_chapter.create_time IS '创建时间';
COMMENT ON COLUMN data_ancient_book_chapter.updater IS '更新用户';
COMMENT ON COLUMN data_ancient_book_chapter.update_time IS '更新时间';
CREATE INDEX idx_chapter_book_id ON data_ancient_book_chapter (book_id);

-- LOL英雄表
DROP TABLE IF EXISTS data_lol_hero;
CREATE TABLE data_lol_hero (
    hero_id INTEGER PRIMARY KEY,
    name VARCHAR(50),
    title VARCHAR(50),
    alias VARCHAR(50),
    roles VARCHAR(100),
    introduce VARCHAR(2000),
    short_bio VARCHAR(2000),
    attack INTEGER,
    defense INTEGER,
    magic INTEGER,
    difficulty INTEGER,
    hp DECIMAL(9, 4),
    hp_per_level DECIMAL(9, 4),
    mp DECIMAL(9, 4),
    mp_per_level DECIMAL(9, 4),
    move_speed DECIMAL(9, 4),
    armor DECIMAL(9, 4),
    armor_per_level DECIMAL(9, 4),
    spell_block DECIMAL(9, 4),
    spell_block_per_level DECIMAL(9, 4),
    attack_range DECIMAL(9, 4),
    hp_regen DECIMAL(9, 4),
    hp_regen_per_level DECIMAL(9, 4),
    mp_regen DECIMAL(9, 4),
    mp_regen_per_level DECIMAL(9, 4),
    crit DECIMAL(9, 4),
    crit_per_level DECIMAL(9, 4),
    attack_damage DECIMAL(9, 4),
    attack_damage_per_level DECIMAL(9, 4),
    attack_speed DECIMAL(9, 4),
    attack_speed_per_level DECIMAL(9, 4),
    ally_tips VARCHAR(2000),
    enemy_tips VARCHAR(2000),
    damage_type VARCHAR(30),
    style INTEGER,
    difficulty_level INTEGER,
    damage INTEGER,
    durability INTEGER,
    crowd_control INTEGER,
    mobility INTEGER,
    utility INTEGER,
    select_audio VARCHAR(200),
    ban_audio VARCHAR(200),
    gold_price INTEGER,
    coupon_price INTEGER,
    keywords VARCHAR(200),
    avatar VARCHAR(200),
    loading_image VARCHAR(200),
    guide_image VARCHAR(200),
    position VARCHAR(100)
);
COMMENT ON TABLE data_lol_hero IS 'LOL英雄表';
COMMENT ON COLUMN data_lol_hero.hero_id IS '英雄id';
COMMENT ON COLUMN data_lol_hero.name IS '名称';
COMMENT ON COLUMN data_lol_hero.title IS '标题';
COMMENT ON COLUMN data_lol_hero.alias IS '英文名称';
COMMENT ON COLUMN data_lol_hero.roles IS '角色';
COMMENT ON COLUMN data_lol_hero.introduce IS '介绍';
COMMENT ON COLUMN data_lol_hero.short_bio IS '短介绍';
COMMENT ON COLUMN data_lol_hero.attack IS '攻击指数';
COMMENT ON COLUMN data_lol_hero.defense IS '防御指数';
COMMENT ON COLUMN data_lol_hero.magic IS '魔法指数';
COMMENT ON COLUMN data_lol_hero.difficulty IS '操作难度';
COMMENT ON COLUMN data_lol_hero.hp IS '血量';
COMMENT ON COLUMN data_lol_hero.hp_per_level IS '每级血量成长';
COMMENT ON COLUMN data_lol_hero.mp IS '法力';
COMMENT ON COLUMN data_lol_hero.mp_per_level IS '每级法力成长';
COMMENT ON COLUMN data_lol_hero.move_speed IS '移动速度';
COMMENT ON COLUMN data_lol_hero.armor IS '护甲';
COMMENT ON COLUMN data_lol_hero.armor_per_level IS '每级护甲成长';
COMMENT ON COLUMN data_lol_hero.spell_block IS '魔抗';
COMMENT ON COLUMN data_lol_hero.spell_block_per_level IS '每级魔抗成长';
COMMENT ON COLUMN data_lol_hero.attack_range IS '攻击距离';
COMMENT ON COLUMN data_lol_hero.hp_regen IS '生命回复';
COMMENT ON COLUMN data_lol_hero.hp_regen_per_level IS '每级生命回复成长';
COMMENT ON COLUMN data_lol_hero.mp_regen IS '法力回复';
COMMENT ON COLUMN data_lol_hero.mp_regen_per_level IS '每级法力回复成长';
COMMENT ON COLUMN data_lol_hero.crit IS '暴击';
COMMENT ON COLUMN data_lol_hero.crit_per_level IS '每级暴击成长';
COMMENT ON COLUMN data_lol_hero.attack_damage IS '攻击力';
COMMENT ON COLUMN data_lol_hero.attack_damage_per_level IS '每级攻击力成长';
COMMENT ON COLUMN data_lol_hero.attack_speed IS '攻击速度';
COMMENT ON COLUMN data_lol_hero.attack_speed_per_level IS '每级攻击速度成长';
COMMENT ON COLUMN data_lol_hero.ally_tips IS '队友建议';
COMMENT ON COLUMN data_lol_hero.enemy_tips IS '对手建议';
COMMENT ON COLUMN data_lol_hero.damage_type IS '伤害类型';
COMMENT ON COLUMN data_lol_hero.style IS '伤害风格';
COMMENT ON COLUMN data_lol_hero.difficulty_level IS '操作难度';
COMMENT ON COLUMN data_lol_hero.damage IS '伤害系数';
COMMENT ON COLUMN data_lol_hero.durability IS '抗伤系数';
COMMENT ON COLUMN data_lol_hero.crowd_control IS '群体控制';
COMMENT ON COLUMN data_lol_hero.mobility IS '移动系数';
COMMENT ON COLUMN data_lol_hero.utility IS '实用系数';
COMMENT ON COLUMN data_lol_hero.select_audio IS '英雄选择语音';
COMMENT ON COLUMN data_lol_hero.ban_audio IS '英雄禁掉语音';
COMMENT ON COLUMN data_lol_hero.gold_price IS '金币购买价格';
COMMENT ON COLUMN data_lol_hero.coupon_price IS '券购买价格';
COMMENT ON COLUMN data_lol_hero.keywords IS '关键词';
COMMENT ON COLUMN data_lol_hero.avatar IS '头像';
COMMENT ON COLUMN data_lol_hero.loading_image IS '原皮肤';
COMMENT ON COLUMN data_lol_hero.guide_image IS '指南图片';
COMMENT ON COLUMN data_lol_hero.position IS '位置';

-- LOL英雄皮肤表
DROP TABLE IF EXISTS data_lol_hero_skin;
CREATE TABLE data_lol_hero_skin (
    skin_id INTEGER PRIMARY KEY,
    hero_id INTEGER,
    hero_name VARCHAR(50),
    hero_title VARCHAR(50),
    name VARCHAR(50),
    chroma BOOLEAN,
    chroma_belong_id INTEGER,
    base BOOLEAN,
    emblems_name VARCHAR(50),
    description VARCHAR(500),
    main_img VARCHAR(200),
    icon_img VARCHAR(200),
    loading_img VARCHAR(200),
    video_img VARCHAR(200),
    source_img VARCHAR(200),
    chroma_img VARCHAR(200)
);
COMMENT ON TABLE data_lol_hero_skin IS 'LOL英雄皮肤表';
COMMENT ON COLUMN data_lol_hero_skin.skin_id IS '皮肤id';
COMMENT ON COLUMN data_lol_hero_skin.hero_id IS '英雄id';
COMMENT ON COLUMN data_lol_hero_skin.hero_name IS '英雄名称';
COMMENT ON COLUMN data_lol_hero_skin.hero_title IS '英雄标题';
COMMENT ON COLUMN data_lol_hero_skin.name IS '皮肤名称';
COMMENT ON COLUMN data_lol_hero_skin.chroma IS '是否是炫彩皮肤（true=是，false=否）';
COMMENT ON COLUMN data_lol_hero_skin.chroma_belong_id IS '属于哪个皮肤（炫彩皮肤所属的基础皮肤id）';
COMMENT ON COLUMN data_lol_hero_skin.base IS '是否为基础皮肤（true=是，false=否）';
COMMENT ON COLUMN data_lol_hero_skin.emblems_name IS '徽章名称';
COMMENT ON COLUMN data_lol_hero_skin.description IS '描述';
COMMENT ON COLUMN data_lol_hero_skin.main_img IS '主图';
COMMENT ON COLUMN data_lol_hero_skin.icon_img IS '图标';
COMMENT ON COLUMN data_lol_hero_skin.loading_img IS '加载皮肤';
COMMENT ON COLUMN data_lol_hero_skin.video_img IS '视频图片';
COMMENT ON COLUMN data_lol_hero_skin.source_img IS '原图片';
COMMENT ON COLUMN data_lol_hero_skin.chroma_img IS '炫彩皮肤图片';
CREATE INDEX idx_lol_hero_skin_hero_id ON data_lol_hero_skin (hero_id);

-- LOL-英雄-技能
DROP TABLE IF EXISTS data_lol_hero_spell;
CREATE TABLE data_lol_hero_spell (
    id SERIAL PRIMARY KEY,
    hero_id INTEGER,
    spell_key VARCHAR(10),
    name VARCHAR(50),
    description VARCHAR(2000),
    ability_icon VARCHAR(300),
    ability_video VARCHAR(300),
    dynamic_description VARCHAR(2000),
    cost VARCHAR(200),
    cost_burn VARCHAR(200),
    cool_down VARCHAR(200),
    cool_down_burn VARCHAR(200),
    spell_range VARCHAR(200)
);
COMMENT ON TABLE data_lol_hero_spell IS 'LOL英雄技能表';
COMMENT ON COLUMN data_lol_hero_spell.id IS '主键id';
COMMENT ON COLUMN data_lol_hero_spell.hero_id IS '英雄id';
COMMENT ON COLUMN data_lol_hero_spell.spell_key IS '按键（Q/W/E/R/被动）';
COMMENT ON COLUMN data_lol_hero_spell.name IS '技能名称';
COMMENT ON COLUMN data_lol_hero_spell.description IS '技能描述';
COMMENT ON COLUMN data_lol_hero_spell.ability_icon IS '技能图标';
COMMENT ON COLUMN data_lol_hero_spell.ability_video IS '技能演示';
COMMENT ON COLUMN data_lol_hero_spell.dynamic_description IS '动态描述';
COMMENT ON COLUMN data_lol_hero_spell.cost IS '消耗（如：100法力值）';
COMMENT ON COLUMN data_lol_hero_spell.cost_burn IS '消耗描述';
COMMENT ON COLUMN data_lol_hero_spell.cool_down IS '冷却（如：10/9/8秒）';
COMMENT ON COLUMN data_lol_hero_spell.cool_down_burn IS '冷却描述';
COMMENT ON COLUMN data_lol_hero_spell.spell_range IS '施法范围';
CREATE INDEX idx_lol_hero_spell_hero_id ON data_lol_hero_spell (hero_id);

-- LOL装备表
DROP TABLE IF EXISTS data_lol_item;
CREATE TABLE data_lol_item (
    item_id INTEGER PRIMARY KEY,
    name VARCHAR(20),
    icon_path VARCHAR(200),
    price INTEGER,
    description TEXT,
    maps VARCHAR(200),
    plain_text TEXT,
    sell INTEGER,
    total INTEGER,
    make_into VARCHAR(500),
    make_from VARCHAR(500),
    suit_hero_ids VARCHAR(500),
    types VARCHAR(500),
    keywords VARCHAR(500),
    item_desc TEXT
);
COMMENT ON TABLE data_lol_item IS 'LOL装备表';
COMMENT ON COLUMN data_lol_item.item_id IS '物品id';
COMMENT ON COLUMN data_lol_item.name IS '名称';
COMMENT ON COLUMN data_lol_item.icon_path IS '图标';
COMMENT ON COLUMN data_lol_item.price IS '价格';
COMMENT ON COLUMN data_lol_item.description IS '描述';
COMMENT ON COLUMN data_lol_item.maps IS '所在地图（如：11=召唤师峡谷, 12=嚎哭深渊）';
COMMENT ON COLUMN data_lol_item.plain_text IS '简要描述';
COMMENT ON COLUMN data_lol_item.sell IS '售价';
COMMENT ON COLUMN data_lol_item.total IS '总价格';
COMMENT ON COLUMN data_lol_item.make_into IS '可合成（合成的高级装备ID列表）';
COMMENT ON COLUMN data_lol_item.make_from IS '由什么合成（合成所需的基础装备ID列表）';
COMMENT ON COLUMN data_lol_item.suit_hero_ids IS '适合英雄（推荐英雄ID列表）';
COMMENT ON COLUMN data_lol_item.types IS '物品类型（如：AD, AP, 防御, 辅助）';
COMMENT ON COLUMN data_lol_item.keywords IS '关键词';
COMMENT ON COLUMN data_lol_item.item_desc IS '装备描述';

-- LOL召唤师技能表
DROP TABLE IF EXISTS data_lol_summoner;
CREATE TABLE data_lol_summoner (
    id INTEGER PRIMARY KEY,
    name VARCHAR(20),
    icon_path VARCHAR(200),
    description VARCHAR(200),
    summoner_level INTEGER,
    cool_down INTEGER,
    game_mode VARCHAR(200)
);
COMMENT ON TABLE data_lol_summoner IS 'LOL召唤师技能表';
COMMENT ON COLUMN data_lol_summoner.id IS '技能id';
COMMENT ON COLUMN data_lol_summoner.name IS '名称';
COMMENT ON COLUMN data_lol_summoner.icon_path IS '图标';
COMMENT ON COLUMN data_lol_summoner.description IS '描述';
COMMENT ON COLUMN data_lol_summoner.summoner_level IS '召唤师等级';
COMMENT ON COLUMN data_lol_summoner.cool_down IS '冷却时间';
COMMENT ON COLUMN data_lol_summoner.game_mode IS '游戏模式（如：CLASSIC, ARAM）';

-- LOL符文表
DROP TABLE IF EXISTS data_lol_rune;
CREATE TABLE data_lol_rune (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50),
    icon_path VARCHAR(200),
    key VARCHAR(50),
    tooltip TEXT,
    short_desc TEXT,
    long_desc TEXT,
    slot_label VARCHAR(50),
    style_name VARCHAR(50)
);
COMMENT ON TABLE data_lol_rune IS 'LOL符文表';
COMMENT ON COLUMN data_lol_rune.id IS '符文id';
COMMENT ON COLUMN data_lol_rune.name IS '名称';
COMMENT ON COLUMN data_lol_rune.icon_path IS '图标';
COMMENT ON COLUMN data_lol_rune.key IS 'key';
COMMENT ON COLUMN data_lol_rune.tooltip IS '提示';
COMMENT ON COLUMN data_lol_rune.short_desc IS '短描述';
COMMENT ON COLUMN data_lol_rune.long_desc IS '长描述';
COMMENT ON COLUMN data_lol_rune.slot_label IS '组标签';
COMMENT ON COLUMN data_lol_rune.style_name IS '风格标签';

-- 中国姓氏
DROP TABLE IF EXISTS data_chinese_surname;
CREATE TABLE data_chinese_surname (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    traditional_name VARCHAR(20),
    pinyin VARCHAR(100),
    char_order VARCHAR(1),
    "order" INTEGER,
    images VARCHAR(50),
    people_count VARCHAR(30),
    origin TEXT,
    distribution TEXT,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_chinese_surname IS '中国姓氏表';
COMMENT ON COLUMN data_chinese_surname.id IS '主键id';
COMMENT ON COLUMN data_chinese_surname.name IS '名称';
COMMENT ON COLUMN data_chinese_surname.traditional_name IS '繁体名称';
COMMENT ON COLUMN data_chinese_surname.pinyin IS '拼音';
COMMENT ON COLUMN data_chinese_surname.char_order IS '字符排序';
COMMENT ON COLUMN data_chinese_surname."order" IS '排名';
COMMENT ON COLUMN data_chinese_surname.images IS '图片地址';
COMMENT ON COLUMN data_chinese_surname.people_count IS '人数';
COMMENT ON COLUMN data_chinese_surname.origin IS '起源';
COMMENT ON COLUMN data_chinese_surname.distribution IS '分布';
COMMENT ON COLUMN data_chinese_surname.creator IS '创建用户';
COMMENT ON COLUMN data_chinese_surname.create_time IS '创建时间';
COMMENT ON COLUMN data_chinese_surname.updater IS '更新用户';
COMMENT ON COLUMN data_chinese_surname.update_time IS '更新时间';

-- 诗词表
DROP TABLE IF EXISTS data_poem;
CREATE TABLE data_poem (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    poet VARCHAR(20),
    poet_id INTEGER,
    dynasty_id INTEGER,
    content TEXT,
    remark VARCHAR(100),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_poem IS '诗词表';
COMMENT ON COLUMN data_poem.id IS '主键id';
COMMENT ON COLUMN data_poem.title IS '标题';
COMMENT ON COLUMN data_poem.poet IS '诗人';
COMMENT ON COLUMN data_poem.poet_id IS '关联诗人id';
COMMENT ON COLUMN data_poem.dynasty_id IS '朝代id';
COMMENT ON COLUMN data_poem.content IS '内容';
COMMENT ON COLUMN data_poem.remark IS '备注';
COMMENT ON COLUMN data_poem.creator IS '创建用户';
COMMENT ON COLUMN data_poem.create_time IS '创建时间';
COMMENT ON COLUMN data_poem.updater IS '更新用户';
COMMENT ON COLUMN data_poem.update_time IS '更新时间';
CREATE INDEX idx_poem_poet_id ON data_poem (poet_id);
CREATE INDEX idx_poem_dynasty_id ON data_poem (dynasty_id);

-- 诗人表
DROP TABLE IF EXISTS data_poet;
CREATE TABLE data_poet (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dynasty_id INTEGER,
    avatar VARCHAR(200),
    intro TEXT,
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_poet IS '诗人表';
COMMENT ON COLUMN data_poet.id IS '主键id';
COMMENT ON COLUMN data_poet.name IS '名称';
COMMENT ON COLUMN data_poet.dynasty_id IS '朝代id';
COMMENT ON COLUMN data_poet.avatar IS '头像地址';
COMMENT ON COLUMN data_poet.intro IS '介绍';
COMMENT ON COLUMN data_poet.creator IS '创建用户';
COMMENT ON COLUMN data_poet.create_time IS '创建时间';
COMMENT ON COLUMN data_poet.updater IS '更新用户';
COMMENT ON COLUMN data_poet.update_time IS '更新时间';
CREATE INDEX idx_poet_dynasty_id ON data_poet (dynasty_id);

-- 网络节目表
DROP TABLE IF EXISTS data_live;
CREATE TABLE data_live (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    group_title VARCHAR(50),
    live_type_id INTEGER NOT NULL,
    image_url VARCHAR(200),
    live_url VARCHAR(300),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_live IS '网络节目表';
COMMENT ON COLUMN data_live.id IS '主键id';
COMMENT ON COLUMN data_live.name IS '名称';
COMMENT ON COLUMN data_live.group_title IS '分组标题';
COMMENT ON COLUMN data_live.live_type_id IS '网络节目类型';
COMMENT ON COLUMN data_live.image_url IS '图片地址';
COMMENT ON COLUMN data_live.live_url IS '网络节目地址';
COMMENT ON COLUMN data_live.creator IS '创建用户';
COMMENT ON COLUMN data_live.create_time IS '创建时间';
COMMENT ON COLUMN data_live.updater IS '更新用户';
COMMENT ON COLUMN data_live.update_time IS '更新时间';

-- 中草药表
DROP TABLE IF EXISTS data_crude_drug;
CREATE TABLE data_crude_drug (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    title VARCHAR(50),
    alias VARCHAR(200),
    en_name VARCHAR(50),
    image_url VARCHAR(100),
    medicinal_part VARCHAR(1000),
    plant_shape VARCHAR(1000),
    origin_distribution VARCHAR(1000),
    collect_process VARCHAR(1000),
    trait VARCHAR(1000),
    channel_tropism VARCHAR(50),
    efficacy VARCHAR(1000),
    clinical_practice VARCHAR(1000),
    pharmacological_research VARCHAR(1000),
    chemical_composition VARCHAR(1000),
    use_taboo VARCHAR(1000),
    compatibility_prescription VARCHAR(1000),
    creator VARCHAR(20),
    create_time TIMESTAMP(3),
    updater VARCHAR(20),
    update_time TIMESTAMP(3)
);
COMMENT ON TABLE data_crude_drug IS '中草药表';
COMMENT ON COLUMN data_crude_drug.id IS '主键id';
COMMENT ON COLUMN data_crude_drug.name IS '名称';
COMMENT ON COLUMN data_crude_drug.title IS '标题';
COMMENT ON COLUMN data_crude_drug.alias IS '别名';
COMMENT ON COLUMN data_crude_drug.en_name IS '英文名';
COMMENT ON COLUMN data_crude_drug.image_url IS '图片地址';
COMMENT ON COLUMN data_crude_drug.medicinal_part IS '药用部位';
COMMENT ON COLUMN data_crude_drug.plant_shape IS '植物形态';
COMMENT ON COLUMN data_crude_drug.origin_distribution IS '产地分布';
COMMENT ON COLUMN data_crude_drug.collect_process IS '收集加工';
COMMENT ON COLUMN data_crude_drug.trait IS '药材性状';
COMMENT ON COLUMN data_crude_drug.channel_tropism IS '性味归经';
COMMENT ON COLUMN data_crude_drug.efficacy IS '功效与作用';
COMMENT ON COLUMN data_crude_drug.clinical_practice IS '临床研究';
COMMENT ON COLUMN data_crude_drug.pharmacological_research IS '药理研究';
COMMENT ON COLUMN data_crude_drug.chemical_composition IS '化学成分';
COMMENT ON COLUMN data_crude_drug.use_taboo IS '使用禁忌';
COMMENT ON COLUMN data_crude_drug.compatibility_prescription IS '药方配伍';
COMMENT ON COLUMN data_crude_drug.creator IS '创建用户';
COMMENT ON COLUMN data_crude_drug.create_time IS '创建时间';
COMMENT ON COLUMN data_crude_drug.updater IS '更新用户';
COMMENT ON COLUMN data_crude_drug.update_time IS '更新时间';
-- 创建三元组索引，支持 name、title、alias 的双边模糊搜索
CREATE INDEX idx_crude_drug_name_trgm ON data_crude_drug USING GIN (name gin_trgm_ops);
CREATE INDEX idx_crude_drug_title_trgm ON data_crude_drug USING GIN (title gin_trgm_ops);
CREATE INDEX idx_crude_drug_alias_trgm ON data_crude_drug USING GIN (alias gin_trgm_ops);
