DELETE FROM mysql.user where user='DBUser';
FLUSH PRIVILEGES;
DROP DATABASE IF EXISTS concepts;
CREATE DATABASE concepts;
CREATE USER DBUser IDENTIFIED BY 'N614$S7}0s:ZQ?*W6B3,3KOwcN7NJ1';
grant all privileges on concepts.* to DBUser@localhost IDENTIFIED BY 'N614$S7}0s:ZQ?*W6B3,3KOwcN7NJ1';
use concepts;

CREATE TABLE JOBS(id int AUTO_INCREMENT, job TEXT, PRIMARY KEY(id));
