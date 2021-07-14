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

#게시물 테이블에 회원번호 칼럼 추가
ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER updateDate;

# 기존 게시물의 작성자를 회원1로 지정
UPDATE article SET memberId = 1 WHERE memberId = 0;


#LEFT JOIN 연습
SELECT A.*,
IFNULL(M.nickname, "탈퇴회원") AS extra__writer
FROM article AS A
LEFT JOIN `member` AS M
ON A.memberId = M.id
WHERE A.ID = 1;



