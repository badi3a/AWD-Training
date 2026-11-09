/**
 * Meeting controller.
 *
 * Only `hello` is implemented. The meeting logic is a TODO for students (see TODO.md).
 */

const hello = (req, res) => {
  res.status(200).json({ message: "hello I'm microservice meeting" });
};

// TODO (students): implement the Meeting logic.
// No database: keep meetings in memory, for example:
//
// let meetings = [];
// let nextId = 1;
//
// const findAll = (req, res) => { ... };
// const findById = (req, res) => { ... };   // 404 if not found
// const create = (req, res) => { ... };     // 400 if invalid, 201 + created meeting
// const update = (req, res) => { ... };     // 404 / 400 / 200
// const remove = (req, res) => { ... };     // 404 / 204

module.exports = {
  hello,
  // TODO (students): export findAll, findById, create, update, remove
};
