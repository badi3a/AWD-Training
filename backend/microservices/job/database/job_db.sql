-- =====================================================================
--  Job microservice - MySQL database
--  Run with:  mysql -u root -p < job_db.sql
--  (or paste it in phpMyAdmin / MySQL Workbench)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS job_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE job_db;

DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS category;

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
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(1000) NULL,
    available   BOOLEAN      NOT NULL DEFAULT TRUE,
    `date`      DATE         NOT NULL,
    category_id BIGINT       NOT NULL,
    CONSTRAINT pk_job PRIMARY KEY (id),
    CONSTRAINT fk_job_category FOREIGN KEY (category_id)
        REFERENCES category (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE INDEX idx_job_available ON job (available);

-- ---------------------------------------------------------------------
--  Sample data
-- ---------------------------------------------------------------------
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
