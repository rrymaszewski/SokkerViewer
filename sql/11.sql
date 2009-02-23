delete from arena where id not in (select min(id) from arena group by location, type, roof, days);
UPDATE system SET version = 11;