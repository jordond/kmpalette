# Base64 Extensions

This extension provides `Base64Loader` a `ImageBitmapLoader` for loading base64 encoded images to
a `ImageBitmap`.

| Loader         | Input Class | Demo                                                                                                           |
|----------------|-------------|----------------------------------------------------------------------------------------------------------------|
| `Base64Loader` | `String`    | [`Base64DemoScreen`](../demo/composeApp/src/commonMain/kotlin/com/kmpalette/demo/dominant/Base64DemoScreen.kt) |

# Setup

You need to add the following to your `build.gradle.kts` file:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmpalette.core)
                implementation(libs.kmpalette.extensions.base64)
            }
        }
    }
}
```

## Usage

You will need to have a image (png, jpg, etc) encoded as a Base64 `String`. You can then
use `Base64Loader` to load the image.

**Note:** If there is a header on the Base64 `String` it will be stripped and ignored.

```kotlin
val base64Image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAAD" +
    "gdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3" +
    "NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQB" +
    "Kci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tC" +
    "a00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5X" +
    "Pp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO" +
    "+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ih" +
    "MsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQP" +
    "srHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl" +
    "5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3e" +
    "kokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc" +
    "/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc" +
    "6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDF" +
    "Nx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66Pfyu" +
    "Rj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3k" +
    "vgLI5AzFfo379UAAAAASUVORK5CYII="

val imageBitmap: ImageBitmap = Base64Loader.load(base64Image)

// Or using one of the composeable functions
val paletteState = rememebrPaletteState(Base64Loader)
LaunchedEffect(base64Image) {
    paletteState.generate(base64Image)
}
```