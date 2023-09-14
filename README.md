<img width="500px" src="art/logo.png" alt="logo"/>
<br />

![Maven Central](https://img.shields.io/maven-central/v/dev.jordond.kmpalette/kmpalette)
[![Kotlin](https://img.shields.io/badge/kotlin-v1.9.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Build](https://github.com/jordond/kmpalette/actions/workflows/ci.yml/badge.svg)](https://github.com/jordond/kmpalette/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/jordond/kmPalette)](https://opensource.org/license/mit/)

[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.5.1-blue)](https://github.com/JetBrains/compose-multiplatform)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-DB413D.svg?style=flat)

A multiplatform library for generating color palettes. This is a port of
the [`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette)
library. And when/if that library ever goes multiplatform, this library will be archived.

<img width="300px" src="art/demo.gif" />

## TODO

- base64 support
- Documentation
- Update readme on publish (github action)
- add linter (spotless/detekt)
- Logo
- demo
  - Artwork
  - Screen for loading from network and camera

## Table of Contents

- [Platforms](#platforms)
- [Inspiration](#inspiration)
- [Setup](#setup)
    - [Multiplatform](#multiplatform)
    - [Single Platform](#single-platform)
    - [Version Catalog](#version-catalog)
- [Usage](#usage)
- [Demo](#demo)
- [License](#license)
    - [Changes from original source](#changes-from-original-source)

## Platforms

This library is written for Compose Multiplatform, and can be used on the following platforms:

- Android
- iOS
- JVM (Desktop)

## Inspiration

## Setup

You can add this library to your project using Gradle.

### Multiplatform

To add to a multiplatform project, add the dependency to the common source-set:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("dev.jordond.kmpalette:kmpalette-core:1.2.5")
            }
        }
    }
}
```

### Single Platform

For an Android only project, add the dependency to app level `build.gradle.kts`:

```kotlin
dependencies {
    implementation("dev.jordond.kmpalette:kmpalette-core:1.2.5")
}
```

### Version Catalog

In `libs.versions.toml`:

```toml
[versions]
kmpalette = "1.2.5"

[libraries]
kmpalette = { module = "dev.jordond.kmpalette:kmpalette-core", version.ref = "kmpalette" }
```

In `build.gradle.kts`:

```kotlin
dependencies {
    implementation(libs.kmpalette)
}
```

## Usage

## Demo

A demo app is available in the `demo` directory. It is a Compose Multiplatform app that runs on
Android, iOS and Desktop.

**Note:** While the demo does build a Browser version, it doesn't seem to work. However I don't know
if that is the fault of this library or the Compose Multiplatform library. Therefore I haven't
marked Javascript as supported.

See the [README](demo/README.md) for more information.

## License

The module `material-color-utilities` is licensed under the Apache License, Version 2.0. See
their [LICENSE](material-color-utilities/src/commonMain/kotlin/com/kmpalette/LICENSE) and their
repository [here](https://github.com/material-foundation/material-color-utilities) for more
information.

### Changes from original source

- Convert Java code to Kotlin
- Convert library to Kotlin Multiplatform

For the remaining code see [LICENSE](LICENSE) for more information.
