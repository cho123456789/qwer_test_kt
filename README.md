<div align="center">
  <h1>📸 리센느 포토 위젯</h1>
  <p><strong>향기로 다시(RE) 장면(SCENE)을 떠올리는 리센느의 순간을 홈 화면에 담다</strong></p>
</div>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
</p>

리센느 멤버 사진을 선택하고, 원하는 사진을 Android 배경화면 또는 홈 화면 위젯으로 등록할 수 있는 팬 프로젝트입니다.

해당 프로젝트 소개 : https://idol-made-info.duringkara.workers.dev/#features

## 멤버 프로필 음성 파일

각 멤버 프로필을 누를 때 재생할 MP3는 `app/src/main/res/raw/`에 다음 파일명으로 넣습니다.

- `woni.mp3`
- `liv.mp3`
- `mei.mp3`
- `jena.mp3`
- `minami.mp3`

## 🛠 기술 스택

| 구분 | 기술 |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | Hilt, ViewModel, Repository / UseCase |
| Async | Coroutines, Flow |
| Widget | GlanceAppWidget, GlanceAppWidgetReceiver |
| Image | Coil |
| Data | Supabase, SharedPreferences / DataStore |
| Security | ProGuard/R8 |

## 🔧 구현 포인트

- **랜덤 사진 선택:** 멤버를 선택할 때마다 해당 멤버의 원격 이미지 목록에서 무작위 사진을 표시합니다.
- **배경화면 연동:** 이미지를 캐시에 저장한 뒤 `FileProvider` URI와 `ACTION_SET_WALLPAPER` 인텐트로 시스템 배경화면 설정 화면을 엽니다.
- **위젯별 상태 저장:** 위젯 ID를 기준으로 사진 URL·위젯 유형·D-Day 설정·시계 위치를 저장합니다.
- **Glance 기반 위젯:** 포토·D-Day·시계 위젯을 `GlanceAppWidget`으로 구성하고, 저장된 상태를 읽어 각 위젯을 갱신합니다.

### 멤버별 개인화 기능

- 멤버를 선택하면 해당 멤버의 사진 목록에서 랜덤 이미지를 제공합니다.
- 멤버별 음성을 재생해 선택한 멤버에 따른 상호작용을 제공합니다.
- 현재 선택된 멤버를 프로필 배경색 등으로 구분해 상태를 직관적으로 보여줍니다.

### 위젯별 독립 설정

- 위젯 ID를 기준으로 배경 이미지, 글자 위치, 글자 크기, 글자 색상을 개별 저장합니다.
- 여러 위젯을 추가해도 각 위젯이 서로 다른 설정을 유지할 수 있습니다.

## Coil 이미지 처리 방식

이 프로젝트는 원격 이미지 URL을 화면과 위젯에서 사용하기 위해 Coil을 사용합니다.

### Compose 화면

- `AsyncImage`: 배경화면과 상세 이미지처럼 단순히 이미지를 표시할 때 사용합니다.
- `SubcomposeAsyncImage`: 프로필 이미지처럼 로딩 및 오류 상태를 별도 UI로 표시할 때 사용합니다.
- `ImageRequest`: `crossfade(true)`로 이미지 전환 효과를 적용하고 요청 옵션을 관리합니다.

### Coil API별 활용도

- `AsyncImage`: 일반적인 이미지 표시를 담당합니다. 이미지 URL을 전달하면 Coil이 이미지를 로드하고 Compose 화면에 표시합니다.
- `SubcomposeAsyncImage`: 로딩 중 또는 이미지 로딩 실패 시 별도의 Compose UI를 표시할 때 사용합니다. 이 프로젝트에서는 프로필 이미지와 메인 멤버 사진의 로딩 인디케이터 및 오류 화면에 사용합니다.
- `ImageLoader`: 위젯이나 배경화면 설정처럼 이미지를 `Bitmap`으로 직접 처리해야 할 때 사용합니다. `ImageLoader.execute()`로 이미지를 로드한 뒤 `BitmapDrawable`에서 Bitmap을 추출합니다.

```kotlin
SubcomposeAsyncImage(
    model = ImageRequest.Builder(context)
        .data(imageUrl)
        .crossfade(true)
        .build(),
    contentDescription = null,
    loading = { CircularProgressIndicator() },
    error = { /* 오류 UI */ }
)
```

### 위젯 이미지

Glance 위젯에서는 `context.imageLoader.execute(request)`로 이미지를 로드합니다. 위젯과 파일 저장에 사용할 수 있는 소프트웨어 `Bitmap`을 얻기 위해 `allowHardware(false)`를 설정하고, 필요한 크기를 `size(1080, 1920)`으로 제한합니다. 변환한 Bitmap은 위젯별 캐시 파일로 저장합니다.

```kotlin
val request = ImageRequest.Builder(context)
    .data(imageUrl)
    .allowHardware(false)
    .size(1080, 1920)
    .build()

val result = context.imageLoader.execute(request)
val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
```

Coil을 사용하면 네트워크 통신, 이미지 디코딩, 캐시 처리를 직접 구현하지 않아도 됩니다. 화면에서는 로딩·오류 상태를 쉽게 표현할 수 있고, 위젯에서는 필요한 크기의 Bitmap만 생성해 메모리 사용량을 줄일 수 있습니다.

### Coil 사용 이점

- **비동기 이미지 로딩**: 이미지 다운로드가 UI 스레드를 막지 않아 화면이 멈추는 현상을 줄입니다.
- **캐시 활용**: 이미 불러온 이미지는 메모리·디스크 캐시를 활용해 반복 로딩 시 네트워크 요청과 대기 시간을 줄일 수 있습니다.
- **필요한 크기로 디코딩**: 위젯 이미지 요청에 표시 영역에 맞는 크기를 지정해 불필요한 메모리 사용을 줄입니다.
- **Compose와 자연스러운 연동**: `AsyncImage`와 `SubcomposeAsyncImage`을 사용해 Compose 코드 안에서 이미지 로딩과 상태 UI를 관리할 수 있습니다.
- **화면과 위젯에서 동일한 처리**: Compose 화면에서는 이미지를 표시하고, 위젯에서는 같은 `ImageLoader`로 Bitmap을 생성해 일관된 이미지 처리 흐름을 유지합니다.
- **부드러운 화면 전환**: `crossfade(true)`를 통해 이미지가 갑자기 바뀌는 현상을 줄이고 자연스러운 전환 효과를 제공합니다.

단, 최초 이미지 로딩 속도는 네트워크 상태, 서버 응답 시간, 원본 이미지 크기에 영향을 받습니다. Coil은 특히 반복 로딩과 이미지 처리 과정의 효율을 높이는 역할을 합니다.

## Glance 위젯 구성

이 프로젝트의 홈 화면 위젯은 `GlanceAppWidget`과 `GlanceAppWidgetReceiver`를 역할별로 분리해 구성합니다.

### GlanceAppWidget

`GlanceAppWidget`을 상속한 Provider는 위젯의 실제 화면과 상태를 정의합니다. `provideGlance()`에서 저장된 `Preferences`를 읽고, 이미지 경로·텍스트 위치·글자 색상·D-Day 설정 등을 바탕으로 Glance UI를 구성합니다.

현재 다음 세 가지 Provider가 있습니다.

| Provider | 역할 |
| --- | --- |
| `PhotoWidgetProvider` | 선택한 사진을 배경으로 표시하고 앱을 실행 |
| `GoWatchWidgetProvider` | 배경 이미지 위에 시간·날짜·시간대별 아이콘 표시 |
| `GoDdayWidgetProvider` | 배경 이미지 위에 D-Day 또는 D-Day 제목과 날짜 표시 |

각 Provider는 `PreferencesGlanceStateDefinition`을 사용해 위젯별 상태를 읽습니다. 저장된 이미지 경로를 `BitmapFactory`로 Bitmap으로 변환한 뒤 Glance의 `ImageProvider`로 배경 이미지를 표시합니다.

### GlanceAppWidgetReceiver

`GlanceAppWidgetReceiver`를 상속한 Receiver는 Android 시스템과 위젯 Provider를 연결하는 진입점입니다. 위젯 추가·갱신 이벤트를 받고, Coil로 원격 이미지를 Bitmap으로 다운로드한 뒤 위젯별 캐시 파일에 저장합니다. 이후 저장된 상태를 갱신하고 Provider를 다시 실행해 최신 화면을 반영합니다.

- `PhotoWidgetReceiver`: 사진 위젯의 이미지 다운로드와 위젯 갱신
- `GoWatchWidgetReceiver`: 시계 위젯의 이미지 다운로드, 주기적 갱신, 새로고침 액션 처리
- `GoDdayWidgetReceiver`: D-Day 위젯의 이미지 다운로드와 D-Day 상태 갱신

정리하면 Provider는 **무엇을 그릴지** 담당하고, Receiver는 **언제 위젯을 갱신하고 필요한 데이터를 준비할지** 담당합니다.

## 프로젝트 구성도

```text
app/src/main/java/com/example/qwer_test_kt/
├─ data/
│  ├─ model/              # 외부 데이터 모델
│  ├─ source/              # Supabase·원격 데이터 소스
│  ├─ repository/          # Repository 구현체
│  └─ RepositoryModule.kt  # Repository 의존성 주입
├─ domin/
│  ├─ model/               # 앱의 도메인 모델
│  ├─ repository/          # Repository 인터페이스
│  └─ usecase/             # 앱의 주요 비즈니스 로직
├─ presentation/
│  └─ *ViewModel.kt         # 화면 상태와 사용자 이벤트 처리
├─ gomin/
│  ├─ view/                # 주요 Compose 화면
│  ├─ wiget/               # Glance 위젯 Provider·Receiver
│  │  ├─ dialog/           # 위젯 설정 및 이미지 상세 다이얼로그
│  │  └─ screen/           # 위젯 관련 화면
│  └─ util/                # 위젯 설정값 저장·조회 유틸리티
├─ NavigationGraph.kt      # 화면 이동 구성
└─ MainActivity.kt         # 앱 진입점
```

### 데이터 흐름

```text
Supabase / assets/member.json
            ↓
      DataSource
            ↓
   Repository 구현체
            ↓
         UseCase
            ↓
       ViewModel
            ↓
      Compose 화면
            ↓
   위젯 설정 및 갱신
```

`data` 계층은 외부 데이터 접근을 담당하고, `domin` 계층은 앱의 핵심 모델과 비즈니스 규칙을 담당합니다. `presentation` 계층은 ViewModel을 통해 화면 상태를 관리하며, `gomin/wiget`은 Glance 위젯과 위젯 설정 화면을 담당합니다.

## Firebase 기반 운영 및 보안

이 프로젝트에서 멤버·사진과 같은 핵심 원격 데이터는 Supabase에서 관리하고, Firebase는 앱 운영과 보안을 보조하는 역할로 사용합니다.

- **Crashlytics**: 앱 충돌과 예외 정보를 수집해 실제 사용자 환경의 문제 원인을 추적합니다. Crashlytics는 `google-services.json`이 설정된 빌드에서 자동으로 초기화됩니다.
- **Analytics**: 앱 설치와 최초 실행 같은 기본 이벤트를 수집하고, 주요 화면 및 기능의 사용 현황을 분석할 수 있도록 Firebase Analytics를 초기화합니다.
- **App Check**: 앱에서 발생하는 Firebase 요청이 정상적인 앱과 환경에서 온 것인지 검증합니다. Debug 빌드에서는 Debug Provider를 사용하고, Release 빌드에서는 Play Integrity Provider를 사용합니다.

따라서 데이터 저장·조회는 Supabase Repository가 담당하고, Firebase는 오류 모니터링·사용 현황 분석·앱 위변조 및 비정상 요청 방어를 담당하는 구조입니다.

## ⚖️ 안내

본 프로젝트는 비상업적 팬 프로젝트입니다. 사용된 리센느 관련 이미지와 상표의 권리는 각 권리자에게 있으며, 권리자의 요청이 있을 경우 관련 콘텐츠를 조정하거나 비공개로 전환합니다.

## 🧑‍💻 Author

- Contact: [dkdkdodo123@gmail.com](mailto:dkdkdodo123@gmail.com)
- GitHub: [@cho123456789](https://github.com/cho123456789)
