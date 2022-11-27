DELETE FROM person WHERE id = 3;
DELETE FROM transaction;
UPDATE person SET balance = 1000 WHERE id in (1,2)