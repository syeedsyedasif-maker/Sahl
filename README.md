# Sahl

**This is a zero to hero Arabic & Quran learning open source android app.**

Built with Kotlin and Jetpack Compose.

| | |
|---|---|
| Application ID | `com.sahl.app` (debug builds install as `com.sahl.app.debug`) |
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| minSdk / targetSdk / compileSdk | 24 / 37 / 37 — **provisional, see roadmap item 1** |
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
  src/main/java/com/sahl/app/   MainActivity + Compose UI
             ui/theme/          Material 3 color, type, theme
  src/main/res/                 strings, themes, launcher icon
  src/test/                     JVM unit tests
  src/androidTest/              instrumented / Compose UI tests
gradle/libs.versions.toml       single source of truth for dependency versions
```

## Roadmap

### 1. Decide which devices to support

Everything downstream — layout work, testing matrix, which APIs are safe to call — hangs off this,
so settle it before writing feature code. The values currently in
[`app/build.gradle.kts`](app/build.gradle.kts) are placeholders chosen to compile, not decisions.

Open questions:

- **Minimum Android version.** `minSdk` is set to 24 (Android 7.0, ~98% of active devices). Raising
  it to 26 or 28 removes a lot of compatibility branching; lowering it is rarely worth it. Check the
  distribution numbers in Android Studio (*Help → New Project → minSdk → Help me choose*).
- **Form factors.** Phone only, or also tablets / foldables / Chromebooks? Tablet and foldable
  support means adaptive layouts (window size classes) from day one rather than a painful retrofit.
- **Orientation.** Portrait-only is a real constraint to state up front; note that Android 16 ignores
  orientation locks on large screens.
- **Wear OS, TV, Auto.** Almost certainly out of scope, but say so explicitly so it stays out.
- **Target market.** Affects which locales, RTL (the manifest already sets `supportsRtl="true"`),
  device price tier, and therefore performance budget.
- **Test matrix.** Which two or three physical devices are the ones that must always work.

Record the answers in this README and update `minSdk` / `targetSdk` to match.

### 2. Project foundations

- [ ] Pick an architecture (ViewModel + repository, DI with Hilt or Koin, etc.)
- [ ] Add navigation once there is more than one screen
- [ ] Decide on local persistence (Room / DataStore / none)
- [ ] Set up the release signing config and keep the keystore out of git

### 3. First feature

- [ ] Scope the first lesson — alphabet recognition, letter forms in word position, or short-surah reading
- [ ] Decide Arabic text rendering: font, shaping, and whether diacritics (tashkeel) are shown, toggleable, or drilled
- [ ] Choose the Quran text source and confirm its licence permits redistribution
- [ ] Decide on recitation audio — bundled, streamed, or absent in v1
- [ ] Replace the placeholder launcher icon and theme colors

### 4. Quality and release

- [ ] Wire up CI (a GitHub Actions workflow is scaffolded at `.github/workflows/android.yml`)
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
