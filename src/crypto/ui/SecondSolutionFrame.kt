package crypto.ui

import crypto.algorithms.SecondSolution
import java.awt.*
import javax.swing.*

class SecondSolutionFrame : JFrame() {

    // Backend solver instance
    private val solver = SecondSolution()

    // Farby z AppTheme.light
    private val colorPackage = AppTheme.light

    // Komponenty, ku ktorým potrebujeme prístup z funkcií
    private val keyInputField = JTextField("KEY", 12).apply {
        font = Font("SansSerif", Font.BOLD, 14)
        background = colorPackage.mustardYellow
        foreground = colorPackage.textWhite
        caretColor = colorPackage.textWhite
        horizontalAlignment = JTextField.CENTER
    }

    private val tableLabels = Array(25) {
        JLabel("", SwingConstants.CENTER).apply {
            font = Font("SansSerif", Font.BOLD, 18)
            isOpaque = true
            background = colorPackage.oceanicBlue
            foreground = colorPackage.textWhite
            border = BorderFactory.createLineBorder(colorPackage.bgPrimary, 1)
        }
    }

    // Výber jazyka tabuľky (EN / CZ)
    private val languageComboBox = JComboBox(arrayOf("English (J=I)", "Czech (W=V)")).apply {
        font = Font("SansSerif", Font.PLAIN, 12)
        background = colorPackage.bgPrimary
        foreground = colorPackage.textDark
        addActionListener {
            val selectedLang = if (selectedIndex == 0) "english" else "czech"
            solver.setTableLanguage(selectedLang)
            executeCipher() // Po zmene jazyka sa pregeneruje 5x5 matica
        }
    }

    // Výber operácie (Encrypt / Decrypt)
    private val operationComboBox = JComboBox(arrayOf("Encrypt", "Decrypt")).apply {
        font = Font("SansSerif", Font.PLAIN, 12)
        background = colorPackage.bgPrimary
        foreground = colorPackage.textDark
        addActionListener {
            executeCipher()
        }
    }

    // Textové polia vyvedené do triedy
    private val inputTextArea = JTextArea().apply {
        lineWrap = true
        wrapStyleWord = true
        background = colorPackage.oceanicBlue
        foreground = colorPackage.textWhite
        caretColor = colorPackage.textWhite
        font = Font("SansSerif", Font.PLAIN, 14)
        border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
    }

    private val outputTextArea = JTextArea().apply {
        lineWrap = true
        wrapStyleWord = true
        isEditable = false
        background = colorPackage.oceanicBlue
        foreground = colorPackage.textWhite
        font = Font("SansSerif", Font.PLAIN, 14)
        border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
    }

    // Zmenšené textové pole pre footer (filtrovaný text / debug)
    private val filteredTextArea = JTextArea(2, 20).apply {
        lineWrap = true
        wrapStyleWord = true
        isEditable = false
        background = colorPackage.oceanicBlue
        foreground = colorPackage.textWhite
        font = Font("SansSerif", Font.PLAIN, 12)
        border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
    }

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(1200, 700)
        setLocationRelativeTo(null)
        layout = BorderLayout()
        contentPane.background = colorPackage.bgPrimary

        ////////// components //////////

        val headerLabel = JLabel("PlayFair Cipher", SwingConstants.CENTER).apply {
            font = Font("SansSerif", Font.BOLD, 26)
            border = BorderFactory.createEmptyBorder(20, 25, 10, 25)
            foreground = colorPackage.textDark
        }

        val backBtn = JButton("Menu").apply {
            background = colorPackage.menuPurple
            foreground = colorPackage.textWhite
            font = Font("SansSerif", Font.BOLD, 16)
            isFocusPainted = false

            addActionListener {
                val welcomeFrame = WelcomeFrame()
                welcomeFrame.isVisible = true
                dispose()
            }
        }

        ////////// panels //////////

        // Header structure
        val headerLeftSubPanel = JPanel(FlowLayout(FlowLayout.LEFT, 15, 20)).apply {
            preferredSize = Dimension(300, 0)
            background = colorPackage.bgPrimary
            add(backBtn)
        }

        val headerCenterSubPanel = JPanel(BorderLayout()).apply {
            background = colorPackage.bgPrimary
            add(headerLabel, BorderLayout.CENTER)
        }

        val headerRightSubPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(300, 0)
            background = colorPackage.bgPrimary
        }

        // Center structure (Body)
        val bodyLeftSubPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(400, 0)
            background = colorPackage.bgPrimary
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            val titleLabel = JLabel("Input Text").apply {
                font = Font("SansSerif", Font.BOLD, 14)
                foreground = colorPackage.textDark
                border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
            }

            val scrollPane = JScrollPane(inputTextArea).apply {
                border = BorderFactory.createLineBorder(colorPackage.brighterBlue, 2)
            }

            add(titleLabel, BorderLayout.NORTH)
            add(scrollPane, BorderLayout.CENTER)
        }

        val bodyCenterSubPanel = JPanel(BorderLayout()).apply {
            background = colorPackage.bgPrimary
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            // Hore: Kľúč
            val keyPanel = JPanel(FlowLayout(FlowLayout.CENTER, 5, 5)).apply {
                background = colorPackage.bgPrimary
                add(keyInputField)
            }

            // V strede: Mriežka šifry 5x5
            val cipherTablePanel = JPanel(GridLayout(5, 5, 4, 4)).apply {
                background = colorPackage.brighterBlue
                border = BorderFactory.createEmptyBorder(5, 5, 5, 5)
                tableLabels.forEach { label ->
                    add(label)
                }
            }

            // Dole: Tlačidlo na spustenie (Run / Encrypt)
            val generateBtn = JButton("Run").apply {
                font = Font("SansSerif", Font.BOLD, 14)
                background = colorPackage.crystalBlue
                foreground = colorPackage.textWhite
                isFocusPainted = false
                addActionListener {
                    executeCipher()
                }
            }
            val generatePanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
                background = colorPackage.bgPrimary
                add(generateBtn)
            }

            add(keyPanel, BorderLayout.NORTH)
            add(cipherTablePanel, BorderLayout.CENTER)
            add(generatePanel, BorderLayout.SOUTH)
        }

        val bodyRightSubPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(400, 0)
            background = colorPackage.bgPrimary
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            val titleLabel = JLabel("Output Text").apply {
                font = Font("SansSerif", Font.BOLD, 14)
                foreground = colorPackage.textDark
                border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
            }

            val scrollPane = JScrollPane(outputTextArea).apply {
                border = BorderFactory.createLineBorder(colorPackage.brighterBlue, 2)
            }

            add(titleLabel, BorderLayout.NORTH)
            add(scrollPane, BorderLayout.CENTER)
        }

        // Footer structure
        val footerLeftSubPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = colorPackage.bgPrimary
            border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorPackage.brighterBlue),
                "Settings",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                Font("SansSerif", Font.BOLD, 12),
                colorPackage.textDark
            )
            preferredSize = Dimension(220, 0)

            val langLabel = JLabel("Table Language:").apply {
                font = Font("SansSerif", Font.PLAIN, 12)
                foreground = colorPackage.textDark
                alignmentX = Component.LEFT_ALIGNMENT
            }
            languageComboBox.alignmentX = Component.LEFT_ALIGNMENT

            val opLabel = JLabel("Operation:").apply {
                font = Font("SansSerif", Font.PLAIN, 12)
                foreground = colorPackage.textDark
                alignmentX = Component.LEFT_ALIGNMENT
            }
            operationComboBox.alignmentX = Component.LEFT_ALIGNMENT

            add(langLabel)
            add(languageComboBox)
            add(Box.createRigidArea(Dimension(0, 8)))
            add(opLabel)
            add(operationComboBox)
        }

        val footerCenterSubPanel = JPanel(BorderLayout()).apply {
            background = colorPackage.bgPrimary
            border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
            val footerTitleLabel = JLabel("Filtered Text / Debug").apply {
                font = Font("SansSerif", Font.BOLD, 12)
                foreground = colorPackage.textDark
                border = BorderFactory.createEmptyBorder(0, 0, 3, 0)
            }
            val footerScrollPane = JScrollPane(filteredTextArea).apply {
                border = BorderFactory.createLineBorder(colorPackage.brighterBlue, 2)
            }

            add(footerTitleLabel, BorderLayout.NORTH)
            add(footerScrollPane, BorderLayout.CENTER)
        }

        val footerPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(0, 130)
            background = colorPackage.bgPrimary
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            add(footerLeftSubPanel, BorderLayout.WEST)
            add(footerCenterSubPanel, BorderLayout.CENTER)
        }

        // Window structure
        val headerPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(0, 80)
            background = colorPackage.bgPrimary
            add(headerLeftSubPanel, BorderLayout.WEST)
            add(headerCenterSubPanel, BorderLayout.CENTER)
            add(headerRightSubPanel, BorderLayout.EAST)
        }

        val bodyPanel = JPanel(BorderLayout()).apply {
            background = colorPackage.bgPrimary
            add(bodyLeftSubPanel, BorderLayout.WEST)
            add(bodyCenterSubPanel, BorderLayout.CENTER)
            add(bodyRightSubPanel, BorderLayout.EAST)
        }

        // Layer 1 implementation
        this.apply {
            add(headerPanel, BorderLayout.NORTH)
            add(bodyPanel, BorderLayout.CENTER)
            add(footerPanel, BorderLayout.SOUTH)
        }

        // Poštartové naplnenie tabuľky a inicializácia
        executeCipher()
    }

    private fun executeCipher() {
        // 1. Nastavenie kľúča a vygenerovanie 5x5 matice tabuľky
        solver.setKey(keyInputField.text)
        val matrix = solver.tableFactory()

        var index = 0
        for (row in 0..4) {
            for (col in 0..4) {
                tableLabels[index].text = matrix[row][col].toString()
                index++
            }
        }

        // 2. Získanie surového vstupu a filtrovanie cez solver.filter(...)
        val rawInput = inputTextArea.text ?: ""
        val filteredText = solver.filter(rawInput)
        filteredTextArea.text = filteredText

        // 3. Volenie operácie (Encrypt / Decrypt) podľa vybranej položky v comboboxe
        val resultText = if (operationComboBox.selectedIndex == 0) {
            solver.encrypt(filteredText)
        } else {
            solver.decrypt(filteredText)
        }

        // 4. Zapísanie výsledku do výstupného textového poľa
        outputTextArea.text = resultText
    }
}