/**
 * ============================================================================
 *  services/addressService.js — Couche métier pour Address
 * ============================================================================
 */

'use strict';

const addressModel  = require('../models/addressModel');
const { HttpError } = require('../middlewares/errorHandler');

async function getAllAddresses(options) {
  const page  = Number(options.page)  || 1;
  const limit = Math.min(Number(options.limit) || 20, 100);
  const { data, total } = await addressModel.findAll({ page, limit });
  return {
    data,
    pagination: { total, page, limit, totalPages: Math.ceil(total / limit) }
  };
}

async function getAddressById(id) {
  const address = await addressModel.findById(id);
  if (!address) {
    throw new HttpError(404, `Address avec l'id ${id} non trouvée`);
  }
  return address;
}

async function createAddress(data) {
  const id = await addressModel.create(data);
  return addressModel.findById(id);
}

async function updateAddress(id, data) {
  const existing = await addressModel.findById(id);
  if (!existing) {
    throw new HttpError(404, `Address avec l'id ${id} non trouvée`);
  }
  await addressModel.update(id, data);
  return addressModel.findById(id);
}

async function deleteAddress(id) {
  const success = await addressModel.remove(id);
  if (!success) {
    throw new HttpError(404, `Address avec l'id ${id} non trouvée`);
  }
}

module.exports = {
  getAllAddresses,
  getAddressById,
  createAddress,
  updateAddress,
  deleteAddress
};
