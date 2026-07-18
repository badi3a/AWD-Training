/**
 * ============================================================================
 *  controllers/meetingController.js — Contrôleur HTTP pour Meeting
 * ============================================================================
 *
 *  ⚠️  MODULE À COMPLÉTER PAR LES ÉTUDIANTS ⚠️
 *
 * ----------------------------------------------------------------------------
 *  TP — Ce que vous devez implémenter :
 *
 *    1. CRUD REST complet.
 *    2. Gestion des réunions :
 *         - reference doit être unique (contrainte 409 Conflict).
 *         - status ne peut passer que d'un état valide à un autre.
 *         - endpoint bonus : PATCH /api/meetings/:id/status pour changer
 *           uniquement le statut.
 *    3. Validation : reference obligatoire, link URL valide, status ENUM.
 *    4. Swagger doc.
 *
 *  Endpoints attendus :
 *    GET    /api/meetings
 *    GET    /api/meetings/:id
 *    POST   /api/meetings
 *    PUT    /api/meetings/:id
 *    DELETE /api/meetings/:id
 * ============================================================================
 */

'use strict';

// TODO : const meetingService = require('../services/meetingService');

async function getAllMeetings(req, res, next) {
  next(new Error('meetingController.getAllMeetings : à implémenter'));
}

async function getMeetingById(req, res, next) {
  next(new Error('meetingController.getMeetingById : à implémenter'));
}

async function createMeeting(req, res, next) {
  next(new Error('meetingController.createMeeting : à implémenter'));
}

async function updateMeeting(req, res, next) {
  next(new Error('meetingController.updateMeeting : à implémenter'));
}

async function deleteMeeting(req, res, next) {
  next(new Error('meetingController.deleteMeeting : à implémenter'));
}

module.exports = {
  getAllMeetings,
  getMeetingById,
  createMeeting,
  updateMeeting,
  deleteMeeting
};
