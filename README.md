<div align="center">
  <h1>📸 리센느 포토 위젯</h1>
  <p><strong>일상의 장면(Scene)을 향기로 다시 떠올리게 하는 리센느와 함께하는 홈 화면</strong></p>
</div>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
</p>

리센느의 멤버 사진을 고르고, 원하는 이미지를 배경화면 또는 홈 화면 위젯으로 등록할 수 있는 Android 팬 프로젝트입니다. 앨범 콘셉트별 랜덤 사진 선택과 포토·D-Day·시계 위젯을 제공해 나만의 홈 화면을 꾸밀 수 있습니다.

## ✨ 주요 기능

### 1. 리센느 메인 화면과 앨범별 사진 선택

멤버와 앨범 콘셉트를 한 화면에서 확인할 수 있습니다. 앨범 메뉴를 선택하면 해당 카테고리의 사진을 랜덤으로 불러와 매번 새로운 분위기의 이미지를 만날 수 있습니다.

<p align="center">
  <img src="docs/images/main.gif" width="280" alt="리센느 메인 화면과 앨범 사진 선택 시연" />
</p>

### 2. 멤버 포토 위젯 등록

프로필을 선택해 멤버 사진을 갱신하고, 선택한 이미지를 포토 위젯으로 홈 화면에 추가할 수 있습니다. 위젯마다 선택한 사진 정보를 저장해 여러 장의 사진을 원하는 방식으로 배치할 수 있습니다.

<p align="center">
  <img src="docs/images/widget.gif" width="280" alt="멤버 포토 위젯 등록 시연" />
</p>

### 3. 배경화면 적용과 위젯 유형 선택

선택한 사진을 기기 배경화면으로 설정하고, 이어서 포토 위젯·D-Day 위젯·시계 위젯 중 원하는 유형을 골라 홈 화면에 등록할 수 있습니다. D-Day와 시계 위젯은 사진과 함께 필요한 정보를 더해 보여 줍니다.

<p align="center">
  <img src="docs/images/wallpaper.gif" width="280" alt="배경화면 및 위젯 설정 시연" />
</p>

## 🛠 기술 스택

| 구분 | 기술 |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | Clean Architecture, Hilt, ViewModel |
| Async | Coroutines, Flow |
| Widget | GlanceAppWidget, GlanceAppWidgetReceiver |
| Image | Coil |
| Data | Supabase, SharedPreferences / DataStore |
| Security | ProGuard/R8 |

## 🧩 구현 포인트

- **랜덤 이미지 선택:** 앨범 카테고리별 사진 목록에서 이미지를 무작위로 선택합니다.
- **위젯 상태 유지:** 위젯 ID별 사진 URL과 위젯 유형을 저장해 앱과 홈 화면 위젯 사이의 상태를 유지합니다.
- **이미지 캐시:** 위젯에 사용할 이미지를 캐시 파일로 저장하고, 이미지가 변경된 경우에만 다시 내려받아 불필요한 작업을 줄입니다.
- **선언형 위젯 UI:** Glance 기반으로 포토·D-Day·시계 위젯을 구성해 위젯 종류별 화면을 일관되게 관리합니다.

## 📁 프로젝트 구조

```text
app/src/main/java/
├── data/          # 원격 데이터 소스와 저장소 구현
├── domin/         # 도메인 모델·UseCase·Repository 인터페이스
├── presentation/  # ViewModel과 UI 상태
└── gomin/         # Compose 화면, 배경화면 및 위젯 기능
```

## ⚖️ 안내

본 프로젝트는 비상업적 팬 프로젝트입니다. 사용된 리센느 관련 이미지와 상표의 권리는 각 권리자에게 있으며, 권리자의 요청이 있을 경우 관련 콘텐츠를 조정하거나 비공개로 전환합니다.

## 🧑‍💻 Author

- Contact: [dkdkdodo123@gmail.com](mailto:dkdkdodo123@gmail.com)
- GitHub: [@cho123456789](https://github.com/cho123456789)
