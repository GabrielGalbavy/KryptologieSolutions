package crypto.ui

import java.awt.Dimension
import javax.swing.JFrame

class FirstSolutionFrame(): JFrame() {
    init {

        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(800   , 300)
        setLocationRelativeTo(null)
    }
}