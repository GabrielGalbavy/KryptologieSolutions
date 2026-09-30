package crypto

import crypto.ui.*
import javax.swing.SwingUtilities

val WORKING_ALGORITHMS = arrayOf("test", "test_two")

// This is where the fun begins
fun main() {
    SwingUtilities.invokeLater {
        val frame = MainFrame()
        frame.btnProcess.addActionListener {
            val text = frame.inputField.text
            frame.outputField.text = text
        }

        frame.isVisible = true
    }
}
