-- ============================================
-- 전자정부 프레임워크 사용자 관리 테이블
-- 기존 매퍼 XML에 맞춘 컬럼명 사용
-- ============================================

-- 기존 테이블 삭제 (있다면)
DROP TABLE IF EXISTS tb_login_hist CASCADE;
DROP TABLE IF EXISTS tb_user_role CASCADE;
DROP TABLE IF EXISTS tb_user CASCADE;
DROP TABLE IF EXISTS tb_role CASCADE;
DROP TABLE IF EXISTS tb_dept CASCADE;

-- 1. 부서 테이블
CREATE TABLE tb_dept (
    dept_cd VARCHAR(20) PRIMARY KEY,
    dept_nm VARCHAR(100) NOT NULL,
    upper_dept_cd VARCHAR(20),
    dept_dc VARCHAR(500),
    use_yn CHAR(1) DEFAULT 'Y',
    register_id VARCHAR(50),
    register_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updt_id VARCHAR(50),
    updt_dt TIMESTAMP
);

COMMENT ON TABLE tb_dept IS '부서 정보';

-- 2. 역할 테이블
CREATE TABLE tb_role (
    role_cd VARCHAR(50) PRIMARY KEY,
    role_nm VARCHAR(100) NOT NULL,
    role_dc VARCHAR(500),
    role_type VARCHAR(20),
    use_yn CHAR(1) DEFAULT 'Y',
    register_id VARCHAR(50),
    register_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE tb_role IS '역할 정보';

-- 3. 사용자 테이블
CREATE TABLE tb_user (
    user_id VARCHAR(50) PRIMARY KEY,
    user_password VARCHAR(200) NOT NULL,
    user_nm VARCHAR(100) NOT NULL,
    email_adres VARCHAR(100),
    moblphon_no VARCHAR(20),
    dept_cd VARCHAR(20),
    user_sttus_cd VARCHAR(20) DEFAULT 'NORMAL',
    sexdstn_cd VARCHAR(10),
    brthdy VARCHAR(8),
    jbgd_cd VARCHAR(20),
    jssfc_cd VARCHAR(20),
    join_de DATE,
    password_change_de DATE,
    last_login_dt TIMESTAMP,
    login_fail_co INTEGER DEFAULT 0,
    acnt_lock_yn CHAR(1) DEFAULT 'N',
    use_yn CHAR(1) DEFAULT 'Y',
    register_id VARCHAR(50),
    register_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updt_id VARCHAR(50),
    updt_dt TIMESTAMP,
    CONSTRAINT fk_user_dept 
        FOREIGN KEY (dept_cd) 
        REFERENCES tb_dept(dept_cd)
);

COMMENT ON TABLE tb_user IS '사용자 정보';

-- 4. 사용자-역할 매핑 테이블
CREATE TABLE tb_user_role (
    user_id VARCHAR(50) NOT NULL,
    role_cd VARCHAR(50) NOT NULL,
    register_id VARCHAR(50),
    register_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_cd),
    CONSTRAINT fk_user_role_user 
        FOREIGN KEY (user_id) 
        REFERENCES tb_user(user_id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role 
        FOREIGN KEY (role_cd) 
        REFERENCES tb_role(role_cd)
);

COMMENT ON TABLE tb_user_role IS '사용자-역할 매핑';

-- 5. 로그인 이력 테이블
CREATE TABLE tb_login_hist (
    hist_id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    login_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    login_ip VARCHAR(50),
    login_success_yn CHAR(1) DEFAULT 'Y',
    login_fail_resn VARCHAR(200),
    session_id VARCHAR(100)
);

COMMENT ON TABLE tb_login_hist IS '로그인 이력';

-- 인덱스 생성
CREATE INDEX idx_user_email ON tb_user(email_adres);
CREATE INDEX idx_user_dept ON tb_user(dept_cd);
CREATE INDEX idx_login_hist_user ON tb_login_hist(user_id);
CREATE INDEX idx_login_hist_dt ON tb_login_hist(login_dt);
