def stageStatus = [:]  // etapa -> estado
def failedStage = ""
def allStages = ['Notify Build Start','Checkout', 'Build Backend', 'Test Backend', 'SonarQube Backend Analysis', 'Build Frontend', 'SonarQube Frontend Analysis', 'Deploy']

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
                    sh "docker compose --profile ${profile} down || true"
                    sh "docker compose --profile ${profile} up -d --build"
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
                // Completar con NOT_EXECUTED
                allStages.each { stageName ->
                    if (!stageStatus.containsKey(stageName)) {
                        stageStatus[stageName] = 'NOT_EXECUTED'
                    }
                }

                // Construir reporte de texto plano con colores como etiqueta
                def reportStages = stageStatus.collect { stageName, status ->
                    def color = status == 'FAILURE' ? '[FAIL]' : (status == 'SUCCESS' ? '[OK]' : '[SKIPPED]')
                    return "${stageName}: ${color}"
                }.join("\n")

                def fecha = new Date().format("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone('UTC'))

                // Notificar GitHub
                def contextName = env.CHANGE_ID ? 'continuous-integration/jenkins/pr-merge' : 'continuous-integration/jenkins/branch'
                githubNotify context: contextName, status: 'FAILURE', description: "Falló en ${failedStage}", targetUrl: env.BUILD_URL

                // Enviar correo
                mail(
                    to: 'abrilsofia159@gmail.com,jflores@unis.edu.gt',
                    subject: "TEST",
                    body: """
Reporte de Fallo - Jenkins Pipeline

Fecha: ${fecha} UTC
Job: ${env.JOB_NAME}
Build #: ${env.BUILD_NUMBER}
Rama: ${env.BRANCH_NAME}

El pipeline falló en la etapa: ${failedStage}

Estado de las etapas:
${reportStages}
"""
                )
            }
        }

        success {
            script {
                def contextName = env.CHANGE_ID ? 'continuous-integration/jenkins/pr-merge' : 'continuous-integration/jenkins/branch'
                githubNotify context: contextName, status: 'SUCCESS', description: "Pipeline OK", targetUrl: env.BUILD_URL
            }
        }
    }
}
