/**
 * ============================================================================
 *  models/addressModel.js — Couche d'accès aux données pour Address
 * ============================================================================
 *
 *  Rôle pédagogique :
 *  ------------------
 *  Le "Model" est la seule couche qui parle SQL. Les autres couches
 *  (service, controller) manipulent des objets JavaScript.
 *
 *  Toutes les requêtes SQL sont écrites À LA MAIN avec des paramètres (?)
 *  pour prévenir les injections SQL.
 * ============================================================================
 */

'use strict';

const { pool } = require('../config/database');

const COLUMNS = `id, street, houseNumber, zipCode, created_at, updated_at`;

/**
 * Récupère toutes les adresses (avec pagination optionnelle).
 */
async function findAll({ page = 1, limit = 20 } = {}) {
  const offset = (page - 1) * limit;
  const [rows] = await pool.query(
    `SELECT ${COLUMNS} FROM addresses ORDER BY id ASC LIMIT ? OFFSET ?`,
    [limit, offset]
  );
  const [countRows] = await pool.query(`SELECT COUNT(*) AS total FROM addresses`);
  return { data: rows, total: countRows[0].total };
}

/**
 * Récupère une adresse par son id.
 */
async function findById(id) {
  const [rows] = await pool.query(
    `SELECT ${COLUMNS} FROM addresses WHERE id = ? LIMIT 1`,
    [id]
  );
  return rows[0] || null;
}

/**
 * Crée une nouvelle adresse et retourne l'id généré.
 */
async function create(data) {
  const [result] = await pool.query(
    `INSERT INTO addresses (street, houseNumber, zipCode) VALUES (?, ?, ?)`,
    [data.street, data.houseNumber, data.zipCode]
  );
  return result.insertId;
}

/**
 * Met à jour une adresse existante.
 * @returns {Promise<boolean>} true si une ligne a été modifiée.
 */
async function update(id, data) {
  const [result] = await pool.query(
    `UPDATE addresses SET
       street = ?, houseNumber = ?, zipCode = ?,
       updated_at = CURRENT_TIMESTAMP
     WHERE id = ?`,
    [data.street, data.houseNumber, data.zipCode, id]
  );
  return result.affectedRows > 0;
}

/**
 * Supprime une adresse.
 * @returns {Promise<boolean>} true si une ligne a été supprimée.
 */
async function remove(id) {
  const [result] = await pool.query(`DELETE FROM addresses WHERE id = ?`, [id]);
  return result.affectedRows > 0;
}

module.exports = { findAll, findById, create, update, remove };
