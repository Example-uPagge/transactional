INSERT INTO person values (1, 100);
INSERT INTO person values (2, 100);

UPDATE person SET balance = (balance - 10) WHERE id = 1;
INSERT INTO transaction(person_from, person_to, amount) values (1, 3, 10);
UPDATE person SET balance = (balance + 10) WHERE id = 2;
