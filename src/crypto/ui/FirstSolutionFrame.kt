package crypto.ui

import crypto.algorithms.FirstSolution
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SwingConstants

class FirstSolutionFrame : JFrame() {
    init {

        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(1000, 400)
        setLocationRelativeTo(null)
        layout = BorderLayout()

        ////////// backend and logic //////////

        val cipherBackend = FirstSolution()


        ////////// components //////////

        // header label
        val headerLabel = JLabel("Afinní šifra", SwingConstants.LEFT).apply {
            font = font.deriveFont(Font.BOLD, 22f)
            border = BorderFactory.createEmptyBorder(
                20, 25, 10, 25
            ) // Margins: 20px up down, 25px sides
        }

        // left text field
        val inputTextArea = JTextArea().apply {
            lineWrap = true // auto text folding
            wrapStyleWord = true // won't slice words in half
        }
        val inputScrollPane = JScrollPane(inputTextArea).apply {
            border = BorderFactory.createTitledBorder("Input")
        }

        // right text field
        val outputTextArea = JTextArea().apply {
            lineWrap = true // auto text folding
            wrapStyleWord = true // won't slice words in half
            isEditable = false

        }
        val outputScrollPane = JScrollPane(outputTextArea).apply {
            border = BorderFactory.createTitledBorder("Output")
        }

        // center field
        val keyAField = JTextField("1").apply {
            maximumSize = Dimension(200, 30)
            alignmentX = CENTER_ALIGNMENT
            border = BorderFactory.createTitledBorder("Key A")
        }
        val keyBField = JTextField("0").apply {
            maximumSize = Dimension(200, 30)
            alignmentX = CENTER_ALIGNMENT
            border = BorderFactory.createTitledBorder("Key B")
        }

        val modeComboBox = JComboBox(arrayOf("Encrypt", "Decrypt")).apply {
            maximumSize = Dimension(200, 30)
            alignmentX = CENTER_ALIGNMENT
        }

        val runBtn = JButton("Run").apply {
            alignmentX = CENTER_ALIGNMENT
            preferredSize = Dimension(120, 35)
            maximumSize = Dimension(200, 35)
        }
        runBtn.addActionListener {
            val text = inputTextArea.text.toString()
            val keyA = keyAField.text.toIntOrNull()
            val keyB = keyBField.text.toIntOrNull()

            if (keyA == null) {
                JOptionPane.showMessageDialog(this, "Neplatná hodnota klíče A")
            } else if (keyB == null) {
                JOptionPane.showMessageDialog(this, "Neplatná hodnota klíče B")
            } else {
                cipherBackend.setKeyA(keyA)
                cipherBackend.setKeyB(keyB)

                val mode = modeComboBox.selectedItem as String

                when (mode) {
                    "Encrypt" -> {
                        outputTextArea.text = cipherBackend.encrypt(text)
                    }

                    "Decrypt" -> {
                        outputTextArea.text = cipherBackend.decrypt(text)
                    }
                }
            }
        }


        ////////// panels //////////

        val middleControlsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)

            // vertical component arrangement
            add(Box.createVerticalGlue())
            add(keyAField)
            add(Box.createRigidArea(Dimension(0, 10)))
            add(keyBField)
            add(Box.createRigidArea(Dimension(0, 15)))
            add(modeComboBox)
            add(Box.createRigidArea(Dimension(0, 15)))
            add(runBtn)
            add(Box.createVerticalGlue())
        }

        val centerPanel = JPanel(GridLayout(1, 3, 10, 0)).apply {
            border = BorderFactory.createEmptyBorder(
                10, 25, 20, 25
            )

            add(inputScrollPane)
            add(middleControlsPanel)
            add(outputScrollPane)
        }


        ////////// implementation //////////

        add(headerLabel, BorderLayout.NORTH)

        add(centerPanel, BorderLayout.CENTER)
    }

}