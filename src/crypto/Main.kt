// Main.kt
package crypto

import crypto.ui.WelcomeFrame
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val window = WelcomeFrame()
        window.isVisible = true
    }
}