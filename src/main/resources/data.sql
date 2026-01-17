-- 모든 요구사항이 반영되지 않음
-- 1. 초기화 (외래키 체크 해제 후 전체 삭제)
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE alert;
TRUNCATE TABLE maintenance;
TRUNCATE TABLE accident;
TRUNCATE TABLE rent;
TRUNCATE TABLE coupon;
TRUNCATE TABLE car;
TRUNCATE TABLE employee;
TRUNCATE TABLE client;
TRUNCATE TABLE branch;

SET FOREIGN_KEY_CHECKS = 1;

-- (차량 110대, 직원 66명)
-- 2. Branch (지점)
-- 본사(10명/0대), 가락(10명/20대), 왕십리(8명/15대), 사당(12명/25대), 신도림(10명/20대), 노원(7명/12대), 공덕(9명/18대)
INSERT INTO branch (name, latitude, longitude, phone_number, address, employ_count, car_count, created_at, updated_at)
VALUES ('본사', 37.5026, 127.1008, '02-111-0000', '서울 송파구 가락동', 10, 0, NOW(), NOW()),      -- ID 1
       ('가락시장지점', 37.4925, 127.1182, '02-111-0001', '서울 송파구 가락동', 10, 20, NOW(), NOW()), -- ID 2
       ('왕십리지점', 37.5612, 127.0385, '02-111-0002', '서울 성동구 행당동', 8, 15, NOW(), NOW()),   -- ID 3
       ('사당지점', 37.4765, 126.9816, '02-111-0003', '서울 동작구 사당동', 12, 25, NOW(), NOW()),   -- ID 4
       ('신도림지점', 37.5088, 126.8912, '02-111-0004', '서울 구로구 신도림동', 10, 20, NOW(), NOW()), -- ID 5
       ('노원지점', 37.6542, 127.0605, '02-111-0005', '서울 노원구 상계동', 7, 12, NOW(), NOW()),    -- ID 6
       ('공덕지점', 37.5428, 126.9519, '02-111-0006', '서울 마포구 공덕동', 9, 18, NOW(), NOW());
-- ID 7


-- 3. Client (고객) - 20명
INSERT INTO client (email, name, birthday, gender, licence_number, licence_area, licence_day, password, blacklisted,
                    blacklist_info, phone_number, created_at, updated_at)
VALUES ('kim@pickcar.com', '김민수', '1992-03-15', 'MALE', '11-22-123456-11', '서울', '2015-05-20', 'Pass123!', false, NULL,
        '010-1234-5678', NOW(), NOW()),
       ('lee@pickcar.com', '이영희', '1995-07-22', 'FEMALE', '22-33-654321-22', '경기', '2018-11-10', 'Pass123!', false,
        NULL, '010-2345-6789', NOW(), NOW()),
       ('park@pickcar.com', '박철호', '1988-12-05', 'MALE', '13-11-112233-33', '인천', '2010-02-15', 'Pass123!', true,
        '상습 연체', '010-3456-7890', NOW(), NOW()),
       ('choi@pickcar.com', '최지우', '1998-01-30', 'FEMALE', '11-99-887766-44', '서울', '2020-06-01', 'Pass123!', false,
        NULL, '010-4567-8901', NOW(), NOW()),
       ('jung@pickcar.com', '정대만', '1993-05-22', 'MALE', '12-44-556677-55', '강원', '2016-09-12', 'Pass123!', false, NULL,
        '010-5678-9012', NOW(), NOW()),
       ('kang@pickcar.com', '강백호', '1997-04-01', 'MALE', '15-22-334455-66', '부산', '2019-10-20', 'Pass123!', false, NULL,
        '010-6789-0123', NOW(), NOW()),
       ('yoon@pickcar.com', '윤세리', '1991-08-11', 'FEMALE', '11-55-998877-77', '서울', '2014-03-05', 'Pass123!', false,
        NULL, '010-7890-1234', NOW(), NOW()),
       ('han@pickcar.com', '한결', '1985-06-18', 'MALE', '21-66-778899-88', '경남', '2008-07-22', 'Pass123!', false, NULL,
        '010-8901-2345', NOW(), NOW()),
       ('lim@pickcar.com', '임상옥', '1980-02-14', 'MALE', '17-77-112244-99', '전북', '2005-12-01', 'Pass123!', true,
        '사고 미신고', '010-9012-3456', NOW(), NOW()),
       ('ko@pickcar.com', '고은아', '1996-10-25', 'FEMALE', '11-88-223344-00', '서울', '2021-01-15', 'Pass123!', false, NULL,
        '010-0123-4567', NOW(), NOW()),
       ('son@pickcar.com', '손흥민', '1992-07-08', 'MALE', '11-11-777777-11', '강원', '2012-08-30', 'Pass123!', false, NULL,
        '010-1111-7777', NOW(), NOW()),
       ('faker@pickcar.com', '이상혁', '1996-05-07', 'MALE', '11-22-555555-55', '서울', '2017-04-21', 'Pass123!', false,
        NULL, '010-5555-5555', NOW(), NOW()),
       ('iu@pickcar.com', '이지은', '1993-05-16', 'FEMALE', '11-33-333333-33', '서울', '2014-06-10', 'Pass123!', false, NULL,
        '010-5160-5160', NOW(), NOW()),
       ('suzy@pickcar.com', '배수지', '1994-10-10', 'FEMALE', '11-44-444444-44', '광주', '2015-11-20', 'Pass123!', false,
        NULL, '010-1010-1010', NOW(), NOW()),
       ('v@pickcar.com', '김태형', '1995-12-30', 'MALE', '11-55-555555-55', '대구', '2016-01-10', 'Pass123!', false, NULL,
        '010-1230-1230', NOW(), NOW()),
       ('jennie@pickcar.com', '김제니', '1996-01-16', 'FEMALE', '11-66-666666-66', '서울', '2017-09-05', 'Pass123!', false,
        NULL, '010-0116-0116', NOW(), NOW()),
       ('bogum@pickcar.com', '박보검', '1993-06-16', 'MALE', '11-77-777777-77', '서울', '2013-12-25', 'Pass123!', false,
        NULL, '010-0616-0616', NOW(), NOW()),
       ('bin@pickcar.com', '현빈', '1982-09-25', 'MALE', '11-88-888888-88', '서울', '2004-10-11', 'Pass123!', false, NULL,
        '010-0925-0925', NOW(), NOW()),
       ('yejin@pickcar.com', '손예진', '1982-01-11', 'FEMALE', '11-99-999999-99', '대구', '2003-05-20', 'Pass123!', false,
        NULL, '010-0111-0111', NOW(), NOW()),
       ('minji@pickcar.com', '민지', '2004-05-07', 'FEMALE', '11-00-000000-00', '강원', '2023-06-01', 'Pass123!', false,
        NULL, '010-0507-0507', NOW(), NOW());


-- 4. Employee (직원) - 총 66명
-- 본사 (10명)
INSERT INTO employee (branch_id, name, phone_number, email, grade, authority, entry_date, login_id, password,
                      created_at, updated_at)
VALUES (1, '김철수', '010-1111-1234', 'ceo@pickcar.com', 'CEO', 'ADMIN', '2026-01-01', 'PC20260010001',
        'PC202600100011234!', NOW(), NOW()),
       (1, '박민준', '010-1111-2345', 'pm@pickcar.com', 'PM', 'ADMIN', '2026-01-01', 'PC20260010002', 'PC202600100022345!',
        NOW(), NOW()),
       (1, '최서연', '010-1111-3456', 'ops1@pickcar.com', '운영매니저', 'ADMIN', '2026-01-01', 'PC20260010003',
        'PC202600100033456!', NOW(), NOW()),
       (1, '이현우', '010-1111-4567', 'ops2@pickcar.com', '운영매니저', 'ADMIN', '2026-01-01', 'PC20260010004',
        'PC202600100044567!', NOW(), NOW()),
       (1, '정지윤', '010-1111-5678', 'dev1@pickcar.com', '개발자', 'ADMIN', '2026-01-01', 'PC20260010005',
        'PC202600100055678!', NOW(), NOW()),
       (1, '강도현', '010-1111-6789', 'dev2@pickcar.com', '개발자', 'ADMIN', '2026-01-01', 'PC20260010006',
        'PC202600100066789!', NOW(), NOW()),
       (1, '유제이', '010-1111-7890', 'tech1@pickcar.com', '기술지원', 'ADMIN', '2026-01-01', 'PC20260010007',
        'PC202600100077890!', NOW(), NOW()),
       (1, '한은지', '010-1111-8901', 'tech2@pickcar.com', '기술지원', 'ADMIN', '2026-01-01', 'PC20260010008',
        'PC202600100088901!', NOW(), NOW()),
       (1, '윤준서', '010-1111-9012', 'ops3@pickcar.com', '운영매니저', 'ADMIN', '2026-01-01', 'PC20260010009',
        'PC202600100099012!', NOW(), NOW()),
       (1, '임지후', '010-1111-0123', 'hr@pickcar.com', '인사관리', 'ADMIN', '2026-01-01', 'PC20260010010',
        'PC202600100100123!', NOW(), NOW());

-- 가락시장지점 (10명)
INSERT INTO employee (branch_id, name, phone_number, email, grade, authority, entry_date, login_id, password,
                      created_at, updated_at)
VALUES (2, '김가락', '010-2222-1001', 'garak_m@pickcar.com', '지점장', 'MANAGER', '2026-01-01', 'PC20260020001',
        'PC202600200011001!', NOW(), NOW()),
       (2, '박현장', '010-2222-1002', 'garak_s1@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020002',
        'PC202600200021002!', NOW(), NOW()),
       (2, '이현장', '010-2222-1003', 'garak_s2@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020003',
        'PC202600200031003!', NOW(), NOW()),
       (2, '최현장', '010-2222-1004', 'garak_s3@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020004',
        'PC202600200041004!', NOW(), NOW()),
       (2, '정현장', '010-2222-1005', 'garak_s4@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020005',
        'PC202600200051005!', NOW(), NOW()),
       (2, '조현장', '010-2222-1006', 'garak_s5@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020006',
        'PC202600200061006!', NOW(), NOW()),
       (2, '강현장', '010-2222-1007', 'garak_s6@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020007',
        'PC202600200071007!', NOW(), NOW()),
       (2, '유현장', '010-2222-1008', 'garak_s7@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020008',
        'PC202600200081008!', NOW(), NOW()),
       (2, '한현장', '010-2222-1009', 'garak_s8@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020009',
        'PC202600200091009!', NOW(), NOW()),
       (2, '오현장', '010-2222-1010', 'garak_s9@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260020010',
        'PC202600200101010!', NOW(), NOW());

-- 왕십리지점 (8명)
INSERT INTO employee (branch_id, name, phone_number, email, grade, authority, entry_date, login_id, password,
                      created_at, updated_at)
VALUES (3, '이왕십', '010-3333-2001', 'wang_m@pickcar.com', '지점장', 'MANAGER', '2026-01-01', 'PC20260030001',
        'PC202600300012001!', NOW(), NOW()),
       (3, '김왕십', '010-3333-2002', 'wang_s1@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260030002',
        'PC202600300022002!', NOW(), NOW()),
       (3, '박왕십', '010-3333-2003', 'wang_s2@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260030003',
        'PC202600300032003!', NOW(), NOW()),
       (3, '최왕십', '010-3333-2004', 'wang_s3@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260030004',
        'PC202600300042004!', NOW(), NOW()),
       (3, '정왕십', '010-3333-2005', 'wang_s4@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260030005',
        'PC202600300052005!', NOW(), NOW()),
       (3, '조왕십', '010-3333-2006', 'wang_s5@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260030006',
        'PC202600300062006!', NOW(), NOW()),
       (3, '강왕십', '010-3333-2007', 'wang_s6@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260030007',
        'PC202600300072007!', NOW(), NOW()),
       (3, '유왕십', '010-3333-2008', 'wang_s7@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260030008',
        'PC202600300082008!', NOW(), NOW());

-- 사당지점 (12명)
INSERT INTO employee (branch_id, name, phone_number, email, grade, authority, entry_date, login_id, password,
                      created_at, updated_at)
VALUES (4, '정사당', '010-4444-3001', 'sadang_m@pickcar.com', '지점장', 'MANAGER', '2026-01-01', 'PC20260040001',
        'PC202600400013001!', NOW(), NOW()),
       (4, '배현장', '010-4444-3002', 'sadang_s1@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040002',
        'PC202600400023002!', NOW(), NOW()),
       (4, '송현장', '010-4444-3003', 'sadang_s2@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040003',
        'PC202600400033003!', NOW(), NOW()),
       (4, '권현장', '010-4444-3004', 'sadang_s3@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040004',
        'PC202600400043004!', NOW(), NOW()),
       (4, '황현장', '010-4444-3005', 'sadang_s4@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040005',
        'PC202600400053005!', NOW(), NOW()),
       (4, '안현장', '010-4444-3006', 'sadang_s5@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040006',
        'PC202600400063006!', NOW(), NOW()),
       (4, '백현장', '010-4444-3007', 'sadang_s6@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040007',
        'PC202600400073007!', NOW(), NOW()),
       (4, '서현장', '010-4444-3008', 'sadang_s7@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040008',
        'PC202600400083008!', NOW(), NOW()),
       (4, '남현장', '010-4444-3009', 'sadang_s8@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040009',
        'PC202600400093009!', NOW(), NOW()),
       (4, '심현장', '010-4444-3010', 'sadang_s9@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040010',
        'PC202600400103010!', NOW(), NOW()),
       (4, '노현장', '010-4444-3011', 'sadang_s10@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040011',
        'PC202600400113011!', NOW(), NOW()),
       (4, '고현장', '010-4444-3012', 'sadang_s11@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260040012',
        'PC202600400123012!', NOW(), NOW());

-- 신도림지점 (10명)
INSERT INTO employee (branch_id, name, phone_number, email, grade, authority, entry_date, login_id, password,
                      created_at, updated_at)
VALUES (5, '박도림', '010-5555-4001', 'sindorim_m@pickcar.com', '지점장', 'MANAGER', '2026-01-01', 'PC20260050001',
        'PC202600500014001!', NOW(), NOW()),
       (5, '신현장', '010-5555-4002', 'sindorim_s1@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050002',
        'PC202600500024002!', NOW(), NOW()),
       (5, '장현장', '010-5555-4003', 'sindorim_s2@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050003',
        'PC202600500034003!', NOW(), NOW()),
       (5, '성현장', '010-5555-4004', 'sindorim_s3@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050004',
        'PC202600500044004!', NOW(), NOW()),
       (5, '허현장', '010-5555-4005', 'sindorim_s4@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050005',
        'PC202600500054005!', NOW(), NOW()),
       (5, '홍현장', '010-5555-4006', 'sindorim_s5@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050006',
        'PC202600500064006!', NOW(), NOW()),
       (5, '문현장', '010-5555-4007', 'sindorim_s6@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050007',
        'PC202600500074007!', NOW(), NOW()),
       (5, '양현장', '010-5555-4008', 'sindorim_s7@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050008',
        'PC202600500084008!', NOW(), NOW()),
       (5, '손현장', '010-5555-4009', 'sindorim_s8@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050009',
        'PC202600500094009!', NOW(), NOW()),
       (5, '곽현장', '010-5555-4010', 'sindorim_s9@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260050010',
        'PC202600500104010!', NOW(), NOW());

-- 노원지점 (7명)
INSERT INTO employee (branch_id, name, phone_number, email, grade, authority, entry_date, login_id, password,
                      created_at, updated_at)
VALUES (6, '최노원', '010-6666-5001', 'nowon_m@pickcar.com', '지점장', 'MANAGER', '2026-01-01', 'PC20260060001',
        'PC202600600015001!', NOW(), NOW()),
       (6, '차현장', '010-6666-5002', 'nowon_s1@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260060002',
        'PC202600600025002!', NOW(), NOW()),
       (6, '주현장', '010-6666-5003', 'nowon_s2@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260060003',
        'PC202600600035003!', NOW(), NOW()),
       (6, '엄현장', '010-6666-5004', 'nowon_s3@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260060004',
        'PC2026006000445004!', NOW(), NOW()),
       (6, '우현장', '010-6666-5005', 'nowon_s4@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260060005',
        'PC202600600055005!', NOW(), NOW()),
       (6, '라현장', '010-6666-5006', 'nowon_s5@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260060006',
        'PC202600600065006!', NOW(), NOW()),
       (6, '민현장', '010-6666-5007', 'nowon_s6@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260060007',
        'PC202600600075007!', NOW(), NOW());

-- 공덕지점 (9명)
INSERT INTO employee (branch_id, name, phone_number, email, grade, authority, entry_date, login_id, password,
                      created_at, updated_at)
VALUES (7, '유공덕', '010-7777-6001', 'gongdeok_m@pickcar.com', '지점장', 'MANAGER', '2026-01-01', 'PC20260070001',
        'PC202600700016001!', NOW(), NOW()),
       (7, '탁현장', '010-7777-6002', 'gongdeok_s1@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070002',
        'PC202600700026002!', NOW(), NOW()),
       (7, '진현장', '010-7777-6003', 'gongdeok_s2@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070003',
        'PC202600700036003!', NOW(), NOW()),
       (7, '구현장', '010-7777-6004', 'gongdeok_s3@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070004',
        'PC202600700046004!', NOW(), NOW()),
       (7, '지현장', '010-7777-6005', 'gongdeok_s4@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070005',
        'PC2026007000556005!', NOW(), NOW()),
       (7, '표현장', '010-7777-6006', 'gongdeok_s5@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070006',
        'PC202600700066006!', NOW(), NOW()),
       (7, '금현장', '010-7777-6007', 'gongdeok_s6@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070007',
        'PC202600700076007!', NOW(), NOW()),
       (7, '기현장', '010-7777-6008', 'gongdeok_s7@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070008',
        'PC202600700086008!', NOW(), NOW()),
       (7, '명현장', '010-7777-6009', 'gongdeok_s8@pickcar.com', '현장직원', 'STAFF', '2026-01-01', 'PC20260070009',
        'PC202600700096009!', NOW(), NOW());


-- 5. Car (차량) 데이터 적재 - 총 110대
-- 가락시장지점(2): 20대
INSERT INTO car (branch_id, vehicle_id_number, model, price, brand, year, age_limit, fuel_type, car_number, mileage,
                 status, purchase_price, model_price, seater, color, created_at, updated_at)
VALUES (2, 'KMHCT41CDPA001001', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '12허1001', 5200, 'WAITING', 21000000,
        23000000, 5, 'WHITE', NOW(), NOW()),
       (2, 'KMHCT41CDPA001002', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '12허1002', 8900, 'WAITING', 21000000,
        23000000, 5, 'GRAY', NOW(), NOW()),
       (2, 'KMHCT41CDPA001003', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '12허1003', 12500, 'DRIVING', 21000000,
        23000000, 5, 'BLACK', NOW(), NOW()),
       (2, 'KMHGN41JDPA001004', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '12허1004', 3200, 'WAITING', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (2, 'KMHGN41JDPA001005', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '12허1005', 15600, 'WAITING', 28000000,
        31000000, 5, 'GRAY', NOW(), NOW()),
       (2, 'KMHGN41JDPA001006', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '12허1006', 22100, 'DRIVING', 28000000,
        31000000, 5, 'BLACK', NOW(), NOW()),
       (2, 'KNAGD41LDPA001007', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '12호1007', 9800, 'WAITING', 27000000, 30000000,
        5, 'WHITE', NOW(), NOW()),
       (2, 'KNAGD41LDPA001008', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '12호1008', 14200, 'WAITING', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (2, 'KNAGD41LDPA001009', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '12호1009', 28500, 'MAINTENANCE', 27000000,
        30000000, 5, 'BLACK', NOW(), NOW()),
       (2, 'KMHCT41CDPA001010', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '12허1010', 3100, 'WAITING', 21000000,
        23000000, 5, 'WHITE', NOW(), NOW()),
       (2, 'KMHCT41CDPA001011', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '12허1011', 11000, 'WAITING', 21000000,
        23000000, 5, 'GRAY', NOW(), NOW()),
       (2, 'KMHGN41JDPA001012', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '12허1012', 4500, 'WAITING', 28000000,
        31000000, 5, 'BLACK', NOW(), NOW()),
       (2, 'KMHGN41JDPA001013', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '12허1013', 18900, 'DRIVING', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (2, 'KNAGD41LDPA001014', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '12호1014', 13500, 'WAITING', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (2, 'KNAGD41LDPA001015', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '12호1015', 31000, 'MAINTENANCE', 27000000,
        30000000, 5, 'BLACK', NOW(), NOW()),
       (2, 'KMHCT41CDPA001016', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '12허1016', 42000, 'WAITING', 23000000, 25000000,
        5, 'WHITE', NOW(), NOW()),
       (2, 'KMHCT41CDPA001017', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '12허1017', 56000, 'WAITING', 23000000, 25000000,
        5, 'GRAY', NOW(), NOW()),
       (2, 'KMHGN41JDPA001018', '소나타', 75000, '현대', 2024, 21, 'DIESEL', '12허1018', 38000, 'DRIVING', 30000000, 33000000,
        5, 'BLACK', NOW(), NOW()),
       (2, 'KNAGD41LDPA001019', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '12호1019', 47000, 'WAITING', 29000000, 32000000,
        5, 'WHITE', NOW(), NOW()),
       (2, 'KNAGD41LDPA001020', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '12호1020', 61000, 'MAINTENANCE', 29000000,
        32000000, 5, 'GRAY', NOW(), NOW());

-- 왕십리지점(3): 15대
INSERT INTO car (branch_id, vehicle_id_number, model, price, brand, year, age_limit, fuel_type, car_number, mileage,
                 status, purchase_price, model_price, seater, color, created_at, updated_at)
VALUES (3, 'KMHCT41CDPA002001', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '22허2001', 4100, 'WAITING', 21000000,
        23000000, 5, 'WHITE', NOW(), NOW()),
       (3, 'KMHCT41CDPA002002', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '22허2002', 7200, 'WAITING', 21000000,
        23000000, 5, 'BLACK', NOW(), NOW()),
       (3, 'KMHCT41CDPA002003', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '22허2003', 18500, 'DRIVING', 21000000,
        23000000, 5, 'GRAY', NOW(), NOW()),
       (3, 'KMHGN41JDPA002004', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '22허2004', 2100, 'WAITING', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (3, 'KMHGN41JDPA002005', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '22허2005', 9500, 'WAITING', 28000000,
        31000000, 5, 'BLACK', NOW(), NOW()),
       (3, 'KMHGN41JDPA002006', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '22허2006', 15600, 'MAINTENANCE', 28000000,
        31000000, 5, 'GRAY', NOW(), NOW()),
       (3, 'KNAGD41LDPA002007', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '22호2007', 8800, 'WAITING', 27000000, 30000000,
        5, 'WHITE', NOW(), NOW()),
       (3, 'KNAGD41LDPA002008', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '22호2008', 21000, 'DRIVING', 27000000,
        30000000, 5, 'BLACK', NOW(), NOW()),
       (3, 'KNAGD41LDPA002009', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '22호2009', 32500, 'WAITING', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (3, 'KMHCT41CDPA002010', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '22허2010', 43000, 'WAITING', 23000000, 25000000,
        5, 'WHITE', NOW(), NOW()),
       (3, 'KMHCT41CDPA002011', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '22허2011', 51000, 'DRIVING', 23000000, 25000000,
        5, 'BLACK', NOW(), NOW()),
       (3, 'KMHGN41JDPA002012', '소나타', 75000, '현대', 2024, 21, 'DIESEL', '22허2012', 37000, 'WAITING', 30000000, 33000000,
        5, 'GRAY', NOW(), NOW()),
       (3, 'KMHGN41JDPA002013', '소나타', 75000, '현대', 2024, 21, 'DIESEL', '22허2013', 44000, 'MAINTENANCE', 30000000,
        33000000, 5, 'WHITE', NOW(), NOW()),
       (3, 'KNAGD41LDPA002014', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '22호2014', 52000, 'WAITING', 29000000, 32000000,
        5, 'BLACK', NOW(), NOW()),
       (3, 'KNAGD41LDPA002015', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '22호2015', 68000, 'WAITING', 29000000, 32000000,
        5, 'GRAY', NOW(), NOW());

-- 사당지점(4): 25대
INSERT INTO car (branch_id, vehicle_id_number, model, price, brand, year, age_limit, fuel_type, car_number, mileage,
                 status, purchase_price, model_price, seater, color, created_at, updated_at)
VALUES (4, 'KMHGN41JDPA003001', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '33허3001', 5000, 'WAITING', 42000000,
        45000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KMHGN41JDPA003002', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '33허3002', 12500, 'DRIVING', 42000000,
        45000000, 5, 'GRAY', NOW(), NOW()),
       (4, 'KMHGN41JDPA003003', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '33허3003', 21000, 'WAITING', 42000000,
        45000000, 5, 'WHITE', NOW(), NOW()),
       (4, 'KMHGN41JDPA003004', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '33허3004', 35000, 'MAINTENANCE', 42000000,
        45000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KMHGN41JDPA003005', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '33허3005', 48000, 'WAITING', 42000000,
        45000000, 5, 'GRAY', NOW(), NOW()),
       (4, 'KMHGN41JDPA003006', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '33허3006', 1500, 'WAITING', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (4, 'KMHGN41JDPA003007', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '33허3007', 8500, 'DRIVING', 28000000,
        31000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KMHGN41JDPA003008', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '33허3008', 19000, 'WAITING', 28000000,
        31000000, 5, 'GRAY', NOW(), NOW()),
       (4, 'KMHGN41JDPA003009', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '33허3009', 24500, 'WAITING', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (4, 'KMHGN41JDPA003010', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '33허3010', 31000, 'MAINTENANCE', 28000000,
        31000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KNAGD41LDPA003011', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '33호3011', 12000, 'WAITING', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (4, 'KNAGD41LDPA003012', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '33호3012', 18500, 'DRIVING', 27000000,
        30000000, 5, 'WHITE', NOW(), NOW()),
       (4, 'KNAGD41LDPA003013', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '33호3013', 26000, 'WAITING', 27000000,
        30000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KNAGD41LDPA003014', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '33호3014', 33000, 'WAITING', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (4, 'KNAGD41LDPA003015', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '33호3015', 41000, 'MAINTENANCE', 27000000,
        30000000, 5, 'WHITE', NOW(), NOW()),
       (4, 'KMHCT41CDPA003016', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '33허3016', 5000, 'WAITING', 21000000,
        23000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KMHCT41CDPA003017', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '33허3017', 9200, 'DRIVING', 21000000,
        23000000, 5, 'GRAY', NOW(), NOW()),
       (4, 'KMHCT41CDPA003018', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '33허3018', 16000, 'WAITING', 21000000,
        23000000, 5, 'WHITE', NOW(), NOW()),
       (4, 'KMHCT41CDPA003019', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '33허3019', 24000, 'WAITING', 21000000,
        23000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KMHCT41CDPA003020', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '33허3020', 32000, 'MAINTENANCE', 21000000,
        23000000, 5, 'GRAY', NOW(), NOW()),
       (4, 'KMHGN41JDPA003021', '그랜저', 110000, '현대', 2023, 26, 'DIESEL', '33허3021', 45000, 'WAITING', 45000000,
        48000000, 5, 'WHITE', NOW(), NOW()),
       (4, 'KMHGN41JDPA003022', '그랜저', 110000, '현대', 2023, 26, 'DIESEL', '33허3022', 58000, 'DRIVING', 45000000,
        48000000, 5, 'BLACK', NOW(), NOW()),
       (4, 'KMHGN41JDPA003023', '소나타', 75000, '현대', 2024, 21, 'DIESEL', '33허3023', 39000, 'WAITING', 30000000, 33000000,
        5, 'GRAY', NOW(), NOW()),
       (4, 'KNAGD41LDPA003024', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '33호3024', 42000, 'WAITING', 29000000, 32000000,
        5, 'WHITE', NOW(), NOW()),
       (4, 'KMHCT41CDPA003025', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '33허3025', 54000, 'MAINTENANCE', 23000000,
        25000000, 5, 'BLACK', NOW(), NOW());

-- 신도림지점(5): 20대
INSERT INTO car (branch_id, vehicle_id_number, model, price, brand, year, age_limit, fuel_type, car_number, mileage,
                 status, purchase_price, model_price, seater, color, created_at, updated_at)
VALUES (5, 'KMHCT41CDPA004001', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '44허4001', 3200, 'WAITING', 21000000,
        23000000, 5, 'WHITE', NOW(), NOW()),
       (5, 'KMHCT41CDPA004002', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '44허4002', 9500, 'WAITING', 21000000,
        23000000, 5, 'BLACK', NOW(), NOW()),
       (5, 'KMHCT41CDPA004003', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '44허4003', 14200, 'DRIVING', 21000000,
        23000000, 5, 'GRAY', NOW(), NOW()),
       (5, 'KMHCT41CDPA004004', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '44허4004', 22500, 'WAITING', 21000000,
        23000000, 5, 'WHITE', NOW(), NOW()),
       (5, 'KMHCT41CDPA004005', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '44허4005', 31000, 'MAINTENANCE', 21000000,
        23000000, 5, 'BLACK', NOW(), NOW()),
       (5, 'KMHGN41JDPA004006', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '44허4006', 2500, 'WAITING', 28000000,
        31000000, 5, 'GRAY', NOW(), NOW()),
       (5, 'KMHGN41JDPA004007', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '44허4007', 7800, 'DRIVING', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (5, 'KMHGN41JDPA004008', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '44허4008', 13400, 'WAITING', 28000000,
        31000000, 5, 'BLACK', NOW(), NOW()),
       (5, 'KMHGN41JDPA004009', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '44허4009', 21000, 'WAITING', 28000000,
        31000000, 5, 'GRAY', NOW(), NOW()),
       (5, 'KMHGN41JDPA004010', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '44허4010', 28500, 'MAINTENANCE', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (5, 'KNAGD41LDPA004011', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '44호4011', 5400, 'WAITING', 27000000, 30000000,
        5, 'BLACK', NOW(), NOW()),
       (5, 'KNAGD41LDPA004012', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '44호4012', 12100, 'DRIVING', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (5, 'KNAGD41LDPA004013', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '44호4013', 23400, 'WAITING', 27000000,
        30000000, 5, 'WHITE', NOW(), NOW()),
       (5, 'KNAGD41LDPA004014', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '44호4014', 35600, 'WAITING', 27000000,
        30000000, 5, 'BLACK', NOW(), NOW()),
       (5, 'KNAGD41LDPA004015', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '44호4015', 41200, 'MAINTENANCE', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (5, 'KMHCT41CDPA004016', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '44허4016', 39000, 'WAITING', 23000000, 25000000,
        5, 'WHITE', NOW(), NOW()),
       (5, 'KMHCT41CDPA004017', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '44허4017', 52000, 'DRIVING', 23000000, 25000000,
        5, 'BLACK', NOW(), NOW()),
       (5, 'KMHGN41JDPA004018', '소나타', 75000, '현대', 2024, 21, 'DIESEL', '44허4018', 41000, 'WAITING', 30000000, 33000000,
        5, 'GRAY', NOW(), NOW()),
       (5, 'KNAGD41LDPA004019', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '44호4019', 46000, 'WAITING', 29000000, 32000000,
        5, 'WHITE', NOW(), NOW()),
       (5, 'KNAGD41LDPA004020', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '44호4020', 58000, 'MAINTENANCE', 29000000,
        32000000, 5, 'BLACK', NOW(), NOW());

-- 노원지점(6): 12대
INSERT INTO car (branch_id, vehicle_id_number, model, price, brand, year, age_limit, fuel_type, car_number, mileage,
                 status, purchase_price, model_price, seater, color, created_at, updated_at)
VALUES (6, 'KMHCT41CDPA005001', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '55허5001', 5600, 'WAITING', 21000000,
        23000000, 5, 'WHITE', NOW(), NOW()),
       (6, 'KMHCT41CDPA005002', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '55허5002', 12300, 'DRIVING', 21000000,
        23000000, 5, 'BLACK', NOW(), NOW()),
       (6, 'KMHCT41CDPA005003', '아반떼', 55000, '현대', 2023, 21, 'GASOLINE', '55허5003', 21000, 'WAITING', 21000000,
        23000000, 5, 'GRAY', NOW(), NOW()),
       (6, 'KMHGN41JDPA005004', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '55허5004', 4200, 'WAITING', 28000000,
        31000000, 5, 'WHITE', NOW(), NOW()),
       (6, 'KMHGN41JDPA005005', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '55허5005', 9800, 'DRIVING', 28000000,
        31000000, 5, 'BLACK', NOW(), NOW()),
       (6, 'KMHGN41JDPA005006', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '55허5006', 16500, 'MAINTENANCE', 28000000,
        31000000, 5, 'GRAY', NOW(), NOW()),
       (6, 'KNAGD41LDPA005007', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '55호5007', 11000, 'WAITING', 27000000,
        30000000, 5, 'WHITE', NOW(), NOW()),
       (6, 'KNAGD41LDPA005008', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '55호5008', 24000, 'DRIVING', 27000000,
        30000000, 5, 'BLACK', NOW(), NOW()),
       (6, 'KNAGD41LDPA005009', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '55호5009', 33500, 'WAITING', 27000000,
        30000000, 5, 'GRAY', NOW(), NOW()),
       (6, 'KMHCT41CDPA005010', '아반떼', 55000, '현대', 2023, 21, 'DIESEL', '55허5010', 41000, 'WAITING', 23000000, 25000000,
        5, 'WHITE', NOW(), NOW()),
       (6, 'KMHGN41JDPA005011', '소나타', 75000, '현대', 2024, 21, 'DIESEL', '55허5011', 48000, 'MAINTENANCE', 30000000,
        33000000, 5, 'BLACK', NOW(), NOW()),
       (6, 'KNAGD41LDPA005012', 'K5', 72000, '기아', 2023, 21, 'DIESEL', '55호5012', 59000, 'WAITING', 29000000, 32000000,
        5, 'GRAY', NOW(), NOW());

-- 공덕지점(7): 정비 상태 및 알림 로직 검증용 데이터 18대
INSERT INTO car (branch_id, vehicle_id_number, model, price, brand, year, age_limit, fuel_type, car_number, mileage,
                 maintenance_date, status, purchase_price, model_price, seater, color, created_at, updated_at)
VALUES
-- 케이스 1. [정상/신차] 정비일이 멀었거나 등록한 지 얼마 안 됨 (알림 X)
(7, 'VIN-NEW-001', '아반떼', 55000, '현대', 2025, 21, 'GASOLINE', '66허1001', 100,  '2026-01-01', 'WAITING', 21000000, 23000000, 5, 'WHITE', NOW(), NOW()),
(7, 'VIN-NEW-002', '아반떼', 55000, '현대', 2025, 21, 'GASOLINE', '66허1002', 2000, '2025-11-15', 'DRIVING', 21000000, 23000000, 5, 'BLACK', NOW(), NOW()),
(7, 'VIN-NEW-003', '아반떼', 55000, '현대', 2025, 21, 'GASOLINE', '66허1003', 4500, '2025-09-20', 'WAITING', 21000000, 23000000, 5, 'GRAY',  NOW(), NOW()),

-- 케이스 2. [정비 중 상태] 정비 기간이 지났거나 소모품이 바닥났지만, status가 'MAINTENANCE'라 알림이 안 떠야 함 (4대)
-- (정비 중인 차량은 재촉하지 않는 구조 검증)
(7, 'VIN-MAINT-001', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '66허2001', 12000, '2025-01-10', 'MAINTENANCE', 28000000, 31000000, 5, 'WHITE', NOW(), NOW()), -- 날짜지남+거리초과지만 status로 차단
(7, 'VIN-MAINT-002', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '66허2002', 55000, '2025-07-14', 'MAINTENANCE', 28000000, 31000000, 5, 'BLACK', NOW(), NOW()), -- 정기검사 당일이지만 status로 차단
(7, 'VIN-MAINT-003', '소나타', 75000, '현대', 2024, 21, 'GASOLINE', '66허2003', 100500,'2024-01-01', 'MAINTENANCE', 28000000, 31000000, 5, 'GRAY',  NOW(), NOW()), -- 모든 소모품 초과지만 status로 차단

-- 케이스 3. [소모품/검사 지연] 정비가 시급하며 status가 WAITING/DRIVING이라 알림이 떠야 함 (5대)
(7, 'VIN-ALERT-001', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '66호3001', 10500, '2025-07-20', 'WAITING', 27000000, 30000000, 5, 'WHITE', NOW(), NOW()), -- 엔진오일 초과 (10k)
(7, 'VIN-ALERT-002', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '66호3002', 20500, '2025-08-01', 'DRIVING', 27000000, 30000000, 5, 'BLACK', NOW(), NOW()), -- 에어클리너 초과 (20k)
(7, 'VIN-ALERT-003', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '66호3003', 15000, '2025-07-10', 'WAITING', 27000000, 30000000, 5, 'GRAY',  NOW(), NOW()), -- 검사일 4일 지남 (overdueDays 발생)
(7, 'VIN-ALERT-004', 'K5', 72000, '기아', 2023, 21, 'GASOLINE', '66호3004', 11000, '2025-01-14', 'DRIVING', 27000000, 30000000, 5, 'WHITE', NOW(), NOW()), -- 정확히 1년 미정비 (due: true)

-- 케이스 4. [임박/D-Day] 오늘로부터 일주일 이내 알림 대상 (6대)
(7, 'VIN-SOON-001', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '66허4001', 9950,  '2026-01-10', 'WAITING', 42000000, 45000000, 5, 'BLACK', NOW(), NOW()), -- 엔진오일 50km 남음
(7, 'VIN-SOON-002', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '66허4002', 15000, '2025-07-14', 'WAITING', 42000000, 45000000, 5, 'GRAY',  NOW(), NOW()), -- 정기검사 오늘 (D-Day, remainingDays: 0)
(7, 'VIN-SOON-003', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '66허4003', 15000, '2025-07-20', 'DRIVING', 42000000, 45000000, 5, 'WHITE', NOW(), NOW()), -- 정기검사 6일 남음 (remainingDays: 6)
(7, 'VIN-SOON-004', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '66허4004', 79900, '2025-10-01', 'WAITING', 42000000, 45000000, 5, 'BLACK', NOW(), NOW()), -- 구동벨트 100km 남음
(7, 'VIN-SOON-005', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '66허4005', 19990, '2025-12-01', 'DRIVING', 42000000, 45000000, 5, 'WHITE', NOW(), NOW()), -- 에어클리너 10km 남음
(7, 'VIN-SOON-006', '그랜저', 110000, '현대', 2023, 26, 'GASOLINE', '66허4006', 15000, '2025-07-08', 'WAITING', 42000000, 45000000, 5, 'BLACK', NOW(), NOW()); -- 검사일 6일 지남 (overdueDays: 6)
-- 6. Coupon (쿠폰) 데이터 적재 (15건)
INSERT INTO coupon (coupon_name, discount, exp_date, code, created_at, updated_at)
VALUES ('신규 가입 축하 쿠폰', 10000, '2026-12-31', 'WELCOME2026', NOW(), NOW()),
       ('VIP 전용 특별 할인권', 50000, '2026-12-31', 'VIPONLY50', NOW(), NOW()),
       ('생일 축하 기프트 쿠폰', 20000, '2026-12-31', 'HBD2026', NOW(), NOW()),
       ('장거리 이용 감사 쿠폰', 30000, '2026-07-31', 'LONGDIST30', NOW(), NOW()),
       ('주말 나들이 응원 쿠폰', 15000, '2026-05-31', 'WEEKEND15', NOW(), NOW()),
       ('가락시장지점 단독 할인', 12000, '2026-03-31', 'GARAK12', NOW(), NOW()),
       ('사당역 환승객 이벤트', 8000, '2026-04-30', 'SADANG08', NOW(), NOW()),
       ('왕십리 대학생 응원권', 10000, '2026-06-30', 'WANG10', NOW(), NOW()),
       ('신도림 출퇴근 할인권', 7000, '2026-08-31', 'DORIM07', NOW(), NOW()),
       ('노원 지점 첫 방문 할인', 5000, '2026-02-28', 'NOWON05', NOW(), NOW()),
       ('정비 지연 사과 쿠폰', 25000, '2026-12-31', 'SORRY25', NOW(), NOW()),
       ('차량 청결 우수 리뷰권', 3000, '2026-03-31', 'CLEAN03', NOW(), NOW()),
       ('앱 업데이트 감사 쿠폰', 4000, '2026-01-31', 'UPDATE04', NOW(), NOW()),
       ('설문조사 참여 보상', 2000, '2026-03-31', 'SURVEY02', NOW(), NOW()),
       ('서비스 개선 쿠폰', 10000, '2026-12-31', 'VROOM10', NOW(), NOW());


-- 7. Rent (예약) 데이터 적재 (40건 이상)
-- 주의: car_id(1~110)와 client_id(1~20) 범위 내에서 매핑
INSERT INTO rent (car_id, client_id, car_name, car_image, model, price, brand, year, start_rent_date_time,
                  end_rent_date_time, fuel_type, age_limit, seater, color, created_at, updated_at)
VALUES (2, 1, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-09 10:00:00', '2026-01-10 10:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (6, 2, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-09 12:00:00', '2026-01-11 12:00:00', 'GASOLINE',
        '21', '5', 'GRAY', NOW(), NOW()),
       (13, 3, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-08 15:00:00', '2026-01-10 15:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (20, 4, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-01 10:00:00', '2026-01-02 10:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (35, 5, '그랜저', 'grandeur.png', 'GN7', '110000', '현대', 2023, '2026-01-15 10:00:00', '2026-01-16 10:00:00',
        'GASOLINE', '26', '5', 'BLACK', NOW(), NOW()),
       (40, 6, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-20 09:00:00', '2026-01-21 09:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (50, 7, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-05 10:00:00', '2026-01-06 10:00:00', 'GASOLINE',
        '21', '5', 'WHITE', NOW(), NOW()),
       (60, 8, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-06 09:00:00', '2026-01-07 09:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (70, 9, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-01 11:00:00', '2026-01-02 11:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (80, 10, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-03 14:00:00', '2026-01-04 14:00:00', 'DIESEL',
        '21', '5', 'WHITE', NOW(), NOW()),
       (90, 11, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-05 07:00:00', '2026-01-06 07:00:00', 'DIESEL',
        '21', '5', 'WHITE', NOW(), NOW()),
       (100, 12, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-07 16:00:00', '2026-01-08 16:00:00',
        'DIESEL', '21', '5', 'WHITE', NOW(), NOW()),
       (105, 13, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-08 10:00:00', '2026-01-10 10:00:00',
        'GASOLINE', '21', '5', 'BLACK', NOW(), NOW()),
       (108, 14, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-08 15:00:00', '2026-01-11 15:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (15, 15, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-09 09:00:00', '2026-01-11 18:00:00', 'GASOLINE',
        '21', '5', 'WHITE', NOW(), NOW()),
       (25, 16, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-09 11:00:00', '2026-01-10 20:00:00',
        'GASOLINE', '21', '5', 'GRAY', NOW(), NOW()),
       (45, 17, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-08 17:00:00', '2026-01-09 23:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (55, 18, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-09 10:00:00', '2026-01-12 10:00:00',
        'GASOLINE', '21', '5', 'GRAY', NOW(), NOW()),
       (65, 19, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-08 13:00:00', '2026-01-10 13:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (75, 20, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-09 14:00:00', '2026-01-11 14:00:00', 'GASOLINE',
        '21', '5', 'GRAY', NOW(), NOW()),
       (85, 1, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-09 08:00:00', '2026-01-10 08:00:00',
        'GASOLINE', '21', '5', 'GRAY', NOW(), NOW()),
       (95, 2, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-08 20:00:00', '2026-01-10 20:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (3, 3, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-09 12:00:00', '2026-01-11 12:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (7, 4, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-08 18:00:00', '2026-01-10 18:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (9, 5, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-09 15:00:00', '2026-01-12 15:00:00', 'GASOLINE',
        '21', '5', 'WHITE', NOW(), NOW()),
       (18, 6, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-09 07:00:00', '2026-01-11 07:00:00', 'DIESEL', '21',
        '5', 'GRAY', NOW(), NOW()),
       (22, 7, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-08 10:00:00', '2026-01-10 10:00:00', 'DIESEL',
        '21', '5', 'WHITE', NOW(), NOW()),
       (33, 8, '그랜저', 'grandeur.png', 'GN7', '110000', '현대', 2023, '2026-01-11 10:00:00', '2026-01-12 10:00:00',
        'GASOLINE', '26', '5', 'GRAY', NOW(), NOW()),
       (44, 9, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-12 09:00:00', '2026-01-14 18:00:00',
        'GASOLINE', '21', '5', 'GRAY', NOW(), NOW()),
       (55, 10, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-15 13:00:00', '2026-01-16 13:00:00',
        'GASOLINE', '21', '5', 'GRAY', NOW(), NOW()),
       (66, 11, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-20 10:00:00', '2026-01-22 10:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (77, 12, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-11 15:00:00', '2026-01-12 15:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (88, 13, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-13 11:00:00', '2026-01-14 11:00:00', 'GASOLINE',
        '21', '5', 'WHITE', NOW(), NOW()),
       (99, 14, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-14 09:00:00', '2026-01-15 09:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (102, 15, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-18 12:00:00', '2026-01-20 12:00:00',
        'GASOLINE', '21', '5', 'GRAY', NOW(), NOW()),
       (107, 16, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-22 14:00:00', '2026-01-23 14:00:00',
        'GASOLINE', '21', '5', 'GRAY', NOW(), NOW()),
       (12, 17, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-25 10:00:00', '2026-01-27 10:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (27, 18, 'K5', 'k5.png', 'DL3', '72000', '기아', 2023, '2026-01-02 18:00:00', '2026-01-04 18:00:00', 'GASOLINE',
        '21', '5', 'BLACK', NOW(), NOW()),
       (49, 19, '아반떼', 'avante.png', 'CN7', '55000', '현대', 2023, '2026-01-03 15:00:00', '2026-01-05 15:00:00',
        'GASOLINE', '21', '5', 'WHITE', NOW(), NOW()),
       (71, 20, '소나타', 'sonata.png', 'DN8', '75000', '현대', 2024, '2026-01-04 07:00:00', '2026-01-06 07:00:00', 'DIESEL',
        '21', '5', 'GRAY', NOW(), NOW());


-- 8. Accident (사고) 데이터 적재 (20건)
INSERT INTO accident (client_id, car_id, accident_status, client_name, accident_time, accident_detail, accident_locate,
                      accident_part, accident_image, insurance_info, brand, model, year, created_at, updated_at)
VALUES (1, 2, 'REPORT', '김민수', '2026-01-09 14:20:00', '주차장 후진 중 기둥 충돌', '서울 송파구 가락동 주차장', '후방 범퍼', 'acc_01.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '소나타', '2024', NOW(), NOW()),
       (2, 6, 'REPORT', '이영희', '2026-01-09 09:15:00', '교차로 내 단순 접촉 사고', '서울 성동구 왕십리 오거리', '전방 휀더', 'acc_02.jpg',
        'KB손해보험 렌터카 종합보험', '기아', 'K5', '2023', NOW(), NOW()),
       (5, 35, 'REPORT', '정대만', '2026-01-08 22:40:00', '가드레일 단독 접촉', '서울 동작구 사당로 인근', '측면 도어', 'acc_03.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '그랜저', '2023', NOW(), NOW()),
       (11, 90, 'REPORT', '손흥민', '2026-01-08 11:30:00', '후방 추돌 피해', '서울 마포구 공덕역 인근', '트렁크 및 범퍼', 'acc_04.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '아반떼', '2023', NOW(), NOW()),
       (3, 13, 'REPAIRING', '박철호', '2026-01-05 13:00:00', '차선 변경 중 측면 충돌', '서울 구로구 신도림역 인근', '우측 앞문', 'acc_05.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '아반떼', '2023', NOW(), NOW()),
       (7, 50, 'REPAIRING', '윤세리', '2026-01-04 16:20:00', '빙판길 미끄러짐', '서울 노원구 상계동 인근', '전면 하부', 'acc_06.jpg',
        'KB손해보험 렌터카 종합보험', '기아', 'K5', '2023', NOW(), NOW()),
       (12, 100, 'REPAIRING', '이상혁', '2026-01-03 10:45:00', '중앙분리대 접촉', '서울 마포구 강변북로', '좌측 사이드미러 및 도어', 'acc_07.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '아반떼', '2023', NOW(), NOW()),
       (16, 25, 'REPAIRING', '김제니', '2026-01-02 19:10:00', '야생동물 출몰로 인한 급정거 및 추돌', '강원도 춘천시 인근 도로', '전방 라디에이터 그릴',
        'acc_08.jpg', 'KB손해보험 렌터카 종합보험', '현대', '아반떼', '2023', NOW(), NOW()),
       (10, 80, 'REPAIRED', '고은아', '2025-12-28 08:30:00', '신호대기 중 후방 추돌', '서울 송파구 잠실역', '후방 일체', 'acc_09.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '아반떼', '2023', NOW(), NOW()),
       (4, 20, 'SUIT', '최지우', '2025-12-25 21:00:00', '음주 의심 차량에 의한 뺑소니 피해', '서울 동작구 이수역', '전측면 전체', 'acc_10.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '소나타', '2024', NOW(), NOW()),
       (14, 108, 'REPAIRED', '배수지', '2025-12-20 14:00:00', '골목길 서행 중 오토바이 충돌', '서울 노원구 광운대역 인근', '우측 휀더', 'acc_11.jpg',
        'KB손해보험 렌터카 종합보험', '기아', 'K5', '2023', NOW(), NOW()),
       (8, 60, 'SUIT', '한결', '2025-12-15 11:30:00', '불법 유턴 차량과 충돌(과실 분쟁 중)', '서울 구로구 경인로', '운전석 도어', 'acc_12.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '아반떼', '2023', NOW(), NOW()),
       (15, 15, 'COMPLETED', '김태형', '2025-11-20 10:00:00', '단순 외관 스크래치(물피도주)', '서울 서대문구 신촌', '리어 쿼터패널', 'acc_13.jpg',
        'KB손해보험 렌터카 종합보험', '기아', 'K5', '2023', NOW(), NOW()),
       (18, 55, 'COMPLETED', '현빈', '2025-10-10 15:00:00', '빗길 수막현상으로 인한 미끄러짐', '서울 영등포구 올림픽대로', '휠 및 타이어', 'acc_14.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '소나타', '2024', NOW(), NOW()),
       (20, 71, 'COMPLETED', '민지', '2025-09-05 12:40:00', '낙하물로 인한 앞유리 파손', '경기도 하남시 인근 도로', '전면 유리', 'acc_15.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '소나타', '2024', NOW(), NOW()),
       (6, 40, 'COMPLETED', '강백호', '2025-08-15 17:30:00', '우회전 중 연석 충돌', '부산광역시 해운대구', '우측 하단 스텝', 'acc_16.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '소나타', '2024', NOW(), NOW()),
       (13, 105, 'COMPLETED', '이지은', '2025-07-01 13:10:00', '고속도로 주행 중 스톤칩 발생', '경부고속도로 하행선', '본네트', 'acc_17.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '소나타', '2024', NOW(), NOW()),
       (9, 70, 'COMPLETED', '임상옥', '2025-06-20 09:00:00', '주차 중 타 차량 문콕 피해', '서울 종로구 혜화동', '측면 도어', 'acc_18.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '소나타', '2024', NOW(), NOW()),
       (17, 45, 'COMPLETED', '박보검', '2025-05-12 20:15:00', '야간 주행 중 시야 미확보로 인한 경계석 충돌', '서울 강남구 역삼역 인근', '범퍼 하단',
        'acc_19.jpg', 'KB손해보험 렌터카 종합보험', '기아', 'K5', '2023', NOW(), NOW()),
       (19, 49, 'COMPLETED', '손예진', '2025-04-01 11:00:00', '터널 내 안전거리 미확보 추돌', '서울 중구 남산터널', '전방 일체', 'acc_20.jpg',
        'KB손해보험 렌터카 종합보험', '현대', '아반떼', '2023', NOW(), NOW());


-- 1. 정비 예약 데이터 (Maintenance 테이블)
-- 엔티티의 모든 nullable = false 필드를 포함해야 합니다.
INSERT INTO maintenance (
    branch_id, car_id, employee_id, employee_name,
    vehicle_id_number, title, maintenance_date,
    cost, status, consumables, created_at, updated_at
)
VALUES
-- 테스트 케이스 1: 소모품 교체 및 주행거리 초기화 테스트 대상 (엔진오일)
(7, 8, 14, '탁현장', 'VIN-ALERT-001', '엔진오일 교체', '2026-01-15', 85000, 'SCHEDULE', '엔진오일', NOW(), NOW()),

-- 테스트 케이스 2: 정비 완료 및 차량 상태 변경 테스트 대상
(7, 9, 14, '탁현장', 'VIN-ALERT-002', '에어클리너 점검', '2026-01-15', 30000, 'ONGOING', '에어클리너', NOW(), NOW()),

-- 테스트 케이스 3: 이미 진행 중인 정비 (상태 변화 확인용)
(7, 12, 14, '탁현장', 'VIN-SOON-001', '복합 정비', '2026-01-14', 150000, 'COMPLETED', '엔진오일,와이퍼', NOW(), NOW());

-- 10. Alert (알림) 데이터 적재 (30건)
INSERT INTO alert (employee_id, type, message, date, is_read,created_at, updated_at)
VALUES (1, 'ACCIDENT', '[긴급] 가락시장지점 소나타 사고 접수. 확인 요망', '2026-01-09', false,NOW(), NOW()),
       (2, 'NOTICE', '전사 공지: 보안 점검 안내', '2026-01-08', false,NOW(), NOW()),
       (3, 'ACCIDENT', '[지점사고] 소속 지점 K5 사고 발생', '2026-01-09', false, NOW(), NOW()),
       (4, 'MAINTENANCE', '담당 차량 엔진오일 교체 주기 도달', '2026-01-09', false, NOW(), NOW()),
       (5, 'APPROVAL', '세차 완료 승인 요청', '2026-01-09', false, NOW(), NOW()),
       (6, 'SYSTEM', '비밀번호 변경 권장 알림', '2026-01-05', false, NOW(), NOW()),
       (7, 'ACCIDENT', '[지점사고] 소속 지점 그랜저 접촉 사고', '2026-01-09', false, NOW(), NOW()),
       (8, 'MAINTENANCE', '타이어 교체 업무 할당됨', '2026-01-09', false,NOW(), NOW()),
       (9, 'NOTICE', '신도림 지점 회식 안내', '2026-01-07', false, NOW(), NOW()),
       (10, 'MAINTENANCE', '브레이크 패드 점검 요망', '2026-01-09', false,NOW(), NOW()),
       (11, 'APPROVAL', '정비 완료 승인 대기 중', '2026-01-09', false,NOW(), NOW()),
       (12, 'MAINTENANCE', '엔진오일 교체 주기 임박', '2026-01-09', false,NOW(), NOW()),
       (13, 'ACCIDENT', '[긴급] 공덕지점 아반떼 사고 접수', '2026-01-09', false,NOW(), NOW()),
       (14, 'SYSTEM', '시스템 점검 예정 안내', '2026-01-08', false,NOW(), NOW()),
       (3, 'MAINTENANCE', '지점 가동률 90% 초과 경고', '2026-01-09', false, NOW(), NOW()),
       (5, 'NOTICE', '본사 지침 전달 사항', '2026-01-06', false,NOW(), NOW()),
       (7, 'APPROVAL', '차량 상태 점검 보고서 승인 요청', '2026-01-09', false, NOW(), NOW()),
       (9, 'MAINTENANCE', '정비 대기 차량 증가 알림', '2026-01-09', false,NOW(), NOW()),
       (11, 'SYSTEM', '개인정보 취급 방침 변경 안내', '2026-01-01', false,NOW(), NOW()),
       (13, 'NOTICE', '월간 매출 마감 안내', '2026-01-31', false,NOW(), NOW()),
       (1, 'APPROVAL', '휴가 신청 승인 대기', '2026-01-09', false,NOW(), NOW()),
       (2, 'MAINTENANCE', '전사 차량 정비 현황 보고', '2026-01-09', false,NOW(), NOW()),
       (4, 'SYSTEM', '로그인 시도 알림', '2026-01-09', false,NOW(), NOW()),
       (6, 'NOTICE', '주말 근무 일정 안내', '2026-01-09', false,NOW(), NOW()),
       (8, 'ACCIDENT', '[지점사고] 사당지점 접촉 사고 처리 완료', '2026-01-08', false, NOW(), NOW()),
       (10, 'APPROVAL', '비품 구매 요청 승인', '2026-01-09', false,NOW(), NOW()),
       (12, 'MAINTENANCE', '배터리 교체 주기 도달', '2026-01-09', false,NOW(), NOW()),
       (14, 'SYSTEM', '업데이트 완료 알림', '2026-01-09', false,NOW(), NOW()),
       (3, 'NOTICE', '우수 사원 포상 안내', '2026-01-05', false,NOW(), NOW()),
       (7, 'MAINTENANCE', '차량 정기 검사 일정 안내', '2026-01-09', false, NOW(), NOW());