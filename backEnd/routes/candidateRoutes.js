/**
 * ============================================================================
 *  routes/candidateRoutes.js — Routes REST pour Candidate
 * ============================================================================
 */

'use strict';

const express    = require('express');
const router     = express.Router();
const controller = require('../controllers/candidateController');
const validator  = require('../middlewares/candidateValidator');
const validate   = require('../middlewares/validate');

// ---------------------------------------------------------------------------
// GET /api/candidates
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/candidates:
 *   get:
 *     tags: [Candidates]
 *     summary: Liste tous les candidats
 *     description: Retourne la liste paginée des candidats avec leur adresse.
 *     parameters:
 *       - $ref: '#/components/parameters/Page'
 *       - $ref: '#/components/parameters/Limit'
 *     responses:
 *       200:
 *         description: Liste paginée des candidats.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/CandidateList'
 *       500:
 *         $ref: '#/components/responses/ServerError'
 */
router.get('/', controller.getAllCandidates);

// ---------------------------------------------------------------------------
// GET /api/candidates/:id
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/candidates/{id}:
 *   get:
 *     tags: [Candidates]
 *     summary: Récupère un candidat par son id
 *     parameters:
 *       - $ref: '#/components/parameters/CandidateId'
 *     responses:
 *       200:
 *         description: Le candidat demandé (avec son adresse).
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Candidate'
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 *       404:
 *         $ref: '#/components/responses/NotFound'
 */
router.get('/:id', validator.idRule, validate, controller.getCandidateById);

// ---------------------------------------------------------------------------
// POST /api/candidates
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/candidates:
 *   post:
 *     tags: [Candidates]
 *     summary: Crée un nouveau candidat
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/CandidateInput'
 *           example:
 *             firstname: "Youssef"
 *             lastname: "Mzoughi"
 *             email: "youssef.mzoughi@example.tn"
 *             address_id: 1
 *     responses:
 *       201:
 *         description: Candidat créé.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Candidate'
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 *       409:
 *         $ref: '#/components/responses/Conflict'
 */
router.post('/', validator.createRules, validate, controller.createCandidate);

// ---------------------------------------------------------------------------
// PUT /api/candidates/:id
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/candidates/{id}:
 *   put:
 *     tags: [Candidates]
 *     summary: Met à jour un candidat existant
 *     parameters:
 *       - $ref: '#/components/parameters/CandidateId'
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/CandidateInput'
 *     responses:
 *       200:
 *         description: Le candidat mis à jour.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Candidate'
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 *       404:
 *         $ref: '#/components/responses/NotFound'
 *       409:
 *         $ref: '#/components/responses/Conflict'
 */
router.put('/:id', validator.updateRules, validate, controller.updateCandidate);

// ---------------------------------------------------------------------------
// DELETE /api/candidates/:id
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/candidates/{id}:
 *   delete:
 *     tags: [Candidates]
 *     summary: Supprime un candidat
 *     description: Supprime aussi ses candidatures liées (CASCADE).
 *     parameters:
 *       - $ref: '#/components/parameters/CandidateId'
 *     responses:
 *       204:
 *         description: Candidat supprimé (pas de corps).
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 *       404:
 *         $ref: '#/components/responses/NotFound'
 */
router.delete('/:id', validator.idRule, validate, controller.deleteCandidate);

module.exports = router;
