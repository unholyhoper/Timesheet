pipeline {
    agent {
        docker {
            image 'timesheet'
            registryCredentialsId 'docker'
            args '-v /var/jenkins_home/.m2:/root/.m2'
        }
    }
    environment {

        imagename = "timesheet"
        registryCredential = 'yenigul-dockerhub'
        dockerImage = ''
    }

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "Maven 3.8.1"
    }

    stages {
        stage('Cloning Project from Git') {
            steps {
                git branch: 'Jenkinsfile',
                        url: 'https://gitlab.com/Unholyhoper/timesheet.git'
            }
        }
        stage('CLEAN') {
            steps {
                echo 'Cleaning project'
                bat 'mvn clean'
            }
        }
        stage('COMPILE') {
            steps {
                echo 'Compiling Project';
                bat 'mvn compile';

            }
        }

        stage('SONARQUE SCAN') {
            steps {
                echo 'Launching sonarqube scan';
                bat 'mvn sonar:sonar -Dsonar.projectKey=Projet-DEVOPS-Sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=03e7a8c0ba1a11b502e1bfa698cee58f370ddf6a';
            }
        }
        stage('TEST') {
            steps {
                echo 'Running JUnit tests'
                bat 'mvn test';

            }
        }
        stage('PACKAGE') {
            steps {
                echo 'Packaging Project';
                bat 'mvn package';
            }
        }
        stage('DEPLOY') {
            steps {
                echo 'Deploying the Project';
                bat 'mvn clean package -DskipTests deploy:deploy-file -DgroupId=tn.esprit.spring -DartifactId=Timesheet -Dversion=1.0.0-SNAPSHOT -DgeneratePom=true -Dpackaging=war -DrepositoryId=deploymentRepo -Durl=http://localhost:8081/repository/maven-snapshots/ -Dfile=target/Timesheet-1.0.0-SNAPSHOT.war';
            }
        }
        stage('Building our image') {
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
        stage('Push our image') {
            steps {
                script {
                    dockerImage.push("$BUILD_NUMBER")
                    dockerImage.push('latest')
                }
            }
        }
    }


}
