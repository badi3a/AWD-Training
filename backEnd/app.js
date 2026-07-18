/**
 * ============================================================================
 *  app.js — Configuration de l'application Express
 * ============================================================================
 *
 *  On sépare :
 *    - app.js    : configure l'application (routes, middlewares, Swagger).
 *    - server.js : lance effectivement l'écoute sur un port TCP.
 *
 *  Cette séparation permet :
 *    - de tester l'app sans démarrer un serveur (Supertest, Jest).
 *    - de déployer l'app dans différents contextes (Docker, cluster).
 * ============================================================================
 */

'use strict';

require('dotenv').config();

const express       = require('express');
const cors          = require('cors');
const helmet        = require('helmet');
const morgan        = require('morgan');
const swaggerUi     = require('swagger-ui-express');

const swaggerSpec   = require('./swagger/swagger');
const apiRouter     = require('./routes');
const systemRouter  = require('./routes/system');
const requestLogger = require('./middlewares/requestLogger');
const { notFoundHandler, errorHandler } = require('./middlewares/errorHandler');

const app = express();

// ============================================================================
// MIDDLEWARES DE SÉCURITÉ + INFRASTRUCTURE
// ============================================================================

// -- Helmet : ajoute une douzaine d'en-têtes HTTP de sécurité par défaut.
//    contentSecurityPolicy désactivé car Swagger UI utilise des inline styles.
app.use(helmet({
  contentSecurityPolicy: false,
  crossOriginEmbedderPolicy: false
}));

// -- CORS : origines autorisées lues depuis .env
const corsOrigins = (process.env.CORS_ORIGIN || '*')
  .split(',').map(o => o.trim()).filter(Boolean);

app.use(cors({
  origin: corsOrigins.length === 1 && corsOrigins[0] === '*' ? '*' : corsOrigins,
  methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE'],
  credentials: false
}));

// -- Morgan : logger HTTP standard
if (process.env.NODE_ENV !== 'test') {
  app.use(morgan(process.env.LOG_FORMAT || 'dev'));
}

// -- Logger custom (dev)
app.use(requestLogger);

// -- Parsing du body JSON (limite 1MB anti-DoS)
app.use(express.json({ limit: '1mb' }));

// ============================================================================
// ROUTES
// ============================================================================

// Route racine → message d'accueil JSON
app.get('/', (req, res) => {
  res.status(200).json({
    name:          'JobBoard REST API',
    version:       '1.0.0',
    documentation: '/api-docs',
    health:        '/health',
    resources: {
      candidates:    '/api/candidates    (CRUD complet)',
      addresses:     '/api/addresses     (CRUD complet)',
      applications:  '/api/applications  (à implémenter - TP)',
      jobs:          '/api/jobs          (à implémenter - TP)',
      notifications: '/api/notifications (à implémenter - TP)',
      meetings:      '/api/meetings      (à implémenter - TP)'
    }
  });
});

app.use('/', systemRouter);
app.use('/api', apiRouter);

// ============================================================================
// SWAGGER DOCUMENTATION
// ============================================================================

// Spec brute au format JSON (utile pour Postman, autres outils)
app.get('/api-docs.json', (req, res) => {
  res.setHeader('Content-Type', 'application/json');
  res.send(swaggerSpec);
});

// Swagger UI sur /api-docs
app.use(
  '/api-docs',
  swaggerUi.serve,
  swaggerUi.setup(swaggerSpec, {
    explorer: true,
    customSiteTitle: 'JobBoard API — Documentation',
    swaggerOptions: {
      persistAuthorization: true,
      docExpansion: 'list',
      filter: true,
      tryItOutEnabled: true
    }
  })
);

// ============================================================================
// GESTION DES ERREURS (à ajouter EN DERNIER)
// ============================================================================
app.use(notFoundHandler);   // 404 pour toute route inconnue
app.use(errorHandler);      // Handler centralisé

module.exports = app;
