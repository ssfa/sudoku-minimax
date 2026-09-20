# Sudoku Minimax

스도쿠 퍼즐 앱 (안드로이드)

## 버전

- **현재**: 0.2.5 (versionCode 8)

## 빌드

```bash
mise run build
```

APK: `artifacts/ssfa.sudoku.minimax-0.2.5.apk`

## 기능

- 3단계 난이도 (하/중/상)
- 힌트 / 메모 기능
- 타이머 (백그라운드 시 자동 정지)
- 저장/복원 (SharedPreferences)
- 라이트/다크/시스템 테마 수동 선택
- APK 최적화 (R8 minify, 1.4MB)

## 개발

```bash
mise run build      # 디버그 APK
mise run install    # 연결된 기기(에뮬/실기)에 APK 설치
mise run launch     # 빌드·설치 후 앱 실행
```

### 에뮬레이터

```bash
emulator -list-avds     # 사용 가능한 AVD 목록
emulator -avd <이름> &   # 에뮬레이터 실행

# 에뮬레이터 연결 후
mise run install
mise run launch
```

## 변경 이력

[CHANGES.md](./CHANGES.md) 참고.