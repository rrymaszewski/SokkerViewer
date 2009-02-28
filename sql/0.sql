CREATE MEMORY TABLE SYSTEM(VERSION INTEGER NOT NULL,LAST_MODIFICATION_MILLIS BIGINT,LAST_MODIFICATION_SK_WEEK INTEGER,LAST_MODIFICATION_SK_DAY INTEGER, CHECK_COUNTRIES BOOLEAN, CHECK_UPDATE_DB BOOLEAN, REPAIR_DB BOOLEAN, TEAM_ID INT, junior_minimum_pop DOUBLE default 3 NOT NULL, scan_counter INTEGER DEFAULT 0 NOT NULL);
CREATE MEMORY TABLE ARENA(ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,LOCATION INTEGER NOT NULL,CAPACITY INTEGER NOT NULL,TYPE TINYINT NOT NULL,DAYS DOUBLE NOT NULL,ROOF TINYINT NOT NULL,NOTE VARCHAR,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER, team_id INTEGER DEFAULT 0 NOT NULL)
CREATE MEMORY TABLE ASSISTANT(ID_POSITION TINYINT NOT NULL,FORM TINYINT DEFAULT 0 NOT NULL,STAMINA TINYINT DEFAULT 0 NOT NULL,PACE TINYINT DEFAULT 0 NOT NULL,TECHNIQUE TINYINT DEFAULT 0 NOT NULL,PASSING TINYINT DEFAULT 0 NOT NULL,KEEPER TINYINT DEFAULT 0 NOT NULL,DEFENDER TINYINT DEFAULT 0 NOT NULL,PLAYMAKER TINYINT DEFAULT 0 NOT NULL,SCORER TINYINT DEFAULT 0 NOT NULL,RED INTEGER DEFAULT 255 NOT NULL,GREEN INTEGER DEFAULT 255 NOT NULL,BLUE INTEGER DEFAULT 255 NOT NULL,CONSTRAINT ASSISTANT_ID_POSITION_FK PRIMARY KEY(ID_POSITION))
CREATE MEMORY TABLE TRAINING(ID_TRAINING INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,TYPE INTEGER NOT NULL,FORMATION INTEGER NOT NULL,NOTE VARCHAR,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,REPORTED BOOLEAN DEFAULT true,CONSTRAINT TRAINING_PK PRIMARY KEY(ID_TRAINING))
CREATE MEMORY TABLE COACH(ID_COACH INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,SIGNED TINYINT NOT NULL,NAME VARCHAR NOT NULL,SURNAME VARCHAR NOT NULL,JOB INTEGER NOT NULL,COUNTRYFROM INTEGER NOT NULL,AGE TINYINT NOT NULL,SALARY INTEGER NOT NULL,GENERALSKILL INTEGER NOT NULL,STAMINA TINYINT NOT NULL,PACE TINYINT NOT NULL,TECHNIQUE TINYINT NOT NULL,PASSING TINYINT NOT NULL,KEEPERS TINYINT NOT NULL,DEFENDERS TINYINT NOT NULL,PLAYMAKERS TINYINT NOT NULL,SCORERS TINYINT NOT NULL,STATUS TINYINT NOT NULL,NOTE VARCHAR,CONSTRAINT COACH_PK PRIMARY KEY(ID_COACH))
CREATE MEMORY TABLE JUNIOR(ID_JUNIOR BIGINT NOT NULL,NAME VARCHAR NOT NULL,SURNAME VARCHAR NOT NULL,STATUS INTEGER NOT NULL,NOTE VARCHAR,CONSTRAINT JUNIORS_PKEY PRIMARY KEY(ID_JUNIOR))
CREATE MEMORY TABLE PLAYER(ID_PLAYER BIGINT NOT NULL,NAME VARCHAR NOT NULL,SURNAME VARCHAR NOT NULL,COUNTRYFROM INTEGER NOT NULL,ID_JUNIOR_FK BIGINT DEFAULT NULL,STATUS INTEGER DEFAULT 0,NOTE VARCHAR,ID_POSITION TINYINT,SOLD_PRICE INTEGER,BUY_PRICE DOUBLE,ID_CLUB_FK BIGINT,TRANSFER_LIST INTEGER DEFAULT 0,NATIONAL INTEGER DEFAULT 0,YOUTH_TEAM_ID INTEGER DEFAULT 0, EXISTS_IN_SOKKER INT DEFAULT 0 NOT NULL, CONSTRAINT PLAYER_PKEY PRIMARY KEY(ID_PLAYER),CONSTRAINT PLAYER_ID_JUNIOR_FK_FKEY FOREIGN KEY(ID_JUNIOR_FK) REFERENCES JUNIOR(ID_JUNIOR) ON DELETE SET NULL)
CREATE MEMORY TABLE PLAYER_SKILLS(ID_SKILL INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,ID_PLAYER_FK BIGINT NOT NULL,AGE TINYINT NOT NULL,VALUE INTEGER NOT NULL,SALARY INTEGER NOT NULL,FORM TINYINT NOT NULL,STAMINA TINYINT NOT NULL,PACE TINYINT NOT NULL,TECHNIQUE TINYINT NOT NULL,PASSING TINYINT NOT NULL,KEEPER TINYINT NOT NULL,DEFENDER TINYINT NOT NULL,PLAYMAKER TINYINT NOT NULL,SCORER TINYINT NOT NULL,MATCHES INTEGER,GOALS INTEGER,ASSISTS INTEGER,CARDS TINYINT,INJURYDAYS DOUBLE,ID_TRAINING_FK INTEGER,NOTE VARCHAR,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,EXPERIENCE INTEGER DEFAULT 0,TEAMWORK INTEGER DEFAULT 0,DISCIPLINE INTEGER DEFAULT 0,pass_training BOOLEAN DEFAULT true NOT NULL,CONSTRAINT SKILLS_PKEY PRIMARY KEY(ID_SKILL),CONSTRAINT PLAYER_SKILLS_ID_TRAINING_FK_FKEY FOREIGN KEY(ID_TRAINING_FK) REFERENCES TRAINING(ID_TRAINING) ON DELETE SET NULL,CONSTRAINT SKILLS_ID_PLAYER_FK_FKEY FOREIGN KEY(ID_PLAYER_FK) REFERENCES PLAYER(ID_PLAYER) ON DELETE CASCADE)
CREATE MEMORY TABLE COACHES_AT_TRAININGS(ID_TRAINING INTEGER NOT NULL,ID_COACH INTEGER NOT NULL,ID_JOB INTEGER NOT NULL,NOTE VARCHAR,CONSTRAINT COACHES_AT_TRAININGS_PK PRIMARY KEY(ID_TRAINING,ID_COACH),CONSTRAINT COACHES_AT_TRAININGS_ID_COACH_FK FOREIGN KEY(ID_COACH) REFERENCES COACH(ID_COACH) ON DELETE CASCADE,CONSTRAINT COACHES_AT_TRAININGS_ID_TRAINING_FK FOREIGN KEY(ID_TRAINING) REFERENCES TRAINING(ID_TRAINING) ON DELETE CASCADE)
CREATE MEMORY TABLE JUNIOR_SKILLS(ID_SKILL INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,ID_JUNIOR_FK BIGINT NOT NULL,WEEKS TINYINT NOT NULL,SKILL TINYINT NOT NULL,ID_TRAINING_FK INTEGER,NOTE VARCHAR,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,CONSTRAINT JUNIOR_SKILLS_PKEY PRIMARY KEY(ID_SKILL),CONSTRAINT JUNIOR_SKILLS_ID_TRAINING_FK_FKEY FOREIGN KEY(ID_TRAINING_FK) REFERENCES TRAINING(ID_TRAINING) ON DELETE SET NULL,CONSTRAINT JUNIOR_SKILLS_ID_JUNIOR_FK_FKEY FOREIGN KEY(ID_JUNIOR_FK) REFERENCES JUNIOR(ID_JUNIOR) ON DELETE CASCADE)
CREATE MEMORY TABLE CLUB(ID BIGINT NOT NULL,COUNTRY INTEGER NOT NULL,REGION INTEGER NOT NULL,NOTE VARCHAR,IMAGE_PATH VARCHAR,DATE_CREATED BIGINT,JUNIORS_MAX INTEGER,CONSTRAINT CLUB_ID_PK PRIMARY KEY(ID))
CREATE MEMORY TABLE CLUB_DATA_MONEY(ID_DATA INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,ID_CLUB_FK BIGINT NOT NULL,MONEY BIGINT NOT NULL,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,CONSTRAINT CLUB_DATA_MONEY_ID_DATA_PK PRIMARY KEY(ID_DATA),CONSTRAINT CLUB_DATA_MONEY_ID_CLUB_FK_FKEY FOREIGN KEY(ID_CLUB_FK) REFERENCES CLUB(ID) ON DELETE CASCADE)
CREATE MEMORY TABLE CLUB_DATA_FANCLUB(ID_DATA INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,ID_CLUB_FK BIGINT NOT NULL,FANCLUBCOUNT INTEGER NOT NULL,FANCLUBMOOD TINYINT NOT NULL,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,CONSTRAINT CLUB_DATA_FANCLUB_ID_DATA_PK PRIMARY KEY(ID_DATA),CONSTRAINT CLUB_DATA_FANCLUB_ID_CLUB_FK_FKEY FOREIGN KEY(ID_CLUB_FK) REFERENCES CLUB(ID) ON DELETE CASCADE)
CREATE MEMORY TABLE CLUB_NAME(ID_DATA INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,ID_CLUB_FK BIGINT NOT NULL,NAME VARCHAR,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,CONSTRAINT CLUB_NAME_ID_DATA_PK PRIMARY KEY(ID_DATA),CONSTRAINT CLUB_NAME_ID_CLUB_FK_FKEY FOREIGN KEY(ID_CLUB_FK) REFERENCES CLUB(ID) ON DELETE CASCADE)
CREATE MEMORY TABLE CLUB_ARENA_NAME(ID_DATA INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,ID_CLUB_FK BIGINT NOT NULL,ARENA_NAME VARCHAR,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,CONSTRAINT CLUB_ARENA_NAME_ID_DATA_PK PRIMARY KEY(ID_DATA),CONSTRAINT CLUB_ARENA_NAME_ID_CLUB_FK_FKEY FOREIGN KEY(ID_CLUB_FK) REFERENCES CLUB(ID) ON DELETE CASCADE)
CREATE MEMORY TABLE NOTE(ID_NOTE INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,TITLE VARCHAR,TEXT VARCHAR,CHECKED BOOLEAN DEFAULT false NOT NULL,MILLIS BIGINT,ALERT_MILLIS BIGINT,MOD_MILLIS BIGINT,CONSTRAINT NOTES_ID_NOTE_PKEY PRIMARY KEY(ID_NOTE))
CREATE MEMORY TABLE NT_PLAYER_SKILLS(ID_SKILL INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,NT_ASSISTS INTEGER DEFAULT 0 NOT NULL,NT_GOALS INTEGER DEFAULT 0 NOT NULL,NT_MATCHES INTEGER DEFAULT 0 NOT NULL,NT_CARDS INTEGER DEFAULT 0 NOT NULL,ID_PLAYER_FK BIGINT NOT NULL,MILLIS BIGINT,DAY INTEGER,WEEK INTEGER,CONSTRAINT NT_SKILLS_PKEY PRIMARY KEY(ID_SKILL),CONSTRAINT NT_SKILLS_ID_PLAYER_FK_FKEY FOREIGN KEY(ID_PLAYER_FK) REFERENCES PLAYER(ID_PLAYER) ON DELETE CASCADE)
CREATE MEMORY TABLE TRANSFERS(ID BIGINT NOT NULL,SELLER_TEAM_ID INTEGER NOT NULL,BUYER_TEAM_ID INTEGER NOT NULL,SELLER_TEAM_NAME VARCHAR NOT NULL,BUYER_TEAM_NAME VARCHAR NOT NULL,PLAYER_ID BIGINT NOT NULL,MILLIS BIGINT NOT NULL,DAY INTEGER NOT NULL,WEEK INTEGER NOT NULL,PRICE INTEGER NOT NULL,PLAYER_VALUE INTEGER NOT NULL,CHECKED BOOLEAN DEFAULT FALSE NOT NULL, CONSTRAINT TRANSFERS_PK PRIMARY KEY(ID))
CREATE MEMORY TABLE RANK(ID_DATA INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,ID_CLUB_FK BIGINT NOT NULL,MILLIS BIGINT,RANK DOUBLE NOT NULL,DAY INTEGER,WEEK INTEGER,CONSTRAINT RANK_ID_DATA_PK PRIMARY KEY(ID_DATA),CONSTRAINT RANK_ID_CLUB_FK_FKEY FOREIGN KEY(ID_CLUB_FK) REFERENCES CLUB(ID) ON DELETE CASCADE)
CREATE MEMORY TABLE REPORTS(ID_REPORT BIGINT NOT NULL,TYPE INTEGER NOT NULL,PLAYER_ID BIGINT NOT NULL,MILLIS BIGINT NOT NULL,WEEK INTEGER NOT NULL,VALUE INTEGER NOT NULL, checked BOOLEAN DEFAULT false NOT NULL,CONSTRAINT REPORTS_PKEY PRIMARY KEY(ID_REPORT));
CREATE MEMORY TABLE PLAYER_ARCHIVE(PLAYER_ID BIGINT NOT NULL,NAME VARCHAR DEFAULT '' NOT NULL,SURNAME VARCHAR DEFAULT '' NOT NULL,COUNTRY_ID INTEGER DEFAULT 0 NOT NULL,TEAM_ID INTEGER DEFAULT 0 NOT NULL,AGE INTEGER DEFAULT 0 NOT NULL,TRANSFER_LIST INTEGER DEFAULT 0 NOT NULL,VALUE INTEGER DEFAULT 0 NOT NULL,WAGE INTEGER DEFAULT 0 NOT NULL,CARDS INTEGER DEFAULT 0 NOT NULL,GOALS INTEGER DEFAULT 0 NOT NULL,ASSISTS INTEGER DEFAULT 0 NOT NULL,MATCHES INTEGER DEFAULT 0 NOT NULL,NT_CARDS INTEGER DEFAULT 0 NOT NULL,NT_MATCHES INTEGER DEFAULT 0 NOT NULL,NT_ASSISTS INTEGER DEFAULT 0 NOT NULL,NT_GOALS INTEGER DEFAULT 0 NOT NULL,INJURY_DAYS DOUBLE DEFAULT 0.0E0 NOT NULL,SKILL_FORM INTEGER DEFAULT 0 NOT NULL,SKILL_EXPERIENCE INTEGER DEFAULT 0 NOT NULL,SKILL_TEAMWORK INTEGER DEFAULT 0 NOT NULL,SKILL_DISCIPLINE INTEGER DEFAULT 0 NOT NULL,NATIONAL INTEGER DEFAULT 0 NOT NULL,YOUTH_TEAM_ID INTEGER DEFAULT 0 NOT NULL,EXISTS_IN_SOKKER INTEGER DEFAULT 0 NOT NULL, NOTE VARCHAR DEFAULT '' NOT NULL,CONSTRAINT PLAYER_ARCHIVE_PKEY PRIMARY KEY(PLAYER_ID))CREATE INDEX ARENA_INDEX ON ARENA(ID)
CREATE INDEX TRAINING_INDEX ON TRAINING(ID_TRAINING)
CREATE INDEX COACH_INDEX ON COACH(ID_COACH)
CREATE INDEX JUNIOR_INDEX ON JUNIOR(ID_JUNIOR)
CREATE INDEX CLUB_INDEX ON CLUB(ID)
CREATE INDEX CLUB_DATA_MONEY_INDEX ON CLUB_DATA_MONEY(ID_DATA)
CREATE INDEX CLUB_DATA_FANCLUB_INDEX ON CLUB_DATA_FANCLUB(ID_DATA)
CREATE INDEX CLUB_NAME_INDEX ON CLUB_NAME(ID_DATA)
CREATE INDEX CLUB_ARENA_NAME_INDEX ON CLUB_ARENA_NAME(ID_DATA)
CREATE INDEX NOTE_INDEX ON NOTE(ID_NOTE)
CREATE INDEX RANK ON RANK(ID_DATA);
CREATE INDEX PLAYER_INDEX ON PLAYER(ID_PLAYER)
CREATE INDEX IDX_REPORTS ON REPORTS(ID_REPORT);
CREATE MEMORY TABLE COUNTRIES(COUNTRY_ID INTEGER NOT NULL,NAME VARCHAR,CURRENCY_NAME VARCHAR,CURRENCY_RATE DOUBLE,CONSTRAINT COUNTRIES_PKEY PRIMARY KEY(COUNTRY_ID));
CREATE MEMORY TABLE REGIONS(REGION_ID INTEGER NOT NULL,COUNTRY_ID INTEGER,NAME VARCHAR,WEATHER INTEGER,CONSTRAINT REGIONS_PKEY PRIMARY KEY(REGION_ID),CONSTRAINT REGIONS_COUNTRY_ID_FKEY FOREIGN KEY(COUNTRY_ID) REFERENCES COUNTRIES(COUNTRY_ID) ON DELETE CASCADE);
CREATE MEMORY TABLE LEAGUES( LEAGUE_ID INTEGER NOT NULL, NAME VARCHAR, COUNTRY_ID INTEGER, DIVISION INTEGER, TYPE INTEGER, IS_OFFICIAL INTEGER, IS_CUP INTEGER, USER_ID INTEGER, CONSTRAINT LEAGUES_LEAGUE_ID_PKEY PRIMARY KEY(LEAGUE_ID))
CREATE MEMORY TABLE MATCHES_TEAM( MATCH_ID INTEGER NOT NULL, LEAGUE_ID INTEGER, ROUND INTEGER, SEASON INTEGER, WEEK INTEGER, HOME_TEAM_ID INTEGER, AWAY_TEAM_ID INTEGER, HOME_TEAM_NAME VARCHAR, AWAY_TEAM_NAME VARCHAR, DAY INTEGER, DATE_EXPECTED TIMESTAMP, DATE_STARTED TIMESTAMP, HOME_TEAM_SCORE INTEGER, AWAY_TEAM_SCORE INTEGER, SUPPORTERS INTEGER, WEATHER INTEGER, IS_FINISHED INTEGER, CONSTRAINT MATCHES_TEAM_PKEY PRIMARY KEY(MATCH_ID), CONSTRAINT MATCHES_TEAM_LEAGUE_ID_FKEY FOREIGN KEY(LEAGUE_ID) REFERENCES LEAGUES(LEAGUE_ID) ON DELETE CASCADE)
CREATE MEMORY TABLE LEAGUE_TEAM( TEAM_ID INTEGER NOT NULL, LEAGUE_ID INTEGER, SEASON INTEGER, ROUND INTEGER, POINTS INTEGER, WINS INTEGER, DRAWS INTEGER, LOSSES INTEGER, GOALS_SCORED INTEGER, GOALS_LOST INTEGER, RANK_TOTAL VARCHAR, CONSTRAINT LEAGUE_TEAM_PKEY PRIMARY KEY(TEAM_ID, LEAGUE_ID, SEASON, ROUND))
CREATE MEMORY TABLE TEAM_STATS( MATCH_ID INTEGER NOT NULL, TEAM_ID INTEGER NOT NULL, TIME_ON_HALF INTEGER, TIME_POSSESSION INTEGER, OFFSIDES INTEGER, SHOOTS INTEGER, FOULS INTEGER, YELLOW_CARDS INTEGER, RED_CARDS INTEGER, TACTIC_NAME VARCHAR, RATING_SCORING INTEGER, RATING_PASSING INTEGER, RATING_DEFENDING INTEGER, CONSTRAINT TEAM_STATS_PKEY PRIMARY KEY(MATCH_ID,TEAM_ID), CONSTRAINT TEAM_STATS_MATCH_ID_FKEY FOREIGN KEY(MATCH_ID) REFERENCES MATCHES_TEAM(MATCH_ID) ON DELETE CASCADE ON UPDATE CASCADE)
CREATE MEMORY TABLE PLAYERS_STATS( MATCH_ID INTEGER, TEAM_ID INTEGER, PLAYER_ID INTEGER, NUMBER INTEGER, FORMATION INTEGER, TIME_IN INTEGER, TIME_OUT INTEGER, YELLOW_CARDS INTEGER, RED_CARDS INTEGER, IS_INJURED INTEGER, GOALS INTEGER, ASSISTS INTEGER, FOULS INTEGER, SHOOTS INTEGER, RATING INTEGER, TIME_PLAYING INTEGER, TIME_DEFENDING INTEGER,  injury_days INTEGER DEFAULT 0 NOT NULL, CONSTRAINT PLAYERS_STATS_MATCH_ID_FKEY FOREIGN KEY(MATCH_ID) REFERENCES MATCHES_TEAM(MATCH_ID) ON DELETE CASCADE ON UPDATE CASCADE)
CREATE MEMORY TABLE GALLERY( IMAGE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY, FILENAME VARCHAR DEFAULT '' NOT NULL, UPLOAD_DATE BIGINT)
CREATE MEMORY TABLE CONFIGURATION_TRAININGS(TYPE INTEGER,FORMATION INTEGER,GK INTEGER,DEF INTEGER,MID INTEGER,ATT INTEGER)
CREATE MEMORY TABLE CONFIGURATION(KEY VARCHAR,VALUE VARCHAR)
INSERT INTO CONFIGURATION_TRAININGS VALUES(2,0,100,0,0,0)
INSERT INTO CONFIGURATION_TRAININGS VALUES(2,1,100,0,0,0)
INSERT INTO CONFIGURATION_TRAININGS VALUES(2,2,100,0,0,0)
INSERT INTO CONFIGURATION_TRAININGS VALUES(2,3,100,0,0,0)
INSERT INTO CONFIGURATION_TRAININGS VALUES(3,0,70,50,50,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(3,1,25,70,50,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(3,2,25,50,100,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(3,3,25,50,50,70)
INSERT INTO CONFIGURATION_TRAININGS VALUES(4,0,70,50,50,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(4,1,25,70,50,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(4,2,25,50,100,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(4,3,25,50,50,70)
INSERT INTO CONFIGURATION_TRAININGS VALUES(5,0,100,40,40,40)
INSERT INTO CONFIGURATION_TRAININGS VALUES(5,1,25,100,40,40)
INSERT INTO CONFIGURATION_TRAININGS VALUES(5,2,25,40,100,40)
INSERT INTO CONFIGURATION_TRAININGS VALUES(5,3,25,40,40,100)
INSERT INTO CONFIGURATION_TRAININGS VALUES(6,0,100,40,40,40)
INSERT INTO CONFIGURATION_TRAININGS VALUES(6,1,25,100,40,40)
INSERT INTO CONFIGURATION_TRAININGS VALUES(6,2,25,40,100,40)
INSERT INTO CONFIGURATION_TRAININGS VALUES(6,3,25,40,40,100)
INSERT INTO CONFIGURATION_TRAININGS VALUES(7,0,60,50,50,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(7,1,25,60,50,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(7,2,25,50,80,50)
INSERT INTO CONFIGURATION_TRAININGS VALUES(7,3,25,50,50,100)
INSERT INTO CONFIGURATION_TRAININGS VALUES(8,0,100,100,100,100)
INSERT INTO CONFIGURATION_TRAININGS VALUES(8,1,100,100,100,100)
INSERT INTO CONFIGURATION_TRAININGS VALUES(8,2,100,100,100,100)
INSERT INTO CONFIGURATION_TRAININGS VALUES(8,3,100,100,100,100)
INSERT INTO CONFIGURATION VALUES('TrainingsConfigurationEnabled','false')
INSERT INTO CONFIGURATION VALUES('TrainingsConfigurationOfficialFriendly','120')
INSERT INTO CONFIGURATION VALUES('TrainingsConfigurationNational','20')
INSERT INTO CONFIGURATION VALUES('TrainingsConfigurationFriendly','80')
INSERT INTO CONFIGURATION VALUES('TrainingsConfigurationOfficialCup','115')
INSERT INTO CONFIGURATION VALUES('TrainingsConfigurationOfficial','100')
INSERT INTO system VALUES (0, 0, 0, 0, true, true, false, 0, 3, 0);
SET WRITE_DELAY 0 MILLIS;
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (1, 10, 8, 8, 8, 6, 56, 2, 2, 0);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (2, 10, 10, 10, 10, 10, 0, 40, 10, 0);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (3, 10, 10, 15, 10, 10, 0, 35, 10, 0);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (4, 10, 10, 10, 15, 10, 0, 30, 15, 0);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (5, 10, 10, 8, 14, 12, 0, 8, 30, 8);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (6, 10, 10, 8, 12, 12, 0, 15, 25, 8);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (7, 10, 10, 8, 14, 14, 0, 8, 24, 12);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (8, 10, 10, 12, 14, 12, 0, 8, 26, 8);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (9, 10, 10, 10, 12, 13, 0, 7, 12, 26);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (10, 10, 10, 13, 8, 13, 0, 6, 7, 33);
INSERT INTO assistant (id_position, form, stamina, pace, technique, passing, keeper, defender, playmaker, scorer) VALUES (11, 0, 0, 0, 0, 0, 0, 0, 0, 0);