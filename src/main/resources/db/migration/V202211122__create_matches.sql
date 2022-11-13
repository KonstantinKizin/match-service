CREATE TABLE IF NOT EXISTS matches (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    home_id INT NOT NULL,
    away_id INT NOT NULL,
    home_score TINYINT,
    away_score TINYINT,
    start_date TIMESTAMP NOT NULL
);
ALTER TABLE matches ADD FOREIGN KEY (home_id) REFERENCES teams(id);
ALTER TABLE matches ADD FOREIGN KEY (away_id) REFERENCES teams(id);
INSERT INTO matches(start_date, home_id, away_id, away_score, home_score)
VALUES ('2023-03-22 22:00:00', 1, 2, null , null),
       ('2023-03-23 22:00:00', 3, 4, null , null),
       ('2023-03-24 17:00:00', 5, 6, null , null),
       ('2022-03-25 23:00:00', 1, 3, 0 , 3),
       ('2023-03-25 23:00:00', 5, 3, null , null),
       ('2022-07-29 23:00:00', 5, 3, 0 , 0),
       ('2022-03-25 23:00:00', 2, 7, 1 , 0)

