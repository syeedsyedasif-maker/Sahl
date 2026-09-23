# Sahl

**This is a zero to hero Arabic & Quran learning open source android app.**

Built with Kotlin and Jetpack Compose.

| | |
|---|---|
| Application ID | `com.sahl.app` (debug builds install as `com.sahl.app.debug`) |
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| minSdk / targetSdk / compileSdk | 24 / 37 / 37 — see [Device support](#device-support) |
| Build | Gradle (Kotlin DSL) + version catalog at [`gradle/libs.versions.toml`](gradle/libs.versions.toml) |
| Licence | [AGPL-3.0-or-later](LICENSE) — copyleft; see the Licence section |

## Getting started

You need **Android Studio** (which bundles the Android SDK and a compatible JDK) or a standalone
Android SDK with `ANDROID_HOME` set.

1. Open this folder in Android Studio — it will sync Gradle and write `local.properties` with your
   SDK path. That file is deliberately gitignored; it is machine-specific.
2. Run the `app` configuration on an emulator or a connected device.

From the command line, once the SDK is installed:

```bash
./gradlew assembleDebug        # build the debug APK
./gradlew test                 # JVM unit tests
./gradlew connectedAndroidTest # instrumented tests, needs a running device
./gradlew lint                 # Android Lint
```

## Layout

```
app/
  src/main/java/com/sahl/app/   MainActivity, SahlApp (root composable + main navigation)
             navigation/        the Learn / Quran / Profile tabs
             ui/learn/, ui/quran/, ui/profile/   one package per tab
             ui/components/     composables shared across screens
             ui/theme/          Material 3 color, type, theme
  src/main/res/                 strings, themes, launcher and tab icons
  src/test/                     JVM unit tests
  src/androidTest/              instrumented / Compose UI tests
gradle/libs.versions.toml       single source of truth for dependency versions
```

## Device support

Decided 2026-09-22, using Google's Play Store API-level distribution data.

| | Decision |
|---|---|
| Minimum Android | Android 7.0, API 24 — reaches 99.1% of devices globally |
| Target Android | Android 17, API 37 |
| Screens | Phones first; tablets and foldables get layouts designed for them |
| Orientation | Portrait and landscape, on every device |
| Interface language | English at launch, translation-ready |
| Out of scope | Wear OS, TV, Android Auto, XR |

### Why

- **API 24.** Sahl's audience — Arabic and Quran learners worldwide — is concentrated in regions
  where older phones are more common than the global average, so reach matters more than usual.
  With AndroidX and Compose, supporting old versions costs almost nothing extra. The lowest
  possible value is 23, set by the AndroidX libraries.
- **Tablets and foldables.** For apps targeting API 37, Android ignores orientation locks,
  `resizeableActivity="false"` and aspect-ratio limits on screens at least 600dp across in their
  narrower direction, so Sahl runs on large screens regardless. Reading the Quran and children's
  learning are natural tablet uses.
- **Both orientations.** Short, wide windows happen anyway — on tablets, and in split-screen on
  phones — so locking phones to portrait would save little.
- **English, translation-ready.** A learner starting from zero can't read Arabic yet, so the
  interface must be in their language while lesson content is Arabic.

### Rules this creates for the code

- Layouts adapt to the **window size**, never to device type or orientation.
- Use `start`/`end`, never `left`/`right`, so layouts flip correctly if a right-to-left interface
  language (Arabic, Urdu, Persian) is added.
- Every piece of user-visible text lives in `res/values/strings.xml`. Tests read it from there too.
- UI state must survive the Activity being recreated: rotation, resizing, dark-mode or language
  changes, and the process being killed in the background.
- Quran and lesson text use a bundled font rather than the system's (roadmap item 3), so it looks
  the same on every phone.

### Test devices

| Device | API | Why | Status |
|---|---|---|---|
| Pixel 10a emulator | 37 | Everyday development | Set up |
| Pixel Tablet emulator | 37 | Large-screen layouts | To create |
| Small, low-memory phone emulator | 24 | Oldest supported Android: text rendering and performance | To create |
| Pixel 10 Pro XL emulator | 37 | Large phone | Set up |
| A physical non-Pixel phone | any | Manufacturers such as Samsung, Xiaomi and Tecno change parts of Android, including fonts | If available |

## Releases

Releases are built by GitHub Actions ([`release.yml`](.github/workflows/release.yml)) and
published on the [Releases page](https://github.com/syeedsyedasif-maker/Sahl/releases), where
[Obtainium](https://github.com/ImranR98/Obtainium) installs and updates Sahl from. In Obtainium,
add the repository URL; enable **Include prereleases** to also get release candidates.

**Publishing a release.** Push a tag named after the version:

```bash
git tag v0.1.0
git push origin v0.1.0
```

A tag with a hyphen, such as `v0.1.0-rc.1`, is published as a pre-release.

**Version numbers.** The tag sets both the version name and Android's versionCode, which must
grow with every release or phones refuse the update. `versionCodeFor` in
[`app/build.gradle.kts`](app/build.gradle.kts) maps `0.1.0-rc.1` → 10001, `0.1.0` → 10099 and
`0.1.1` → 10199, so a pre-release always sorts before the release it leads up to.

**Signing.** Every release must be signed with the same key, or Android won't install it as an
update. The key never enters the repository: the workflow reads it from four repository
secrets, under *Settings → Secrets and variables → Actions*.

| Secret | Contents |
|---|---|
| `SAHL_KEYSTORE_BASE64` | The keystore file, base64-encoded |
| `SAHL_KEYSTORE_PASSWORD` | The keystore's password |
| `SAHL_KEY_ALIAS` | The key's alias |
| `SAHL_KEY_PASSWORD` | The key's password |

GitHub secrets can be replaced but never read back, so keep your own backups of the keystore
and its password. If the key is lost, people who installed Sahl can't receive updates without
uninstalling first.

To build a signed release locally, set `SAHL_KEYSTORE_PATH` to the keystore file plus the other
three variables above, then run
`./gradlew :app:assembleRelease -Psahl.version=0.1.0 --no-configuration-cache`.

## Roadmap

### 1. Decide which devices to support ✓

Done — see [Device support](#device-support).

### 2. Project foundations

- [ ] Pick an architecture (ViewModel + repository, DI with Hilt or Koin, etc.)
- [x] Main navigation: Learn, Quran and Profile tabs, as a bottom bar or side rail depending on window size
- [ ] Add a navigation library when a tab gets its first sub-screen
- [ ] Decide on local persistence (Room / DataStore / none)
- [x] Release workflow: tag a version, get a signed APK on the Releases page
- [x] Release signing key stored in the repository secrets (see Releases)

### 3. First feature

- [ ] Scope the first lesson — alphabet recognition, letter forms in word position, or short-surah reading
- [ ] Decide Arabic text rendering: font, shaping, and whether diacritics (tashkeel) are shown, toggleable, or drilled
- [ ] Choose the Quran text source and confirm its licence permits redistribution
- [ ] Decide on recitation audio — bundled, streamed, or absent in v1
- [ ] Replace the placeholder launcher icon and theme colors

### 4. Quality and release

- [x] CI: build, lint and unit tests on every push and pull request
- [ ] Add crash reporting / analytics, if wanted
- [ ] Play Console listing, privacy policy, data safety form

## Notes

- Build from the command line with Android Studio's bundled JDK, which differs from the one on
  `PATH`:
  `JAVA_HOME="/c/Program Files/Android/Android Studio/jbr" ./gradlew assembleDebug`
- AGP 9 compiles Kotlin itself — there is no `org.jetbrains.kotlin.android` plugin. Do not re-add it.
- `gradle/gradle-daemon-jvm.properties` pins the Gradle daemon's JVM and is checked in deliberately,
  like the wrapper, so a machine without JDK 25 provisions one automatically.

## Licence

Sahl is licensed under the **GNU Affero General Public License v3.0 or later** — see
[`LICENSE`](LICENSE). SPDX identifier: `AGPL-3.0-or-later`.

In plain terms — this is the intent, not legal advice:

- **Read it, learn from it, run it, share it.** That is the point of the project.
- **If you ship a modified version, publish your source** under the same licence. That covers a
  modified app you distribute *and* a modified backend you run as a network service. That second
  case is the clause the AGPL adds over the plain GPL.
- **You may charge money for it.** What you may not do is take this work closed-source.

On `-or-later`: the FSF recommends it, and it lets the project adopt a future AGPL version if one is
published. Switch the source headers to `AGPL-3.0-only` if you would rather pin to v3 exactly.

The AGPL covers **the code in this repository**. Quran text, translations, recitation audio and
fonts each carry their own separate licences and must be checked individually before bundling —
tracked as roadmap item 3.

The tab icons are [Material Symbols](https://github.com/google/material-design-icons) by Google,
under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0), which is compatible
with the AGPL. Each icon file notes its source and the one change made to it.
