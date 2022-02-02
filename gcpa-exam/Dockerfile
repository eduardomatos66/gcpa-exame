FROM node:lts-alpine
# instala um servidor http simples para servir conteúdo estático
RUN npm install -g http-server
# faz da pasta 'app' o diretório atual de trabalho
WORKDIR /app
# copia os arquivos 'package.json' e 'package-lock.json' (se disponível)
COPY package*.json ./
# instala dependências do projeto
RUN npm install
# copia arquivos e pastas para o diretório atual de trabalho (pasta 'app')
COPY . .
# compila a aplicação de produção com minificação
RUN npm run build
EXPOSE 8080
CMD [ "http-server", "dist" ]

#Agora vamos compilar a imagem Docker da nossa aplicação Vue:
#docker build -t vuejs-gcpae/dockerize-vuejs-app .

#Finalmente, vamos executar a aplicação Vue em um contêiner Docker:
#docker run -d -p 8080:8080 --rm --name dockerize-vuejs-app-1 vuejs-gcpae/dockerize-vuejs-app
