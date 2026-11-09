# =====================================================================
#  meeting – image Docker (Node.js 22, Express)
#  >>> Solution du « travail à faire » de l'atelier <<<
# =====================================================================
FROM node:22-alpine

ENV NODE_ENV=production
WORKDIR /app

# 1. Dépendances d'abord (couche mise en cache) ; npm ci = installation exacte du package-lock.json
COPY package.json package-lock.json ./
RUN npm ci --omit=dev

# 2. Code de l'application
COPY src ./src

EXPOSE 8083
# Ne pas exécuter l'application en root : l'image fournit l'utilisateur "node"
USER node
CMD ["node", "src/server.js"]
