ALTER TABLE system ADD COLUMN repair_coaches BOOLEAN;
UPDATE system SET repair_coaches = true;
UPDATE system SET version = 12;