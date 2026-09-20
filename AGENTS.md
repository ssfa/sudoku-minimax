# AGENTS

## 프로젝트

- **Package**: `com.ssfa.sudoku.minimax`
- **App ID**: `ssfa.sudoku.minimax`
- **GitHub**: `ssfa/sudoku-minimax`
- **최종 버전**: 0.2.5 (versionCode 8)

## 빌드

```bash
mise run build
```

APK: `artifacts/ssfa.sudoku.minimax-<version>.apk`

## 버전 관리

- SemVer + versionCode 일치. 기능은 MINOR, 레이아웃/스타일/카피/버그는 PATCH.
- 빌드마다 마이너 버전 올리기 (versionCode도 같이).
- 버전 3곳 동기화: `app/build.gradle.kts` (versionCode/versionName), `GameScreen.kt` (v footer), `MenuScreen.kt` (v footer).

## 파일 구조

| 파일 | 용도 |
|------|------|
| `app/build.gradle.kts` | version, minify, material-icons-extended |
| `app/src/main/java/.../MainActivity.kt` | edge-to-edge, lifecycle observer (타이머) |
| `app/src/main/java/.../ui/theme/Theme.kt` | statusBar TRANSPARENT |
| `app/src/main/java/.../ui/theme/AppCompatTheme.kt` | `isDarkTheme` @Composable |
| `app/src/main/java/.../ui/game/GameScreen.kt` | 메인 UI |
| `app/src/main/java/.../ui/game/GameViewModel.kt` | 게임 로직, `pauseTimer`/`resumeTimer` |
| `app/src/main/java/.../ui/game/MenuScreen.kt` | 메뉴 화면 |

## 개발 규칙

- **테마**: `isSystemInDarkTheme` import (deprecated `isSystemInDarkTheme` 아님)
- **타이머 백그라운드 정지**: `DefaultLifecycleObserver` 사용 — `viewModel()`은 Composable 전용이라 activity 레벨 class property 불가
- **버튼 색상**: `onPrimary` 사용 (라이트=검정, 다크=흰색 자동)
- **Material Icons**: `material-icons-extended:1.7.0` 사용 (Icons.Filled.Clear/Lightbulb/Edit/Menu)
- **Git force push**: `--force-with-lease` 사용

## 테스트

- `mise run test` — 11개 unit test
- JaCoCo plugin 미사용 (isDebuggable=false 충돌), 수동 CRAP 추정만 가능

## gitignore

```
build/
artifacts/
.gradle/
*.apk
```