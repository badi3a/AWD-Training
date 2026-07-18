/**
 * ============================================================================
 *  routes/meetingRoutes.js — Routes REST pour Meeting
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. Câbler les 5 routes CRUD.
 *    2. Ajouter la validation.
 *    3. Documenter avec Swagger.
 *    4. BONUS : PATCH /api/meetings/:id/status pour changer uniquement
 *       le statut de la réunion (scheduled → held ou cancelled).
 * ============================================================================
 */

'use strict';

const express = require('express');
const router  = express.Router();

// TODO : décommenter et créer ces fichiers
// const controller = require('../controllers/meetingController');
// const validator  = require('../middlewares/meetingValidator');
// const validate   = require('../middlewares/validate');

// -- Placeholder --
router.all('/', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Meeting est à développer dans le TP.'
  });
});
router.all('/:id', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Meeting est à développer dans le TP.'
  });
});

// -- Décommenter quand implémenté --
// router.get(   '/',    controller.getAllMeetings);
// router.get(   '/:id', validator.idRule,      validate, controller.getMeetingById);
// router.post(  '/',    validator.createRules, validate, controller.createMeeting);
// router.put(   '/:id', validator.updateRules, validate, controller.updateMeeting);
// router.delete('/:id', validator.idRule,      validate, controller.deleteMeeting);

module.exports = router;
