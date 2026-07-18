-- ============================================================================
--  create_database.sql — Création complète de la base JobBoard
-- ----------------------------------------------------------------------------
--  Ce script est généré à partir du diagramme de classes UML de JobBoard.
--
--  Il crée :
--    - la base MySQL `jobboard` (charset utf8mb4)
--    - les 7 tables du diagramme (candidates, addresses, applications, jobs,
--      categories, meetings, notifications)
--    - toutes les clés primaires
--    - toutes les clés étrangères
--    - les index nécessaires
--    - un jeu de données de démonstration
--
--  Utilisation :
--    # Ligne de commande MySQL
--    mysql -u root -p < sql/create_database.sql
--
--    # Depuis phpMyAdmin
--    Onglet Importer → sélectionner ce fichier
--
--    # Depuis Node.js (script fourni)
--    npm run db:init
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 0. Base
-- ----------------------------------------------------------------------------
DROP DATABASE IF EXISTS `jobboard`;

CREATE DATABASE `jobboard`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `jobboard`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ============================================================================
-- 1. Table `addresses` — adresses postales
-- ============================================================================
CREATE TABLE `addresses` (
    `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `street`      VARCHAR(150) NOT NULL,
    `houseNumber` VARCHAR(20)  NOT NULL,
    `zipCode`     VARCHAR(20)  NOT NULL,
    `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_addresses_zipCode` (`zipCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 2. Table `candidates` — candidats (relation 1:1 avec addresses)
-- ============================================================================
-- D'après le diagramme UML :
--   Candidate 1 ─── 1 Address
-- Chaque candidat possède exactement une adresse. On matérialise cette relation
-- par une FK `address_id` dans `candidates`, avec contrainte UNIQUE pour
-- garantir que 2 candidats ne peuvent pas partager la même adresse.
-- ============================================================================
CREATE TABLE `candidates` (
    `id`         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `firstname`  VARCHAR(80)  NOT NULL,
    `lastname`   VARCHAR(80)  NOT NULL,
    `email`      VARCHAR(150) NOT NULL,
    `address_id` INT UNSIGNED NULL COMMENT '1:1 avec addresses',
    `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_candidates_email`      (`email`),
    UNIQUE KEY `uk_candidates_address_id` (`address_id`),
    CONSTRAINT `fk_candidates_address`
        FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 3. Table `categories` — catégories des offres d'emploi
-- ============================================================================
CREATE TABLE `categories` (
    `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(100) NOT NULL,
    `description` VARCHAR(500) NULL,
    `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_categories_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 4. Table `jobs` — offres d'emploi (relation N:1 avec categories)
-- ============================================================================
-- Diagramme UML : Job N ─── 1 Category
-- ============================================================================
CREATE TABLE `jobs` (
    `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(150) NOT NULL,
    `description` TEXT         NOT NULL,
    `available`   BOOLEAN      NOT NULL DEFAULT TRUE,
    `date`        DATE         NOT NULL,
    `category_id` INT UNSIGNED NOT NULL,
    `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_jobs_category_id` (`category_id`),
    KEY `idx_jobs_available`   (`available`),
    CONSTRAINT `fk_jobs_category`
        FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 5. Table `applications` — candidatures
-- ============================================================================
-- Diagramme UML :
--   Candidate 1 ─── N Application
--   Application N ─── 1 Job
-- Une candidature relie un candidat à une offre d'emploi.
-- Contrainte d'unicité : un candidat ne peut postuler qu'une seule fois
-- à la même offre → clé unique composite (candidate_id, job_id).
-- ============================================================================
CREATE TABLE `applications` (
    `id`              INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `applicationDate` DATE         NOT NULL,
    `motivation`      TEXT         NULL,
    `candidate_id`    INT UNSIGNED NOT NULL,
    `job_id`          INT UNSIGNED NOT NULL,
    `created_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_application_unique` (`candidate_id`, `job_id`)
        COMMENT 'Un candidat ne peut postuler qu''une fois par offre',
    KEY `idx_applications_job_id` (`job_id`),
    CONSTRAINT `fk_applications_candidate`
        FOREIGN KEY (`candidate_id`) REFERENCES `candidates` (`id`)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_applications_job`
        FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 6. Table `meetings` — entretiens/rendez-vous liés à une candidature
-- ============================================================================
-- Diagramme UML : Application 1 ─── 1 Meeting
-- Un meeting est optionnel (toutes les candidatures n'aboutissent pas à un
-- entretien) mais s'il existe, il concerne UNE seule candidature.
-- ============================================================================
CREATE TABLE `meetings` (
    `id`             INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `reference`      VARCHAR(50)  NOT NULL,
    `link`           VARCHAR(500) NULL COMMENT 'Lien de la visioconférence',
    `status`         ENUM('scheduled', 'held', 'cancelled') NOT NULL DEFAULT 'scheduled',
    `application_id` INT UNSIGNED NOT NULL,
    `created_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_meetings_reference`      (`reference`),
    UNIQUE KEY `uk_meetings_application_id` (`application_id`),
    CONSTRAINT `fk_meetings_application`
        FOREIGN KEY (`application_id`) REFERENCES `applications` (`id`)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================================
-- 7. Table `notifications` — notifications liées aux candidatures
-- ============================================================================
-- Diagramme UML : Application 1 ─── 1 Notification
-- ============================================================================
CREATE TABLE `notifications` (
    `id`             INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `sender`         VARCHAR(150) NOT NULL,
    `recipient`      VARCHAR(150) NOT NULL,
    `content`        TEXT         NOT NULL,
    `date`           DATETIME     NOT NULL,
    `application_id` INT UNSIGNED NOT NULL,
    `created_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_notifications_application_id` (`application_id`),
    KEY `idx_notifications_recipient` (`recipient`),
    CONSTRAINT `fk_notifications_application`
        FOREIGN KEY (`application_id`) REFERENCES `applications` (`id`)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ----------------------------------------------------------------------------
-- Réactivation des contraintes
-- ----------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 1;


-- ============================================================================
-- DONNÉES DE DÉMONSTRATION
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Adresses
-- ----------------------------------------------------------------------------
INSERT INTO `addresses` (`id`, `street`, `houseNumber`, `zipCode`) VALUES
    (1, 'Avenue Habib Bourguiba',    '25',  '1000'),
    (2, 'Rue Ibn Khaldoun',          '18',  '2080'),
    (3, 'Rue de Marseille',          '12',  '1002'),
    (4, 'Avenue Mohamed V',          '7',   '3000'),
    (5, 'Rue de la République',      '42',  '4000');


-- ----------------------------------------------------------------------------
-- Candidats
-- ----------------------------------------------------------------------------
INSERT INTO `candidates` (`id`, `firstname`, `lastname`, `email`, `address_id`) VALUES
    (1, 'Youssef',  'Mzoughi',    'youssef.mzoughi@example.tn',  1),
    (2, 'Emna',     'Boudhaouia', 'emna.boudhaouia@example.tn',  2),
    (3, 'Karim',    'Trabelsi',   'karim.trabelsi@example.tn',   3),
    (4, 'Salma',    'Bouaziz',    'salma.bouaziz@example.tn',    4),
    (5, 'Aicha',    'Ben Ali',    'aicha.benali@example.tn',     5);


-- ----------------------------------------------------------------------------
-- Categories
-- ----------------------------------------------------------------------------
INSERT INTO `categories` (`id`, `name`, `description`) VALUES
    (1, 'Développement Web',   'Postes de développeur front, back ou full-stack'),
    (2, 'Data Science',        'Postes en analyse de données et machine learning'),
    (3, 'DevOps & Cloud',      'Postes en infrastructure et CI/CD'),
    (4, 'Cybersécurité',       'Postes en sécurité offensive et défensive'),
    (5, 'UX / UI Design',      'Postes en design d''interface et d''expérience');


-- ----------------------------------------------------------------------------
-- Jobs
-- ----------------------------------------------------------------------------
INSERT INTO `jobs` (`id`, `name`, `description`, `available`, `date`, `category_id`) VALUES
    (1, 'Développeur Full-Stack Node.js',
       'Nous recherchons un développeur Full-Stack pour rejoindre notre équipe produit.',
       TRUE, '2026-02-01', 1),
    (2, 'Data Scientist Junior',
       'Rejoignez notre équipe Data pour construire des modèles prédictifs en Python.',
       TRUE, '2026-02-10', 2),
    (3, 'DevOps Engineer',
       'Automatisation CI/CD, Kubernetes, monitoring. Environnement multi-cloud.',
       TRUE, '2026-02-15', 3),
    (4, 'Pentester Junior',
       'Réaliser des tests d''intrusion sur nos applications.',
       FALSE, '2026-01-20', 4),
    (5, 'UX Designer',
       'Concevoir les nouveaux parcours utilisateur de notre plateforme.',
       TRUE, '2026-02-20', 5);


-- ----------------------------------------------------------------------------
-- Applications
-- ----------------------------------------------------------------------------
INSERT INTO `applications` (`id`, `applicationDate`, `motivation`, `candidate_id`, `job_id`) VALUES
    (1, '2026-02-05', 'Passionné par le Full-Stack, je souhaite mettre mes compétences à votre service.', 1, 1),
    (2, '2026-02-06', 'Data scientist junior, spécialisée en NLP.',                                        2, 2),
    (3, '2026-02-08', 'Expérience de 2 ans en DevOps chez un intégrateur.',                                3, 3),
    (4, '2026-02-12', 'Fascinée par le design d''expérience, très motivée.',                               4, 5);


-- ----------------------------------------------------------------------------
-- Meetings (uniquement pour les 2 premières candidatures)
-- ----------------------------------------------------------------------------
INSERT INTO `meetings` (`id`, `reference`, `link`, `status`, `application_id`) VALUES
    (1, 'MTG-2026-001', 'https://meet.jobboard.tn/mtg-2026-001', 'scheduled', 1),
    (2, 'MTG-2026-002', 'https://meet.jobboard.tn/mtg-2026-002', 'held',      2);


-- ----------------------------------------------------------------------------
-- Notifications
-- ----------------------------------------------------------------------------
INSERT INTO `notifications` (`id`, `sender`, `recipient`, `content`, `date`, `application_id`) VALUES
    (1, 'recruitment@jobboard.tn', 'youssef.mzoughi@example.tn',
       'Votre candidature a bien été reçue. Un entretien vous sera proposé prochainement.',
       '2026-02-05 10:30:00', 1),
    (2, 'recruitment@jobboard.tn', 'emna.boudhaouia@example.tn',
       'Bonne nouvelle : votre profil retient notre attention.',
       '2026-02-06 14:15:00', 2),
    (3, 'recruitment@jobboard.tn', 'karim.trabelsi@example.tn',
       'Nous avons bien reçu votre candidature.',
       '2026-02-08 09:45:00', 3),
    (4, 'recruitment@jobboard.tn', 'salma.bouaziz@example.tn',
       'Merci pour votre candidature au poste d''UX Designer.',
       '2026-02-12 16:00:00', 4);


-- ============================================================================
-- FIN DU SCRIPT
-- ============================================================================
-- Vérifications rapides :
--   SELECT COUNT(*) FROM candidates;     -- doit renvoyer 5
--   SELECT COUNT(*) FROM addresses;      -- doit renvoyer 5
--   SELECT COUNT(*) FROM jobs;           -- doit renvoyer 5
--   SELECT COUNT(*) FROM applications;   -- doit renvoyer 4
--   SELECT COUNT(*) FROM meetings;       -- doit renvoyer 2
--   SELECT COUNT(*) FROM notifications;  -- doit renvoyer 4
-- ============================================================================
