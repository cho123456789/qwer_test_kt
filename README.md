<div align="center">
<h1>📸 QWER Photo Widget</h1>
</div>

<p align="center">
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EB%8B%A8%EC%B2%B4%EC%82%AC%EC%A7%84/%EB%8B%A8%EC%B2%B4%EC%82%AC%EC%A7%842.jpg?raw=true" width="70%">
  <br>
  <br>
  <strong>"일상의 코드(Chord)에 QWER의 색을 입히다"</strong>
</p>

<div align="center">
 QWER을 향한 팬심을 안드로이드의 최신 기술로 구현한 커스텀 위젯 서비스입니다. <br> 단순히 사진을 보여주는 것을 넘어, 팬과 아티스트를 잇는 가장 가까운 연결고리를 홈 화면에 제공합니다.
</div>
<br>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/AI Assistant-Firebender-FF6C37?style=flat-square&logo=openai&logoColor=white"/>
  <img src="https://img.shields.io/badge/Security-ProGuard-red?style=flat-square&logo=security&logoColor=white"/>
</p>

## 🌟 1. Project Inspiration & Benchmarking

### 💌 From Fan's Heart (프로젝트 동기)
> **"갤러리를 넘어 당신의 바탕화면으로, QWER과 함께하는 일상"**

 좋아하는 아티스트의 사진을 찾아보고 저장하는 즐거움은 팬이라면 누구나 공감할 것입니다. 저는 여기서 한 걸음 더 나아가고자 했습니다.
 
 번거롭게 앱을 켜지 않아도, 스마트폰을 켜는 그 짧은 찰나에 **QWER의 에너지**를 마주할 수 있다면 어떨까요?
 최애의 모습을 단지 데이터로 저장하는 것이 아니라, **Android 홈 화면**이라는 가장 가까운 공간에 생동감 있게 배치하고자 하는 진심에서 이 프로젝트를 시작하였습니다!
 
### 🔍 Market Analysis & Benchmarking
> **"최고의 무대를 위해 다른 아티스트의 공연을 분석하는 아티스트처럼"**
* 더 완성도 높은 **조화로운 합**을 구현하기 위해 시중에 출시된 다양한 K-POP 및 위젯 앱들을 연구했습니다.

#### 📱 1) Case Study: [QWER 포토위젯] iOS 앱 분석
<div align="center">
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EA%B8%B0%ED%83%80/IMG_0037.PNG?raw=true" width="160" />
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EA%B8%B0%ED%83%80/IMG_0038.PNG?raw=true" width="160" />
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EA%B8%B0%ED%83%80/IMG_0039.PNG?raw=true" width="160" />
</div>

#### 🎯 핵심 연구 과제: 위젯 설정 인터페이스 및 기능 
> **위젯에 대한 기능** 및  **UX 프로세스**를 집중 분석했습니다.

* **💡 기능적 영감 (Inspiration)**
  * QWER의 각 멤버별 상징색이 가장 돋보일 수 있는 시각적 표현 기법 표현에 대해서 영감을 얻었습니다.
  * 위젯 내에서 특정 정보(디데이, 한마디 등)가 조화롭게 배치되는 다양한 레이아웃 구현 방식에 대한 영감을 받았습니다.

* **✨ 차별화 포인트 (Differentiation)**
  * **Freestyle Positioning** : 사용자가 직접 글자의 위치를 자유롭게 배치할 수 있는 커스텀 기능을 추가하여,
    <br> 어떤 사진에서도 완벽한 시각적 조화를 이룰 수 있도록 설계했습니다.
    
  * **Album Concept Random Pick:** 단순히 사진 한 장을 등록하는 방식을 넘어,
    <br> QWER의 각 앨범 컨셉(포토카드 느낌)에 맞춘 랜덤 추출 방식을 도입했습니다.


#### 📱 2) Case Study: [QWER 배경화면] Android 앱 분석
<div align="center">
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EA%B8%B0%ED%83%80/Screenshot_20251224_110257_QWER%20Wallpapers.jpg?raw=true" width="160" />
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EA%B8%B0%ED%83%80/Screenshot_20251224_110315_QWER%20Wallpapers.jpg?raw=true" width="160" />
  <img src="https://github.com/cho123456789/qwer_data/blob/main/%EA%B8%B0%ED%83%80/Screenshot_20251224_120720_QWER%20Wallpapers.jpg?raw=true" width="160" />
</div>

#### 🎯 핵심 연구 과제: 배경화면 설정 인터페이스
> 아티스트 사진 탐색부터 실제 디바이스 적용까지의 **UX 프로세스**를 집중 분석했습니다.

* **💡 기능적 영감 (Inspiration)**
  * 기존 앱의 직관적인 설정 방식에서 핵심 아이디어를 얻었습니다.
  * 복잡한 과정을 생략하고, **단 한 번의 터치**로 최애의 모습을 적용할 수 있는서비스에 녹여냈습니다.

* **✨ 차별화 포인트 (Differentiation)**
  * **Dual-Registration:** 홈 화면과 잠금 화면을 개별적으로 설정해야 하는 번거로움을 해결하여 **동시 등록 편의성**을 제공합니다.
  * **Personalized Custom:** 사용자의 취향에 맞춰 배경화면을 직접 편집할 수 있는 **고도화된 커스텀 기능**을 추가했습니다
  
---

## 📅 2. Project Timeline & Passion
### ⏳ Project Timeline (프로젝트 개발 기간)

>**"하나의 곡을 완성하듯, 기술과 감성의 조화로운 합(Harmony)을 찾아서"**

 * **전체 기간:** 2025.07 ~ 2025.12 (약 22주)

밴드 QWER이 완벽한 한 곡을 위해 수천 번의 합주를 반복하듯, 저 또한 위젯과 앱 사이의 **조화로운 합**을 찾기 위해 집요하게 몰입했습니다.<br>
단순히 돌아가는 코드를 만드는 것에 그치지 않고, 기술과 팬심이 하나의 앙상블을 이룰 때까지 **수십 번의 빌드업과 갈아엎기를 주저하지 않았습니다.** <br>

**시행착오의 기록:**
* **Architecture Re-design:** MVVM 패턴보다 **jectpack Compose** 와 **Clean Architecture** 를 통한 관심사 분리 및 의존도를 낮추기 위한 노력 
* **UI/UX Refinement:**  **QWER** 컨셉과 **Photo Widget** 이랑 기능의 컨셉에 맞는 최적의 조화를 위해 수십 번의 레이아웃 갈아엎기를 반복
* **Performance Tuning:** 위젯에 사진 및 데이터 전달을 위한 **Coli 활용한 bitmap 변환** 및 **Glance DataStore** 데이터 전달 방식 구현 
---

## 🏗 3. Project Structure & Timeline

### System Architecture
* **UI Layer:** `Jetpack Compose` 기반의 선언형 UI
* **Domain/Data Layer:** `Clean Architecture` 비즈니스 로직과 UI 분리
* **Widget Engine:** `GlanceAppWidget` & `GlanceAppWidgetReceiver`를 통한  위젯 데이터 연동 및 UI 표시
* **Networking:** `Data Store` & `Coil`을 이용한 이미지 데이터 변환 및 저장 
* **DataBase:** `Supabase` `SQL Database` 를 통하여 사용자 데이터 관리

### Development Timeline
1.  **Phase 1 (기획):** 아이돌 컨셉 분석 및 디자인 분석
2.  **Phase 2 (개발):** 위젯 핵심 로직 및 DB 연동 및 위젯 데이터 전송 수정
3.  **Phase 3 (고도화):** 위젯 요소 크기 및 글자 위치 변경 추가
4.  **Phase 4 (안정화):** 디바이스 UI 테스트 

---

## 🚀 4.Key Features (핵심 기능)

| 기능 화면 | 상세 설명 |
| :--- | :--- |
| <img src="https://github.com/user-attachments/assets/57f91c7c-9c4b-4d96-870c-befe4cc9ca5f" width="260" /> | **📸 위젯 홈 화면 (Photo Management)**<br><br>• **감성적인 UI/UX:** QWER 멤버 소개와 더불어 각 앨범 컨셉 버튼을 제공합니다. 버튼 클릭 시 **랜덤 이미지 추출** 방식을 도입하여 사용자 접근성과 재미 요소를 극대화했습니다.<br><br> • **메모리 최적화:** `Coil` 라이브러리의 **자동 캐싱 기능**을 활용하여 캐싱된 이미지 재활용 및 BitMap 데이터로 직접 변환 |
| <img src="https://github.com/user-attachments/assets/090fc2f3-03e6-4c7a-9df1-2f6d92f5ba3b" width="260" width="260" /> | **🖼️ 이미지 배경화면 등록 (Live Image & Custom)**<br><br>• **사용자 정의 배경화면 커스텀:** 단순히 이미지를 적용하는 것을 넘어, 사용자가 원하는 구도와 위치에 맞춰 최적화된 배경화면을 구성할 수 있습니다.<br><br>• **One-Stop 시스템:** 단 한 번의 설정으로 **홈 화면과 잠금 화면을 동시에 등록**할 수 있는 편의성을 제공하며, 디바이스의 해상도에 맞춰 이미지가 자연스럽게 배치되도록 인터페이스를 설계했습니다.|
| <img src="https://github.com/user-attachments/assets/ef251004-0fa4-4f54-bf83-2d24684f7fac" width="260" /> | **📅 맞춤형 위젯 커스텀 (Fandom D-Day & Edit)**<br><br>• **UI/UX Personalization:** 정형화된 위젯에서 벗어나, 사용자가 선택한 사진 위에 **글자 크기, 색상, 위치를 직접 지정**할 수 있는 자유로운 커스텀 환경을 제공합니다.<br><br> • **시각적 접근성 강화:** 배경 이미지의 톤에 맞춰 텍스트 가독성을 확보할 수 있도록 설계하여, 팬들이 자신만의 감성으로 가장 '조화로운' 위젯을 완성할 수 있게 구현했습니다. |
| <img src="https://github.com/user-attachments/assets/6f056ec2-f10a-44fe-b7d8-693952fb7dd1" width="260" /> | **📅 다채로운 위젯 라인업 (Multi-Widget System)**<br><br>• **다양한 위젯 모드 지원:** **기본 포토 위젯**부터 팬덤 필수 기능인 **디데이 설정 위젯**, 실시간성을 강조한 **시계 위젯**까지 폭넓은 옵션을 제공합니다.<br><br>• **인터렉티브 새로고침:** 시계 위젯의 경우, 사용자가 위젯을 클릭하는 즉시 최신 데이터로 업데이트되는 **실시간 동기화 인터렉션**을 구현하여 편의성과 정확성을 높였습니다. |


---
### 기술 스택
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Async:** Coroutines, Flow
- **Image:** Coil (Widget Image Loading)
- **Jetpack:** GlanceAppWidget, GlanceAppWidgetReceiver
- **Database:** Supabase (PostgreSQL)
- **Security:** ProGuard/R8 난독화

---

## 🔐 5. Security Implementation

### ProGuard/R8 난독화

앱의 민감한 데이터(Supabase API 키)를 보호하기 위해 ProGuard/R8 난독화를 적용했습니다.

#### 🛡️ 보안 레이어

|     레벨      | 기술                 | 설명                                   |
|:-----------:|:-------------------|:-------------------------------------|
| **Level 1** | `local.properties` | 개발 환경에서 키를 Git에서 제외하여 소스 코드 유출 방지    |
| **Level 2** | ProGuard/R8 난독화    | Release 빌드 시 코드 및 BuildConfig 난독화 적용 |

#### 🔒 주요 보안 기능

* **ProGuard/R8 최적화**
  * Release 빌드에서 `isMinifyEnabled = true` 적용
  * BuildConfig 필드명 및 클래스명 난독화 (`a`, `b`, `c` 등으로 변환)
  * 리소스 축소(`isShrinkResources = true`)로 APK 최적화
  * 패키지 리패키징 및 최적화 5회 반복

* **난독화 효과**
  ```java
  // Before: 디컴파일 시 즉시 노출
  public class BuildConfig {
      public static final String SUPABASE_URL = "https://...";
      public static final String SUPABASE_KEY = "eyJ...";
  }
  
  // After: 필드명 난독화로 찾기 어려움
  public class a {
      public static final String a = "https://...";
      public static final String b = "eyJ...";
  }
  ```
---

## 🛠 6. Troubleshooting

### ✅ Widget 컴포넌트의 상태 관리 제약

* **Issue:** 위젯은 Activity와 달리 별도의 프로세스(SystemUI)에서 실행되므로, `ViewModel`을 통한 직접적인 상태 공유 및 실시간 데이터 바인딩이 불가능함.

* **Solution:** * `Jetpack DataStore (Preferences)`를 도입하여 경량 데이터를 영속적으로 저장.
  * 위젯 갱신 시점에 `DataStore`에서 최신 상태를 로드하여 프로세스 간 데이터 일관성을 확보했습니다.

### ✅ RemoteViews 메모리 초과 및 고화질 이미지 처리

* **Issue:** 고화질 아티스트 이미지 사용 시 위젯의 메모리 할당량 한계(약 1-2MB)를 초과하여 `RemoteViews` 업데이트 실패 및 성능 저하 발생.

* **Solution:** * **Coil 최적화:** `Coil` 라이브러리의 리사이징 및 **Downsampling** 기능을 활용하여, 위젯의 실제 크기에 최적화된 비트맵만 메모리에 적재.
  * **메모리 가드:** 위젯 이미지 전송 전 비트맵 크기를 체크하고 최적화하여 안정적인 렌더링을 구현했습니다.

### ✅ AppWidgetProvider의 동적 레이아웃 구현 한계

* **Issue:** 전통적인 XML 방식의 위젯 개발 시, 사용자의 커스텀 설정(텍스트 위치 변경 등)에 따른 동적 레이아웃 업데이트 로직이 복잡해지고 유지보수가 어려움.

* **Solution:** * **Jetpack Glance 도입:** `GlanceAppWidget`을 활용하여 선언형 UI(Compose) 스타일로 위젯을 재설계.
  * 복잡한 XML 연동 없이 Kotlin 코드만으로 레이아웃 상태를 효율적으로 관리하고, 동적인 UI 변경 사항을 직관적으로 반영할 수 있는 구조를 구축했습니다.
---

## 📅 7. Future Plans
* **🗂️ Data Expansion (데이터베이스 고도화)**
  * QWER의 활동에 맞춘 신규 앨범 컨셉 및 고화질 아티스트 사진 데이터를 지속적으로 수집하고 업데이트할 예정입니다.
  * 약 420장의 사진 업데이트 실시 완료 (12/26) 
    
* **🔧 Stability & UX Optimization (안정성 및 UX 최적화)**
  * 실사용자 피드백을 바탕으로 한 예외 처리 및 버그 수정 작업을 진행합니다.
  * 위젯 배치 및 설정 과정의 뎁스(Depth)를 줄여 더욱 직관적인 **UX 접근성**을 확보할 계획입니다.
    
* **🎁 Community Release (팬덤 공유 및 배포)**
  * 베타 테스트 및 서비스 안정화 단계가 완료되는 대로,<br>
  * **QWER 공식 팬카페 및 커뮤니티**를 통해 팬분들에게 무료로 배포하여 소중한 피드백을 나눌 예정입니다.
---
## ⚖️ 8. License & Copyright
* 본 앱에 사용된 모든 아티스트(QWER) 관련 이미지 및 상표권은 **쓰리와이코퍼레이션**에 귀속됩니다.
* 현재 서비스 배포와 관련하여 소속사 측에 정식 문의를 진행 중이며, 결과에 따라 운영 방침이 결정될 예정입니다.
* 비영리 팬 프로젝트로서 저작권자의 요청이 있을 경우 즉시 비공개 전환될 수 있습니다.

## 🧑‍💻 Author
* **Contact:** [dkdkdodo123@gmail.com]
* **GitHub:** [@cho][https://github.com/cho123456789]

