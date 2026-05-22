This is a Kotlin Multiplatform project targeting Android, iOS.

* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Development Environment Setup (Nix + direnv)

This project uses [Nix](https://nixos.org/) with Flakes for robust and reproducible tool dependency management. This ensures every developer uses the exact same tool versions without manual setup:
- **Git**
- **JDK 17** (Temurin/OpenJDK)
- **Kotlin CLI**
- **Gradle 9.5.1** (custom unwrapped build)

#### 🚀 Quick Start (Automated via `direnv`)

We highly recommend using [direnv](https://direnv.net/) to automatically load and unload the development environment when you enter the project directory:

1. **Install Nix** with Flakes enabled (or use [Determinate Nix Installer](https://github.com/DeterminateSystems/nix-installer)).
2. **Install direnv** and hook it into your shell configuration.
3. Allow `direnv` in the project root:
   ```bash
   direnv allow
   ```
   *The shell will automatically download and cache all toolchains on the first enter.*

#### 🛠️ Manual Activation

If you prefer not to use `direnv`, you can drop into the configured shell environment manually at any time:

```bash
nix develop
```

This will start a subshell with `JAVA_HOME`, `gradle`, `kotlin`, and `git` pre-configured and ready to go.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `gradlew :androidApp:assembleDebug`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `gradle :shared:testAndroidHostTest`
- iOS tests: `gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…