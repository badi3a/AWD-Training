/**
 * ============================================================================
 *  models/candidateModel.js — Couche d'accès aux données pour Candidate
 * ============================================================================
 *
 *  D'après le diagramme UML :
 *    Candidate 1 ─── 1 Address (via address_id UNIQUE)
 *    Candidate 1 ─── N Application
 *
 *  Ce modèle expose la ressource Candidate ainsi qu'une méthode
 *  utilitaire pour récupérer un candidat AVEC son adresse associée
 *  (jointure SQL LEFT JOIN).
 * ============================================================================
 */

'use strict';

const { pool } = require('../config/database');

const COLUMNS = `c.id, c.firstname, c.lastname, c.email, c.address_id, c.created_at, c.updated_at`;

// Jointure standard avec la table addresses (LEFT JOIN car un candidat
// peut ne pas avoir encore d'adresse renseignée).
const JOIN_ADDRESS = `LEFT JOIN addresses a ON a.id = c.address_id`;

// Sélection combinée (candidat + adresse imbriquée)
const SELECT_WITH_ADDRESS = `
  SELECT ${COLUMNS},
         a.street       AS address_street,
         a.houseNumber  AS address_houseNumber,
         a.zipCode      AS address_zipCode
  FROM candidates c
  ${JOIN_ADDRESS}
`;

/**
 * Reformate le résultat SQL brut en objet JSON avec `address` imbriqué.
 * @param {Object} row
 * @returns {Object}
 */
function formatRow(row) {
  if (!row) return null;
  const address = row.address_id
    ? {
        id:          row.address_id,
        street:      row.address_street,
        houseNumber: row.address_houseNumber,
        zipCode:     row.address_zipCode
      }
    : null;

  return {
    id:         row.id,
    firstname:  row.firstname,
    lastname:   row.lastname,
    email:      row.email,
    address_id: row.address_id,
    address,
    created_at: row.created_at,
    updated_at: row.updated_at
  };
}

/**
 * Liste paginée des candidats, avec leur adresse (jointure).
 */
async function findAll({ page = 1, limit = 20 } = {}) {
  const offset = (page - 1) * limit;
  const [rows] = await pool.query(
    `${SELECT_WITH_ADDRESS} ORDER BY c.id ASC LIMIT ? OFFSET ?`,
    [limit, offset]
  );
  const [countRows] = await pool.query(`SELECT COUNT(*) AS total FROM candidates`);
  return { data: rows.map(formatRow), total: countRows[0].total };
}

/**
 * Récupère un candidat par son id (avec adresse).
 */
async function findById(id) {
  const [rows] = await pool.query(
    `${SELECT_WITH_ADDRESS} WHERE c.id = ? LIMIT 1`,
    [id]
  );
  return formatRow(rows[0]);
}

/**
 * Recherche un candidat par email (utile pour la contrainte d'unicité).
 * @param {string} email
 * @param {number|null} excludeId
 */
async function findByEmail(email, excludeId = null) {
  let query = `SELECT id FROM candidates WHERE email = ?`;
  const params = [email];
  if (excludeId !== null) {
    query += ` AND id <> ?`;
    params.push(excludeId);
  }
  const [rows] = await pool.query(query, params);
  return rows[0] || null;
}

/**
 * Vérifie qu'une address_id existe et n'est pas déjà utilisée
 * par un autre candidat (contrainte 1:1).
 */
async function findByAddressId(addressId, excludeId = null) {
  let query = `SELECT id FROM candidates WHERE address_id = ?`;
  const params = [addressId];
  if (excludeId !== null) {
    query += ` AND id <> ?`;
    params.push(excludeId);
  }
  const [rows] = await pool.query(query, params);
  return rows[0] || null;
}

/**
 * Crée un nouveau candidat.
 */
async function create(data) {
  const [result] = await pool.query(
    `INSERT INTO candidates (firstname, lastname, email, address_id)
     VALUES (?, ?, ?, ?)`,
    [data.firstname, data.lastname, data.email, data.address_id || null]
  );
  return result.insertId;
}

/**
 * Met à jour un candidat existant.
 */
async function update(id, data) {
  const [result] = await pool.query(
    `UPDATE candidates SET
        firstname = ?, lastname = ?, email = ?, address_id = ?,
        updated_at = CURRENT_TIMESTAMP
     WHERE id = ?`,
    [data.firstname, data.lastname, data.email, data.address_id || null, id]
  );
  return result.affectedRows > 0;
}

/**
 * Supprime un candidat (les applications liées sont supprimées en cascade
 * grâce à la contrainte ON DELETE CASCADE de la table applications).
 */
async function remove(id) {
  const [result] = await pool.query(`DELETE FROM candidates WHERE id = ?`, [id]);
  return result.affectedRows > 0;
}

module.exports = {
  findAll,
  findById,
  findByEmail,
  findByAddressId,
  create,
  update,
  remove
};
