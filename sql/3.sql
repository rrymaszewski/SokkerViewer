ALTER TABLE player_skills ADD COLUMN training BOOLEAN;
ALTER TABLE player_skills ALTER COLUMN training SET DEFAULT false;
UPDATE player_skills SET player_skills.id_training_fk = (SELECT distinct s.id_training_fk FROM player_skills as s WHERE player_skills.date = s.date AND s.id_training_fk is not null) WHERE player_skills.id_training_fk is null AND player_skills.date = (SELECT distinct s.date from player_skills as s WHERE player_skills.date = s.date AND s.id_training_fk is not null); 
UPDATE system SET version = 3;
