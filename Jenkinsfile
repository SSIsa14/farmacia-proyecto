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

    stage('Build Backend') {
      when {
        expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') }
      }
      steps {
        dir('pharmacy') {
          sh 'mvn clean install'
        }
      }
    }

    stage('Test Backend') {
      when {
        expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') }
      }
      steps {
        dir('pharmacy') {
          sh 'mvn test'
        }
      }
    }

    stage('SonarQube Backend Analysis') {
      when {
        expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') }
      }
      steps {
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def sonarConfig = [
            'main': ['backend': ['projectKey': 'FP-BackendMain', 'tokenId': 'sonarqube-token-backend-main']],
            'development' : ['backend': ['projectKey': 'FP-BackendDev',  'tokenId': 'sonarqube-token-backend-dev']],
            'qa'  : ['backend': ['projectKey': 'FP-BackendQA',   'tokenId': 'sonarqube-token-backend-qa']]
          ]

          def config = sonarConfig[branch]
          if (config == null) {
            error "No hay configuración de SonarQube para la rama '${branch}'"
          }

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
        }
      }
    }

    stage('Build Frontend') {
      when {
        expression { fileExists('frontend/package.json') }
      }
      steps {
        dir('frontend') {
          sh 'npm install'
        }
      }
    }

    stage('SonarQube Frontend Analysis') {
      when {
        expression { fileExists('frontend/package.json') }
      }
      steps {
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def sonarConfig = [
            'main': ['frontend': ['projectKey': 'FP-FrontendMain', 'tokenId': 'sonarqube-token-frontend-main']],
            'development' : ['frontend': ['projectKey': 'FP-FrontendDev',  'tokenId': 'sonarqube-token-frontend-dev']],
            'qa'  : ['frontend': ['projectKey': 'FP-FrontendQA',   'tokenId': 'sonarqube-token-frontend-qa']]
          ]

          def config = sonarConfig[branch]
          if (config == null) {
            error "No hay configuración de SonarQube para la rama '${branch}'"
          }

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
        }
      }
    }
  }

  post {
    failure {
      withCredentials([string(credentialsId: 'emails-recipients', variable: 'EMAIL_LIST')]) {
        emailext(
          subject: "TEST",
          body: """
            <p>Hola!!,</p>
            <p>El pipeline <b>${env.JOB_NAME} #${env.BUILD_NUMBER}</b> ha fallado.</p>
            <p>Rama: <b>${env.BRANCH_NAME}</b></p>
            <p>Revisar detalles en: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
          """,
          mimeType: 'text/html',
          to: "${EMAIL_LIST}"
        )
      }
    }
  }
}
