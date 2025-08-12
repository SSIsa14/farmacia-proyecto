def stageStatus = [:]  // etapa -> estado
def failedStage = ""
def allStages = ['Checkout', 'Build Backend', 'Test Backend', 'SonarQube Backend Analysis', 'Build Frontend', 'SonarQube Frontend Analysis']

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
      post {
        success {
          script { stageStatus['Checkout'] = 'SUCCESS' }
        }
        failure {
          script {
            stageStatus['Checkout'] = 'FAILURE'
            failedStage = "Checkout"
          }
        }
        aborted {
          script { stageStatus['Checkout'] = 'NOT_EXECUTED' }
        }
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
      post {
        success {
          script { stageStatus['Build Backend'] = 'SUCCESS' }
        }
        failure {
          script {
            stageStatus['Build Backend'] = 'FAILURE'
            failedStage = "Build Backend"
          }
        }
        aborted {
          script { stageStatus['Build Backend'] = 'NOT_EXECUTED' }
        }
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
      post {
        success {
          script { stageStatus['Test Backend'] = 'SUCCESS' }
        }
        failure {
          script {
            stageStatus['Test Backend'] = 'FAILURE'
            failedStage = "Test Backend"
          }
        }
        aborted {
          script { stageStatus['Test Backend'] = 'NOT_EXECUTED' }
        }
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
      post {
        success {
          script { stageStatus['SonarQube Backend Analysis'] = 'SUCCESS' }
        }
        failure {
          script {
            stageStatus['SonarQube Backend Analysis'] = 'FAILURE'
            failedStage = "SonarQube Backend Analysis"
          }
        }
        aborted {
          script { stageStatus['SonarQube Backend Analysis'] = 'NOT_EXECUTED' }
        }
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
      post {
        success {
          script { stageStatus['Build Frontend'] = 'SUCCESS' }
        }
        failure {
          script {
            stageStatus['Build Frontend'] = 'FAILURE'
            failedStage = "Build Frontend"
          }
        }
        aborted {
          script { stageStatus['Build Frontend'] = 'NOT_EXECUTED' }
        }
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
      post {
        success {
          script { stageStatus['SonarQube Frontend Analysis'] = 'SUCCESS' }
        }
        failure {
          script {
            stageStatus['SonarQube Frontend Analysis'] = 'FAILURE'
            failedStage = "SonarQube Frontend Analysis"
          }
        }
        aborted {
          script { stageStatus['SonarQube Frontend Analysis'] = 'NOT_EXECUTED' }
        }
      }
    }
  }

  post {
    failure {
      script {
        def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))

        // Completar con NOT_EXECUTED las etapas que no se actualizaron
        allStages.each { stageName ->
          if (!stageStatus.containsKey(stageName)) {
            stageStatus[stageName] = 'NOT_EXECUTED'
          }
        }

        // Construir el listado de etapas con sus estados
        def reportStages = stageStatus.collect { stageName, status ->
          def color = status == 'FAILURE' ? 'red' : (status == 'SUCCESS' ? 'green' : 'gray')
          return "<li><b>${stageName}:</b> <span style='color:${color}'>${status}</span></li>"
        }.join("")

        withCredentials([string(credentialsId: 'emails-recipients', variable: 'EMAIL_LIST')]) {
          emailext(
            subject: "Reporte de Fallo - Jenkins Pipeline",
            body: """
              <html>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color:red;"> Reporte de Fallo - Jenkins Pipeline</h2>
                    <p><b>Fecha:</b> ${fecha} UTC</p>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Rama:</b> ${env.BRANCH_NAME}</p>
                    <hr>
                    <p>El pipeline falló en la etapa de <b>${failedStage}</b>.</p>
                    <hr>
                    <h3>Estado de las etapas:</h3>
                    <ul>${reportStages}</ul>
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
