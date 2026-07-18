/**
 * ============================================================================
 *  models/meetingModel.js — Couche d'accès aux données pour Meeting
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 *  Rappel du diagramme UML :
 *    Application 1 ─── 1 Meeting
 *
 *  Schéma de la table :
 *    id (PK), reference, link, status,
 *    application_id (FK applications.id, UNIQUE),
 *    created_at, updated_at
 *
 *  Le champ `status` est un ENUM('scheduled', 'held', 'cancelled').
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. findAll({ page, limit, status })
 *         - filtre optionnel par statut (?status=scheduled)
 *    2. findById(id)
 *    3. findByApplicationId(applicationId)
 *    4. findByReference(reference) — la reference doit être unique
 *    5. create(data)
 *    6. update(id, data)
 *    7. remove(id)
 *
 *  BONUS : machine à états pour le status
 *    scheduled → held      (marquer comme réalisé)
 *    scheduled → cancelled (annuler)
 * ============================================================================
 */

'use strict';

const { pool } = require('../config/database');

async function findAll(/* options */) {
  throw new Error('meetingModel.findAll : à implémenter');
}

async function findById(/* id */) {
  throw new Error('meetingModel.findById : à implémenter');
}

async function findByApplicationId(/* applicationId */) {
  throw new Error('meetingModel.findByApplicationId : à implémenter');
}

async function findByReference(/* reference */) {
  throw new Error('meetingModel.findByReference : à implémenter');
}

async function create(/* data */) {
  throw new Error('meetingModel.create : à implémenter');
}

async function update(/* id, data */) {
  throw new Error('meetingModel.update : à implémenter');
}

async function remove(/* id */) {
  throw new Error('meetingModel.remove : à implémenter');
}

module.exports = {
  findAll, findById, findByApplicationId, findByReference,
  create, update, remove
};
