package crypto.ui

import crypto.algorithms.ThirdSolution
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class ThirdSolutionFrame : JFrame("ADFG(V)X CIPHER") {

    private val solver = ThirdSolution()

    // Farby podľa dizajnu
    private val bgDarkBlue = Color(0x34, 0x49, 0x5E) // Tmavomodrá pre políčka
    private val appBackground = Color(0xF8, 0xF9, 0xFA) // Svetlé pozadie okna
    private val orangeAccent = Color(0xF2, 0x99, 0x4A) // Oranžová pre aktívny stav
    private val inactiveGray = Color(0x7F, 0x8C, 0x8D) // Šedá pre neaktívny stav
    private val purpleMenu = Color(0xBA, 0x8E, 0xEC)  // Fialová pre Menu
    private val redEncrypt = Color(0xE0, 0x5A, 0x47)  // Červeno-oranžová pre Encrypt
    private val blueDecrypt = Color(0x4A, 0x90, 0xE2) // Modrá pre Decrypt

    // Komponenty rozhrania
    private val btnCzech = JToggleButton("CZECH", true).apply { font = Font("SansSerif", Font.BOLD, 14) }
    private val btnEnglish = JToggleButton("ENGLISH").apply { font = Font("SansSerif", Font.BOLD, 14) }
    private val btnSmall = JToggleButton("SMALL", true).apply { font = Font("SansSerif", Font.BOLD, 14) }
    private val btnLarge = JToggleButton("LARGE").apply { font = Font("SansSerif", Font.BOLD, 14) }

    // Zväčšené písmo (23) pre Input a Output textové polia
    private val txtInput = createStyledTextArea().apply {
        font = Font("Monospaced", Font.PLAIN, 23)
        isEditable = true
    }
    private val txtOutput = createStyledTextArea().apply {
        font = Font("Monospaced", Font.PLAIN, 23)
    }

    private val txtKeyword = JTextField(10).apply {
        background = orangeAccent
        foreground = Color.WHITE
        font = Font("Monospaced", Font.BOLD, 16)
        horizontalAlignment = JTextField.CENTER
    }
    private val txtSeed = JTextField("123", 6).apply {
        background = orangeAccent
        foreground = Color.WHITE
        font = Font("Monospaced", Font.BOLD, 16)
        horizontalAlignment = JTextField.CENTER
    }
    private val btnEncrypt = JButton("ENCRYPT").apply { font = Font("SansSerif", Font.BOLD, 14) }
    private val btnDecrypt = JButton("DECRYPT").apply { font = Font("SansSerif", Font.BOLD, 14) }

    // Spodné vizualizéry a náhľad tabuľky (Filtered text zväčšený na 19)
    private val txtFilteredText = createStyledTextArea().apply {
        font = Font("Monospaced", Font.PLAIN, 19)
    }
    private val txtUnsortedMatrix = createStyledTextArea()
    private val txtSortedMatrix = createStyledTextArea()
    private val txtTablePreview = JTextPane().apply {
        background = bgDarkBlue
        foreground = Color.WHITE
        font = Font("Monospaced", Font.BOLD, 15)
        isEditable = false
        border = EmptyBorder(10, 10, 10, 10)

        // Nastavenie počiatočného zarovnania na stred
        val doc = styledDocument
        val center = SimpleAttributeSet()
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER)
        doc.setParagraphAttributes(0, doc.length, center, false)
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(1200, 1000)
        setLocationRelativeTo(null)
        contentPane.layout = BorderLayout(15, 15)
        contentPane.background = appBackground

        setupToggleListeners()
        setupHeader()
        setupCenterPanel()
        setupBottomPanel()
        bindEvents()
        updateToggleStyles() // Inicializácia farieb tlačidiel
    }

    private fun createStyledTextArea(): JTextArea {
        return JTextArea(8, 15).apply {
            background = bgDarkBlue
            foreground = Color.WHITE
            font = Font("Monospaced", Font.PLAIN, 15)
            caretColor = Color.WHITE
            lineWrap = true
            wrapStyleWord = true
            isEditable = false
            border = EmptyBorder(10, 10, 10, 10)
        }
    }

    private fun updateTablePreviewText(text: String) {
        txtTablePreview.text = text
        val doc = txtTablePreview.styledDocument
        val center = SimpleAttributeSet()
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER)
        doc.setParagraphAttributes(0, doc.length, center, false)
    }

    private fun updateToggleStyles() {
        btnCzech.background = if (btnCzech.isSelected) orangeAccent else inactiveGray
        btnEnglish.background = if (btnEnglish.isSelected) orangeAccent else inactiveGray
        btnSmall.background = if (btnSmall.isSelected) orangeAccent else inactiveGray
        btnLarge.background = if (btnLarge.isSelected) orangeAccent else inactiveGray
    }

    private fun setupToggleListeners() {
        val listener = java.awt.event.ActionListener { updateToggleStyles() }
        btnCzech.addActionListener(listener)
        btnEnglish.addActionListener(listener)
        btnSmall.addActionListener(listener)
        btnLarge.addActionListener(listener)
    }

    private fun setupHeader() {
        val headerPanel = JPanel(BorderLayout()).apply {
            isOpaque = false
            border = EmptyBorder(15, 20, 10, 20)
        }

        // Tlačidlo MENU s návratom do WelcomeFrame
        val btnMenu = JButton("MENU").apply {
            background = purpleMenu
            foreground = Color.WHITE
            font = Font("SansSerif", Font.BOLD, 16)
            isFocusPainted = false
            border = EmptyBorder(10, 20, 10, 20)

            addActionListener {
                val welcomeFrame = WelcomeFrame()
                welcomeFrame.isVisible = true
                dispose()
            }
        }
        headerPanel.add(btnMenu, BorderLayout.WEST)

        val configPanel = JPanel(FlowLayout(FlowLayout.CENTER, 30, 0)).apply { isOpaque = false }

        val langGroup = ButtonGroup().apply { add(btnCzech); add(btnEnglish) }
        val sizeGroup = ButtonGroup().apply { add(btnSmall); add(btnLarge) }

        fun styleToggle(btn: JToggleButton) {
            btn.foreground = Color.WHITE
            btn.isFocusPainted = false
            btn.border = EmptyBorder(6, 12, 6, 12)
        }
        listOf(btnCzech, btnEnglish, btnSmall, btnLarge).forEach { styleToggle(it) }

        val langBox = JPanel(GridLayout(2, 1, 0, 5)).apply {
            isOpaque = false
            add(JLabel("TABLE LANGUAGE", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 12); foreground = Color.DARK_GRAY })
            add(JPanel(FlowLayout(FlowLayout.CENTER, 5, 0)).apply { isOpaque = false; add(btnCzech); add(btnEnglish) })
        }

        val sizeBox = JPanel(GridLayout(2, 1, 0, 5)).apply {
            isOpaque = false
            add(JLabel("TABLE SIZE", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 12); foreground = Color.DARK_GRAY })
            add(JPanel(FlowLayout(FlowLayout.CENTER, 5, 0)).apply { isOpaque = false; add(btnSmall); add(btnLarge) })
        }

        configPanel.add(langBox)
        configPanel.add(sizeBox)
        headerPanel.add(configPanel, BorderLayout.CENTER)

        val lblTitle = JLabel("ADFG(V)X CIPHER").apply {
            font = Font("Monospaced", Font.BOLD, 32)
            foreground = Color.DARK_GRAY
        }
        headerPanel.add(lblTitle, BorderLayout.EAST)

        add(headerPanel, BorderLayout.NORTH)
    }

    private fun setupCenterPanel() {
        val centerPanel = JPanel(GridLayout(1, 3, 20, 0)).apply {
            isOpaque = false
            border = EmptyBorder(0, 20, 0, 20)
        }

        // 1. Input Text (užívateľ môže písať, zväčšené písmo)
        val editableInput = JScrollPane(txtInput)
        val inputPanel = JPanel(BorderLayout(0, 8)).apply {
            isOpaque = false
            add(JLabel("INPUT TEXT", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 16); foreground = Color.DARK_GRAY }, BorderLayout.NORTH)
            add(editableInput, BorderLayout.CENTER)
        }

        // 2. Stredný panel: Polybius tabuľka, kľúč, seed, tlačidlá
        val middleControls = JPanel(GridBagLayout()).apply { isOpaque = false }
        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = GridBagConstraints.RELATIVE
            insets = Insets(8, 0, 8, 0)
            fill = GridBagConstraints.HORIZONTAL
        }

        val tablePanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(220, 200)
            border = BorderFactory.createLineBorder(orangeAccent, 3)
            background = bgDarkBlue
            add(JScrollPane(txtTablePreview), BorderLayout.CENTER)
        }
        middleControls.add(tablePanel, gbc)

        middleControls.add(JLabel("KEYWORD", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 13); foreground = orangeAccent }, gbc)
        middleControls.add(txtKeyword, gbc)

        middleControls.add(JLabel("SEED", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 13); foreground = orangeAccent }, gbc)
        middleControls.add(txtSeed, gbc)

        val btnPanel = JPanel(GridLayout(1, 2, 10, 0)).apply {
            isOpaque = false
            add(btnEncrypt.apply { background = redEncrypt; foreground = Color.WHITE; isFocusPainted = false; border = EmptyBorder(8, 10, 8, 10) })
            add(btnDecrypt.apply { background = blueDecrypt; foreground = Color.WHITE; isFocusPainted = false; border = EmptyBorder(8, 10, 8, 10) })
        }
        middleControls.add(btnPanel, gbc)

        // 3. Output Text (zväčšené písmo)
        val outputPanel = JPanel(BorderLayout(0, 8)).apply {
            isOpaque = false
            add(JLabel("OUTPUT TEXT", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 16); foreground = Color.DARK_GRAY }, BorderLayout.NORTH)
            add(JScrollPane(txtOutput), BorderLayout.CENTER)
        }

        centerPanel.add(inputPanel)
        centerPanel.add(middleControls)
        centerPanel.add(outputPanel)

        add(centerPanel, BorderLayout.CENTER)
    }

    private fun setupBottomPanel() {
        val bottomPanel = JPanel(GridLayout(1, 3, 20, 0)).apply {
            isOpaque = false
            border = EmptyBorder(10, 20, 20, 20)
            preferredSize = Dimension(1200, 280)
        }

        val p1 = JPanel(BorderLayout()).apply {
            isOpaque = false
            add(JLabel("UNSORTED MATRIX", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 14); foreground = Color.DARK_GRAY }, BorderLayout.NORTH)
            add(JScrollPane(txtUnsortedMatrix), BorderLayout.CENTER)
        }

        val p2 = JPanel(BorderLayout()).apply {
            isOpaque = false
            add(JLabel("FILTERED TEXT", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 14); foreground = Color.DARK_GRAY }, BorderLayout.NORTH)
            add(JScrollPane(txtFilteredText), BorderLayout.CENTER)
        }

        val p3 = JPanel(BorderLayout()).apply {
            isOpaque = false
            add(JLabel("SORTED MATRIX", SwingConstants.CENTER).apply { font = Font("SansSerif", Font.BOLD, 14); foreground = Color.DARK_GRAY }, BorderLayout.NORTH)
            add(JScrollPane(txtSortedMatrix), BorderLayout.CENTER)
        }

        bottomPanel.add(p1)
        bottomPanel.add(p2)
        bottomPanel.add(p3)

        add(bottomPanel, BorderLayout.SOUTH)
    }

    private fun bindEvents() {
        fun updateSolverParams() {
            solver.keyword = solver.filter(txtKeyword.text.trim())
            solver.seed = txtSeed.text.toIntOrNull() ?: 123
            solver.tableSize = if (btnSmall.isSelected) ThirdSolution.TableSizes.SMALL else ThirdSolution.TableSizes.LARGE
            solver.tableLanguage = if (btnCzech.isSelected) ThirdSolution.Tables.CZ else ThirdSolution.Tables.EN
        }

        btnEncrypt.addActionListener {
            try {
                updateSolverParams()

                if (solver.keyword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Prosím, zadajte kľúč (Keyword)!", "Chýba kľúč", JOptionPane.WARNING_MESSAGE)
                    return@addActionListener
                }
                if (txtInput.text.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Prosím, zadajte vstupný text!", "Chýba vstup", JOptionPane.WARNING_MESSAGE)
                    return@addActionListener
                }

                val table = solver.tableFactory()
                solver.ourTable = table
                updateTablePreviewText(formatTable(table, solver.tableSize))

                val filtered = solver.filter(txtInput.text)
                txtFilteredText.text = filtered

                val mapped = mapMessageForPreview(filtered, table, solver.tableSize)
                val unsorted = solver.formFactory(mapped)
                txtUnsortedMatrix.text = formatMatrix(unsorted)

                val sorted = solver.sortFormColumns(unsorted)
                txtSortedMatrix.text = formatMatrix(sorted)

                val result = solver.encrypt(txtInput.text)
                txtOutput.text = result

            } catch (e: Exception) {
                e.printStackTrace()
                JOptionPane.showMessageDialog(this, "Chyba pri šifrovaní: ${e.message}", "Chyba", JOptionPane.ERROR_MESSAGE)
            }
        }

        btnDecrypt.addActionListener {
            try {
                updateSolverParams()

                if (solver.keyword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Prosím, zadajte kľúč (Keyword)!", "Chýba kľúč", JOptionPane.WARNING_MESSAGE)
                    return@addActionListener
                }
                if (txtInput.text.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Prosím, zadajte text na dešifrovanie!", "Chýba vstup", JOptionPane.WARNING_MESSAGE)
                    return@addActionListener
                }

                val table = solver.tableFactory()
                solver.ourTable = table
                updateTablePreviewText(formatTable(table, solver.tableSize))

                val cleanInput = txtInput.text.replace(" ", "")
                val desorted = solver.desortFormColumns(cleanInput)
                if (desorted != null) {
                    txtUnsortedMatrix.text = formatMatrix(desorted)
                }

                val result = solver.decrypt(txtInput.text)
                txtOutput.text = result

            } catch (e: Exception) {
                e.printStackTrace()
                JOptionPane.showMessageDialog(this, "Chyba pri dešifrovaní: ${e.message}", "Chyba", JOptionPane.ERROR_MESSAGE)
            }
        }
    }

    private fun formatTable(table: Array<CharArray>, size: ThirdSolution.TableSizes): String {
        val headers = if (size == ThirdSolution.TableSizes.SMALL) listOf('A', 'D', 'F', 'G', 'X') else listOf('A', 'D', 'F', 'G', 'V', 'X')
        val sb = StringBuilder()
        sb.append("    ").append(headers.joinToString(" ")).append("\n")
        table.forEachIndexed { index, row ->
            sb.append("${headers[index]} | ").append(row.joinToString(" ")).append("\n")
        }
        return sb.toString()
    }

    private fun formatMatrix(matrix: List<List<String>>): String {
        if (matrix.isEmpty()) return ""
        val maxRows = matrix.maxOfOrNull { it.size } ?: 0
        val sb = StringBuilder()
        for (row in 0 until maxRows) {
            val rowElements = mutableListOf<String>()
            for (col in matrix) {
                rowElements.add(if (row < col.size) col[row] else " ")
            }
            sb.append(rowElements.joinToString("   ")).append("\n")
        }
        return sb.toString()
    }

    private fun mapMessageForPreview(input: String, table: Array<CharArray>, size: ThirdSolution.TableSizes): String {
        val output = StringBuilder()
        val usableHeaders = if (size == ThirdSolution.TableSizes.SMALL) charArrayOf('A', 'D', 'F', 'G', 'X') else charArrayOf('A', 'D', 'F', 'G', 'V', 'X')
        input.forEach { ch ->
            table.forEachIndexed { rIndex, row ->
                val cIndex = row.indexOf(ch)
                if (cIndex != -1) {
                    output.append(usableHeaders[rIndex]).append(usableHeaders[cIndex])
                }
            }
        }
        return output.toString()
    }
}