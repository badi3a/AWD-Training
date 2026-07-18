/**
 * ============================================================================
 *  scripts/init-db.js — Initialise la base depuis Node.js
 * ============================================================================
 *
 *  Alternative pratique à `mysql -u root -p < sql/create_database.sql`.
 *  Lance :
 *      npm run db:init
 *
 *  ⚠️  Ce script SUPPRIME la base `jobboard` si elle existe déjà.
 *  Ne pas l'exécuter en production !
 * ============================================================================
 */

'use strict';

require('dotenv').config();
const fs    = require('fs');
const path  = require('path');
const mysql = require('mysql2/promise');

async function initDb() {
  const sqlPath = path.join(__dirname, '..', 'sql', 'create_database.sql');

  console.log('📂 Lecture du script SQL...');
  if (!fs.existsSync(sqlPath)) {
    console.error(`❌ Fichier introuvable : ${sqlPath}`);
    process.exit(1);
  }
  const sqlContent = fs.readFileSync(sqlPath, 'utf8');

  console.log('🔌 Connexion à MySQL...');
  const connection = await mysql.createConnection({
    host:               process.env.DB_HOST     || 'localhost',
    port:               Number(process.env.DB_PORT) || 3306,
    user:               process.env.DB_USER     || 'root',
    password:           process.env.DB_PASSWORD || '',
    multipleStatements: true
  });

  console.log('⚙️  Exécution du script SQL...');
  try {
    await connection.query(sqlContent);
    console.log('✅ Base de données initialisée avec succès !');
    console.log(`   Base : ${process.env.DB_NAME || 'jobboard'}`);
    console.log('   Vous pouvez maintenant démarrer le serveur : npm run dev');
  } catch (err) {
    console.error('❌ Erreur lors de l\'exécution du script :');
    console.error('   ', err.message);
    process.exit(1);
  } finally {
    await connection.end();
  }
}

initDb();
