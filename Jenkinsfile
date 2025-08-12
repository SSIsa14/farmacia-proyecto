pipeline {
  agent any

  environment {
    SONAR_HOST_URL = 'http://sonarqube:9000'
  }

  stages {
    stage('Checkout') {
      steps {
        echo "==== [Checkout] Iniciando ===="
        checkout scm
        echo "==== [Checkout] Finalizado ===="
      }
    }

    stage('Build Backend') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [Build Backend] Iniciando ===="
        dir('pharmacy') {
          sh 'mvn clean install'
        }
        echo "==== [Build Backend] Finalizado ===="
      }
    }

    stage('Test Backend') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [Test Backend] Iniciando ===="
        dir('pharmacy') {
          sh 'mvn test'
        }
        echo "==== [Test Backend] Finalizado ===="
      }
    }

    stage('SonarQube Backend Analysis') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [SonarQube Backend] Iniciando ===="
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def sonarConfig = [
            'main': ['backend': ['projectKey': 'FP-BackendMain', 'tokenId': 'sonarqube-token-backend-main']],
            'development': ['backend': ['projectKey': 'FP-BackendDev', 'tokenId': 'sonarqube-token-backend-dev']],
            'qa': ['backend': ['projectKey': 'FP-BackendQA', 'tokenId': 'sonarqube-token-backend-qa']]
          ]

          def config = sonarConfig[branch]
          if (!config) {
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
        echo "==== [SonarQube Backend] Finalizado ===="
      }
    }

    stage('Build Frontend') {
      when { expression { fileExists('frontend/package.json') } }
      steps {
        echo "==== [Build Frontend] Iniciando ===="
        dir('frontend') {
          sh 'npm install'
        }
        echo "==== [Build Frontend] Finalizado ===="
      }
    }

    stage('SonarQube Frontend Analysis') {
      when { expression { fileExists('frontend/package.json') } }
      steps {
        echo "==== [SonarQube Frontend] Iniciando ===="
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def sonarConfig = [
            'main': ['frontend': ['projectKey': 'FP-FrontendMain', 'tokenId': 'sonarqube-token-frontend-main']],
            'development': ['frontend': ['projectKey': 'FP-FrontendDev', 'tokenId': 'sonarqube-token-frontend-dev']],
            'qa': ['frontend': ['projectKey': 'FP-FrontendQA', 'tokenId': 'sonarqube-token-frontend-qa']]
          ]

          def config = sonarConfig[branch]
          if (!config) {
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
        echo "==== [SonarQube Frontend] Finalizado ===="
      }
    }
  }

  post {
    failure {
      script {
        def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))

        withCredentials([string(credentialsId: 'emails-recipients', variable: 'EMAIL_LIST')]) {
          emailext(
            subject: "❌ Reporte Fallo - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
              <html>
                <body style="font-family: Arial, sans-serif;">
                  <h2 style="color:red;">Reporte de Fallo - Jenkins Pipeline</h2>
                  <p><b>Fecha:</b> ${fecha} UTC</p>
                  <p><b>Job:</b> ${env.JOB_NAME}</p>
                  <p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
                  <p><b>Rama:</b> ${env.BRANCH_NAME}</p>
                  <hr>
                  <h3>Revisa el log completo en Jenkins para más detalles.</h3>
                </body>
              </html>
            """,
            mimeType: 'text/html',
            to: EMAIL_LIST
          )
        }
      }
    }
  }
}
