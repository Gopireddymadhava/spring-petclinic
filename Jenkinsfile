pipeline {
    agent any

    environment {

        DOCKER_HUB_USER = "lokokun290"
        DOCKER_IMAGE    = "spring-petclinic-bar"
        JFROG_DOMAIN    = "bardpetclinic"
        JFROG_REPO      = "libs-release-local"
    }

    stages {
        stage('Compile') {
            steps {
                echo 'Compiling the code'
                sh './mvnw compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests'
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging project to JAR'

                sh './mvnw package -DskipTests'
            }
        }

        stage('Artifact to JFrog') {
            steps {
                script {
                    echo 'Uploading JAR to JFrog Artifactory'
                    withCredentials([usernamePassword(credentialsId: 'jfrog-creds', passwordVariable: 'JF_PASS', usernameVariable: 'JF_USER')]) {
                        sh """
                        curl -u ${JF_USER}:${JF_PASS} -X PUT "https://${JFROG_DOMAIN}.jfrog.io/artifactory/${JFROG_REPO}/spring-petclinic-${env.BUILD_ID}.jar" -T target/*.jar
                        """
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image'
                sh "docker build -t ${DOCKER_IMAGE}:${env.BUILD_ID} ."
                sh "docker tag ${DOCKER_IMAGE}:${env.BUILD_ID} ${DOCKER_HUB_USER}/${DOCKER_IMAGE}:latest"
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    echo 'Pushing image to Docker Hub'
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}"
                        sh "docker push ${DOCKER_HUB_USER}/${DOCKER_IMAGE}:latest"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! Artifact is in JFrog and Image is in Docker Hub.'
        }
        failure {
            echo 'Pipeline failed. Check the logs.'
        }
    }
}