CREATE DATABASE IF EXISTS untact;

CREATE DATABASE untact;

USE untact;

# 게시물 테이블
CREATE TABLE article(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	title CHAR(100) NOT NULL,
	`body` TEXT NOT NULL	
);

# 게시물, 테스트 데이터생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목1 입니다.",
`body` = "내용1 입니다.";

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목2 입니다.",
`body` = "내용2 입니다.";

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목3 입니다.",
`body` = "내용3 입니다.";

TRUNCATE article;
SELECT * FROM article;


# 회원 테이블 
CREATE TABLE `member`(
	id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	loginId CHAR(30) NOT NULL,
	loginPw VARCHAR(100) NOT NULL,
	`name` CHAR(30) NOT NULL,
	`nickname` CHAR(30) NOT NULL,
	`email` CHAR(100) NOT NULL,
	`cellphoneNo` CHAR(20) NOT NULL
	
		
);

# 회원 테이블 데이터 생성
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "user1",
loginPw = "user1",
`name` = "user1",
nickname = "user",
cellphoneNo = "01012341234",
email = "jangka@gmail.com";

INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = "user2",
loginPw = "user2",
`name` = "user2",
nickname = "user2",
cellphoneNo = "010888888",
email = "tdfwa@gmail.com";

TRUNCATE `member`;

SELECT * FROM `member`;

#로그인 ID로 검색했을 때
ALTER TABLE `member` ADD UNIQUE INDEX(`loginId`);

DESC article;

#게시물 테이블에 회원번호 칼럼 추가!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER updateDate;

# 기존 게시물의 작성자를 회원1로 지정!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
UPDATE article SET memberId = 1 WHERE memberId = 0;


#LEFT JOIN 연습
SELECT A.*,
IFNULL(M.nickname, "탈퇴회원") AS extra__writer
FROM article AS A
LEFT JOIN `member` AS M
ON A.memberId = M.id
WHERE A.ID = 1;


# 테스트 게시물 데이터 추가
INSERT INTO article
(regDate, updateDate, memberId, title, `body`)
SELECT NOW(), NOW(), FLOOR(RAND()*2) + 1, CONCAT('제목', FLOOR(RAND()*1000) + 1), CONCAT('내용', FLOOR(RAND()*1000) + 1)
FROM article;

DESC article;

SELECT * FROM article;

SELECT COUNT(*) FROM article;



# 게시판 테이블 추가 
CREATE TABLE board(
id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
`code` CHAR(20) UNIQUE NOT NULL,
`name` CHAR(20) UNIQUE NOT NULL
);

# 공지사항 게시판 추가
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'notice',
`name` = '공지사항';

# 자유 게시판 추가
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'free',
`name` = '자유';

#기존 게시물 테이블에 게시판 번호 칼럼 추가, updateDate 칼럼 뒤에~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
ALTER TABLE article ADD COLUMN boardId INT(10) UNSIGNED NOT NULL AFTER updateDate;

#기존 게시물들에임의로 랜덤 boardId 부여해서 보드아이디가 0이었던것을 수습함 
UPDATE article
SET boardId = FLOOR(RAND()*2) + 1
WHERE boardId = 0;

# 공지사항인 게시물들
SELECT COUNT(*) FROM article WHERE boardId = 1;

# 자유게시판인 게시물들
SELECT COUNT(*) FROM article WHERE boardId = 2;


# 댓글 테이블 추가 
CREATE TABLE reply(
id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
articleId INT(10) UNSIGNED NOT NULL,
memberId INT(10) UNSIGNED NOT NULL,
`body` TEXT NOT NULL
);


DESC reply;

# 댓글 테스트 데이터생성
INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
articleId = 1,
memberId = 1,
`body` = "댓글내용1 입니다.";

INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
articleId = 1,
memberId = 2,
`body` = "댓글내용2 입니다.";

INSERT INTO reply
SET regDate = NOW(),
updateDate = NOW(),
articleId = 2,
memberId = 2,
`body` = "댓글내용3 입니다.";

SELECT * FROM reply;

# 인덱스 지정을 꼭 까먹지 말아야한다......알겄지.. 고속 검색을 위해서 인덱스 걸기
ALTER TABLE `untact`.`reply` ADD KEY (`relTypeCode` , `relId`); 


# 게시물 전용이 아닌 범용 댓글로 구조 변경
ALTER TABLE reply CHANGE `articleId` `relId` INT(10) UNSIGNED NOT NULL;
ALTER TABLE reply ad`reply`d COLUMN `relTypeCode` CHAR(20) NOT NULL AFTER updateDate;


# 범용댓글로 구조 변경 후 기존 댓글 relTypeCode 수정
UPDATE reply
SET relTypeCode = 'article';
WHERE relTypeCode = '';


# left join 테스트
		SELECT R.*,
		IFNULL(M.nickname, "탈퇴회원") AS extra__writer
		FROM reply AS R
		LEFT JOIN `member` AS M
		ON R.memberId = M.id
		WHERE 1
		AND R.relTypeCode = 'article'
		AND R.relId = 1;
		
		
DESC MEMBER;

# member 테이블에 authKey 칼럼 추가, 인덱스도 
ALTER TABLE `member` ADD COLUMN authKey CHAR(80) NOT NULL AFTER loginPw;
ALTER TABLE `untact`.`member` ADD UNIQUE INDEX(`authKey`);
	
# 기존회원의 authKey 데이터 채우기	
UPDATE `member`
SET authKey = CONCAT("authKey1__", UUID(), "__", RAND())
WHERE authKey = '';



SELECT * FROM `member`	

