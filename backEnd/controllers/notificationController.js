/**
 * ============================================================================
 *  controllers/notificationController.js — Contrôleur HTTP pour Notification
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. CRUD REST complet.
 *    2. Gestion spécifique des notifications :
 *         - filtrer par destinataire (?recipient=email@…)
 *         - endpoint bonus : GET /api/notifications/recipient/:email
 *    3. Validation : sender / recipient au format email, content non vide,
 *       application_id doit exister.
 *    4. Swagger doc.
 *
 *  Endpoints attendus :
 *    GET    /api/notifications
 *    GET    /api/notifications/:id
 *    POST   /api/notifications
 *    PUT    /api/notifications/:id
 *    DELETE /api/notifications/:id
 * ============================================================================
 */

'use strict';

// TODO : const notificationService = require('../services/notificationService');

async function getAllNotifications(req, res, next) {
  next(new Error('notificationController.getAllNotifications : à implémenter'));
}

async function getNotificationById(req, res, next) {
  next(new Error('notificationController.getNotificationById : à implémenter'));
}

async function createNotification(req, res, next) {
  next(new Error('notificationController.createNotification : à implémenter'));
}

async function updateNotification(req, res, next) {
  next(new Error('notificationController.updateNotification : à implémenter'));
}

async function deleteNotification(req, res, next) {
  next(new Error('notificationController.deleteNotification : à implémenter'));
}

module.exports = {
  getAllNotifications,
  getNotificationById,
  createNotification,
  updateNotification,
  deleteNotification
};
