# Sahl

An Android app, built with Kotlin and Jetpack Compose.

| | |
|---|---|
| Application ID | `com.sahl.app` (debug builds install as `com.sahl.app.debug`) |
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| minSdk / targetSdk / compileSdk | 24 / 36 / 36 — **provisional, see roadmap item 1** |
| Build | Gradle (Kotlin DSL) + version catalog at [`gradle/libs.versions.toml`](gradle/libs.versions.toml) |

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

- [ ] Commit the Gradle wrapper JAR so `./gradlew` works on a clean checkout (see below)
- [ ] Pick an architecture (ViewModel + repository, DI with Hilt or Koin, etc.)
- [ ] Add navigation once there is more than one screen
- [ ] Decide on local persistence (Room / DataStore / none)
- [ ] Set up the release signing config and keep the keystore out of git

### 3. First feature

- [ ] Define what Sahl actually does — one sentence, then a first screen
- [ ] Replace the placeholder launcher icon and app theme colors

### 4. Quality and release

- [ ] Wire up CI (a GitHub Actions workflow is scaffolded at `.github/workflows/android.yml`)
- [ ] Add crash reporting / analytics, if wanted
- [ ] Play Console listing, privacy policy, data safety form

## Notes

- The Gradle wrapper JAR (`gradle/wrapper/gradle-wrapper.jar`) is **not** in this repo yet, so
  `./gradlew` will not run until it is added. Android Studio regenerates it on first sync, or run
  `gradle wrapper --gradle-version 8.13` if you have a system Gradle.
- This machine has JDK 25 on `PATH`. AGP builds want JDK 17–21; use the JDK bundled with Android
  Studio (*Settings → Build Tools → Gradle → Gradle JDK*) rather than the system one.
