pipeline {
    agent any

    environment {
        IMAGE_NAME = "springboot-app"
        IMAGE_TAG  = "v1"
        SONAR_HOST_URL = "http://host.docker.internal:9000"
    }

    stages {

        stage('Checkout Source') {
            steps {
                git branch: 'main',
                credentialsId: 'github-creds',
                url: 'https://github.com/Gopireddymadhava/spring-petclinic.git'
            }
        }

        stage('Build Maven') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Scan') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    bat """
                    mvn sonar:sonar ^
                    -Dsonar.projectKey=spring-petclinic ^
                    -Dsonar.projectName=spring-petclinic ^
                    -Dsonar.host.url=%SONAR_HOST_URL% ^
                    -Dsonar.login=%SONAR_TOKEN%
                    """
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t %IMAGE_NAME%:%IMAGE_TAG% .'
            }
        }

        stage('Run Container') {
            steps {
                bat '''
                docker stop spring-petclinic-container || exit 0
                docker rm spring-petclinic-container || exit 0
                docker run -d --name spring-petclinic-container -p 8080:8080 %IMAGE_NAME%:%IMAGE_TAG%
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                kubectl delete deployment spring-petclinic --ignore-not-found=true
                kubectl delete service spring-petclinic-service --ignore-not-found=true

                kubectl create deployment spring-petclinic --image=%IMAGE_NAME%:%IMAGE_TAG%

                kubectl expose deployment spring-petclinic ^
                --type=LoadBalancer ^
                --port=80 ^
                --target-port=8080
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }
    }
}
