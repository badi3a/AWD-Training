const app = require('./app');
const createEurekaClient = require('./eureka');

const PORT = Number(process.env.PORT || 8083);

const server = app.listen(PORT, () => {
  console.log(`meeting microservice running on http://localhost:${PORT}`);
  console.log(`Swagger UI: http://localhost:${PORT}/swagger-ui`);
});

// ---- Enregistrement dans Eureka ----
const eurekaClient = createEurekaClient(PORT);
eurekaClient.start((error) => {
  console.log(error ? `Eureka registration failed: ${error}` : 'Registered with Eureka as MEETING');
});

// Désenregistrement propre à l'arrêt (DELETE /eureka/apps/MEETING/{id})
function shutdown() {
  eurekaClient.stop(() => server.close(() => process.exit(0)));
}
process.on('SIGINT', shutdown);
process.on('SIGTERM', shutdown);
