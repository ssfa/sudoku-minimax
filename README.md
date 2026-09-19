# Sudoku Minimax

스도쿠 퍼즐 앱 (안드로이드)

## 버전

- **현재**: 0.1.0 (versionCode 2)

## 빌드

```bash
mise run build
```

APK: `artifacts/ssfa.sudoku.minimax-0.1.0.apk`

## 기능

- 3단계 난이도 (하/중/상)
- 힌트 / 메모 기능
- 타이머 (정지/재개)
- 저장/복원 (SharedPreferences)
- 라이트/다크 테마 (시스템 설정 따름)

## 개발

```bash
mise run build      # 디버그 APK
mise run install    # 기기 설치
mise run launch     # 실행
```

## 버전 히스토리

| 버전 | 변경 |
|------|------|
| 0.0.1 | 프로젝트 뼈대 + 빌드 확인 |
| 0.1.0 | 게임 화면 + 난이도 선택 + 숫자 입력 + 힌트/메모/타이머 기초 |