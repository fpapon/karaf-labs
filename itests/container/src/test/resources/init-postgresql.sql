CREATE SCHEMA test;

CREATE TABLE test.bar (
  foo VARCHAR(255)
);

INSERT INTO test.bar (foo) VALUES ('hello world');