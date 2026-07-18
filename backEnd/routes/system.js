/**
 * ============================================================================
 *  routes/system.js — Endpoints techniques (santé + accueil)
 * ============================================================================
 */

'use strict';

const express = require('express');
const router  = express.Router();
const { pool } = require('../config/database');

/**
 * @swagger
 * /health:
 *   get:
 *     tags: [System]
 *     summary: Vérifie que l'API et la base sont opérationnelles
 *     responses:
 *       200:
 *         description: Tout fonctionne.
 *         content:
 *           application/json:
 *             example:
 *               status: "ok"
 *               uptime: 123.45
 *               database: "up"
 *               timestamp: "2026-02-10T09:30:00Z"
 *       503:
 *         description: Base injoignable.
 */
router.get('/health', async (req, res) => {
  const health = {
    status:    'ok',
    uptime:    process.uptime(),
    database:  'unknown',
    timestamp: new Date().toISOString()
  };
  try {
    await pool.query('SELECT 1');
    health.database = 'up';
    res.status(200).json(health);
  } catch (error) {
    health.status = 'error';
    health.database = 'down';
    res.status(503).json(health);
  }
});

module.exports = router;
