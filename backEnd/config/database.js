/**
 * ============================================================================
 *  config/database.js — Configuration de la connexion à MySQL
 * ============================================================================
 *
 *  Rôle pédagogique :
 *  ------------------
 *  Ce fichier centralise la connexion à la base de données. Il expose un
 *  « pool » de connexions au lieu d'une connexion unique.
 *
 *  Pourquoi un POOL ?
 *  ------------------
 *  Un serveur Web sert des dizaines de requêtes en parallèle. Ouvrir/fermer
 *  une connexion MySQL à chaque requête serait très coûteux. Le pool maintient
 *  un petit ensemble de connexions ouvertes et les réutilise. C'est l'équivalent
 *  d'un « ThreadPool » pour la base de données.
 *
 *  On utilise également l'API en mode PROMISES (`mysql2/promise`) pour
 *  bénéficier de la syntaxe async/await dans le reste du code.
 * ============================================================================
 */

'use strict';

const mysql = require('mysql2/promise');
require('dotenv').config();

// ----------------------------------------------------------------------------
// Création du pool de connexions
// Les paramètres sont lus depuis le fichier .env — jamais de secret en dur.
// ----------------------------------------------------------------------------
const pool = mysql.createPool({
  host:               process.env.DB_HOST || 'localhost',
  port:               Number(process.env.DB_PORT) || 3306,
  database:           process.env.DB_NAME || 'jobboard',
  user:               process.env.DB_USER || 'root',
  password:           process.env.DB_PASSWORD || '',
  waitForConnections: true,
  connectionLimit:    Number(process.env.DB_POOL_LIMIT) || 10,
  queueLimit:         0,
  charset:            'utf8mb4',
  timezone:           '+00:00',
  dateStrings:        false
});

/**
 * Vérifie la connexion à MySQL au démarrage du serveur.
 * Si la base est injoignable, on affiche un message clair puis on quitte.
 */
async function testConnection() {
  try {
    const connection = await pool.getConnection();
    await connection.query('SELECT 1');
    connection.release();
    console.log(`✅ Base de données connectée : ${process.env.DB_NAME}@${process.env.DB_HOST}:${process.env.DB_PORT}`);
  } catch (error) {
    console.error('❌ Impossible de se connecter à la base de données.');
    console.error('   Détails :', error.message);
    console.error('   Vérifiez :');
    console.error('     1. Que MySQL est démarré (localhost:3306).');
    console.error('     2. Que votre fichier .env est bien renseigné.');
    console.error('     3. Que la base a été créée (voir sql/create_database.sql).');
    process.exit(1);
  }
}

module.exports = { pool, testConnection };
