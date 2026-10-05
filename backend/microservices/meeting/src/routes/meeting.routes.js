const { Router } = require('express');
const meetingController = require('../controllers/meeting.controller');

const router = Router();

// GET /api/meetings/hello
router.get('/hello', meetingController.hello);

// TODO (students): add the Meeting CRUD routes here, for example:
// router.get('/', meetingController.findAll);
// router.get('/:id', meetingController.findById);
// router.post('/', meetingController.create);
// router.put('/:id', meetingController.update);
// router.delete('/:id', meetingController.remove);
// Then document each route in src/config/swagger.js (see TODO.md).

module.exports = router;
