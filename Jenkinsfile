pipeline {
    agent any

    environment {
        APP_NAME   = "spring-petclinic"
        IMAGE_NAME = "springboot-app"
        IMAGE_TAG  = "${BUILD_NUMBER}"
        SONAR_URL  = "http://sonarqube:9000"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                credentialsId: 'github-creds',
                url: 'https://github.com/Gopireddymadhava/spring-petclinic.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Scan') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                    mvn sonar:sonar \
                    -Dsonar.projectKey=spring-petclinic \
                    -Dsonar.projectName=spring-petclinic \
                    -Dsonar.host.url=$SONAR_URL \
                    -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                docker stop spring-petclinic-container || true
                docker rm spring-petclinic-container || true
                docker run -d --name spring-petclinic-container -p 8080:8080 $IMAGE_NAME:$IMAGE_TAG
                '''
            }
        }

        stage('Deploy Kubernetes') {
            steps {
                sh '''
                kubectl apply -f deployment.yaml
                '''
            }
        }
    }

    post {
        success {
            echo 'Build Success'
        }

        failure {
            echo 'Build Failed'
        }
    }
}
