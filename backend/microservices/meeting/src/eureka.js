const { Eureka } = require('eureka-js-client');

const EUREKA_HOST = process.env.EUREKA_HOST || 'localhost';
const EUREKA_PORT = Number(process.env.EUREKA_PORT || 8761);
const HOST = process.env.HOST_NAME || 'localhost';

function createEurekaClient(port) {
  return new Eureka({
    instance: {
      app: 'MEETING',
      // id unique : permet de lancer plusieurs instances sur des ports différents
      instanceId: `meeting:${port}`,
      hostName: HOST,
      ipAddr: '127.0.0.1',
      port: { $: port, '@enabled': true },
      vipAddress: 'meeting',
      statusPageUrl: `http://${HOST}:${port}/swagger-ui`,
      healthCheckUrl: `http://${HOST}:${port}/health`,
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: EUREKA_HOST,
      port: EUREKA_PORT,
      servicePath: '/eureka/apps/',
      maxRetries: 10,
      requestRetryDelay: 2000,
    },
  });
}

module.exports = createEurekaClient;
