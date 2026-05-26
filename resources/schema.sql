DROP TABLE IF EXISTS rezultat_quiz;
DROP TABLE IF EXISTS inscriere;
DROP TABLE IF EXISTS quiz;
DROP TABLE IF EXISTS curs;
DROP TABLE IF EXISTS cursant;
DROP TABLE IF EXISTS instructor;

CREATE TABLE instructor (
    id INTEGER PRIMARY KEY,
    nume VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    username VARCHAR(100) NOT NULL,
    specializare VARCHAR(200) NOT NULL
);

CREATE TABLE cursant (
    id INTEGER PRIMARY KEY,
    nume VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    username VARCHAR(100) NOT NULL,
    an_studiu INTEGER NOT NULL
);

CREATE TABLE curs (
    cod VARCHAR(50) PRIMARY KEY,
    titlu VARCHAR(200) NOT NULL,
    instructor_id INTEGER NOT NULL,
    durata_ore INTEGER NOT NULL,
    FOREIGN KEY (instructor_id) REFERENCES instructor(id)
);

CREATE TABLE quiz (
    id INTEGER PRIMARY KEY,
    titlu VARCHAR(200) NOT NULL,
    curs_cod VARCHAR(50) NOT NULL,
    numar_rezultate INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (curs_cod) REFERENCES curs(cod)
);

CREATE TABLE inscriere (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cursant_id INTEGER NOT NULL,
    curs_cod VARCHAR(50) NOT NULL,
    data_inscriere VARCHAR(20) NOT NULL,
    FOREIGN KEY (cursant_id) REFERENCES cursant(id),
    FOREIGN KEY (curs_cod) REFERENCES curs(cod)
);

CREATE TABLE rezultat_quiz (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cursant_id INTEGER NOT NULL,
    quiz_id INTEGER NOT NULL,
    scor INTEGER NOT NULL,
    data_rezultat VARCHAR(20) NOT NULL,
    FOREIGN KEY (cursant_id) REFERENCES cursant(id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);