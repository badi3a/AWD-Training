/**
 * ============================================================================
 *  models/notificationModel.js — Couche d'accès aux données pour Notification
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 *  Rappel du diagramme UML :
 *    Application 1 ─── 1 Notification
 *
 *  Schéma de la table :
 *    id (PK), sender, recipient, content, date,
 *    application_id (FK applications.id, UNIQUE), created_at
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. findAll({ page, limit, recipient })
 *         - Filtre optionnel par destinataire (?recipient=email@…)
 *    2. findById(id)
 *    3. findByApplicationId(applicationId)
 *    4. create(data)
 *    5. update(id, data)
 *    6. remove(id)
 * ============================================================================
 */

'use strict';

const { pool } = require('../config/database');

async function findAll(/* options */) {
  throw new Error('notificationModel.findAll : à implémenter');
}

async function findById(/* id */) {
  throw new Error('notificationModel.findById : à implémenter');
}

async function findByApplicationId(/* applicationId */) {
  throw new Error('notificationModel.findByApplicationId : à implémenter');
}

async function create(/* data */) {
  throw new Error('notificationModel.create : à implémenter');
}

async function update(/* id, data */) {
  throw new Error('notificationModel.update : à implémenter');
}

async function remove(/* id */) {
  throw new Error('notificationModel.remove : à implémenter');
}

module.exports = {
  findAll, findById, findByApplicationId,
  create, update, remove
};
