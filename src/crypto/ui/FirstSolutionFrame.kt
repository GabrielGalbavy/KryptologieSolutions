package crypto.ui

import crypto.algorithms.FirstSolution
import java.awt.BorderLayout
import java.awt.Color
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
    private val alphabetGridLabels = Array(26) { JLabel("", SwingConstants.CENTER) }

    init {

        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(1000, 400)
        setLocationRelativeTo(null)
        layout = BorderLayout()


        ////////// style //////////

        val colorPackage = AppTheme.dark
        val gradientFrom = AppTheme.gradientColors[0]
        val gradientTill = AppTheme.gradientColors[1]

        contentPane.background = colorPackage.bgPrimary


        val mainBackground = GradientPanel(
            startColor = Color(gradientFrom[0], gradientFrom[1], gradientFrom[2]), endColor = Color(
                gradientTill[0], gradientTill[1], gradientTill[2]
            ), isVertical = false
        ).apply {
            layout = BorderLayout()
        }

        contentPane = mainBackground

        ////////// backend and logic //////////

        val cipherBackend = FirstSolution()

        fun updateAlphabetGrid() {
            for (i in 0..<26) {
                val originalChar = ('A' + i).toString()
                val encryptedChar = cipherBackend.encrypt(originalChar)
                alphabetGridLabels[i].text = encryptedChar.trim()
            }
        }

        ////////// components //////////

        // header label
        val headerLabel = JLabel("Afinní šifra", SwingConstants.LEFT).apply {
            font = font.deriveFont(Font.BOLD, 22f)
            border = BorderFactory.createEmptyBorder(
                20, 25, 10, 25
            ) // Margins: 20px up down, 25px sides
            foreground = colorPackage.textPrimary
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
        val keyAField = JTextField("").apply {

            maximumSize = Dimension(200, 30)
            alignmentX = CENTER_ALIGNMENT
            border = BorderFactory.createTitledBorder("Key A")
        }
        val keyBField = JTextField("").apply {
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

            if (keyA == null || keyB == null) {
                JOptionPane.showMessageDialog(this, "Klíče musí být čísla")
            } else if (!cipherBackend.validateKeyA(keyA)) {
                JOptionPane.showMessageDialog(this, "Neplatná hodnota klíče A")
            } else {
                cipherBackend.setKeyA(keyA)
                cipherBackend.setKeyB(keyB)

                updateAlphabetGrid()

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

        val backBtn = JButton("Menu").apply {
            addActionListener {
                val welcomeFrame = WelcomeFrame()
                welcomeFrame.isVisible = true
                dispose()
            }
        }


        ////////// panels //////////

        val headerPanel = JPanel(BorderLayout()).apply {
            border = BorderFactory.createEmptyBorder(15, 25, 10, 25)
            isOpaque = false

            add(backBtn, BorderLayout.WEST)
            add(headerLabel, BorderLayout.CENTER)
        }

        val middleControlsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            isOpaque = false

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
            isOpaque = false

            add(inputScrollPane)
            add(middleControlsPanel)
            add(outputScrollPane)
        }

        val alphabetPanel = JPanel(GridLayout(2, 26, 2, 2)).apply {
//            border = BorderFactory.createCompoundBorder(
//                BorderFactory.createEmptyBorder(0, 25, 1, 25), BorderFactory.createTitledBorder("")
//            )
            background = colorPackage.bgSecondary


            for (ch in 'A'..'Z') {
                add(JLabel(ch.toString(), SwingConstants.CENTER).apply {
                    font = font.deriveFont(Font.BOLD, 24f)
                    background = colorPackage.textPrimary
                    foreground = colorPackage.textPrimary
                })
            }

            for (label in alphabetGridLabels) {
                add(label.apply {
                    font = font.deriveFont(Font.BOLD, 24f)
                    foreground = colorPackage.textMuted

                })
            }
        }

        val footerPanel = JPanel(BorderLayout()).apply {
            border = BorderFactory.createEmptyBorder(0, 25, 20, 25)
            isOpaque = false

            add(alphabetPanel, BorderLayout.CENTER)
        }


        ////////// implementation //////////

        add(headerPanel, BorderLayout.NORTH)
        add(centerPanel, BorderLayout.CENTER)
        add(footerPanel, BorderLayout.SOUTH)

        updateAlphabetGrid()
    }
}