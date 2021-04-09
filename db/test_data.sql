INSERT INTO posts(name, description, created) VALUES ('Junior Java Job', 'Junior should suffer', '23-03-2021');
INSERT INTO posts(name, description, created) VALUES ('Middle Java Job', 'Works the most', '01-01-2014');
INSERT INTO posts(name, description, created) VALUES ('Senior Java Job', '400kk/sec', '12-01-2020');

INSERT INTO candidates(name, cityid) VALUES ('Junior Java', 1);
INSERT INTO candidates(name, cityid) VALUES ('Middle Java', 2);
INSERT INTO candidates(name, cityid) VALUES ('Senior Java', 3);

INSERT INTO users (name, email, password) VALUES ('root', 'root@localhost', crypt('root', gen_salt('bf')));

INSERT INTO cities (name) VALUES ('Moscow');
INSERT INTO cities (name) VALUES ('Saint Petersburg');
INSERT INTO cities (name) VALUES ('Novosibirsk');