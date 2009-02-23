ALTER TABLE system ADD COLUMN last_modification_millis BIGINT;
ALTER TABLE system ADD COLUMN last_modification_sk_week INTEGER;
ALTER TABLE system ADD COLUMN last_modification_sk_day INTEGER;
UPDATE system SET version = 8;