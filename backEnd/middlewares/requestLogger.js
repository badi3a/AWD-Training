/**
 * ============================================================================
 *  middlewares/requestLogger.js — Journalisation légère additionnelle
 * ============================================================================
 *  Morgan couvre déjà le format standard (ex : "GET /api/candidates 200 42ms").
 *  Ce middleware ajoute en développement un affichage coloré du statut + durée.
 * ============================================================================
 */

'use strict';

function requestLogger(req, res, next) {
  if (process.env.NODE_ENV === 'production') return next();

  const start = Date.now();
  res.on('finish', () => {
    const duration = Date.now() - start;
    const color =
      res.statusCode >= 500 ? '\x1b[31m' :
      res.statusCode >= 400 ? '\x1b[33m' :
      res.statusCode >= 300 ? '\x1b[36m' :
                              '\x1b[32m';
    console.log(`   ↳ ${color}${res.statusCode}\x1b[0m · ${duration}ms`);
  });
  next();
}

module.exports = requestLogger;
