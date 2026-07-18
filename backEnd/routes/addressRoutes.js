/**
 * ============================================================================
 *  routes/addressRoutes.js — Routes REST pour Address
 * ============================================================================
 *  Chaque endpoint est documenté par un bloc JSDoc @swagger utilisé par
 *  swagger-jsdoc pour générer la spec OpenAPI 3.
 * ============================================================================
 */

'use strict';

const express   = require('express');
const router    = express.Router();
const controller = require('../controllers/addressController');
const validator  = require('../middlewares/addressValidator');
const validate   = require('../middlewares/validate');

// ---------------------------------------------------------------------------
// GET /api/addresses
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/addresses:
 *   get:
 *     tags: [Addresses]
 *     summary: Liste toutes les adresses
 *     description: Retourne la liste paginée des adresses.
 *     parameters:
 *       - $ref: '#/components/parameters/Page'
 *       - $ref: '#/components/parameters/Limit'
 *     responses:
 *       200:
 *         description: Liste des adresses avec pagination.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/AddressList'
 *       500:
 *         $ref: '#/components/responses/ServerError'
 */
router.get('/', controller.getAllAddresses);

// ---------------------------------------------------------------------------
// GET /api/addresses/:id
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/addresses/{id}:
 *   get:
 *     tags: [Addresses]
 *     summary: Récupère une adresse par son id
 *     parameters:
 *       - $ref: '#/components/parameters/AddressId'
 *     responses:
 *       200:
 *         description: L'adresse demandée.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Address'
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 *       404:
 *         $ref: '#/components/responses/NotFound'
 */
router.get('/:id', validator.idRule, validate, controller.getAddressById);

// ---------------------------------------------------------------------------
// POST /api/addresses
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/addresses:
 *   post:
 *     tags: [Addresses]
 *     summary: Crée une nouvelle adresse
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/AddressInput'
 *           example:
 *             street: "Avenue Bourguiba"
 *             houseNumber: "42"
 *             zipCode: "1000"
 *     responses:
 *       201:
 *         description: Adresse créée.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Address'
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 */
router.post('/', validator.createRules, validate, controller.createAddress);

// ---------------------------------------------------------------------------
// PUT /api/addresses/:id
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/addresses/{id}:
 *   put:
 *     tags: [Addresses]
 *     summary: Met à jour une adresse existante
 *     parameters:
 *       - $ref: '#/components/parameters/AddressId'
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/AddressInput'
 *     responses:
 *       200:
 *         description: L'adresse mise à jour.
 *         content:
 *           application/json:
 *             schema:
 *               $ref: '#/components/schemas/Address'
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 *       404:
 *         $ref: '#/components/responses/NotFound'
 */
router.put('/:id', validator.updateRules, validate, controller.updateAddress);

// ---------------------------------------------------------------------------
// DELETE /api/addresses/:id
// ---------------------------------------------------------------------------
/**
 * @swagger
 * /api/addresses/{id}:
 *   delete:
 *     tags: [Addresses]
 *     summary: Supprime une adresse
 *     parameters:
 *       - $ref: '#/components/parameters/AddressId'
 *     responses:
 *       204:
 *         description: Adresse supprimée (pas de corps).
 *       400:
 *         $ref: '#/components/responses/BadRequest'
 *       404:
 *         $ref: '#/components/responses/NotFound'
 */
router.delete('/:id', validator.idRule, validate, controller.deleteAddress);

module.exports = router;
