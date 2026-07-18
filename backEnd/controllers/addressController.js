/**
 * ============================================================================
 *  controllers/addressController.js — Couche HTTP pour Address
 * ============================================================================
 *
 *  Le contrôleur est le pont entre HTTP et la logique métier :
 *   - Il extrait les paramètres de req.
 *   - Il appelle la couche service.
 *   - Il renvoie une réponse HTTP (statut + JSON).
 *
 *  Les erreurs sont transmises à next(err) → le middleware errorHandler
 *  se charge de renvoyer un JSON standardisé.
 * ============================================================================
 */

'use strict';

const addressService = require('../services/addressService');

async function getAllAddresses(req, res, next) {
  try {
    const result = await addressService.getAllAddresses({
      page:  req.query.page,
      limit: req.query.limit
    });
    res.status(200).json(result);
  } catch (err) { next(err); }
}

async function getAddressById(req, res, next) {
  try {
    const address = await addressService.getAddressById(Number(req.params.id));
    res.status(200).json(address);
  } catch (err) { next(err); }
}

async function createAddress(req, res, next) {
  try {
    const address = await addressService.createAddress(req.body);
    res.status(201)
       .location(`/api/addresses/${address.id}`)
       .json(address);
  } catch (err) { next(err); }
}

async function updateAddress(req, res, next) {
  try {
    const address = await addressService.updateAddress(Number(req.params.id), req.body);
    res.status(200).json(address);
  } catch (err) { next(err); }
}

async function deleteAddress(req, res, next) {
  try {
    await addressService.deleteAddress(Number(req.params.id));
    res.status(204).send();
  } catch (err) { next(err); }
}

module.exports = {
  getAllAddresses,
  getAddressById,
  createAddress,
  updateAddress,
  deleteAddress
};
