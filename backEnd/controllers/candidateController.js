/**
 * ============================================================================
 *  controllers/candidateController.js — Couche HTTP pour Candidate
 * ============================================================================
 */

'use strict';

const candidateService = require('../services/candidateService');

async function getAllCandidates(req, res, next) {
  try {
    const result = await candidateService.getAllCandidates({
      page:  req.query.page,
      limit: req.query.limit
    });
    res.status(200).json(result);
  } catch (err) { next(err); }
}

async function getCandidateById(req, res, next) {
  try {
    const candidate = await candidateService.getCandidateById(Number(req.params.id));
    res.status(200).json(candidate);
  } catch (err) { next(err); }
}

async function createCandidate(req, res, next) {
  try {
    const candidate = await candidateService.createCandidate(req.body);
    res.status(201)
       .location(`/api/candidates/${candidate.id}`)
       .json(candidate);
  } catch (err) { next(err); }
}

async function updateCandidate(req, res, next) {
  try {
    const candidate = await candidateService.updateCandidate(Number(req.params.id), req.body);
    res.status(200).json(candidate);
  } catch (err) { next(err); }
}

async function deleteCandidate(req, res, next) {
  try {
    await candidateService.deleteCandidate(Number(req.params.id));
    res.status(204).send();
  } catch (err) { next(err); }
}

module.exports = {
  getAllCandidates,
  getCandidateById,
  createCandidate,
  updateCandidate,
  deleteCandidate
};
