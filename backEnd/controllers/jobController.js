/**
 * ============================================================================
 *  controllers/jobController.js — Contrôleur HTTP pour Job
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. Implémenter le CRUD REST complet.
 *    2. Ajouter la validation (name obligatoire, date valide, category_id FK).
 *    3. Documenter avec Swagger (JSDoc @swagger).
 *    4. Ajouter la RECHERCHE par nom (?search=), la PAGINATION (?page, ?limit)
 *       et le filtre par catégorie (?category_id=).
 *    5. Tester avec Postman + Swagger UI + curl.
 *
 *  Endpoints attendus :
 *    GET    /api/jobs                → Liste (avec recherche + pagination)
 *    GET    /api/jobs/:id            → Détail
 *    POST   /api/jobs                → Création
 *    PUT    /api/jobs/:id            → Modification
 *    DELETE /api/jobs/:id            → Suppression
 *
 *  Codes HTTP attendus :
 *    200, 201, 204, 400, 404, 500
 * ============================================================================
 */

'use strict';

// TODO : const jobService = require('../services/jobService');

async function getAllJobs(req, res, next) {
  next(new Error('jobController.getAllJobs : à implémenter'));
}

async function getJobById(req, res, next) {
  next(new Error('jobController.getJobById : à implémenter'));
}

async function createJob(req, res, next) {
  next(new Error('jobController.createJob : à implémenter'));
}

async function updateJob(req, res, next) {
  next(new Error('jobController.updateJob : à implémenter'));
}

async function deleteJob(req, res, next) {
  next(new Error('jobController.deleteJob : à implémenter'));
}

module.exports = {
  getAllJobs,
  getJobById,
  createJob,
  updateJob,
  deleteJob
};
