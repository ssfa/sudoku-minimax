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
mise run install    # 기기 설치
mise run launch     # 실행
```

## 버전 히스토리

| 버전 | 변경 |
|------|------|
| 0.0.1 | 프로젝트 뼈대 + 빌드 확인 |
| 0.1.0 | 게임 화면 + 난이도 선택 + 숫자 입력 + 힌트/메모/타이머 기초 |
| 0.2.0 | 크래시 수정 + 완성 Dialog/Confetti + 테스트 (13 tests) |
| 0.2.1 | APK 용량 최적화 (8.4MB→1.4MB) + 상태바 노출 + 게임 제목 + 남은 빈칸 표시 |
| 0.2.2 | 일시정지 제거 + 햄버거 버튼 우측 배치 + 앱 제목 26sp bold |
| 0.2.3 | Material Icons 적용 (Clear/Lightbulb/Edit/Menu) + 버튼 UI 개선 |
| 0.2.4 | 버튼 텍스트 가시성 개선 (높이 56dp, 흰색 텍스트) |
| 0.2.5 | 백그라운드 타이머 자동 정지/재개 + 다크모드 버튼 텍스트 색상 동적 계산 |