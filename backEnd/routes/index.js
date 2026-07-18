/**
 * ============================================================================
 *  routes/index.js — Point d'entrée qui monte toutes les routes /api/*
 * ============================================================================
 *
 *  Cette version monte :
 *    - /api/candidates      → CRUD complet (fourni)
 *    - /api/addresses       → CRUD complet (fourni)
 *    - /api/applications    → 501 Not Implemented (TP étudiants)
 *    - /api/jobs            → 501 Not Implemented (TP étudiants)
 *    - /api/notifications   → 501 Not Implemented (TP étudiants)
 *    - /api/meetings        → 501 Not Implemented (TP étudiants)
 * ============================================================================
 */

'use strict';

const express = require('express');
const router  = express.Router();

// -- Modules implémentés --
const candidateRoutes = require('./candidateRoutes');
const addressRoutes   = require('./addressRoutes');

// -- Modules squelettes (à compléter par les étudiants) --
const applicationRoutes  = require('./applicationRoutes');
const jobRoutes          = require('./jobRoutes');
const notificationRoutes = require('./notificationRoutes');
const meetingRoutes      = require('./meetingRoutes');

router.use('/candidates',    candidateRoutes);
router.use('/addresses',     addressRoutes);
router.use('/applications',  applicationRoutes);
router.use('/jobs',          jobRoutes);
router.use('/notifications', notificationRoutes);
router.use('/meetings',      meetingRoutes);

module.exports = router;
