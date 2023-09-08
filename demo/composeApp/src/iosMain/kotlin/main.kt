import androidx.compose.ui.window.ComposeUIViewController
import dev.jordond.kmpalette.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController { App() }
}
