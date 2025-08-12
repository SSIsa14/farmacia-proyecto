def pipelineFailed = false
def failedStages = []

pipeline {
  agent any

  environment {
    SONAR_HOST_URL = 'http://sonarqube:9000'
  }

  stages {
    stage('Checkout') {
      steps {
        echo "==== [Checkout] Iniciando ===="
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
          checkout scm
        }
        echo "==== [Checkout] Finalizado ===="
      }
      post {
        failure {
          script {
            pipelineFailed = true
            failedStages << "Checkout"
            echo "DEBUG: pipelineFailed=${pipelineFailed}, failedStages=${failedStages}"
          }
        }
      }
    }

    stage('Build Backend') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [Build Backend] Iniciando ===="
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
          dir('pharmacy') {
            sh 'mvn clean install'
          }
        }
        echo "==== [Build Backend] Finalizado ===="
      }
      post {
        failure {
          script {
            pipelineFailed = true
            failedStages << "Build Backend"
            echo "DEBUG: pipelineFailed=${pipelineFailed}, failedStages=${failedStages}"
          }
        }
      }
    }

    stage('Test Backend') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [Test Backend] Iniciando ===="
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
          dir('pharmacy') {
            sh 'mvn test'
          }
        }
        echo "==== [Test Backend] Finalizado ===="
      }
      post {
        failure {
          script {
            pipelineFailed = true
            failedStages << "Test Backend"
            echo "DEBUG: pipelineFailed=${pipelineFailed}, failedStages=${failedStages}"
          }
        }
      }
    }

    stage('SonarQube Backend Analysis') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [SonarQube Backend] Iniciando ===="
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
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
        }
        echo "==== [SonarQube Backend] Finalizado ===="
      }
      post {
        failure {
          script {
            pipelineFailed = true
            failedStages << "SonarQube Backend Analysis"
            echo "DEBUG: pipelineFailed=${pipelineFailed}, failedStages=${failedStages}"
          }
        }
      }
    }

    stage('Build Frontend') {
      when { expression { fileExists('frontend/package.json') } }
      steps {
        echo "==== [Build Frontend] Iniciando ===="
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
          dir('frontend') {
            sh 'npm install'
          }
        }
        echo "==== [Build Frontend] Finalizado ===="
      }
      post {
        failure {
          script {
            pipelineFailed = true
            failedStages << "Build Frontend"
            echo "DEBUG: pipelineFailed=${pipelineFailed}, failedStages=${failedStages}"
          }
        }
      }
    }

    stage('SonarQube Frontend Analysis') {
      when { expression { fileExists('frontend/package.json') } }
      steps {
        echo "==== [SonarQube Frontend] Iniciando ===="
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
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
        }
        echo "==== [SonarQube Frontend] Finalizado ===="
      }
      post {
        failure {
          script {
            pipelineFailed = true
            failedStages << "SonarQube Frontend Analysis"
            echo "DEBUG: pipelineFailed=${pipelineFailed}, failedStages=${failedStages}"
          }
        }
      }
    }

    stage('Enviar correo si hubo fallo') {
      when { expression { pipelineFailed } }
      steps {
        script {
          def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))
          def resumen = failedStages.collect { "- ${it}" }.join("<br>")
          def consoleOutput = currentBuild.getRawBuild().getLog(50).join("\n")

          echo "DEBUG: Enviando correo..."
          echo "DEBUG: failedStages=${failedStages}"
          echo "DEBUG: Últimas 50 líneas del log:\n${consoleOutput}"

          withCredentials([string(credentialsId: 'emails-recipients', variable: 'EMAIL_LIST')]) {
            emailext(
              subject: "TEST",
              body: """
                <html>
                  <body style="font-family: Arial, sans-serif;">
                    <h2 style="color:red;"> Reporte de Fallo - Jenkins Pipeline</h2>
                    <p><b>Fecha:</b> ${fecha} UTC</p>
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
                    <p><b>Rama:</b> ${env.BRANCH_NAME}</p>
                    <hr>
                    <h3>Etapas que fallaron:</h3>
                    <p>${resumen}</p>
                    <hr>
                    <h3>Últimas 50 líneas del log:</h3>
                    <pre>${consoleOutput}</pre>
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
}
