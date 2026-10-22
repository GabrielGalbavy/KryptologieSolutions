package crypto.ui

import crypto.algorithms.SecondSolution
import java.awt.*
import javax.swing.*

class SecondSolutionFrame : JFrame() {

    // Backend solver instance
    private val solver = SecondSolution()

    // Farby z AppTheme.light
    private val colorPackage = AppTheme.light

    // Aktuálny stav operácie (true = Encrypt, false = Decrypt)
    private var isEncryptMode = true

    // Komponenty, ku ktorým potrebujeme prístup z funkcií
    private val keyInputField = JTextField("KEY", 12).apply {
        font = Font("SansSerif", Font.BOLD, 18)
        background = colorPackage.mustardYellow
        foreground = colorPackage.textWhite
        caretColor = colorPackage.textWhite
        horizontalAlignment = JTextField.CENTER
    }

    private val tableLabels = Array(25) {
        JLabel("", SwingConstants.CENTER).apply {
            font = Font("SansSerif", Font.BOLD, 22)
            isOpaque = true
            background = colorPackage.oceanicBlue
            foreground = colorPackage.textWhite
            border = BorderFactory.createLineBorder(colorPackage.bgPrimary, 1)
        }
    }

    // Tlačidlá pre výber jazyka (podľa nákresu)
    private val czechBtn = JButton("CZECH").apply {
        font = Font("SansSerif", Font.BOLD, 16)
        isFocusPainted = false
        addActionListener {
            solver.setTableLanguage("czech")
            updateLanguageButtonStyles(isCzech = true)
            executeCipher()
        }
    }

    private val englishBtn = JButton("ENGLISH").apply {
        font = Font("SansSerif", Font.BOLD, 16)
        isFocusPainted = false
        addActionListener {
            solver.setTableLanguage("english")
            updateLanguageButtonStyles(isCzech = false)
            executeCipher()
        }
    }

    // Tlačidlá pre Encrypt / Decrypt (podľa nákresu v strede)
    private val encryptBtn = JButton("Encrypt").apply {
        font = Font("SansSerif", Font.BOLD, 18)
        background = colorPackage.meltdownRed
        foreground = colorPackage.textWhite
        isFocusPainted = false
        addActionListener {
            isEncryptMode = true
            executeCipher()
        }
    }

    private val decryptBtn = JButton("Decrypt").apply {
        font = Font("SansSerif", Font.BOLD, 18)
        background = colorPackage.crystalBlue
        foreground = colorPackage.textWhite
        isFocusPainted = false
        addActionListener {
            isEncryptMode = false
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
        font = Font("SansSerif", Font.PLAIN, 18)
        border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
    }

    private val outputTextArea = JTextArea().apply {
        lineWrap = true
        wrapStyleWord = true
        isEditable = false
        background = colorPackage.oceanicBlue
        foreground = colorPackage.textWhite
        font = Font("SansSerif", Font.PLAIN, 18)
        border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
    }

    // Zmenšené textové pole pre footer (filtrovaný text / debug)
    private val filteredTextArea = JTextArea(2, 20).apply {
        lineWrap = true
        wrapStyleWord = true
        isEditable = false
        background = colorPackage.oceanicBlue
        foreground = colorPackage.textWhite
        font = Font("SansSerif", Font.PLAIN, 16)
        border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
    }

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(1200, 700)
        setLocationRelativeTo(null)
        layout = BorderLayout()
        contentPane.background = colorPackage.bgPrimary

        // Predvolene nastavíme češtinu (alebo angličtinu podľa potreby, tu napr. česká tabuľka ako na obrázku)
        solver.setTableLanguage("czech")
        updateLanguageButtonStyles(isCzech = true)

        ////////// components //////////

        val headerLabel = JLabel("PLAYFAIR CIPHER", SwingConstants.CENTER).apply {
            font = Font("SansSerif", Font.BOLD, 30)
            border = BorderFactory.createEmptyBorder(20, 25, 5, 25)
            foreground = colorPackage.textDark
        }

        val subHeaderLabel = JLabel("", SwingConstants.CENTER).apply {
            font = Font("SansSerif", Font.BOLD, 16)
            foreground = colorPackage.textDark
            border = BorderFactory.createEmptyBorder(0, 0, 10, 0)
        }

        val backBtn = JButton("Menu").apply {
            background = colorPackage.menuPurple
            foreground = colorPackage.textWhite
            font = Font("SansSerif", Font.BOLD, 20)
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

        val headerCenterSubPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = colorPackage.bgPrimary
            headerLabel.alignmentX = Component.CENTER_ALIGNMENT
            subHeaderLabel.alignmentX = Component.CENTER_ALIGNMENT
            add(headerLabel)
            add(subHeaderLabel)
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

            val titleLabel = JLabel("INPUT TEXT").apply {
                font = Font("SansSerif", Font.BOLD, 18)
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

            // Dole: Tlačidlá Encrypt a Decrypt vedľa seba
            val actionButtonsPanel = JPanel(FlowLayout(FlowLayout.CENTER, 10, 5)).apply {
                background = colorPackage.bgPrimary
                add(encryptBtn)
                add(decryptBtn)
            }

            add(keyPanel, BorderLayout.NORTH)
            add(cipherTablePanel, BorderLayout.CENTER)
            add(actionButtonsPanel, BorderLayout.SOUTH)
        }

        val bodyRightSubPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(400, 0)
            background = colorPackage.bgPrimary
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            val titleLabel = JLabel("OUTPUT TEXT").apply {
                font = Font("SansSerif", Font.BOLD, 18)
                foreground = colorPackage.textDark
                border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
            }

            val scrollPane = JScrollPane(outputTextArea).apply {
                border = BorderFactory.createLineBorder(colorPackage.brighterBlue, 2)
            }

            add(titleLabel, BorderLayout.NORTH)
            add(scrollPane, BorderLayout.CENTER)
        }

        // Footer structure (Vľavo jazykové tlačidlá pod sebou, v strede filtrovaný text)
        val langButtonsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = colorPackage.bgPrimary
            czechBtn.alignmentX = Component.CENTER_ALIGNMENT
            englishBtn.alignmentX = Component.CENTER_ALIGNMENT

            add(czechBtn)
            add(Box.createRigidArea(Dimension(0, 5)))
            add(englishBtn)
        }

        val footerLeftSubPanel = JPanel(BorderLayout()).apply {
            background = colorPackage.bgPrimary
            border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(colorPackage.brighterBlue),
                "TABLE LANGUAGE",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                Font("SansSerif", Font.BOLD, 14),
                colorPackage.textDark
            )
            preferredSize = Dimension(220, 0)
            add(langButtonsPanel, BorderLayout.CENTER)
        }

        val footerCenterSubPanel = JPanel(BorderLayout()).apply {
            background = colorPackage.bgPrimary
            border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
            val footerTitleLabel = JLabel("FILTERED TEXT").apply {
                font = Font("SansSerif", Font.BOLD, 16)
                foreground = colorPackage.textDark
                horizontalAlignment = SwingConstants.CENTER
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
            preferredSize = Dimension(0, 90)
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

    private fun updateLanguageButtonStyles(isCzech: Boolean) {
        if (isCzech) {
            czechBtn.background = colorPackage.mustardYellow
            czechBtn.foreground = colorPackage.textWhite
            englishBtn.background = colorPackage.brighterBlue
            englishBtn.foreground = colorPackage.textWhite
        } else {
            englishBtn.background = colorPackage.mustardYellow
            englishBtn.foreground = colorPackage.textWhite
            czechBtn.background = colorPackage.brighterBlue
            czechBtn.foreground = colorPackage.textWhite
        }
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

        val rawInput = inputTextArea.text ?: ""

        if (isEncryptMode) {
            // Šifrovanie: prebehne cez bežný filter
            val filteredText = solver.filter(rawInput)
            filteredTextArea.text = filteredText.chunked(2).toList().joinToString(" ")
            outputTextArea.text = solver.encrypt(filteredText)
        } else {
            // Dešifrovanie: ošetriť tak, aby sa poslal čistý text bez bielych znakov
            val solidCiphertext = rawInput.replace(Regex("\\s+"), "").uppercase()
            filteredTextArea.text = solidCiphertext.chunked(2).toList().joinToString(" ")
            outputTextArea.text = solver.decrypt(solidCiphertext)
        }
    }
}