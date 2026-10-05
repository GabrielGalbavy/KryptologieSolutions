package crypto

import crypto.ui.WelcomeFrame
import javax.swing.SwingUtilities

val availableSolutions = arrayOf(
    "Afinní šifra",
    "test two",
    "test three"
)

fun main() {
    SwingUtilities.invokeLater {
        val window = WelcomeFrame()
        window.isVisible = true
    }
}