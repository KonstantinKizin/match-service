CREATE TABLE IF NOT EXISTS teams (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

INSERT INTO teams(id, name)
VALUES (1,'Chelsea FC'),
       (2,'Brighton & Hove Albion FC'),
       (3,'Aston Villa FC'),
       (4,'Leeds United'),
       (5,'Southampton FC'),
       (6,'Manchester United FC'),
       (7, 'West Ham United FC');

