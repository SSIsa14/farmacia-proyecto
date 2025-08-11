pipeline {
  agent any

  environment {
    SONAR_HOST_URL = 'http://sonarqube:9000'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        script {
          if (fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml')) {
            dir('pharmacy') {
              sh 'mvn clean install'
            }
          } else if (fileExists('frontend/package.json')) {
            dir('frontend') {
              sh 'npm install'
            }
          } else {
            error "No se encontró ni backend ni frontend"
          }
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def isFrontend = fileExists('frontend/package.json')
          def isBackend = fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml')

          // Mapa tokens y keys Sonar por rama y tipo
          def sonarConfig = [
            'main': [
              'frontend': ['projectKey': 'FP-FrontendMain', 'tokenId': 'sonarqube-token-frontend-main'],
              'backend':  ['projectKey': 'FP-BackendMain',  'tokenId': 'sonarqube-token-backend-main']
            ],
            'dev': [
              'frontend': ['projectKey': 'FP-FrontendDev', 'tokenId': 'sonarqube-token-frontend-dev'],
              'backend':  ['projectKey': 'FP-BackendDev',  'tokenId': 'sonarqube-token-backend-dev']
            ],
            'qa': [
              'frontend': ['projectKey': 'FP-FrontendQA', 'tokenId': 'sonarqube-token-frontend-qa'],
              'backend':  ['projectKey': 'FP-BackendQA',  'tokenId': 'sonarqube-token-backend-qa']
            ]
          ]

          def config = sonarConfig[branch]
          if (config == null) {
            error "No hay configuración de SonarQube para la rama '${branch}'"
          }

          if (isFrontend) {
            def key = config['frontend']['projectKey']
            def tokenId = config['frontend']['tokenId']

            withCredentials([string(credentialsId: tokenId, variable: 'SONAR_TOKEN')]) {
              dir('frontend') {
                sh """
                  npx sonar-scanner \
                    -Dsonar.projectKey=${key} \
                    -Dsonar.sources=. \
                    -Dsonar.host.url=${SONAR_HOST_URL} \
                    -Dsonar.login=${SONAR_TOKEN} \
                    -Dsonar.language=ts \
                    -Dsonar.sourceEncoding=UTF-8
                """
              }
            }

          } else if (isBackend) {
            def key = config['backend']['projectKey']
            def tokenId = config['backend']['tokenId']

            withCredentials([string(credentialsId: tokenId, variable: 'SONAR_TOKEN')]) {
              dir('pharmacy') {
                sh """
                  mvn sonar:sonar \
                    -Dsonar.projectKey=${key} \
                    -Dsonar.host.url=${SONAR_HOST_URL} \
                    -Dsonar.login=${SONAR_TOKEN}
                """
              }
            }

          } else {
            error "No se detectó frontend ni backend"
          }
        }
      }
    }
  }
}
