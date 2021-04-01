INSERT INTO posts(name, description, created) VALUES ('Junior Java Job', 'Junior should suffer', '23-03-2021');
INSERT INTO posts(name, description, created) VALUES ('Middle Java Job', 'Works the most', '01-01-2014');
INSERT INTO posts(name, description, created) VALUES ('Senior Java Job', '400kk/sec', '12-01-2020');

INSERT INTO candidates(name) VALUES ('Junior Java');
INSERT INTO candidates(name) VALUES ('Middle Java');
INSERT INTO candidates(name) VALUES ('Senior Java');

INSERT INTO users (name, email, password) VALUES ('root', 'root@localhost', crypt('root', gen_salt('bf')));