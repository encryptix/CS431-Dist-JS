use concepts;
INSERT INTO JOBS(NULL,"<javascript  function>");
CREATE TABLE DATA_<JOBID>(data_id int AUTO_INCREMENT, resultCount int DEFAULT 0, isAnswered TINYINT(1) DEFAULT 0, data TEXT, PRIMARY KEY(data_id));
CREATE TABLE RESULTS_<JOBID>(id int, result TEXT, PRIMARY KEY(id));
INSERT INTO DATA_<JOBID> VALUES(NULL,0,0,"<DATA HERE>");
