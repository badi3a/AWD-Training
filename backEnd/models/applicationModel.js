/**
 * ============================================================================
 *  models/applicationModel.js — Couche d'accès aux données pour Application
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 *  Rappel du diagramme UML :
 *    Candidate  1 ─── N  Application
 *    Application N ─── 1  Job
 *    Application 1 ─── 1  Meeting
 *    Application 1 ─── 1  Notification
 *
 *  Schéma de la table (voir sql/create_database.sql) :
 *    id (PK), applicationDate, motivation,
 *    candidate_id (FK candidates.id),
 *    job_id (FK jobs.id),
 *    created_at, updated_at
 *
 *  Contrainte d'unicité : (candidate_id, job_id) — un candidat ne postule
 *  qu'une seule fois par offre.
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. findAll({ page, limit })
 *         SELECT * FROM applications
 *         ORDER BY id ASC
 *         LIMIT ? OFFSET ?
 *         → Bonus : JOIN sur candidates + jobs pour retourner les données
 *                    associées imbriquées (voir candidateModel.js pour l'idée).
 *
 *    2. findById(id)
 *         SELECT * FROM applications WHERE id = ?
 *
 *    3. findByCandidateAndJob(candidateId, jobId)
 *         Utile pour vérifier la contrainte d'unicité avant INSERT.
 *
 *    4. create(data)
 *         INSERT INTO applications (applicationDate, motivation,
 *                                    candidate_id, job_id) VALUES (?, ?, ?, ?)
 *
 *    5. update(id, data)
 *         UPDATE applications SET … WHERE id = ?
 *
 *    6. remove(id)
 *         DELETE FROM applications WHERE id = ?
 *         (le meeting et la notification liés seront supprimés en CASCADE)
 *
 *  N'oubliez pas :
 *    - d'utiliser des requêtes paramétrées (?), jamais de concaténation SQL.
 *    - de gérer le pool depuis `../config/database`.
 *    - d'exporter les fonctions à la fin du fichier.
 * ============================================================================
 */

'use strict';

const { pool } = require('../config/database');

// TODO : implémenter findAll
async function findAll(/* { page, limit } */) {
  throw new Error('applicationModel.findAll : à implémenter');
}

// TODO : implémenter findById
async function findById(/* id */) {
  throw new Error('applicationModel.findById : à implémenter');
}

// TODO : implémenter findByCandidateAndJob (contrainte d'unicité)
async function findByCandidateAndJob(/* candidateId, jobId */) {
  throw new Error('applicationModel.findByCandidateAndJob : à implémenter');
}

// TODO : implémenter create
async function create(/* data */) {
  throw new Error('applicationModel.create : à implémenter');
}

// TODO : implémenter update
async function update(/* id, data */) {
  throw new Error('applicationModel.update : à implémenter');
}

// TODO : implémenter remove
async function remove(/* id */) {
  throw new Error('applicationModel.remove : à implémenter');
}

module.exports = {
  findAll,
  findById,
  findByCandidateAndJob,
  create,
  update,
  remove
};
