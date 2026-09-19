# Sudoku Minimax — 개발 계획

## 1. 개요
- **앱 ID**: `ssfa.sudoku.minimax`
- **패키지 네임스페이스**: `com.ssfa.sudoku.minimax`
- **최초 버전**: 0.0.1 (versionCode 1)
- **최소 SDK**: 26 / **타겟 SDK**: 35 / **컴파일 SDK**: 35
- **언어**: Kotlin + Jetpack Compose

## 2. 기능 목록

| # | 기능 | 설명 |
|---|------|------|
| F1 | 난이도 선택 | 하(초급), 중(중급), 상(고급) 3단계 |
| F2 | 스도쿠 게임 화면 | 9×9 그리드, 숫자 입력, 선택 highlighting |
| F3 | 선명しい 그리드 렌더링 | Canvas drawLine으로 3×3 박스 테두리 강조 |
| F4 | 힌트 기능 | 현재 선택 셀에 정답 표시 |
| F5 | 메모 기능 | 셀에 가능한 수候補 메모 토글 |
| F6 | 타이머 | 정지/재개 가능, 상단 표시 |
| F7 | 게임 저장/복원 | Activity lifecycle + SharedPreferences로 죽였다 켜도 이어짐 |
| F8 | 라이트 / 다크 테마 | Material 3 dynamic / fixed theming |
| F9 | 버전 정보 | 하단 고정 표시 (v0.0.1) |

## 3. 기술 스택

| 계층 | 선택 |
|------|------|
| UI | Jetpack Compose + Material 3 |
| 언어 | Kotlin 1.9 |
| 상태 관리 | Compose ViewModel + StateFlow |
| 퍼즐 생성 | 순수 Java/Kotlin 로직 (미리 채워진 스도쿠 + 빈 셀 제거) |
| 저장 | SharedPreferences (JSON 직렬화) |
| 빌드 | Gradle 8.13, AGP 8.5.2, Java 17 |

## 4. 세마포어 (커밋 시점)

각 마일스톤 완료 후 버전 올리고 커밋:

| 마일스톤 | 버전 |
|---------|------|
| MS1: 프로젝트 뼈대 + 빌드 확인 | 0.0.1 |
| MS2: 게임 화면 + 난이도 선택 | 0.1.0 |
| MS3: 숫자 입력 + 검증 + 하이라이트 | 0.2.0 |
| MS4: 힌트 + 메모 기능 | 0.3.0 |
| MS5: 타이머 | 0.4.0 |
| MS6: 저장/복원 | 0.5.0 |
| MS7: 라이트/다크 테마 | 0.6.0 |
| MS8: 버전 정보 + 최종 정리 | 0.7.0 |

## 5. 파일 구조

```
sudoku-minimax/
├── PLAN.md
├── README.md
├── mise.toml
├── gradle/
│   └── wrapper/
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── local.properties
├── artifacts/           # APK 출력
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/ssfa/sudoku/minimax/
        │   ├── MainActivity.kt
        │   ├── SudokuApplication.kt
        │   ├── ui/
        │   │   ├── theme/
        │   │   ├── game/
        │   │   └── menu/
        │   ├── engine/
        │   │   ├── SudokuGenerator.kt
        │   │   └── GameState.kt
        │   └── data/
        │       └── GameStorage.kt
        └── res/
            ├── values/
            ├── values-night/
            └── drawable/
```

## 6. 빌드 & 배포

```bash
mise run build          # 디버그 APK → artifacts/ssfa.sudoku.minimax-<ver>.apk
mise run build:release  # 릴리즈 APK
adb install -r artifacts/ssfa.sudoku.minimax-*.apk
```