/**
 * ============================================================================
 *  swagger/swagger.js — Configuration OpenAPI 3 + Swagger UI
 * ============================================================================
 *
 *  Ce fichier :
 *   1. Déclare le document OpenAPI de base (métadonnées, schémas, composants
 *      réutilisables).
 *   2. Utilise swagger-jsdoc pour scanner les blocs @swagger dans les routes.
 *
 *  Résultat : une spec OpenAPI 3 complète servie par Swagger UI sur /api-docs.
 * ============================================================================
 */

'use strict';

const swaggerJsdoc = require('swagger-jsdoc');
const path         = require('path');

const openApiDefinition = {
  openapi: '3.0.3',

  info: {
    title:       'JobBoard REST API',
    version:     '1.0.0',
    description:
      'API REST pédagogique pour la plateforme JobBoard.\n\n' +
      "Ce backend accompagne le **Chapitre 2** du module *Applications Web Distribuées* " +
      "(ESPRIT — 4ème année Ingénieur). Il expose actuellement le CRUD complet des " +
      "ressources **Candidate** et **Address**. Les ressources **Application**, **Job**, " +
      "**Notification** et **Meeting** sont modélisées dans la base et fournies avec " +
      "un squelette de code — leur implémentation constitue le TP de l'atelier.",
    contact: {
      name:  'Équipe pédagogique AWD — ESPRIT',
      email: 'awd@esprit.tn'
    },
    license: { name: 'MIT' }
  },

  servers: [
    {
      url:         `http://localhost:${process.env.PORT || 3000}`,
      description: 'Serveur local de développement'
    }
  ],

  tags: [
    { name: 'Candidates', description: 'Gestion des candidats (CRUD complet).' },
    { name: 'Addresses',  description: 'Gestion des adresses postales (CRUD complet).' },
    { name: 'System',     description: 'Endpoints techniques (santé, information).' }
  ],

  components: {

    schemas: {
      // ==================================================================
      // ADDRESS
      // ==================================================================
      Address: {
        type: 'object',
        required: ['id', 'street', 'houseNumber', 'zipCode'],
        properties: {
          id:          { type: 'integer', example: 1 },
          street:      { type: 'string',  example: 'Avenue Habib Bourguiba' },
          houseNumber: { type: 'string',  example: '25' },
          zipCode:     { type: 'string',  example: '1000' },
          created_at:  { type: 'string', format: 'date-time', example: '2026-01-15T09:12:00Z' },
          updated_at:  { type: 'string', format: 'date-time', example: '2026-01-15T09:12:00Z' }
        }
      },

      AddressInput: {
        type: 'object',
        required: ['street', 'houseNumber', 'zipCode'],
        properties: {
          street:      { type: 'string', example: 'Avenue Habib Bourguiba' },
          houseNumber: { type: 'string', example: '25' },
          zipCode:     { type: 'string', example: '1000' }
        }
      },

      AddressList: {
        type: 'object',
        properties: {
          data:       { type: 'array', items: { $ref: '#/components/schemas/Address' } },
          pagination: { $ref: '#/components/schemas/Pagination' }
        }
      },

      // ==================================================================
      // CANDIDATE
      // ==================================================================
      Candidate: {
        type: 'object',
        required: ['id', 'firstname', 'lastname', 'email'],
        properties: {
          id:         { type: 'integer', example: 1 },
          firstname:  { type: 'string',  example: 'Youssef' },
          lastname:   { type: 'string',  example: 'Mzoughi' },
          email:      { type: 'string',  format: 'email', example: 'youssef.mzoughi@example.tn' },
          address_id: { type: 'integer', example: 1, nullable: true },
          address:    { $ref: '#/components/schemas/Address' },
          created_at: { type: 'string', format: 'date-time', example: '2026-01-15T09:12:00Z' },
          updated_at: { type: 'string', format: 'date-time', example: '2026-01-15T09:12:00Z' }
        }
      },

      CandidateInput: {
        type: 'object',
        required: ['firstname', 'lastname', 'email'],
        properties: {
          firstname:  { type: 'string', example: 'Youssef' },
          lastname:   { type: 'string', example: 'Mzoughi' },
          email:      { type: 'string', format: 'email', example: 'youssef.mzoughi@example.tn' },
          address_id: { type: 'integer', example: 1, nullable: true, description: "Id de l'adresse. Doit exister et ne pas être déjà utilisée." }
        }
      },

      CandidateList: {
        type: 'object',
        properties: {
          data:       { type: 'array', items: { $ref: '#/components/schemas/Candidate' } },
          pagination: { $ref: '#/components/schemas/Pagination' }
        }
      },

      // ==================================================================
      // Communs
      // ==================================================================
      Pagination: {
        type: 'object',
        properties: {
          total:      { type: 'integer', example: 42 },
          page:       { type: 'integer', example: 1 },
          limit:      { type: 'integer', example: 20 },
          totalPages: { type: 'integer', example: 3 }
        }
      },

      Error: {
        type: 'object',
        required: ['status', 'message'],
        properties: {
          status:  { type: 'integer', example: 400 },
          message: { type: 'string',  example: "Les données envoyées sont invalides." },
          errors:  {
            type: 'array',
            items: {
              type: 'object',
              properties: {
                field:   { type: 'string', example: 'email' },
                message: { type: 'string', example: "L'email n'est pas au bon format." }
              }
            }
          }
        }
      }
    },

    // -------------------------------------------------------------------
    // Réponses réutilisables
    // -------------------------------------------------------------------
    responses: {
      NotFound: {
        description: 'Ressource introuvable.',
        content: {
          'application/json': {
            schema: { $ref: '#/components/schemas/Error' },
            example: { status: 404, message: 'Ressource non trouvée' }
          }
        }
      },
      BadRequest: {
        description: 'Requête invalide (données mal formées).',
        content: {
          'application/json': {
            schema: { $ref: '#/components/schemas/Error' },
            example: {
              status: 400,
              message: 'Erreur de validation',
              errors: [{ field: 'email', message: "L'email n'est pas au bon format." }]
            }
          }
        }
      },
      Conflict: {
        description: 'Conflit métier (par exemple email déjà utilisé).',
        content: {
          'application/json': {
            schema: { $ref: '#/components/schemas/Error' },
            example: { status: 409, message: "L'email est déjà utilisé par un autre candidat." }
          }
        }
      },
      ServerError: {
        description: 'Erreur interne du serveur.',
        content: {
          'application/json': {
            schema: { $ref: '#/components/schemas/Error' },
            example: { status: 500, message: 'Une erreur interne est survenue.' }
          }
        }
      }
    },

    // -------------------------------------------------------------------
    // Paramètres réutilisables
    // -------------------------------------------------------------------
    parameters: {
      CandidateId: {
        name: 'id', in: 'path', required: true,
        description: "Identifiant d'un candidat.",
        schema: { type: 'integer', minimum: 1, example: 1 }
      },
      AddressId: {
        name: 'id', in: 'path', required: true,
        description: "Identifiant d'une adresse.",
        schema: { type: 'integer', minimum: 1, example: 1 }
      },
      Page: {
        name: 'page', in: 'query', required: false,
        description: 'Numéro de page (commence à 1).',
        schema: { type: 'integer', minimum: 1, default: 1 }
      },
      Limit: {
        name: 'limit', in: 'query', required: false,
        description: 'Nombre d\'éléments par page (max 100).',
        schema: { type: 'integer', minimum: 1, maximum: 100, default: 20 }
      }
    }
  }
};


// ---------------------------------------------------------------------------
// Options passées à swagger-jsdoc
// ---------------------------------------------------------------------------
const options = {
  definition: openApiDefinition,
  // ⚠️ Utiliser des chemins RELATIFS au cwd (dossier où l'on lance npm start)
  // et non path.join(__dirname, ...) qui pose problème avec le glob interne.
  apis: [
    './routes/*.js',
    './routes/**/*.js'
  ]
};

// Génération du document OpenAPI final
const swaggerSpec = swaggerJsdoc(options);

// -- DEBUG : à retirer une fois que ça marche --
console.log('🔍 Swagger paths trouvés :', Object.keys(swaggerSpec.paths || {}));

module.exports = swaggerSpec;

