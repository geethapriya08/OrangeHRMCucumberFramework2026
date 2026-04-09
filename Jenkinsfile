pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build and Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn -B clean test'
                    } else {
                        bat 'mvn -B clean test'
                    }
                }
            }
        }
        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'target/cucumber-report/**, target/cucumber-report.json, TestResult/*.html', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/cucumber-report/**, target/cucumber-report.json, TestResult/*.html', allowEmptyArchive: true
        }
    }
}
