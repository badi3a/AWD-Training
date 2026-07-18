/**
 * ============================================================================
 *  controllers/applicationController.js — Contrôleur HTTP pour Application
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. Implémenter le CRUD REST complet (getAll, getById, create, update, delete).
 *    2. Ajouter les validations (voir express-validator, s'inspirer du
 *       candidateValidator.js).
 *    3. Documenter chaque endpoint avec Swagger (JSDoc @swagger dans les routes).
 *    4. Tester avec Postman et Swagger UI.
 *
 *  Endpoints attendus :
 *    GET    /api/applications          → Liste (avec pagination)
 *    GET    /api/applications/:id      → Détail
 *    POST   /api/applications          → Création (201 Created)
 *    PUT    /api/applications/:id      → Modification (200 OK)
 *    DELETE /api/applications/:id      → Suppression (204 No Content)
 *
 *  Cas d'erreur à gérer :
 *    - 400 : données invalides (motivation vide, dates malformées, …)
 *    - 400 : candidate_id ou job_id inexistant
 *    - 404 : application inexistante (GET, PUT, DELETE)
 *    - 409 : le candidat a déjà postulé à cette offre
 *
 *  Structure recommandée (voir candidateController.js) :
 *    async function getAllApplications(req, res, next) {
 *      try {
 *        const result = await applicationService.getAllApplications({ ... });
 *        res.status(200).json(result);
 *      } catch (err) { next(err); }
 *    }
 * ============================================================================
 */

'use strict';

// TODO : importer le service applicationService quand vous l'aurez créé
// const applicationService = require('../services/applicationService');

async function getAllApplications(req, res, next) {
  // TODO : implémenter
  next(new Error('applicationController.getAllApplications : à implémenter'));
}

async function getApplicationById(req, res, next) {
  // TODO : implémenter
  next(new Error('applicationController.getApplicationById : à implémenter'));
}

async function createApplication(req, res, next) {
  // TODO : implémenter
  next(new Error('applicationController.createApplication : à implémenter'));
}

async function updateApplication(req, res, next) {
  // TODO : implémenter
  next(new Error('applicationController.updateApplication : à implémenter'));
}

async function deleteApplication(req, res, next) {
  // TODO : implémenter
  next(new Error('applicationController.deleteApplication : à implémenter'));
}

module.exports = {
  getAllApplications,
  getApplicationById,
  createApplication,
  updateApplication,
  deleteApplication
};
