
-- 샘플 시나리오:
-- 직원 3명 12월 한 달간 점심/커피를 함께 함
-- 각자 돌아가며 결제하고, 개인별 금액이 다름

-- 1. 12월 1일 - 김밥천국 (홍길동 결제)

INSERT INTO tb_lunch_master (
    lunch_date, store_name, lunch_type, 
    payer_user_id, payer_user_name, total_amount, 
    settlement_status, register_user_id
) VALUES (
    '2024-12-01', '김밥천국', '점심',
    'hong', '홍길동', 20000,
    '대기', 'hong'
);

-- 참여자별 금액 (각자 먹은 만큼 다름)
INSERT INTO tb_lunch_participant (lunch_id, user_id, user_name, amount) VALUES
(1, 'hong', '홍길동', 5000),   -- 김밥
(1, 'kim', '김철수', 7000),     -- 돈까스
(1, 'lee', '이영희', 8000);     -- 냉면

-- 2. 12월 2일 - 스타벅스 (김철수 결제)
INSERT INTO tb_lunch_master (
    lunch_date, store_name, lunch_type,
    payer_user_id, payer_user_name, total_amount,
    settlement_status, register_user_id
) VALUES (
    '2024-12-02', '스타벅스', '커피',
    'kim', '김철수', 12000,
    '대기', 'kim'
);

INSERT INTO tb_lunch_participant (lunch_id, user_id, user_name, amount) VALUES
(2, 'hong', '홍길동', 4000),
(2, 'kim', '김철수', 4000),
(2, 'lee', '이영희', 4000);


-- 3. 12월 5일 - 중국집 (이영희 결제)
INSERT INTO tb_lunch_master (
    lunch_date, store_name, lunch_type,
    payer_user_id, payer_user_name, total_amount,
    settlement_status, register_user_id
) VALUES (
    '2024-12-05', '중국집', '점심',
    'lee', '이영희', 30000,
    '대기', 'lee'
);

INSERT INTO tb_lunch_participant (lunch_id, user_id, user_name, amount) VALUES
(3, 'hong', '홍길동', 10000),  -- 짜장면
(3, 'kim', '김철수', 10000),   -- 짬뽕
(3, 'lee', '이영희', 10000);   -- 탕수육


-- 4. 12월 10일 - 빽다방 (홍길동 결제)
INSERT INTO tb_lunch_master (
    lunch_date, store_name, lunch_type,
    payer_user_id, payer_user_name, total_amount,
    settlement_status, register_user_id
) VALUES (
    '2024-12-10', '빽다방', '커피',
    'hong', '홍길동', 9000,
    '대기', 'hong'
);

INSERT INTO tb_lunch_participant (lunch_id, user_id, user_name, amount) VALUES
(4, 'hong', '홍길동', 3000),
(4, 'kim', '김철수', 3000),
(4, 'lee', '이영희', 3000);


-- 5. 12월 15일 - 한식당 (김철수 결제)
INSERT INTO tb_lunch_master (
    lunch_date, store_name, lunch_type,
    payer_user_id, payer_user_name, total_amount,
    settlement_status, register_user_id
) VALUES (
    '2024-12-15', '한식당', '점심',
    'kim', '김철수', 35000,
    '대기', 'kim'
);

INSERT INTO tb_lunch_participant (lunch_id, user_id, user_name, amount) VALUES
(5, 'hong', '홍길동', 12000),
(5, 'kim', '김철수', 11000),
(5, 'lee', '이영희', 12000);


-- 6. 12월 20일 - 투썸플레이스 (이영희 결제) - 정산완료 예시
INSERT INTO tb_lunch_master (
    lunch_date, store_name, lunch_type,
    payer_user_id, payer_user_name, total_amount,
    settlement_status, settlement_date, register_user_id
) VALUES (
    '2024-12-20', '투썸플레이스', '커피',
    'lee', '이영희', 15000,
    '완료', '2024-12-21 10:30:00', 'lee'
);

INSERT INTO tb_lunch_participant (lunch_id, user_id, user_name, amount) VALUES
(6, 'hong', '홍길동', 5000),
(6, 'kim', '김철수', 5000),
(6, 'lee', '이영희', 5000);


-- 전체 내역 조회
SELECT 
    m.lunch_id,
    m.lunch_date,
    m.store_name,
    m.lunch_type,
    m.payer_user_name,
    m.total_amount,
    m.settlement_status
FROM tb_lunch_master m
ORDER BY m.lunch_date;

-- 참여자별 상세 조회
SELECT 
    m.lunch_date,
    m.store_name,
    m.payer_user_name as 결제자,
    m.total_amount as 총금액,
    p.user_name as 참여자,
    p.amount as 개인부담금,
    m.settlement_status as 정산상태
FROM tb_lunch_master m
JOIN tb_lunch_participant p ON m.lunch_id = p.lunch_id
ORDER BY m.lunch_date, p.user_id;

-- 사용자별 12월 합계 (정산 전 기준)
SELECT 
    p.user_name,
    SUM(p.amount) as 총부담금,
    COUNT(DISTINCT m.lunch_id) as 참여횟수
FROM tb_lunch_master m
JOIN tb_lunch_participant p ON m.lunch_id = p.lunch_id
WHERE m.settlement_status = '대기'
  AND m.lunch_date >= '2024-12-01'
  AND m.lunch_date < '2025-01-01'
GROUP BY p.user_name
ORDER BY p.user_name;
