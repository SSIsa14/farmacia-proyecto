# Etapa 1: Build con Node.js
FROM node:18 as build

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod

# Etapa 2: Servir con nginx
FROM nginx:alpine
COPY --from=build app/dist/frontend/browser /usr/share/nginx/html
EXPOSE 80
