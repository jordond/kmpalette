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

A Compose Multiplatform library for generating color palettes from images, including the dominant
color. You can use this library in combination
with [MaterialKolor](https://github.com/jordond/materialkolor) to generate dynamic Material
themes based on images.

Supports loading images from several sources, see [sources](#sources).

**Note:** This is a port of
the [`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette)
library. And when/if that library ever goes multiplatform, this library will be archived.

<img width="300px" src="art/ios-demo.gif" />

## TODO

- Documentation
- Update readme on publish (github action)
- add linter (spotless/detekt)
- Logo
- demo
    - Screen for loading from network and camera

## Table of Contents

- [Platforms](#platforms)
- [Inspiration](#inspiration)
- [Setup](#setup)
    - [Version Catalog](#version-catalog)
- [Usage](#usage)
    - [Sources](#sources)
- [Demo](#demo)
- [License](#license)
    - [Changes from original source](#changes-from-original-source)

## Platforms

This library is written for Compose Multiplatform, and can be used on the following platforms:

- Android
- iOS / MacOS
- JVM (Desktop)

## Inspiration

I created this library because I wanted to use the
[`androidx.palette`](https://developer.android.com/jetpack/androidx/releases/palette) library in a
Compose Multiplatform app. But that library is not multiplatform, so I decided to port it.

I also created this library to use in tandem with my dynamic theme generating
library [MaterialKolor](https://github.com/jordond/materialkolor).

## Setup

You can add this library to your project using Gradle. There are several optional extension
libraries, see [sources](#sources).

### Version Catalog

In `libs.versions.toml`:

```toml
[versions]
kmpalette = "1.2.5"

[libraries]
kmpalette-core = { module = "dev.jordond.kmpalette:kmpalette-core", version.ref = "kmpalette" }
# Optional source libraries
kmpalette-extensions-base64 = { module = "dev.jordond.kmpalette:extensions-base64", version.ref = "kmpalette" }
kmpalette-extensions-bytearray = { module = "dev.jordond.kmpalette:extensions-bytearray", version.ref = "kmpalette" }
kmpalette-extensions-libres = { module = "dev.jordond.kmpalette:extensions-libres", version.ref = "kmpalette" }
kmpalette-extensions-network = { module = "dev.jordond.kmpalette:extensions-network", version.ref = "kmpalette" }
kmpalette-extensions-resources = { module = "dev.jordond.kmpalette:extensions-resources", version.ref = "kmpalette" }
```

To add to a multiplatform project, add the dependency to the common source-set:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Core library
                implementation(libs.kmpalette.core)

                // Optional extensions based on your image source
                implementation(libs.kmpalette.extensions.base64)
                implementation(libs.kmpalette.extensions.bytearray)
                implementation(libs.kmpalette.extensions.libres)
                implementation(libs.kmpalette.extensions.network)
                implementation(libs.kmpalette.extensions.resources)
            }
        }
    }
}
```

## Usage

### Sources

## Demo

A demo app is available in the `demo` directory. It is a Compose Multiplatform app that runs on
Android, iOS and Desktop.

See the [README](demo/README.md) for more information.

## License

The module `androidx-palette` is licensed under the Apache License, Version 2.0. See
their [LICENSE](androidx-palette/LICENSE) and their
repository [here](https://github.com/androidx/androidx/tree/androidx-main/palette) for more
information.

### Changes from original source

- Convert Java code to Kotlin
- Convert library to Kotlin Multiplatform

For the remaining code see [LICENSE](LICENSE) for more information.
