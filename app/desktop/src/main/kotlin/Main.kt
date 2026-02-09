import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kmpalette.App
import io.github.vinceglb.filekit.FileKit
import java.awt.Dimension

fun main() {
    FileKit.init(appId = "KMPalette")

    application {
        Window(
            title = "KMPalette Demo",
            state = rememberWindowState(width = 900.dp, height = 700.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(400, 600)
            App()
        }
    }
}
