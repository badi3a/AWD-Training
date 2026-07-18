/**
 * ============================================================================
 *  routes/notificationRoutes.js — Routes REST pour Notification
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. Câbler les 5 routes CRUD.
 *    2. Ajouter la validation (voir express-validator).
 *    3. Documenter avec Swagger.
 *    4. BONUS : route de filtrage /api/notifications?recipient=email@…
 * ============================================================================
 */

'use strict';

const express = require('express');
const router  = express.Router();

// TODO : décommenter et créer ces fichiers
// const controller = require('../controllers/notificationController');
// const validator  = require('../middlewares/notificationValidator');
// const validate   = require('../middlewares/validate');

// -- Placeholder --
router.all('/', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Notification est à développer dans le TP.'
  });
});
router.all('/:id', (req, res) => {
  res.status(501).json({
    status:  501,
    message: 'Not Implemented — Le module Notification est à développer dans le TP.'
  });
});

// -- Décommenter quand implémenté --
// router.get(   '/',    controller.getAllNotifications);
// router.get(   '/:id', validator.idRule,      validate, controller.getNotificationById);
// router.post(  '/',    validator.createRules, validate, controller.createNotification);
// router.put(   '/:id', validator.updateRules, validate, controller.updateNotification);
// router.delete('/:id', validator.idRule,      validate, controller.deleteNotification);

module.exports = router;
