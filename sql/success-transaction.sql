UPDATE person SET balance=100 WHERE id IN (1,2);
DELETE FROM transaction WHERE person_from = 1;

BEGIN;

UPDATE person SET balance = (balance - 10) WHERE id = 1;
INSERT INTO transaction(person_from, person_to, amount) values (1, 3, 10);
UPDATE person SET balance = (balance + 10) WHERE id = 2;

COMMIT;