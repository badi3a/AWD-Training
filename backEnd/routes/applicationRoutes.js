/**
 * ============================================================================
 *  routes/applicationRoutes.js — Routes REST pour Application
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. Câbler les 5 routes CRUD REST vers le contrôleur.
 *    2. Attacher les middlewares de validation (à créer dans
 *       middlewares/applicationValidator.js).
 *    3. Ajouter les blocs JSDoc @swagger pour chaque route (voir
 *       candidateRoutes.js pour l'exemple).
 *    4. Ajouter les schémas Application / ApplicationInput dans
 *       swagger/swagger.js (components.schemas).
 *
 *  Structure recommandée :
 *
 *    router.get   ('/',      controller.getAllApplications);
 *    router.get   ('/:id',   validator.idRule,       validate, controller.getApplicationById);
 *    router.post  ('/',      validator.createRules,  validate, controller.createApplication);
 *    router.put   ('/:id',   validator.updateRules,  validate, controller.updateApplication);
 *    router.delete('/:id',   validator.idRule,       validate, controller.deleteApplication);
 * ============================================================================
 */

'use strict';

const express = require('express');
const router  = express.Router();

// TODO : décommenter et créer ces fichiers
// const controller = require('../controllers/applicationController');
// const validator  = require('../middlewares/applicationValidator');
// const validate   = require('../middlewares/validate');

// -- Placeholder — retire ce bloc quand tu as implémenté les routes --
router.all('/', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Application est à développer dans le TP.',
    hint:    "Voir controllers/applicationController.js et models/applicationModel.js"
  });
});
router.all('/:id', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Application est à développer dans le TP.'
  });
});

// -- Décommenter ces lignes quand vous aurez implémenté le module --
// router.get(   '/',    controller.getAllApplications);
// router.get(   '/:id', validator.idRule,      validate, controller.getApplicationById);
// router.post(  '/',    validator.createRules, validate, controller.createApplication);
// router.put(   '/:id', validator.updateRules, validate, controller.updateApplication);
// router.delete('/:id', validator.idRule,      validate, controller.deleteApplication);

module.exports = router;
