def stageStatus = [:]
def failedStage = ""
def allStages = [
    'Notify Build Start','Checkout',
    'Build Backend', 'Test Backend',
    'SonarQube Backend Analysis','Quality Gate Backend',
    'Build Frontend', 'Test Frontend',
    'SonarQube Frontend Analysis','Quality Gate Frontend',
    'PR: Sonar (Create + Analyze FE & BE)',
    'PR: Quality Gate Frontend',
    'PR: Quality Gate Backend',
    'Deploy'
]

pipeline {
  agent any

  environment {
    SONAR_HOST_URL = 'http://sonarqube:9000'
  }

  stages {

    stage('Notify Build Start') {
      steps {
        script {
          def contextName = env.CHANGE_ID ? 'continuous-integration/jenkins/pr-merge' : 'continuous-integration/jenkins/branch'
          githubNotify context: contextName, status: 'PENDING', description: 'Pipeline iniciado'
        }
      }
      post {
        success { script { stageStatus['Notify Build Start'] = 'SUCCESS' } }
        failure { script { stageStatus['Notify Build Start'] = 'FAILURE'; failedStage = "Notify Build Start" } }
        aborted { script { stageStatus['Notify Build Start'] = 'NOT_EXECUTED' } }
      }
    }

    stage('Checkout') {
      steps {
        echo "==== [Checkout] Iniciando ===="
        checkout scm
        echo "==== [Checkout] Finalizado ===="
      }
      post {
        success { script { stageStatus['Checkout'] = 'SUCCESS' } }
        failure { script { stageStatus['Checkout'] = 'FAILURE'; failedStage = "Checkout" } }
        aborted { script { stageStatus['Checkout'] = 'NOT_EXECUTED' } }
      }
    }

    // ================= Frontend =================
    stage('Build Frontend') {
      when { expression { fileExists('frontend/package.json') } }
      steps {
        echo "==== [Build Frontend] Iniciando ===="
        dir('frontend') { sh 'npm install' }
        echo "==== [Build Frontend] Finalizado ===="
      }
      post {
        success { script { stageStatus['Build Frontend'] = 'SUCCESS' } }
        failure { script { stageStatus['Build Frontend'] = 'FAILURE'; failedStage = "Build Frontend" } }
        aborted { script { stageStatus['Build Frontend'] = 'NOT_EXECUTED' } }
      }
    }

    stage('Test Frontend') {
      when { expression { fileExists('frontend/package.json') } }
      steps {
        echo "==== [Test Frontend] Iniciando ===="
        dir('frontend') { sh 'ng test --watch=false --code-coverage' }
        echo "==== [Test Frontend] Finalizado ===="
      }
      post {
        success { script { stageStatus['Test Frontend'] = 'SUCCESS' } }
        failure { script { stageStatus['Test Frontend'] = 'FAILURE'; failedStage = "Test Frontend" } }
        aborted { script { stageStatus['Test Frontend'] = 'NOT_EXECUTED' } }
      }
    }

    stage('SonarQube Frontend Analysis') {
      when { expression { fileExists('frontend/package.json') && !env.CHANGE_ID } }
      steps {
        echo "==== [SonarQube Frontend] Iniciando ===="
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def sonarConfig = [
            'main':        [projectKey: 'FP:Frontend_Prod',         projectName: 'FP:Frontend_Prod',         tokenId: 'sonarqube-frontend-main'],
            'development': [projectKey: 'FP:Frontend_Development',  projectName: 'FP:Frontend_Development',  tokenId: 'sonarqube-frontend-development'],
            'qa':          [projectKey: 'FP:Frontend_Qa',           projectName: 'FP:Frontend_Qa',           tokenId: 'sonarqube-frontend-qa']
          ]
          def config = sonarConfig[branch]
          if (!config) error "No hay configuración de SonarQube para la rama '${branch}'"

          withSonarQubeEnv('SonarQubeServer') {
            withCredentials([string(credentialsId: config.tokenId, variable: 'SONAR_TOKEN')]) {
              dir('frontend') {
                sh '''
                  npx sonar-scanner \
                    -Dsonar.projectKey=''' + "${config.projectKey}" + ''' \
                    -Dsonar.projectName=''' + "${config.projectName}" + ''' \
                    -Dsonar.sources=. \
                    -Dsonar.host.url=${SONAR_HOST_URL} \
                    -Dsonar.login=${SONAR_TOKEN} \
                    -Dsonar.language=ts \
                    -Dsonar.sourceEncoding=UTF-8 \
                    -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info
                '''
              }
            }
          }
        }
        echo "==== [SonarQube Frontend] Finalizado ===="
      }
      post {
        success { script { stageStatus['SonarQube Frontend Analysis'] = 'SUCCESS' } }
        failure { script { stageStatus['SonarQube Frontend Analysis'] = 'FAILURE'; failedStage = "SonarQube Frontend Analysis" } }
        aborted { script { stageStatus['SonarQube Frontend Analysis'] = 'NOT_EXECUTED' } }
      }
    }

    stage('Quality Gate Frontend') {
      when { expression { fileExists('frontend/package.json') && !env.CHANGE_ID } }
      steps {
        echo "==== [Quality Gate Frontend] Iniciando ===="
        timeout(time: 15, unit: 'MINUTES') {
          script {
            def qg = waitForQualityGate()
            if (qg.status != 'OK') {
              failedStage = "Quality Gate Frontend"
              error "Frontend Quality Gate failed: ${qg.status}"
            }
          }
        }
        echo "==== [Quality Gate Frontend] Finalizado ===="
      }
      post {
        success { script { stageStatus['Quality Gate Frontend'] = 'SUCCESS' } }
        failure { script { stageStatus['Quality Gate Frontend'] = 'FAILURE'; failedStage = "Quality Gate Frontend" } }
        aborted { script { stageStatus['Quality Gate Frontend'] = 'NOT_EXECUTED' } }
      }
    }

    // ================= Backend =================
    stage('Build Backend') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [Build Backend] Iniciando ===="
        dir('pharmacy') { sh 'mvn clean install' }
        echo "==== [Build Backend] Finalizado ===="
      }
      post {
        success { script { stageStatus['Build Backend'] = 'SUCCESS' } }
        failure { script { stageStatus['Build Backend'] = 'FAILURE'; failedStage = "Build Backend" } }
        aborted { script { stageStatus['Build Backend'] = 'NOT_EXECUTED' } }
      }
    }

    stage('Test Backend') {
      when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
      steps {
        echo "==== [Test Backend] Iniciando ===="
        dir('pharmacy') { sh 'mvn test' }
        echo "==== [Test Backend] Finalizado ===="
      }
      post {
        success { script { stageStatus['Test Backend'] = 'SUCCESS' } }
        failure { script { stageStatus['Test Backend'] = 'FAILURE'; failedStage = "Test Backend" } }
        aborted { script { stageStatus['Test Backend'] = 'NOT_EXECUTED' } }
      }
    }

    stage('SonarQube Backend Analysis') {
      when { expression { (fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml')) && !env.CHANGE_ID } }
      steps {
        echo "==== [SonarQube Backend] Iniciando ===="
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def sonarConfig = [
            'main':        [projectKey: 'FP:Backend_Prod',        projectName: 'FP:Backend_Prod',        tokenId: 'sonarqube-backend-main'],
            'development': [projectKey: 'FP:Backend_Development', projectName: 'FP:Backend_Development', tokenId: 'sonarqube-backend-development'],
            'qa':          [projectKey: 'FP:Backend_Qa',          projectName: 'FP:Backend_Qa',          tokenId: 'sonarqube-backend-qa']
          ]
          def config = sonarConfig[branch]
          if (!config) error "No hay configuración de SonarQube para la rama '${branch}'"

          withSonarQubeEnv('SonarQubeServer') {
            withCredentials([string(credentialsId: config.tokenId, variable: 'SONAR_TOKEN')]) {
              dir('pharmacy') {
                sh '''
                  mvn clean verify sonar:sonar -B \
                    -Dsonar.projectKey=''' + "${config.projectKey}" + ''' \
                    -Dsonar.projectName=''' + "${config.projectName}" + ''' \
                    -Dsonar.host.url=${SONAR_HOST_URL} \
                    -Dsonar.login=${SONAR_TOKEN}
                '''
              }
            }
          }
        }
        echo "==== [SonarQube Backend] Finalizado ===="
      }
      post {
        success { script { stageStatus['SonarQube Backend Analysis'] = 'SUCCESS' } }
        failure { script { stageStatus['SonarQube Backend Analysis'] = 'FAILURE'; failedStage = "SonarQube Backend Analysis" } }
        aborted { script { stageStatus['SonarQube Backend Analysis'] = 'NOT_EXECUTED' } }
      }
    }

    stage('Quality Gate Backend') {
      when { expression { (fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml')) && !env.CHANGE_ID } }
      steps {
        echo "==== [Quality Gate Backend] Iniciando ===="
        script {
          def branch = env.BRANCH_NAME.toLowerCase()
          def sonarConfig = [
            'main':        [projectKey: 'FP:Backend_Prod',        tokenId: 'sonarqube-backend-main'],
            'development': [projectKey: 'FP:Backend_Development', tokenId: 'sonarqube-backend-development'],
            'qa':          [projectKey: 'FP:Backend_Qa',          tokenId: 'sonarqube-backend-qa']
          ]
          def config = sonarConfig[branch]
          if (!config) error "No hay configuración de SonarQube para la rama '${branch}'"

          def reportFile = 'pharmacy/target/sonar/report-task.txt'
          def taskIdLine = readFile(reportFile).split("\\n").find { it.startsWith("ceTaskId=") }
          if (!taskIdLine) error "No se pudo leer taskId desde ${reportFile}"
          def backendTaskId = taskIdLine.split("=")[1].trim()
          echo "Backend taskId: ${backendTaskId}"

          withCredentials([string(credentialsId: config.tokenId, variable: 'SONAR_TOKEN')]) {
            def status = ""
            timeout(time: 15, unit: 'MINUTES') {
              while (status != "SUCCESS" && status != "FAILED") {
                status = sh(script: '''
                  curl -s -u ${SONAR_TOKEN}: ${SONAR_HOST_URL}/api/ce/task?id=''' + "${backendTaskId}" + ''' | \
                  grep -o '"task":{[^}]*}' | grep -o '"status":"[^"]*"' | cut -d '"' -f4
                ''', returnStdout: true).trim()
                if (status != "SUCCESS" && status != "FAILED") sleep 5
              }
              if (status == "FAILED") error "Backend SonarQube analysis task failed"
            }

            def qgStatus = sh(script: '''
              curl -s -u ${SONAR_TOKEN}: ${SONAR_HOST_URL}/api/qualitygates/project_status?projectKey=''' + "${config.projectKey}" + ''' | \
              grep -o '"projectStatus":{[^}]*}' | grep -o '"status":"[^"]*"' | cut -d '"' -f4 | head -n1
            ''', returnStdout: true).trim()

            echo "Backend Quality Gate status: ${qgStatus}"
            if (qgStatus != "OK") {
              error "Backend Quality Gate failed: ${qgStatus}"
            }
          }
        }
        echo "==== [Quality Gate Backend] Finalizado ===="
      }
      post {
        success { script { stageStatus['Quality Gate Backend'] = 'SUCCESS' } }
        failure { script { stageStatus['Quality Gate Backend'] = 'FAILURE'; failedStage = "Quality Gate Backend" } }
        aborted { script { stageStatus['Quality Gate Backend'] = 'NOT_EXECUTED' } }
      }
    }

    // ================= Manejo de Pull Request =================
    stage('PR: Sonar (Create + Analyze FE & BE)') {
      when { expression { return env.CHANGE_ID } }
      steps {
        echo "==== [PR Sonar] PR #${env.CHANGE_ID} ===="
        script {
          def prId = env.CHANGE_ID
          env.SONAR_FE_PR_KEY  = "FP:Frontend_PR_${prId}"
          env.SONAR_FE_PR_NAME = "FP:Frontend_PR_${prId}"
          env.SONAR_BE_PR_KEY  = "FP:Backend_PR_${prId}"
          env.SONAR_BE_PR_NAME = "FP:Backend_PR_${prId}"

          withSonarQubeEnv('SonarQubeServer') {
            withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {

              // FE create-if-missing
              sh '''
                set -e
                FE_EXISTS=$(curl -s -u ${SONAR_TOKEN}: "${SONAR_HOST_URL}/api/projects/search?projects=${SONAR_FE_PR_KEY}" | grep -o '"key":"'"${SONAR_FE_PR_KEY}"'"' || true)
                if [ -z "$FE_EXISTS" ]; then
                  curl -s -u ${SONAR_TOKEN}: -X POST "${SONAR_HOST_URL}/api/projects/create" \
                      -d project=${SONAR_FE_PR_KEY} -d name=${SONAR_FE_PR_NAME} >/dev/null
                fi
              '''

              // BE create-if-missing
              sh '''
                set -e
                BE_EXISTS=$(curl -s -u ${SONAR_TOKEN}: "${SONAR_HOST_URL}/api/projects/search?projects=${SONAR_BE_PR_KEY}" | grep -o '"key":"'"${SONAR_BE_PR_KEY}"'"' || true)
                if [ -z "$BE_EXISTS" ]; then
                  curl -s -u ${SONAR_TOKEN}: -X POST "${SONAR_HOST_URL}/api/projects/create" \
                      -d project=${SONAR_BE_PR_KEY} -d name=${SONAR_BE_PR_NAME} >/dev/null
                fi
              '''

              // -------- Analyze FE (si existe) + GitHub Status --------
              if (fileExists('frontend/package.json')) {
                def feLink = "${env.SONAR_HOST_URL}/dashboard?id=${env.SONAR_FE_PR_KEY}"
                githubNotify context: 'sonar-frontend/analyze', status: 'PENDING', description: 'Analizando FE con SonarQube…'
                try {
                  dir('frontend') {
                    sh '''
                      npx sonar-scanner \
                        -Dsonar.projectKey=${SONAR_FE_PR_KEY} \
                        -Dsonar.projectName=${SONAR_FE_PR_NAME} \
                        -Dsonar.sources=. \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_TOKEN} \
                        -Dsonar.language=ts \
                        -Dsonar.sourceEncoding=UTF-8 \
                        -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info
                    '''
                  }
                  githubNotify context: 'sonar-frontend/analyze', status: 'SUCCESS', description: 'Análisis FE completado', targetUrl: feLink
                } catch (e) {
                  githubNotify context: 'sonar-frontend/analyze', status: 'FAILURE', description: 'Falló análisis FE', targetUrl: feLink
                  throw e
                }
              }

              // -------- Analyze BE (si existe) + GitHub Status --------
              if (fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml')) {
                def beLink = "${env.SONAR_HOST_URL}/dashboard?id=${env.SONAR_BE_PR_KEY}"
                githubNotify context: 'sonar-backend/analyze', status: 'PENDING', description: 'Analizando BE con SonarQube…'
                try {
                  dir(fileExists('pharmacy/pom.xml') ? 'pharmacy' : 'backend') {
                    sh '''
                      mvn -B -DskipTests=false clean verify sonar:sonar \
                        -Dsonar.projectKey=${SONAR_BE_PR_KEY} \
                        -Dsonar.projectName=${SONAR_BE_PR_NAME} \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_TOKEN}
                    '''
                  }
                  githubNotify context: 'sonar-backend/analyze', status: 'SUCCESS', description: 'Análisis BE completado', targetUrl: beLink
                } catch (e) {
                  githubNotify context: 'sonar-backend/analyze', status: 'FAILURE', description: 'Falló análisis BE', targetUrl: beLink
                  throw e
                }
              }
            }
          }
        }
        echo "==== [PR Sonar] Finalizado ===="
      }
      post {
        success { script { stageStatus['PR: Sonar (Create + Analyze FE & BE)'] = 'SUCCESS' } }
        failure { script { stageStatus['PR: Sonar (Create + Analyze FE & BE)'] = 'FAILURE'; failedStage = "PR: Sonar (Create + Analyze FE & BE)" } }
        aborted { script { stageStatus['PR: Sonar (Create + Analyze FE & BE)'] = 'NOT_EXECUTED' } }
      }
    }


  stage('PR: Quality Gate Frontend') {
    when { expression { return env.CHANGE_ID && fileExists('frontend/package.json') } }
    steps {
      echo "==== [PR QG FE] Iniciando ===="
      script {
        def feLink = "${env.SONAR_HOST_URL}/dashboard?id=${env.SONAR_FE_PR_KEY}"
        githubNotify context: 'sonar-frontend/qgate', status: 'PENDING', description: 'Evaluando Quality Gate FE…'

        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
          timeout(time: 15, unit: 'MINUTES') {
            def qgStatus = ''
            while (!['OK','ERROR'].contains(qgStatus)) {
              sleep 5
              qgStatus = sh(script: '''
                curl -s -u ${SONAR_TOKEN}: ${SONAR_HOST_URL}/api/qualitygates/project_status?projectKey=${SONAR_FE_PR_KEY} | \
                grep -o '"status":"[^"]*"' | cut -d '"' -f4 | head -n1
              ''', returnStdout: true).trim()
            }
            echo "Frontend PR Quality Gate status: ${qgStatus}"

            if (qgStatus == 'OK') {
              githubNotify context: 'sonar-frontend/qgate', status: 'SUCCESS', description: 'Quality Gate FE OK', targetUrl: feLink
            } else {
              githubNotify context: 'sonar-frontend/qgate', status: 'FAILURE', description: "Quality Gate FE: ${qgStatus}", targetUrl: feLink
              error "Frontend PR Quality Gate failed: ${qgStatus}"
            }
          }
        }
      }
      echo "==== [PR QG FE] Finalizado ===="
    }
    post {
      success { script { stageStatus['PR: Quality Gate Frontend'] = 'SUCCESS' } }
      failure { script { stageStatus['PR: Quality Gate Frontend'] = 'FAILURE'; failedStage = "PR: Quality Gate Frontend" } }
      aborted { script { stageStatus['PR: Quality Gate Frontend'] = 'NOT_EXECUTED' } }
    }
  }


  stage('PR: Quality Gate Backend') {
  when { expression { return env.CHANGE_ID && (fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml')) } }
  steps {
    echo "==== [PR QG BE] Iniciando ===="
    script {
      def beLink = "${env.SONAR_HOST_URL}/dashboard?id=${env.SONAR_BE_PR_KEY}"
      githubNotify context: 'sonar-backend/qgate', status: 'PENDING', description: 'Evaluando Quality Gate BE…'

      withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
        timeout(time: 15, unit: 'MINUTES') {
          def qgStatus = ''
          while (!['OK','ERROR'].contains(qgStatus)) {
            sleep 5
            qgStatus = sh(script: '''
              curl -s -u ${SONAR_TOKEN}: ${SONAR_HOST_URL}/api/qualitygates/project_status?projectKey=${SONAR_BE_PR_KEY} | \
              grep -o '"status":"[^"]*"' | cut -d '"' -f4 | head -n1
            ''', returnStdout: true).trim()
          }
          echo "Backend PR Quality Gate status: ${qgStatus}"

          if (qgStatus == 'OK') {
            githubNotify context: 'sonar-backend/qgate', status: 'SUCCESS', description: 'Quality Gate BE OK', targetUrl: beLink
          } else {
            githubNotify context: 'sonar-backend/qgate', status: 'FAILURE', description: "Quality Gate BE: ${qgStatus}", targetUrl: beLink
            error "Backend PR Quality Gate failed: ${qgStatus}"
          }
        }
      }
    }
    echo "==== [PR QG BE] Finalizado ===="
  }
  post {
    success { script { stageStatus['PR: Quality Gate Backend'] = 'SUCCESS' } }
    failure { script { stageStatus['PR: Quality Gate Backend'] = 'FAILURE'; failedStage = "PR: Quality Gate Backend" } }
    aborted { script { stageStatus['PR: Quality Gate Backend'] = 'NOT_EXECUTED' } }
  }
}


    // ================= Deploy =================
    stage('Deploy') {
      when { expression { return !env.CHANGE_ID } }
      steps {
        script {
          def branch = env.BRANCH_NAME?.toLowerCase()
          def profile = ''
          def project = ''

          if (branch == 'main')      { profile = 'prod'; project = 'pharmacy-prod' }
          else if (branch == 'qa')   { profile = 'qa';   project = 'pharmacy-qa' }
          else if (branch == 'development') { profile = 'dev';  project = 'pharmacy-dev' }
          else { error "No hay configuración de despliegue para la rama '${branch}'" }

          sh '''
            set -e
            FILE="docker-compose.comp.yml"
            PROFILE=''' + "${profile}" + '''
            PROJECT=''' + "${project}" + '''
            DB_CONTAINER="oracle-xe-pharmacy"
            NETWORK="pharmacy-network"

            echo "=== Deploy con perfil: $PROFILE | proyecto: $PROJECT ==="

            if docker compose version >/dev/null 2>&1; then
              COMPOSE="docker compose"
            elif docker-compose version >/dev/null 2>&1; then
              COMPOSE="docker-compose"
            else
              echo "ERROR: ni 'docker compose' ni 'docker-compose' disponibles"
              exit 1
            fi

            docker version || true
            $COMPOSE version || true

            if ! docker ps -a --format '{{.Names}}' | grep -qx "$DB_CONTAINER"; then
              echo "DB no existe, levantando perfil db..."
              $COMPOSE -f "$FILE" --profile db up -d
            else
              RUNNING=$(docker inspect -f '{{.State.Running}}' "$DB_CONTAINER" 2>/dev/null || echo false)
              if [ "$RUNNING" != "true" ]; then
                echo "DB existe pero está detenida, iniciando..."
                docker start "$DB_CONTAINER"
              else
                echo "DB ya está arriba, seguimos."
              fi
            fi

            echo "Bajando perfil $PROFILE del proyecto $PROJECT..."
            $COMPOSE -p "$PROJECT" -f "$FILE" --profile "$PROFILE" down || true

            echo "Subiendo perfil $PROFILE del proyecto $PROJECT..."
            $COMPOSE -p "$PROJECT" -f "$FILE" --profile "$PROFILE" up -d --build

            echo "Estado de servicios del proyecto $PROJECT / perfil $PROFILE:"
            $COMPOSE -p "$PROJECT" -f "$FILE" --profile "$PROFILE" ps
          '''
        }
      }
      post {
        success { script { stageStatus['Deploy'] = 'SUCCESS' } }
        failure { script { stageStatus['Deploy'] = 'FAILURE'; failedStage = "Deploy" } }
        aborted { script { stageStatus['Deploy'] = 'NOT_EXECUTED' } }
      }
    }

  } // stages

  // ================= Notificaciones =================
  post {
    failure {
      script {
        allStages.each { s -> if (!stageStatus.containsKey(s)) stageStatus[s] = 'NOT_EXECUTED' }
        def reportStages = stageStatus.collect { s, st ->
          def color = st == 'FAILURE' ? 'red' : (st == 'SUCCESS' ? 'green' : 'gray')
          "<li><b>${s}:</b> <span style='color:${color}'>${st}</span></li>"
        }.join("")
        def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))
        def contextName = env.CHANGE_ID ? 'continuous-integration/jenkins/pr-merge' : 'continuous-integration/jenkins/branch'
        githubNotify context: contextName, status: 'FAILURE', description: "Falló en ${failedStage}", targetUrl: env.BUILD_URL

        mail(
          to: 'abrilsofia159@gmail.com,jflores@unis.edu.gt',
          subject: "TEST FALLIDO",
          mimeType: 'text/html',
          body: """
<html><body>
<h2 style="color:red;text-align:center;">Reporte de Fallo - Jenkins Pipeline</h2>
<p><b>Fecha:</b> ${fecha} UTC</p>
<p><b>Job:</b> ${env.JOB_NAME}</p>
<p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
<p><b>Rama:</b> ${env.BRANCH_NAME}</p>
<h3>Estado de las etapas:</h3>
<ul style="list-style:none;padding-left:0;">${reportStages}</ul>
</body></html>
"""
        )
      }
    }
    success {
      script {
        allStages.each { s -> if (!stageStatus.containsKey(s)) stageStatus[s] = 'NOT_EXECUTED' }
        def reportStages = stageStatus.collect { s, st ->
          def color = st == 'SUCCESS' ? 'green' : 'gray'
          "<li><b>${s}:</b> <span style='color:${color}'>${st}</span></li>"
        }.join("")
        def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))
        def contextName = env.CHANGE_ID ? 'continuous-integration/jenkins/pr-merge' : 'continuous-integration/jenkins/branch'
        githubNotify context: contextName, status: 'SUCCESS', description: "Pipeline OK", targetUrl: env.BUILD_URL

        mail(
          to: 'abrilsofia159@gmail.com,jflores@unis.edu.gt',
          subject: "TEST EXITOSO",
          mimeType: 'text/html',
          body: """
<html><body>
<h2 style="color:green;text-align:center;">Pipeline Ejecutado Correctamente</h2>
<p><b>Fecha:</b> ${fecha} UTC</p>
<p><b>Job:</b> ${env.JOB_NAME}</p>
<p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
<p><b>Rama:</b> ${env.BRANCH_NAME}</p>
<h3>Estado de las etapas:</h3>
<ul style="list-style:none;padding-left:0;">${reportStages}</ul>
</body></html>
"""
        )
      }
    }
  }
}
