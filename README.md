# 소공 Jenkins/DevOps 과제 (Mission02)

Java(JUnit5) + Jenkins(Docker) + GitHub Webhook + Docker 가산점

---

## 팀원 역할 분담

| 팀원 | 역할 | 담당 작업 |
|------|------|----------|
| **이호재** (팀장) | 인프라 · 통합 | Docker Compose 구성, Jenkins 환경 셋업, ngrok 터널링, GitHub Webhook 연동, 전체 아키텍처 설계 |
| **이주희** | Java · 단위테스트 | `StudentManager` 클래스 구현, JUnit5 테스트 케이스 5종 작성 (추가/제거/중복예외/존재예외/다중추가) |
| **이태경** | Jenkins Pipeline | `Jenkinsfile` 작성 (Checkout / Prepare / Build / Test / Post-Action stage), JUnit 결과 archive 설정 |
| **김태우** | Docker (가산점) | 멀티스테이지 `Dockerfile` 작성, `.dockerignore` 구성, Jenkins Pipeline에 Docker Build stage 추가 |
| **오유준** | 알림 · 검증 | Gmail SMTP 연동, `emailext` 빌드 결과 알림 구성, 의도적 실패 → 분석 → 수정 시연 |

협업 흐름: 각 팀원이 본인 담당 영역을 EGit/GitHub으로 **Clone → Branch → Commit → Push → Pull Request → Merge** 순서로 진행. 각 PR마다 Jenkins가 자동으로 빌드/테스트 수행.

---

## 아키텍처

```
[개발자 IDE (Eclipse + EGit)]
            │ push
            ▼
       [GitHub Repo]
            │ webhook (ngrok 터널)
            ▼
   ┌────────────────────────┐
   │  Jenkins (Docker)      │
   │  ┌─────────────────┐   │
   │  │ Pipeline Stage  │   │
   │  │  Checkout       │   │
   │  │  Prepare        │   │
   │  │  Build (javac)  │   │
   │  │  Test (JUnit5)  │   │
   │  │  Docker Build ★ │   │
   │  └─────────────────┘   │
   └────────────────────────┘
            │
            ├── test-reports/test-output.txt (archive)
            └── 이메일 알림 (SUCCESS / FAIL)
```

(★ Docker Build = 가산점 항목)

---

## 폴더 구조

```
.
├── Jenkinsfile               # Jenkins Pipeline 스크립트
├── Dockerfile                # 멀티스테이지 빌드 (가산점)
├── .dockerignore
├── .gitignore
├── README.md                 # 본 문서
├── docs/
│   └── team-signoff.md       # 팀원 서명
└── Test2/
    └── src/
        └── manager/
            ├── StudentManager.java       # 학생 관리 클래스
            └── StudentManagerTest.java   # JUnit5 테스트
```

---

## 빌드/테스트 (로컬 검증용)

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

---

## CI/CD 동작

1. 개발자가 `git push`
2. GitHub Webhook → ngrok 터널 → Jenkins 수신
3. Jenkins가 자동 Pipeline 실행
4. Build → Test → Docker Build 순으로 stage 실행
5. 결과를 `test-reports/test-output.txt`로 저장하고 archive
6. SUCCESS / FAIL에 따라 팀장 이메일로 알림 전송

---

## 요구사항 충족

| # | 요구사항 | 충족 방식 |
|---|---------|----------|
| ① | 팀장이 GitHub Repository 생성 | 본 레포 |
| ② | EGit으로 Clone/Commit/Push/PR/Merge 협업 | 팀원 5명 각자 PR + 머지 이력 |
| ③ | Push 시 Jenkins 자동 빌드 | Webhook + Pipeline Item 동작 확인 |
| ④ | 빌드 실패 시 원인 분석 및 수정 | 의도적 실패 커밋 → 분석 → 수정 커밋 |
| ⑤ | 빌드 성공 시 txt 저장 or 이메일 | `test-output.txt` archive + Gmail 알림 |
| ★ | (가산점) Docker 활용 | Docker Compose 인프라 + 앱 멀티스테이지 빌드 |
