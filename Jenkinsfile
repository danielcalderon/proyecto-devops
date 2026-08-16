pipeline {

    agent any

    environment {
        IMAGE_NAME = 'ghcr.io/danielcalderon/proyecto-devops'
        REPOSITORY_URL = 'https://github.com/danielcalderon/proyecto-devops.git'
    }

    stages {

        stage('Checkout') {
            steps {
                git(
                    url: REPOSITORY_URL,
                    branch: 'main',
                    credentialsId: 'github-credentials'
                )
            }
        }

        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:latest .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'github-registry-credentials',
                        usernameVariable: 'GITHUB_USERNAME',
                        passwordVariable: 'GITHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        echo "$GITHUB_TOKEN" | docker login ghcr.io \
                            -u "$GITHUB_USERNAME" \
                            --password-stdin
                        docker push ${IMAGE_NAME}:latest
                        docker logout
                    '''
                }
            }
        }
    }
}