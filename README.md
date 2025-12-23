<div align="center">
<h2>📸 QWER Photo Widget</h2>
</div>

<p align="center">
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EB%8B%A8%EC%B2%B4%EC%82%AC%EC%A7%84/%EB%8B%A8%EC%B2%B4%EC%82%AC%EC%A7%84.jpg?raw=true" width="70%">
  <br>
  <strong>"일상의 코드(Chord)에 QWER의 색을 입히다"</strong>
</p>

> **QWER Photo Widget**은 아티스트 **QWER**을 향한 팬심을 안드로이드의 최신 기술로 구현한 커스텀 위젯 서비스입니다. 단순히 사진을 보여주는 것을 넘어, 팬과 아티스트를 잇는 가장 가까운 연결고리를 홈 화면에 제공합니다.

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Design-Generative_AI-FF6C37?style=flat-square&logo=openai&logoColor=white"/>
</p>

## 🌟 1. Project Inspiration & Benchmarking

### 💌 From Fan's Heart (프로젝트 동기)
> "저는 평소 **[아이돌 이름]**의 음악과 무대에서 큰 에너지를 얻는 팬입니다."
> 
> *'어떻게 하면 덕질을 더 즐겁게, 더 일상적으로 할 수 있을까?'* > 고민의 끝은 결국 우리가 가장 많이 보는 **스마트폰의 첫 화면**이었습니다. 
> 최애의 모습을 단지 갤러리 속에 가두지 않고, **Android 홈 화면 위젯**을 통해 매 순간 함께하고자 하는 진심에서 이 프로젝트가 시작되었습니다.

### 🔍 Market Analysis & Benchmarking
기존의 K-POP 포토위젯 앱들이 가진 **직관적인 UI**와 **실시간 이미지 업데이트** 기능을 철저히 분석하고 벤치마킹했습니다.
* **감성(Emotion):** 팬덤의 감성을 자극하는 고퀄리티 비주얼 경험
* **편의성(Usability):** 단 한 번의 설정으로 끊김 없이 업데이트되는 최적화된 로직
* **목표:** 기존 앱들의 장점을 흡수하고, 저만의 **AI 디자인**과 **정교한 안드로이드 기술력**을 더해 '팬들이 진정으로 원하던 위젯'을 구현하는 것을 목표로 삼았습니다.

---

## 🏗 3. Project Structure & Timeline

### System Architecture


* **UI Layer:** Jetpack Compose 기반의 선언형 UI
* **Domain/Data Layer:** MVVM 패턴을 적용하여 비즈니스 로직과 UI 분리
* **Widget Engine:** `AppWidgetProvider` & `RemoteViews`를 통한 홈 화면 연동
* **Networking:** `Retrofit2` & `Coil`을 이용한 이미지 스트리밍

### Development Timeline
1.  **Phase 1 (기획):** 아이돌 컨셉 분석 및 AI 디자인 시스템 구축
2.  **Phase 2 (개발):** 위젯 핵심 로직 및 API 연동
3.  **Phase 3 (고도화):** `WorkManager`를 이용한 백그라운드 동기화 및 캐싱
4.  **Phase 4 (안정화):** 메모리 최적화 및 디바이스 테스트

---

## 🚀 4. Key Features & Tech Stack

### 핵심 기능
* **🖼️ 실시간 이미지 위젯 (Live Image Widget)**
  - 홈 화면에서 최애의 모습을 바로 확인할 수 있는 고해상도 위젯을 제공합니다.
  - **WorkManager**를 통해 배터리 소모를 최소화하면서 설정된 주기에 따라 이미지를 자동으로 갱신합니다.

* **📸 스마트 사진 갤러리 (Photo Management)**
  - AI 디자인 가이드가 적용된 세련된 UI로 아티스트의 사진을 감상하고 관리할 수 있습니다.
  - **Coil** 라이브러리를 활용하여 대용량 이미지도 메모리 부하 없이 부드럽게 로딩합니다.

* **📅 팬덤 맞춤형 디데이 (D-Day Setting)**
  - 컴백일, 데뷔 기념일, 공연 일정 등 소중한 기념일을 위젯에서 바로 확인합니다.
  - **Material 3** 디자인이 적용된 디데이 설정 기능을 통해 위젯의 시각적 요소를 사용자가 커스텀할 수 있습니다.

### 기술 스택
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Async:** Coroutines, Flow
- **Network:** Retrofit2, OkHttp3
- **Image:** Coil (Widget Image Loading)
- **Local DB:** Room
- **Jetpack:** WorkManager, ViewModel, LiveData

---

## 🛠 5. Troubleshooting

### ✅ RemoteViews 메모리 초과 문제 해결
* **Issue:** 고화질 이미지 로딩 시 `TransactionTooLargeException` 발생
* **Solution:** `Coil`의 리사이징 기능을 활용해 위젯 크기에 맞게 비트맵을 최적화(Downsampling)하고, `RGB_565` 포맷을 사용하여 메모리 점유율을 50% 절감했습니다.

### ✅ 백그라운드 업데이트 지연 해결
* **Issue:** Doze 모드 등 OS 정책으로 인한 위젯 업데이트 누락
* **Solution:** `WorkManager`에 네트워크 연결 상태 제약 조건을 설정하여, 최적의 타이밍에 데이터가 갱신되도록 로직을 고도화했습니다.

---

## 📅 6. Future Plans
* **Jetpack Glance:** 위젯 코드를 Compose 기반의 Glance로 마이그레이션 진행 예정
* **FCM Push:** 실시간 사진 업로드 알림 기능 추가
* **Interactive Widget:** 위젯 내에서 좋아요 및 간단한 메모 기능 구현

---

## 🧑‍💻 Author
* **Name:** [본인 이름]
* **Contact:** [이메일 주소]
* **GitHub:** [@본인 계정명](https://github.com/본인계정)
