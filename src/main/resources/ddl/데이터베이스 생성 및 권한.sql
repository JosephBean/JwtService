-- root 사용자로 접속 후 실행
USE mysql;

CREATE DATABASE company;

GRANT ALL ON company.* TO 'test'@'localhost';

FLUSH PRIVILEGES;

SHOW GRANTS FOR 'test'@'localhost';

-- test 사용자로 접속 후 실행
USE company;

CREATE TABLE `user` (
	userNo 			INT 				NOT NULL AUTO_INCREMENT PRIMARY KEY,
	userNm 			VARCHAR(50) 	NOT NULL,
	userPwd			VARCHAR(50)		NOT NULL,
	userEnable		BOOLEAN			NOT NULL DEFAULT 1
);

CREATE TABLE `role` (
	roleNo		INT				NOT NULL AUTO_INCREMENT PRIMARY KEY,
	roleNm		VARCHAR(50) 	NOT NULL
);

CREATE TABLE `user_role` (
	`userNo` INT	NULL DEFAULT NULL,
	`roleNo` INT	NULL DEFAULT NULL
);

-- 비밀번호 : 1234
INSERT INTO `user` (userNm, userPwd) VALUES ('관리자','$2a$10$wQjWW.RDnC/8fQ5X2hYbl.1B2yZeQEE0nreiL5keMpuPtIQW.s0PS');
INSERT INTO `user` (userNm, userPwd) VALUES ('사용자','$2a$10$wQjWW.RDnC/8fQ5X2hYbl.1B2yZeQEE0nreiL5keMpuPtIQW.s0PS');

INSERT INTO `role` (roleNm) VALUES ('ADMIN');
INSERT INTO `role` (roleNm) VALUES ('DEVELOPER');
INSERT INTO `role` (roleNm) VALUES ('USER');

INSERT INTO `user_role` VALUES (1,1);
INSERT INTO `user_role` VALUES (1,2);
INSERT INTO `user_role` VALUES (1,3);
INSERT INTO `user_role` VALUES (2,3);

COMMIT;

