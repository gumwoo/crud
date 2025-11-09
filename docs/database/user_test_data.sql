-- ============================================
-- 테스트 계정 및 기본 데이터
-- 전자정부 표준 컬럼명 사용
-- ============================================

-- 1. 부서 데이터
INSERT INTO tb_dept (dept_cd, dept_nm, dept_dc, use_yn, register_id) VALUES
('DEPT001', '개발팀', 'IT 개발 담당 부서', 'Y', 'admin'),
('DEPT002', '경영지원팀', '경영지원 담당 부서', 'Y', 'admin'),
('DEPT003', '영업팀', '영업 담당 부서', 'Y', 'admin');

-- 2. 역할 데이터
INSERT INTO tb_role (role_cd, role_nm, role_dc, role_type, use_yn, register_id) VALUES
('ROLE_ADMIN', '관리자', '시스템 관리자 역할', 'SYSTEM', 'Y', 'admin'),
('ROLE_USER', '일반사용자', '일반 사용자 역할', 'USER', 'Y', 'admin'),
('ROLE_MANAGER', '매니저', '부서 관리자 역할', 'MANAGER', 'Y', 'admin');

-- 3. 사용자 데이터
-- 비밀번호는 평문으로 저장 (테스트용)
-- 실제 환경에서는 암호화 필요!
INSERT INTO tb_user (
    user_id, user_password, user_nm, email_adres, 
    dept_cd, user_sttus_cd, use_yn, register_id
) VALUES
('admin', 'admin123', '관리자', 'admin@spatialt.com', 
 'DEPT001', 'NORMAL', 'Y', 'admin'),
('hong', 'hong123', '홍길동', 'hong@spatialt.com', 
 'DEPT001', 'NORMAL', 'Y', 'admin'),
('kim', 'kim123', '김철수', 'kim@spatialt.com', 
 'DEPT001', 'NORMAL', 'Y', 'admin'),
('lee', 'lee123', '이영희', 'lee@spatialt.com', 
 'DEPT001', 'NORMAL', 'Y', 'admin');

-- 4. 사용자-역할 매핑
INSERT INTO tb_user_role (user_id, role_cd, register_id) VALUES
('admin', 'ROLE_ADMIN', 'admin'),
('admin', 'ROLE_USER', 'admin'),
('hong', 'ROLE_USER', 'admin'),
('kim', 'ROLE_USER', 'admin'),
('lee', 'ROLE_USER', 'admin');

-- 5. 확인 쿼리
SELECT 
    u.user_id,
    u.user_nm,
    u.email_adres,
    d.dept_nm,
    r.role_nm
FROM tb_user u
LEFT JOIN tb_dept d ON u.dept_cd = d.dept_cd
LEFT JOIN tb_user_role ur ON u.user_id = ur.user_id
LEFT JOIN tb_role r ON ur.role_cd = r.role_cd
ORDER BY u.user_id, r.role_nm;
