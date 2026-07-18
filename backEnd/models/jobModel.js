/**
 * ============================================================================
 *  models/jobModel.js — Couche d'accès aux données pour Job
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 *  Rappel du diagramme UML :
 *    Job N ─── 1 Category
 *    Job 1 ─── N Application
 *
 *  Schéma de la table (voir sql/create_database.sql) :
 *    id (PK), name, description, available, date,
 *    category_id (FK categories.id), created_at, updated_at
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. findAll({ page, limit, search, category_id, available })
 *         Doit gérer :
 *           - recherche (?search=…) sur le nom (LIKE '%…%')
 *           - filtre par catégorie (?category_id=…)
 *           - filtre par disponibilité (?available=true/false)
 *           - tri (?sort=name, -date, …)
 *
 *    2. findById(id)
 *
 *    3. create(data)
 *
 *    4. update(id, data)
 *
 *    5. remove(id)
 *
 *  BONUS :
 *    - findByCategoryId(categoryId) → toutes les offres d'une catégorie.
 *    - JOIN avec la table categories pour retourner le nom de la catégorie.
 * ============================================================================
 */

'use strict';

const { pool } = require('../config/database');

async function findAll(/* options */) {
  throw new Error('jobModel.findAll : à implémenter');
}

async function findById(/* id */) {
  throw new Error('jobModel.findById : à implémenter');
}

async function create(/* data */) {
  throw new Error('jobModel.create : à implémenter');
}

async function update(/* id, data */) {
  throw new Error('jobModel.update : à implémenter');
}

async function remove(/* id */) {
  throw new Error('jobModel.remove : à implémenter');
}

module.exports = { findAll, findById, create, update, remove };
