// Run with: npm test   (uses Node's built-in test runner, no extra dependency)
const { test, before, after } = require('node:test');
const assert = require('node:assert/strict');
const app = require('../src/app');

let server;
let baseUrl;

before(async () => {
  server = app.listen(0);
  await new Promise((resolve) => server.once('listening', resolve));
  baseUrl = `http://localhost:${server.address().port}`;
});

after(() => server.close());

test('GET /api/meetings/hello returns the greeting', async () => {
  const res = await fetch(`${baseUrl}/api/meetings/hello`);
  assert.equal(res.status, 200);
  assert.deepEqual(await res.json(), { message: "hello I'm microservice meeting" });
});

test('GET /v3/api-docs returns the OpenAPI spec', async () => {
  const res = await fetch(`${baseUrl}/v3/api-docs`);
  assert.equal(res.status, 200);
  const spec = await res.json();
  assert.equal(spec.info.title, 'Meeting Microservice API');
  assert.ok(spec.paths['/api/meetings/hello']);
});

test('GET /swagger-ui/ serves the Swagger UI page', async () => {
  const res = await fetch(`${baseUrl}/swagger-ui/`);
  assert.equal(res.status, 200);
  assert.match(await res.text(), /swagger/i);
});

test('unknown route returns 404 JSON', async () => {
  const res = await fetch(`${baseUrl}/api/unknown`);
  assert.equal(res.status, 404);
  assert.equal((await res.json()).status, 404);
});

// TODO (students): add tests for the Meeting CRUD endpoints.
