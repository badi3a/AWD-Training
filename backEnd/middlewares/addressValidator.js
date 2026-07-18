/**
 * ============================================================================
 *  middlewares/addressValidator.js — Règles de validation des adresses
 * ============================================================================
 */

'use strict';

const { body, param } = require('express-validator');

const createRules = [
  body('street')
    .exists({ checkFalsy: true }).withMessage('La rue est obligatoire.')
    .isString().withMessage('La rue doit être une chaîne.')
    .trim()
    .isLength({ min: 2, max: 150 }).withMessage('La rue doit contenir 2 à 150 caractères.'),

  body('houseNumber')
    .exists({ checkFalsy: true }).withMessage('Le numéro de rue est obligatoire.')
    .isString().withMessage('Le numéro de rue doit être une chaîne.')
    .trim()
    .isLength({ min: 1, max: 20 }).withMessage('Le numéro de rue doit contenir 1 à 20 caractères.'),

  body('zipCode')
    .exists({ checkFalsy: true }).withMessage('Le code postal est obligatoire.')
    .isString().withMessage('Le code postal doit être une chaîne.')
    .trim()
    .isLength({ min: 2, max: 20 }).withMessage('Le code postal doit contenir 2 à 20 caractères.')
];

const updateRules = [
  param('id').isInt({ min: 1 }).withMessage("L'identifiant doit être un entier positif."),
  ...createRules
];

const idRule = [
  param('id').isInt({ min: 1 }).withMessage("L'identifiant doit être un entier positif.")
];

module.exports = { createRules, updateRules, idRule };
