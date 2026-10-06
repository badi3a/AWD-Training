-- =====================================================================
--  AWD recruitment platform - monolithic version - MySQL database
--  One database for the three modules: candidat, job, candidature
--
--  Run with:  mysql -u root -p < awd_db.sql
--  (or paste it in phpMyAdmin / MySQL Workbench)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS awd_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE awd_db;

-- Drop in reverse order of the foreign keys
DROP TABLE IF EXISTS application;
DROP TABLE IF EXISTS candidate;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS category;

-- =====================================================================
--  Module CANDIDAT
-- =====================================================================

-- ---------------------------------------------------------------------
--  Table: address
-- ---------------------------------------------------------------------
CREATE TABLE address (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    street       VARCHAR(150) NOT NULL,
    house_number VARCHAR(10)  NOT NULL,
    zip_code     VARCHAR(10)  NOT NULL,
    CONSTRAINT pk_address PRIMARY KEY (id)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
--  Table: candidate   (one candidate -> at most one address, 1..1)
-- ---------------------------------------------------------------------
CREATE TABLE candidate (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    firstname  VARCHAR(50)  NOT NULL,
    lastname   VARCHAR(50)  NOT NULL,
    email      VARCHAR(150) NOT NULL,
    address_id BIGINT       NULL,
    CONSTRAINT pk_candidate PRIMARY KEY (id),
    CONSTRAINT uk_candidate_email UNIQUE (email),
    CONSTRAINT uk_candidate_address UNIQUE (address_id),
    CONSTRAINT fk_candidate_address FOREIGN KEY (address_id)
        REFERENCES address (id)
        ON DELETE SET NULL
) ENGINE = InnoDB;

-- =====================================================================
--  Module JOB
-- =====================================================================

-- ---------------------------------------------------------------------
--  Table: category
-- ---------------------------------------------------------------------
CREATE TABLE category (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uk_category_name UNIQUE (name)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
--  Table: job   (many jobs -> one category)
-- ---------------------------------------------------------------------
CREATE TABLE job (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)  NOT NULL,
    description VARCHAR(1000) NULL,
    available   BOOLEAN       NOT NULL DEFAULT TRUE,
    `date`      DATE          NOT NULL,
    category_id BIGINT        NOT NULL,
    CONSTRAINT pk_job PRIMARY KEY (id),
    CONSTRAINT fk_job_category FOREIGN KEY (category_id)
        REFERENCES category (id)
        ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE INDEX idx_job_available ON job (available);

-- =====================================================================
--  Module CANDIDATURE
-- =====================================================================

-- ---------------------------------------------------------------------
--  Table: application   (links a candidate to a job)
--    one candidate -> many applications
--    one job       -> many applications
--    a candidate applies at most once to the same job
-- ---------------------------------------------------------------------
CREATE TABLE application (
    id               BIGINT        NOT NULL AUTO_INCREMENT,
    application_date DATE          NOT NULL,
    motivation       VARCHAR(2000) NULL,
    candidate_id     BIGINT        NOT NULL,
    job_id           BIGINT        NOT NULL,
    CONSTRAINT pk_application PRIMARY KEY (id),
    CONSTRAINT uk_application_candidate_job UNIQUE (candidate_id, job_id),
    CONSTRAINT fk_application_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidate (id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_application_job FOREIGN KEY (job_id)
        REFERENCES job (id)
        ON DELETE RESTRICT
) ENGINE = InnoDB;

-- =====================================================================
--  Sample data
-- =====================================================================
INSERT INTO address (street, house_number, zip_code) VALUES
  ('Avenue Habib Bourguiba', '12', '1001'),
  ('Rue de Marseille',       '5',  '1000'),
  ('Rue du Lac Leman',       '27', '1053');

INSERT INTO candidate (firstname, lastname, email, address_id) VALUES
  ('Amine', 'Ben Salah', 'amine.bensalah@example.com', 1),
  ('Sarra', 'Trabelsi',  'sarra.trabelsi@example.com', 2),
  ('Yassine', 'Gharbi',  'yassine.gharbi@example.com', 3),
  ('Lina', 'Mansour',    'lina.mansour@example.com',   NULL);

INSERT INTO category (name, description) VALUES
  ('Software Development', 'Backend, frontend and mobile development jobs'),
  ('Data',                 'Data engineering, data science and BI'),
  ('DevOps',               'Cloud, CI/CD and infrastructure');

INSERT INTO job (name, description, available, `date`, category_id) VALUES
  ('Java Spring Boot Developer', 'Build microservices with Spring Boot and Spring Cloud', TRUE,  '2026-09-01', 1),
  ('Angular Developer',          'Develop web front-ends with Angular',                   TRUE,  '2026-09-10', 1),
  ('Data Analyst',               'Dashboards and reporting with SQL and Power BI',       TRUE,  '2026-08-20', 2),
  ('Data Engineer',              'Build data pipelines',                                  FALSE, '2026-07-15', 2),
  ('DevOps Engineer',            'Docker, Kubernetes and Jenkins pipelines',              TRUE,  '2026-09-15', 3);

INSERT INTO application (application_date, motivation, candidate_id, job_id) VALUES
  ('2026-09-12', 'Two years of experience with Spring Boot and REST APIs.', 1, 1),
  ('2026-09-14', 'Passionate about front-end development with Angular.',    2, 2),
  ('2026-09-15', 'Interested in backend development as well.',              2, 1),
  ('2026-09-20', 'Strong SQL skills and Power BI certification.',           3, 3),
  ('2026-09-22', 'Hands-on experience with Docker and GitHub Actions.',     1, 5);
