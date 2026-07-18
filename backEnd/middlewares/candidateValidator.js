/**
 * ============================================================================
 *  middlewares/candidateValidator.js — Validation des candidats
 * ============================================================================
 *  Valide firstname, lastname, email conformément à l'énoncé + address_id
 *  optionnel.
 * ============================================================================
 */

'use strict';

const { body, param } = require('express-validator');

const createRules = [
  body('firstname')
    .exists({ checkFalsy: true }).withMessage('Le prénom est obligatoire.')
    .isString().withMessage('Le prénom doit être une chaîne.')
    .trim()
    .isLength({ min: 2, max: 80 }).withMessage('Le prénom doit contenir 2 à 80 caractères.'),

  body('lastname')
    .exists({ checkFalsy: true }).withMessage('Le nom est obligatoire.')
    .isString().withMessage('Le nom doit être une chaîne.')
    .trim()
    .isLength({ min: 2, max: 80 }).withMessage('Le nom doit contenir 2 à 80 caractères.'),

  body('email')
    .exists({ checkFalsy: true }).withMessage("L'email est obligatoire.")
    .isEmail().withMessage("L'email n'est pas au bon format.")
    .normalizeEmail()
    .isLength({ max: 150 }).withMessage("L'email est trop long (max 150 caractères)."),

  body('address_id')
    .optional({ nullable: true, checkFalsy: true })
    .isInt({ min: 1 }).withMessage("address_id doit être un entier positif.")
];

const updateRules = [
  param('id').isInt({ min: 1 }).withMessage("L'identifiant doit être un entier positif."),
  ...createRules
];

const idRule = [
  param('id').isInt({ min: 1 }).withMessage("L'identifiant doit être un entier positif.")
];

module.exports = { createRules, updateRules, idRule };
