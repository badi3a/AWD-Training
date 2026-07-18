/**
 * ============================================================================
 *  services/candidateService.js — Couche métier pour Candidate
 * ============================================================================
 *
 *  Règles métier implémentées ici :
 *    1. L'email doit être unique parmi les candidats.
 *    2. Si address_id est fourni, l'adresse doit exister (contrainte FK).
 *    3. address_id est unique (relation 1:1) — une adresse déjà utilisée par
 *       un autre candidat ne peut pas être réaffectée.
 * ============================================================================
 */

'use strict';

const candidateModel = require('../models/candidateModel');
const addressModel   = require('../models/addressModel');
const { HttpError }  = require('../middlewares/errorHandler');

async function getAllCandidates(options) {
  const page  = Number(options.page)  || 1;
  const limit = Math.min(Number(options.limit) || 20, 100);
  const { data, total } = await candidateModel.findAll({ page, limit });
  return {
    data,
    pagination: { total, page, limit, totalPages: Math.ceil(total / limit) }
  };
}

async function getCandidateById(id) {
  const candidate = await candidateModel.findById(id);
  if (!candidate) {
    throw new HttpError(404, `Candidate avec l'id ${id} non trouvé`);
  }
  return candidate;
}

async function createCandidate(data) {
  // Règle 1 : email unique
  const duplicate = await candidateModel.findByEmail(data.email);
  if (duplicate) {
    throw new HttpError(409, `L'email "${data.email}" est déjà utilisé par un autre candidat.`);
  }

  // Règles 2 & 3 : contrôles sur address_id
  if (data.address_id) {
    const address = await addressModel.findById(data.address_id);
    if (!address) {
      throw new HttpError(400, `L'adresse ${data.address_id} n'existe pas.`);
    }
    const addressUsed = await candidateModel.findByAddressId(data.address_id);
    if (addressUsed) {
      throw new HttpError(409, `L'adresse ${data.address_id} est déjà associée à un autre candidat.`);
    }
  }

  const id = await candidateModel.create(data);
  return candidateModel.findById(id);
}

async function updateCandidate(id, data) {
  const existing = await candidateModel.findById(id);
  if (!existing) {
    throw new HttpError(404, `Candidate avec l'id ${id} non trouvé`);
  }

  // Email unique (en excluant le candidat lui-même)
  const emailDuplicate = await candidateModel.findByEmail(data.email, id);
  if (emailDuplicate) {
    throw new HttpError(409, `L'email "${data.email}" est déjà utilisé par un autre candidat.`);
  }

  // Vérifications sur address_id
  if (data.address_id) {
    const address = await addressModel.findById(data.address_id);
    if (!address) {
      throw new HttpError(400, `L'adresse ${data.address_id} n'existe pas.`);
    }
    const addressUsed = await candidateModel.findByAddressId(data.address_id, id);
    if (addressUsed) {
      throw new HttpError(409, `L'adresse ${data.address_id} est déjà associée à un autre candidat.`);
    }
  }

  await candidateModel.update(id, data);
  return candidateModel.findById(id);
}

async function deleteCandidate(id) {
  const success = await candidateModel.remove(id);
  if (!success) {
    throw new HttpError(404, `Candidate avec l'id ${id} non trouvé`);
  }
}

module.exports = {
  getAllCandidates,
  getCandidateById,
  createCandidate,
  updateCandidate,
  deleteCandidate
};
