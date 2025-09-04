def stageStatus = [:]
def failedStage = ""
def allStages = [
    'Notify Build Start','Checkout', 
    'Build Backend', 'Test Backend', 
    'SonarQube Backend Analysis', 'SonarQube Backend Quality Gate', 
    'Build Frontend', 'Test Frontend', 
    'SonarQube Frontend Analysis', 'SonarQube Frontend Quality Gate',
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
            when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
            steps {
                echo "==== [SonarQube Backend] Iniciando ===="
                script {
                    def branch = env.BRANCH_NAME.toLowerCase()
                    def sonarConfig = [
                        'main':        ['projectKey': 'FP:Backend_Prod',        'projectName': 'FP:Backend_Prod',        'tokenId': 'sonarqube-backend-main'],
                        'development': ['projectKey': 'FP:Backend_Development','projectName': 'FP:Backend_Development','tokenId': 'sonarqube-backend-development'],
                        'qa':          ['projectKey': 'FP:Backend_Qa',         'projectName': 'FP:Backend_Qa',         'tokenId': 'sonarqube-backend-qa']
                    ]
                    
                    def config = sonarConfig[branch]
                    if (!config) error "No hay configuración de SonarQube para la rama '${branch}'"

                    withCredentials([string(credentialsId: config.tokenId, variable: 'SONAR_TOKEN')]) {
                        dir('pharmacy') {
                            sh """
                                mvn clean verify sonar:sonar \
                                  -Dsonar.projectKey=${config.projectKey} \
                                  -Dsonar.projectName="${config.projectName}" \
                                  -Dsonar.host.url=${SONAR_HOST_URL} \
                                  -Dsonar.login=${SONAR_TOKEN}
                            """
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

        stage('SonarQube Backend Quality Gate') {
            when { expression { fileExists('pharmacy/pom.xml') || fileExists('backend/pom.xml') } }
            steps {
                echo "==== [Quality Gate Backend] Iniciando ===="
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo "==== [Quality Gate Backend] Finalizado ===="
            }
            post {
                success { script { stageStatus['SonarQube Backend Quality Gate'] = 'SUCCESS' } }
                failure { script { stageStatus['SonarQube Backend Quality Gate'] = 'FAILURE'; failedStage = "SonarQube Backend Quality Gate" } }
                aborted { script { stageStatus['SonarQube Backend Quality Gate'] = 'NOT_EXECUTED' } }
            }
        }

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
                dir('frontend') {
                    sh 'npm install'
                    sh 'ng test --watch=false --code-coverage'
                }
                echo "==== [Test Frontend] Finalizado ===="
            }
            post {
                success { script { stageStatus['Test Frontend'] = 'SUCCESS' } }
                failure { script { stageStatus['Test Frontend'] = 'FAILURE'; failedStage = "Test Frontend" } }
                aborted { script { stageStatus['Test Frontend'] = 'NOT_EXECUTED' } }
            }
        }

        stage('SonarQube Frontend Analysis') {
            when { expression { fileExists('frontend/package.json') } }
            steps {
                echo "==== [SonarQube Frontend] Iniciando ===="
                script {
                    def branch = env.BRANCH_NAME.toLowerCase()
                    def sonarConfig = [
                        'main':        ['projectKey': 'FP:Frontend_Prod',        'projectName': 'FP:Frontend_Prod',        'tokenId': 'sonarqube-frontend-main'],
                        'development': ['projectKey': 'FP:Frontend_Development','projectName': 'FP:Frontend_Development','tokenId': 'sonarqube-frontend-development'],
                        'qa':          ['projectKey': 'FP:Frontend_Qa',         'projectName': 'FP:Frontend_Qa',         'tokenId': 'sonarqube-frontend-qa']
                    ]
                    
                    def config = sonarConfig[branch]
                    if (!config) error "No hay configuración de SonarQube para la rama '${branch}'"

                    withCredentials([string(credentialsId: config.tokenId, variable: 'SONAR_TOKEN')]) {
                        dir('frontend') {
                            sh """
                                npx sonar-scanner \
                                  -Dsonar.projectKey=${config.projectKey} \
                                  -Dsonar.projectName="${config.projectName}" \
                                  -Dsonar.sources=. \
                                  -Dsonar.host.url=${SONAR_HOST_URL} \
                                  -Dsonar.login=${SONAR_TOKEN} \
                                  -Dsonar.language=ts \
                                  -Dsonar.sourceEncoding=UTF-8 \
                                  -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info
                            """
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

        stage('SonarQube Frontend Quality Gate') {
            when { expression { fileExists('frontend/package.json') } }
            steps {
                echo "==== [Quality Gate Frontend] Iniciando ===="
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
                echo "==== [Quality Gate Frontend] Finalizado ===="
            }
            post {
                success { script { stageStatus['SonarQube Frontend Quality Gate'] = 'SUCCESS' } }
                failure { script { stageStatus['SonarQube Frontend Quality Gate'] = 'FAILURE'; failedStage = "SonarQube Frontend Quality Gate" } }
                aborted { script { stageStatus['SonarQube Frontend Quality Gate'] = 'NOT_EXECUTED' } }
            }
        }

        stage('Deploy') {
            when { expression { return !env.CHANGE_ID } }
            steps {
                script {
                    def branch = env.BRANCH_NAME.toLowerCase()
                    def profile = ''
                    if (branch == 'main') profile = 'prod'
                    else if (branch == 'development') profile = 'dev'
                    else if (branch == 'qa') profile = 'qa'
                    else error "No hay configuración de despliegue para la rama '${branch}'"

                    echo "=== Deploy con perfil: ${profile} ==="
                    sh "docker-compose -f docker-compose.comp.yml --profile ${profile} down || true"
                    sh "docker-compose -f docker-compose.comp.yml --profile ${profile} up -d --build"
                }
            }
            post {
                success { script { stageStatus['Deploy'] = 'SUCCESS' } }
                failure { script { stageStatus['Deploy'] = 'FAILURE'; failedStage = "Deploy" } }
                aborted { script { stageStatus['Deploy'] = 'NOT_EXECUTED' } }
            }
        }
    }

    post {
        failure {
            script {
                allStages.each { stageName -> if (!stageStatus.containsKey(stageName)) stageStatus[stageName] = 'NOT_EXECUTED' }

                def reportStages = stageStatus.collect { stageName, status ->
                    def color = status == 'FAILURE' ? 'red' : (status == 'SUCCESS' ? 'green' : 'gray')
                    return "<li><b>${stageName}:</b> <span style='color:${color}'>${status}</span></li>"
                }.join("")

                def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))
                def contextName = env.CHANGE_ID ? 'continuous-integration/jenkins/pr-merge' : 'continuous-integration/jenkins/branch'
                githubNotify context: contextName, status: 'FAILURE', description: "Falló en ${failedStage}", targetUrl: env.BUILD_URL

                mail(
                    to: 'abrilsofia159@gmail.com,jflores@unis.edu.gt',
                    subject: "TEST",
                    body: """
<html>
<body style="font-family:Arial,sans-serif;background-color:#f4f4f4;padding:20px;">
<div style="max-width:600px;margin:auto;background:white;padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);">
<h2 style="color:red;text-align:center;">Reporte de Fallo - Jenkins Pipeline</h2>
<p><b>Fecha:</b> ${fecha} UTC</p>
<p><b>Job:</b> ${env.JOB_NAME}</p>
<p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
<p><b>Rama:</b> ${env.BRANCH_NAME}</p>
<hr style="border:none;border-top:1px solid #ccc;">
<p>El pipeline falló en la etapa de <b>${failedStage}</b>.</p>
<hr style="border:none;border-top:1px solid #ccc;">
<h3>Estado de las etapas:</h3>
<ul style="list-style:none;padding-left:0;">${reportStages}</ul>
</div>
</body>
</html>
""",
                    mimeType: 'text/html'
                )
            }
        }

        success {
            script {
                allStages.each { stageName -> if (!stageStatus.containsKey(stageName)) stageStatus[stageName] = 'NOT_EXECUTED' }

                def reportStages = stageStatus.collect { stageName, status ->
                    def color = status == 'SUCCESS' ? 'green' : 'gray'
                    return "<li><b>${stageName}:</b> <span style='color:${color}'>${status}</span></li>"
                }.join("")

                def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))
                def contextName = env.CHANGE_ID ? 'continuous-integration/jenkins/pr-merge' : 'continuous-integration/jenkins/branch'
                githubNotify context: contextName, status: 'SUCCESS', description: "Pipeline OK", targetUrl: env.BUILD_URL

                mail(
                    to: 'abrilsofia159@gmail.com,jflores@unis.edu.gt',
                    subject: "Pipeline Ejecutado Correctamente",
                    body: """
<html>
<body style="font-family:Arial,sans-serif;background-color:#f4f4f4;padding:20px;">
<div style="max-width:600px;margin:auto;background:white;padding:20px;border-radius:8px;box-shadow:0 0 10px rgba(0,0,0,0.1);">
<h2 style="color:green;text-align:center;">Pipeline Ejecutado Correctamente</h2>
<p><b>Fecha:</b> ${fecha} UTC</p>
<p><b>Job:</b> ${env.JOB_NAME}</p>
<p><b>Build #:</b> ${env.BUILD_NUMBER}</p>
<p><b>Rama:</b> ${env.BRANCH_NAME}</p>
<hr style="border:none;border-top:1px solid #ccc;">
<h3>Estado de las etapas:</h3>
<ul style="list-style:none;padding-left:0;">${reportStages}</ul>
</div>
</body>
</html>
""",
                    mimeType: 'text/html'
                )
            }
        }
    }
}
