CREATE TABLE reports(id_report BIGINT NOT NULL, type INTEGER NOT NULL,player_id BIGINT NOT NULL,millis BIGINT NOT NULL,week INTEGER NOT NULL,value INTEGER NOT NULL,CONSTRAINT reports_pkey PRIMARY KEY(id_report));
CREATE INDEX IDX_REPORTS ON REPORTS(ID_REPORT);
UPDATE coaches_at_trainings SET id_job = 4 WHERE id_job = 3;
UPDATE coaches_at_trainings SET id_job = 3 WHERE id_job = 2;
UPDATE coaches_at_trainings SET id_job = 2 WHERE id_job = 4;
UPDATE system SET version = 7;