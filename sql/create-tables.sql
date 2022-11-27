CREATE SEQUENCE seq_person;
CREATE SEQUENCE seq_transaction;

CREATE TABLE person
(
    id      BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq_person'),
    balance BIGINT NOT NULL
);

CREATE TABLE transaction
(
    id          BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq_transaction'),
    person_from BIGINT NOT NULL REFERENCES person (id),
    person_to   BIGINT NOT NULL REFERENCES person (id),
    amount      BIGINT NOT NULL

);

INSERT INTO person
VALUES (1, 1000);

INSERT INTO person
VALUES (2, 1000);