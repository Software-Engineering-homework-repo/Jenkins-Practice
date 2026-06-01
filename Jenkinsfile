pipeline {
    agent any

    //hihi
    environment {
        JUNIT_JAR_URL = 'https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.7.1/junit-platform-console-standalone-1.7.1.jar'
        JUNIT_JAR_PATH = 'lib/junit.jar'
        CLASS_DIR = 'classes'
        REPORT_DIR = 'test-reports'
        IMAGE_NAME = 'student-manager'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                sh '''
                    mkdir -p ${CLASS_DIR}
                    mkdir -p ${REPORT_DIR}
                    mkdir -p lib
                    echo "[+] Downloading JUnit JAR..."
                    curl -L -o ${JUNIT_JAR_PATH} ${JUNIT_JAR_URL}
                '''
            }
        }

        // Compile all Java sources under Test2/src into classes/
        stage('Build') {
            steps {
                sh '''
                    echo "[+] Compiling source files..."
                    cd Test2
                    find src -name "*.java" > sources.txt
                    javac -encoding UTF-8 -d ../${CLASS_DIR} -cp ../${JUNIT_JAR_PATH} @sources.txt
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                    echo "[+] Running tests with JUnit..."
                    java -jar ${JUNIT_JAR_PATH} \
                         --class-path ${CLASS_DIR} \
                         --scan-class-path \
                         --details=tree \
                         --details-theme=ascii \
                         --reports-dir ${REPORT_DIR} \
                         --config=junit.platform.output.capture.stdout=true \
                         --config=junit.platform.reporting.open.xml.enabled=true \
                         > ${REPORT_DIR}/test-output.txt
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    echo "[+] Docker build..."
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                    docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${IMAGE_NAME}:latest
                    docker images | grep ${IMAGE_NAME}
                '''
            }
        }
    }

    post {
        always {
            echo "[*] Archiving test results..."
            junit "${REPORT_DIR}/**/*.xml"
            archiveArtifacts artifacts: "${REPORT_DIR}/**/*", allowEmptyArchive: true
        }

        success {
            echo "Build and test succeeded!"
            emailext (
                subject: "[Jenkins] SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """\
빌드 성공!

Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
URL: ${env.BUILD_URL}
""",
                to: 'ghwo336@gmail.com, yujun1123@g.hongik.ac.kr'
            )
        }

        failure {
            echo "Build or test failed!"
            emailext (
                subject: "[Jenkins] FAIL: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """\
빌드 실패!

Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
URL: ${env.BUILD_URL}
""",
                to: 'ghwo336@gmail.com, yujun1123@g.hongik.ac.kr'
            )
        }
    }
}
