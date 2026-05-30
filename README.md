# 소공 Jenkins/DevOps 과제

Java(JUnit5) + Jenkins(Docker) + GitHub Webhook + Docker(가산점)

## 구성

```
.
├── Jenkinsfile           # Jenkins Pipeline (Build/Test/Docker)
├── Dockerfile            # 멀티스테이지 빌드 (가산점)
├── .dockerignore
├── .gitignore
├── docs/
│   └── team-signoff.md   # 팀원 서명
└── Test2/
    └── src/
        └── manager/
            ├── StudentManager.java
            └── StudentManagerTest.java
```

## 빌드/테스트 (로컬)

```bash
cd Test2
mkdir -p ../lib ../classes
curl -L -o ../lib/junit.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.7.1/junit-platform-console-standalone-1.7.1.jar
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d ../classes -cp ../lib/junit.jar @sources.txt
cd ..
java -jar lib/junit.jar --class-path classes --scan-class-path --details=tree
```

## Docker로 빌드/테스트

```bash
docker build -t student-manager:latest .
docker run --rm student-manager:latest
```

## Jenkins 파이프라인

- Push 시 GitHub Webhook → Jenkins 자동 빌드
- Build → Test → (Docker Build) 순으로 실행
- 결과는 `test-reports/test-output.txt` 로 archive + 이메일 알림

## 팀원

- 호재 (팀장 · 인프라 · Jenkins · Docker)
- 팀원2
- 팀원3
- 팀원4
- 팀원5
