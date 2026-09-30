package crypto.ui

import crypto.*
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.*


class MainFrame : JFrame("Kryptológia") {

    val inputField = JTextField(20)
    val outputField = JTextField(20).apply { isEditable = false }
    val btnProcess = JButton("Spracovať")

    val cipherComboBox = JComboBox(WORKING_ALGORITHMS)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(300, 150)
        setLocationRelativeTo(null)
        layout = FlowLayout()

        add(JLabel("Vstup:"))
        add(inputField)
        add(btnProcess)
        add(JLabel("Výstup:"))
        add(outputField)
        add(cipherComboBox)
    }
}