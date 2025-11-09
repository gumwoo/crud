
-- 1. 점심 내역 테이블
CREATE TABLE tb_lunch_master (
    lunch_id SERIAL PRIMARY KEY,                          -- 고유 ID (자동증가)
    lunch_date DATE NOT NULL,                             -- 날짜
    store_name VARCHAR(200) NOT NULL,                     -- 가게명
    lunch_type VARCHAR(20) DEFAULT '점심',                -- 구분 (점심/커피)
    payer_user_id VARCHAR(50) NOT NULL,                   -- 결제자 사용자 ID
    payer_user_name VARCHAR(100),                         -- 결제자 이름
    total_amount INTEGER NOT NULL CHECK (total_amount > 0), -- 총 금액
    settlement_status VARCHAR(20) DEFAULT '대기',         -- 정산상태 (대기/완료)
    settlement_date TIMESTAMP,                            -- 정산 완료 일시
    description VARCHAR(500),                             -- 비고
    register_user_id VARCHAR(50) NOT NULL,                -- 등록자 ID
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 등록일시
    update_user_id VARCHAR(50),                           -- 수정자 ID
    update_date TIMESTAMP                                 -- 수정일시
);

-- 2. 참여자별 금액 테이블
CREATE TABLE tb_lunch_participant (
    participant_id SERIAL PRIMARY KEY,                    -- 고유 ID (자동증가)
    lunch_id INTEGER NOT NULL,                            -- 점심 내역 ID (FK)
    user_id VARCHAR(50) NOT NULL,                         -- 참여자 사용자 ID
    user_name VARCHAR(100) NOT NULL,                      -- 참여자 이름
    amount INTEGER NOT NULL CHECK (amount >= 0),          -- 개인 부담금
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 등록일시
    CONSTRAINT fk_lunch_participant 
        FOREIGN KEY (lunch_id) 
        REFERENCES tb_lunch_master(lunch_id) 
        ON DELETE CASCADE                                 -- 마스터 삭제 시 자동 삭제
);

-- 3. 인덱스 생성
CREATE INDEX idx_lunch_date ON tb_lunch_master(lunch_date);
CREATE INDEX idx_lunch_status ON tb_lunch_master(settlement_status);
CREATE INDEX idx_lunch_type ON tb_lunch_master(lunch_type);
CREATE INDEX idx_participant_lunch ON tb_lunch_participant(lunch_id);
CREATE INDEX idx_participant_user ON tb_lunch_participant(user_id);

-- 4. 코멘트 추가
COMMENT ON TABLE tb_lunch_master IS '점심비 정산 마스터 테이블';
COMMENT ON TABLE tb_lunch_participant IS '점심비 정산 참여자별 금액 테이블';

COMMENT ON COLUMN tb_lunch_master.lunch_id IS '점심 내역 고유 ID';
COMMENT ON COLUMN tb_lunch_master.lunch_date IS '식사 날짜';
COMMENT ON COLUMN tb_lunch_master.store_name IS '식당/카페 이름';
COMMENT ON COLUMN tb_lunch_master.lunch_type IS '점심/커피 구분';
COMMENT ON COLUMN tb_lunch_master.payer_user_id IS '실제 결제한 사람 ID';
COMMENT ON COLUMN tb_lunch_master.total_amount IS '영수증 총 금액';
COMMENT ON COLUMN tb_lunch_master.settlement_status IS '정산 상태 (대기/완료)';

COMMENT ON COLUMN tb_lunch_participant.lunch_id IS '점심 내역 ID (FK)';
COMMENT ON COLUMN tb_lunch_participant.user_id IS '참여자 사용자 ID';
COMMENT ON COLUMN tb_lunch_participant.amount IS '해당 참여자의 개인 부담금';