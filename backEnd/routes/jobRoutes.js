/**
 * ============================================================================
 *  routes/jobRoutes.js — Routes REST pour Job
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. Câbler les 5 routes CRUD vers le contrôleur Job.
 *    2. Ajouter les middlewares de validation.
 *    3. Documenter chaque endpoint avec Swagger (@swagger).
 *    4. Implémenter :
 *         - la recherche (?search=)
 *         - la pagination (?page=, ?limit=)
 *         - le filtre par catégorie (?category_id=)
 *
 *  Endpoints attendus :
 *    GET    /api/jobs
 *    GET    /api/jobs/:id
 *    POST   /api/jobs
 *    PUT    /api/jobs/:id
 *    DELETE /api/jobs/:id
 * ============================================================================
 */

'use strict';

const express = require('express');
const router  = express.Router();

// TODO : const controller = require('../controllers/jobController');
// TODO : const validator  = require('../middlewares/jobValidator');
// TODO : const validate   = require('../middlewares/validate');

// -- Placeholder --
router.all('/', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Job est à développer dans le TP.',
    hint:    "Voir controllers/jobController.js et models/jobModel.js"
  });
});
router.all('/:id', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Job est à développer dans le TP.'
  });
});

// -- Décommenter quand implémenté --
// router.get(   '/',    controller.getAllJobs);
// router.get(   '/:id', validator.idRule,      validate, controller.getJobById);
// router.post(  '/',    validator.createRules, validate, controller.createJob);
// router.put(   '/:id', validator.updateRules, validate, controller.updateJob);
// router.delete('/:id', validator.idRule,      validate, controller.deleteJob);

module.exports = router;
