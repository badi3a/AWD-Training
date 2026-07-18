/**
 * ============================================================================
 *  server.js — Point d'entrée : démarrage du serveur HTTP
 * ============================================================================
 *
 *  Fonctions :
 *   1. Vérifier la connexion à la base de données.
 *   2. Démarrer le serveur Express sur le port configuré.
 *   3. Gérer les arrêts propres (signaux SIGINT / SIGTERM).
 * ============================================================================
 */

'use strict';

require('dotenv').config();

const app = require('./app');
const { pool, testConnection } = require('./config/database');

const PORT = Number(process.env.PORT) || 3000;

function printBanner() {
  const line = '─'.repeat(74);
  console.log('');
  console.log(line);
  console.log('  🚀  JobBoard REST API — ESPRIT · Module AWD · Chapitre 2');
  console.log(line);
  console.log(`  ▸ Environnement    : ${process.env.NODE_ENV || 'development'}`);
  console.log(`  ▸ URL de base      : http://localhost:${PORT}`);
  console.log(`  ▸ Documentation    : http://localhost:${PORT}/api-docs`);
  console.log(`  ▸ Santé            : http://localhost:${PORT}/health`);
  console.log(line);
  console.log('  Ressources implémentées :');
  console.log(`  ▸ Candidates        : http://localhost:${PORT}/api/candidates`);
  console.log(`  ▸ Addresses         : http://localhost:${PORT}/api/addresses`);
  console.log(line);
  console.log('  Ressources squelettes (TP étudiants) :');
  console.log(`  ▸ Applications      : http://localhost:${PORT}/api/applications`);
  console.log(`  ▸ Jobs              : http://localhost:${PORT}/api/jobs`);
  console.log(`  ▸ Notifications     : http://localhost:${PORT}/api/notifications`);
  console.log(`  ▸ Meetings          : http://localhost:${PORT}/api/meetings`);
  console.log(line);
  console.log('');
}

async function start() {
  await testConnection();

  const server = app.listen(PORT, () => printBanner());

  const gracefulShutdown = async (signal) => {
    console.log(`\n📴 Signal ${signal} reçu. Arrêt propre en cours...`);
    server.close(async () => {
      console.log('  ✓ Serveur HTTP arrêté.');
      await pool.end();
      console.log('  ✓ Pool MySQL fermé.');
      console.log('👋 Au revoir.');
      process.exit(0);
    });
    setTimeout(() => {
      console.error('❌ Arrêt forcé (timeout).');
      process.exit(1);
    }, 10000);
  };

  process.on('SIGINT',  () => gracefulShutdown('SIGINT'));
  process.on('SIGTERM', () => gracefulShutdown('SIGTERM'));

  process.on('unhandledRejection', (reason) => {
    console.error('❌ Rejet non capturé :', reason);
    process.exit(1);
  });
  process.on('uncaughtException', (err) => {
    console.error('❌ Exception non capturée :', err);
    process.exit(1);
  });
}

start().catch((err) => {
  console.error('❌ Impossible de démarrer le serveur :', err);
  process.exit(1);
});
