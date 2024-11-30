// 유저 생성
CREATE USER 'bookmile'@'%' IDENTIFIED BY 'bookmile';

// 유저 권한 부여
GRANT ALL PRIVILEGES ON *.* TO 'bookmile'@'%';
FLUSH PRIVILEGES;

// 생성된 유저 확인
SELECT User, Host FROM mysql.user;

// 데이터베이스 생성
CREATE DATABASE bookmile;

// 생성된 데이터베이스 확인
SHOW DATABASES;