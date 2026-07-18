/**
 * ============================================================================
 *  middlewares/validate.js — Middleware wrapper autour d'express-validator
 * ============================================================================
 *
 *  express-validator fournit :
 *    1. Des chaînes de validation à attacher aux routes (voir *Validator.js)
 *    2. Une fonction validationResult(req) qui collecte les erreurs
 *
 *  Ce middleware centralise l'étape 2 :
 *    - Si des erreurs sont présentes → HttpError 400 (formaté en JSON standard).
 *    - Sinon → passage au contrôleur.
 * ============================================================================
 */

'use strict';

const { validationResult } = require('express-validator');
const { HttpError }        = require('./errorHandler');

function validate(req, res, next) {
  const errors = validationResult(req);
  if (errors.isEmpty()) return next();

  const details = errors.array({ onlyFirstError: true }).map(err => ({
    field:   err.path || err.param,
    message: err.msg
  }));

  next(new HttpError(400, 'Erreur de validation', details));
}

module.exports = validate;
