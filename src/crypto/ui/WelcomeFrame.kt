package crypto.ui

import java.awt.*
import javax.swing.*

class WelcomeFrame() : JFrame("Crypto Solver") {
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(400, 300) // in px
        setLocationRelativeTo(null) // centering window to middle of the screen
    }

}