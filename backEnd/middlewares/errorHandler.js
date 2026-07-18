/**
 * ============================================================================
 *  middlewares/errorHandler.js — Gestion centralisée des erreurs
 * ============================================================================
 *
 *  Un middleware d'erreur Express se reconnaît à sa signature à 4 arguments
 *  (err, req, res, next). Il est appelé automatiquement dès qu'un
 *  `next(err)` est déclenché ou qu'une erreur async est capturée.
 *
 *  Ce middleware convertit TOUTE erreur en une réponse JSON standard :
 *      { "status": 500, "message": "…", "errors": [ … ] }
 *
 *  Avantage : le frontend a UNE SEULE façon de traiter les erreurs.
 *  Sécurité : on ne renvoie JAMAIS la stack trace en production.
 * ============================================================================
 */

'use strict';

/**
 * Erreur HTTP typée. Utilisée par la couche service pour signaler
 * une erreur métier avec un code de statut précis.
 *
 * Exemple d'usage :
 *   throw new HttpError(404, 'Candidat non trouvé');
 *   throw new HttpError(409, 'Email déjà utilisé', [{ field:'email', message:'…' }]);
 */
class HttpError extends Error {
  constructor(status, message, details = null) {
    super(message);
    this.status = status;
    this.details = details;
    this.name = 'HttpError';
  }
}

/**
 * Middleware 404. Attrapé quand aucune route ne correspond à l'URL demandée.
 */
function notFoundHandler(req, res, next) {
  next(new HttpError(404, `Route non trouvée : ${req.method} ${req.originalUrl}`));
}

/**
 * Middleware de gestion centralisée des erreurs.
 * À ajouter APRÈS toutes les routes dans app.js.
 */
function errorHandler(err, req, res, next) {  // eslint-disable-line no-unused-vars
  const status = err.status || err.statusCode || 500;
  const isProduction = process.env.NODE_ENV === 'production';

  const message = status === 500 && isProduction
    ? 'Une erreur interne est survenue.'
    : err.message;

  // Journalisation côté serveur (500+)
  if (status >= 500) {
    console.error(`[${new Date().toISOString()}] 💥 ${req.method} ${req.originalUrl}`);
    console.error(err.stack);
  }

  const response = { status, message };
  if (err.details) response.errors = err.details;
  if (!isProduction && status >= 500) response.stack = err.stack;

  res.status(status).json(response);
}

module.exports = { HttpError, notFoundHandler, errorHandler };
