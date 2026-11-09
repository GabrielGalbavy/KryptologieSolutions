package crypto.gui

import crypto.algorithms.FourthSolution
import crypto.algorithms.FourthSolution.EncodingTypes
import crypto.ui.WelcomeFrame
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class FourthSolutionFrame : JFrame("RSA Šifra") {

    private val backend = FourthSolution()

    // --- FARBY ZO SCREENSHOTU ---
    private val colorMenuPurple = Color.decode("#C481FF")
    private val colorInputBlue = Color.decode("#587F97")
    private val colorDecryptOrange = Color.decode("#F04C18")
    private val colorEncryptBlue = Color.decode("#2955E6")
    private val colorGenerateMint = Color.decode("#58F2C2")
    private val colorKeySectorBg = Color.decode("#ECA400")
    private val colorEncodingBg = Color.decode("#7C93A2")
    private val colorToggleActive = Color.decode("#BC515B")
    private val colorToggleInactive = Color.decode("#9E9E9E")
    private val colorOutputPurple = Color.decode("#664CFF")
    private val colorRedX = Color.decode("#E62020")

    // --- FONT ---
    private val mainFont = Font("Monospaced", Font.BOLD, 18)
    private val titleFont = Font("Serif", Font.BOLD, 22)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(1200, 800)
        setLocationRelativeTo(null)
        layout = BorderLayout(20, 20)
        contentPane.background = Color.decode("#FFFFFF")
        (contentPane as JPanel).border = EmptyBorder(20, 20, 20, 20)

        // ==========================================
        // 1. HORNÁ ČASŤ - PRELÍNAJÚCI SA GRIDBAG LAYOUT
        // ==========================================
        val topPanel = JPanel(GridBagLayout())
        topPanel.background = Color.decode("#FFFFFF")
        val gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.BOTH
        gbc.insets = Insets(10, 10, 10, 10)

        // --- MAGICKÝ "NEVIDITEĽNÝ" RIADOK PRE VYNÚTENIE TRETÍN ---
        gbc.gridy = 0; gbc.gridheight = 1; gbc.weighty = 0.0

        gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0.33
        topPanel.add(Box.createHorizontalStrut(1), gbc) // Ľavý stĺpec (33%)

        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 0.34
        topPanel.add(Box.createHorizontalStrut(1), gbc) // Stredný prelievací stĺpec (34%)

        gbc.gridx = 2; gbc.gridwidth = 1; gbc.weightx = 0.33
        topPanel.add(Box.createHorizontalStrut(1), gbc) // Pravý stĺpec (33%)


        // ---------------- MENU & RSA NADPIS (Vľavo Hore - 1/3) ----------------
        gbc.gridy = 1; gbc.weighty = 0.55

        val leftTopPanel = JPanel(BorderLayout(0, 20))
        leftTopPanel.background = Color.decode("#FFFFFF")
        val backBtn = JButton("Menu").apply {
            background = colorMenuPurple
            foreground = Color.decode("#E3E3E3")
            font = font.deriveFont(Font.BOLD, 16f)

            addActionListener {
                val welcomeFrame = WelcomeFrame()
                welcomeFrame.isVisible = true
                dispose()
            }
        }
        backBtn.addActionListener { dispose() }
        val menuWrapper = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0)).apply { background = Color.decode("#FFFFFF") }
        menuWrapper.add(backBtn)

        // Opravený horizontálny RSA nadpis
        val rsaLabel = JLabel(
            "<html><center>" +
                    "<font color='#8A2BE2' size='+4'><b>RSA</b></font><br>" +
                    "<font color='black' size='+1'>RIVEST SHAMIR ADLEMAN</font>" +
                    "</center></html>",
            SwingConstants.CENTER
        )
        rsaLabel.font = Font("Monospaced", Font.PLAIN, 18)

        leftTopPanel.add(menuWrapper, BorderLayout.NORTH)
        leftTopPanel.add(rsaLabel, BorderLayout.CENTER)

        gbc.gridx = 0; gbc.gridwidth = 1
        topPanel.add(leftTopPanel, gbc)

        // ---------------- MODRÝ INPUT (Vpravo Hore - 2/3) ----------------
        val inputTextArea = JTextArea("THIS IS MY SECRET MESSAGE FOR ENCRYPTION.")
        inputTextArea.background = colorInputBlue; inputTextArea.foreground = Color.WHITE; inputTextArea.font =
            Font("Monospaced", Font.PLAIN, 16)
        inputTextArea.margin = Insets(15, 15, 15, 15); inputTextArea.lineWrap = true
        val inputScroll = JScrollPane(inputTextArea).apply { border = null }

        gbc.gridx = 1; gbc.gridwidth = 2 // Zaberá stredný a pravý stĺpec
        topPanel.add(inputScroll, gbc)

        // ---------------- KEY SECTOR (Vľavo Dole - 2/3) ----------------
        gbc.gridy = 2; gbc.weighty = 0.45

        val keySectorPanel = buildKeySector()
        gbc.gridx = 0; gbc.gridwidth = 2 // Zaberá ľavý a stredný stĺpec
        topPanel.add(keySectorPanel, gbc)

        // ---------------- AKCIE & ENCODING (Vpravo Dole - 1/3) ----------------
        val rightBottomPanel = JPanel(GridBagLayout())
        rightBottomPanel.background = Color.decode("#FFFFFF")
        val rbGbc = GridBagConstraints()
        rbGbc.fill = GridBagConstraints.BOTH; rbGbc.weightx = 1.0

        // Tlačidlá
        rbGbc.gridy = 0; rbGbc.weighty = 0.4
        val btnPanel = JPanel(FlowLayout(FlowLayout.CENTER, 5, 10)).apply { background = Color.decode("#FFFFFF") }
        val generateKeysBtn = createFlatButton("GENERATE KEYS", colorGenerateMint, Color.BLACK).apply {
            preferredSize = Dimension(180, 45)
        }
        val decryptBtn =
            createFlatButton("DECRYPT", colorDecryptOrange, Color.WHITE).apply { preferredSize = Dimension(115, 45) }
        val encryptBtn =
            createFlatButton("ENCRYPT", colorEncryptBlue, Color.WHITE).apply { preferredSize = Dimension(115, 45) }
        btnPanel.add(generateKeysBtn); btnPanel.add(decryptBtn); btnPanel.add(encryptBtn)
        rightBottomPanel.add(btnPanel, rbGbc)

        // Encoding Standard
        rbGbc.gridy = 1; rbGbc.weighty = 0.6
        val encodingPanel =
            JPanel(BorderLayout(10, 15)).apply { background = colorEncodingBg; border = EmptyBorder(10, 15, 15, 15) }
        encodingPanel.add(
            JLabel(
                "<html><center>MESSAGE ENCODING<br>STANDARD</center></html>",
                SwingConstants.CENTER
            ).apply { font = Font("Monospaced", Font.BOLD, 15) }, BorderLayout.NORTH
        )
        val toggleWrapper = JPanel(GridLayout(2, 1, 0, 10)).apply { isOpaque = false }
        val utf8Btn = createFlatButton("UTF-8", colorToggleActive, Color.WHITE)
        val utf16Btn = createFlatButton("UTF-16", colorToggleInactive, Color.WHITE)
        toggleWrapper.add(utf8Btn); toggleWrapper.add(utf16Btn)
        encodingPanel.add(toggleWrapper, BorderLayout.CENTER)

        val narrowEncodingWrapper =
            JPanel(BorderLayout()).apply { background = Color.decode("#FFFFFF"); border = EmptyBorder(10, 30, 10, 30) }
        narrowEncodingWrapper.add(encodingPanel, BorderLayout.CENTER)
        rightBottomPanel.add(narrowEncodingWrapper, rbGbc)

        gbc.gridx = 2; gbc.gridwidth = 1 // Zaberá iba pravý stĺpec
        topPanel.add(rightBottomPanel, gbc)


        // ==========================================
        // 2. SPODNÁ ČASŤ - FIALOVÝ VÝSTUP
        // ==========================================
        val outputTextArea = JTextArea().apply {
            background = colorOutputPurple; foreground = Color.WHITE; font = Font("Monospaced", Font.PLAIN, 16)
            margin = Insets(15, 15, 15, 15); lineWrap = true; isEditable = false
        }
        val outputScroll = JScrollPane(outputTextArea).apply { border = null; preferredSize = Dimension(1000, 180) }
        add(topPanel, BorderLayout.CENTER)
        add(outputScroll, BorderLayout.SOUTH)


        // ==========================================
        // 3. LOGIKA A EVENT LISTENERI
        // ==========================================
        val localPublicNField = findComponentByName(keySectorPanel, "localPublicNField") as JTextField
        val localPublicEField = findComponentByName(keySectorPanel, "localPublicEField") as JTextField
        val localPrivateNField = findComponentByName(keySectorPanel, "localPrivateNField") as JTextField
        val localPrivateDField = findComponentByName(keySectorPanel, "localPrivateDField") as JPasswordField
        val keyLengthField = findComponentByName(keySectorPanel, "keyLengthField") as JTextField
        val remotePublicKeyArea = findComponentByName(keySectorPanel, "remotePublicKeyArea") as JTextArea

        // Nové ovládanie pre Show/Hide tlačidlo privátneho kľúča
        val toggleKeyBtn = findComponentByName(keySectorPanel, "toggleKeyBtn") as JButton
        val defaultEchoChar = localPrivateDField.echoChar

        toggleKeyBtn.addActionListener {
            if (localPrivateDField.echoChar == '\u0000') {
                localPrivateDField.echoChar = defaultEchoChar
                toggleKeyBtn.text = "SHOW"
                toggleKeyBtn.background = Color.decode("#8A2BE2") // Fialová
            } else {
                localPrivateDField.echoChar = '\u0000'
                toggleKeyBtn.text = "HIDE"
                toggleKeyBtn.background = colorToggleInactive // Sivá
            }
        }

        utf8Btn.addActionListener {
            backend.setEncodingType(EncodingTypes.UTF8)
            utf8Btn.background = colorToggleActive; utf16Btn.background = colorToggleInactive
            outputTextArea.text = "Encoding switched to UTF-8 (ASCII mode)."
        }

        utf16Btn.addActionListener {
            backend.setEncodingType(EncodingTypes.UTF16)
            utf16Btn.background = colorToggleActive; utf8Btn.background = colorToggleInactive
            outputTextArea.text = "Encoding switched to UTF-16 (Full Charset mode)."
        }

        generateKeysBtn.addActionListener {
            try {
                backend.primeLength = keyLengthField.text.toInt()
                backend.generateKeys()
                localPublicNField.text = backend.localPublicKey?.first.toString()
                localPublicEField.text = backend.localPublicKey?.second.toString()
                localPrivateNField.text = backend.localPrivateKey?.first.toString()
                localPrivateDField.text = backend.localPrivateKey?.second.toString()
                outputTextArea.text = "Keys successfully generated and loaded into local memory."
            } catch (e: Exception) {
                outputTextArea.text = "Error generating keys. Check the length parameter."
            }
        }

        encryptBtn.addActionListener {
            val remoteKeyRaw = remotePublicKeyArea.text.trim()
            if (remoteKeyRaw.isEmpty()) {
                outputTextArea.text =
                    "ERROR: Remote Public Key is missing! Enter 'N E' into the white area in Key Sector."
                return@addActionListener
            }
            try {
                val parts = remoteKeyRaw.split(Regex("\\s+"))
                backend.remotePublicKey = Pair(parts[0].toBigInteger(), parts[1].toBigInteger())
                outputTextArea.text = backend.encrypt(inputTextArea.text)
            } catch (e: Exception) {
                outputTextArea.text =
                    "ERROR: Invalid Remote Public Key format. Ensure it's two numbers separated by a space."
            }
        }

        decryptBtn.addActionListener {
            if (backend.localPrivateKey == null) {
                outputTextArea.text = "ERROR: Local Private Key is missing! Generate keys first."
                return@addActionListener
            }
            try {
                outputTextArea.text = backend.decrypt(inputTextArea.text)
            } catch (e: Exception) {
                outputTextArea.text = "ERROR: Decryption failed. Verify the input block and your Local Keys."
            }
        }
    }


    // ==========================================
    // POMOCNÉ FUNKCIE
    // ==========================================

    private fun buildKeySector(): JPanel {
        val panel = JPanel(GridBagLayout())
        panel.background = colorKeySectorBg; panel.border = EmptyBorder(15, 15, 15, 15)
        val kGbc = GridBagConstraints().apply { fill = GridBagConstraints.HORIZONTAL; insets = Insets(5, 8, 5, 8) }

        kGbc.gridx = 0; kGbc.gridy = 0; kGbc.gridwidth = 4
        panel.add(JLabel("KEY SECTOR", SwingConstants.CENTER).apply { font = titleFont }, kGbc)

        kGbc.gridwidth = 1; kGbc.gridy = 1; kGbc.weightx = 0.0
        panel.add(JLabel("KEY LENGTH").apply { font = mainFont }, kGbc)
        kGbc.gridx = 1; kGbc.weightx = 0.2
        panel.add(JTextField(backend.primeLength.toString(), 5).apply { name = "keyLengthField" }, kGbc)

        kGbc.gridx = 0; kGbc.gridy = 2; kGbc.weightx = 0.0
        panel.add(JLabel("<html>RABIN MILLER<br>TEST DEPTH</html>").apply { font = mainFont }, kGbc)
        kGbc.gridx = 1; kGbc.weightx = 0.2
        panel.add(JTextField(backend.k.toString(), 5), kGbc)

        kGbc.gridx = 0; kGbc.gridy = 3; kGbc.weightx = 0.0
        panel.add(JLabel("EXPONENT").apply { font = mainFont }, kGbc)
        kGbc.gridx = 1; kGbc.weightx = 0.2
        panel.add(JTextField(backend.e.toString(), 5), kGbc)

        kGbc.gridx = 2; kGbc.gridy = 1; kGbc.gridheight = 3; kGbc.weightx = 0.6; kGbc.fill = GridBagConstraints.BOTH
        val remoteWrapper = JPanel(BorderLayout()).apply { isOpaque = false }
        remoteWrapper.add(JLabel("<html><center>REMOTE PUBLIC KEY<br><font size='-2'>FOR ENCRYPTION</font></center></html>").apply {
            font = mainFont; horizontalAlignment = SwingConstants.CENTER
        }, BorderLayout.NORTH)
        remoteWrapper.add(
            JScrollPane(JTextArea().apply { name = "remotePublicKeyArea"; lineWrap = true }),
            BorderLayout.CENTER
        )
        panel.add(remoteWrapper, kGbc)

        kGbc.gridx = 0; kGbc.gridy = 4; kGbc.gridwidth = 4; kGbc.gridheight = 1; kGbc.fill =
            GridBagConstraints.HORIZONTAL
        panel.add(JLabel("LOCAL KEYS").apply { font = Font("Serif", Font.BOLD, 18) }, kGbc)

        kGbc.gridy = 5; kGbc.gridwidth = 1; kGbc.weightx = 0.0
        panel.add(JLabel(""), kGbc)
        kGbc.gridx = 1; kGbc.weightx = 0.7; panel.add(JLabel("P x Q (Modul N)", SwingConstants.CENTER).apply {
            font = Font("SansSerif", Font.BOLD, 10)
        }, kGbc)
        kGbc.gridx = 2; kGbc.weightx = 0.3; panel.add(JLabel("EXPONENT", SwingConstants.CENTER).apply {
            font = Font("SansSerif", Font.BOLD, 10)
        }, kGbc)

        kGbc.gridx = 0; kGbc.gridy = 6; kGbc.weightx = 0.0
        panel.add(JLabel("PUBLIC").apply { font = mainFont }, kGbc)
        kGbc.gridx = 1; kGbc.weightx = 0.7
        panel.add(JTextField().apply { name = "localPublicNField"; isEditable = false }, kGbc)
        kGbc.gridx = 2; kGbc.weightx = 0.3
        panel.add(JTextField().apply { name = "localPublicEField"; isEditable = false }, kGbc)

        kGbc.gridx = 0; kGbc.gridy = 7; kGbc.weightx = 0.0
        panel.add(JLabel("PRIVATE").apply { font = mainFont }, kGbc)
        kGbc.gridx = 1; kGbc.weightx = 0.7
        panel.add(JTextField().apply { name = "localPrivateNField"; isEditable = false }, kGbc)

        val privateWrapper = JPanel(BorderLayout(5, 0)).apply { isOpaque = false }

        // Vytvoríme JPasswordField s fixnou šírkou, aby sa pri prepínaniu textu hýbalo pole
        val privateField = JPasswordField().apply {
            name = "localPrivateDField"
            isEditable = false
            // Nastavíme pevnú šírku, napr. 170px, výška kopíruje štandard (cca 28px)
            preferredSize = Dimension(170, 28)
        }
        privateWrapper.add(privateField, BorderLayout.CENTER)

        // Tlačidlo SHOW pre odhalenie privátneho kľúča
        val toggleKeyBtn = createFlatButton("SHOW", Color.decode("#8A2BE2"), Color.WHITE).apply {
            name = "toggleKeyBtn"
            margin = Insets(0, 8, 0, 8)
            font = Font("Monospaced", Font.BOLD, 12)
        }
        privateWrapper.add(toggleKeyBtn, BorderLayout.EAST)

        kGbc.gridx = 2; kGbc.weightx = 0.3
        panel.add(privateWrapper, kGbc)

        return panel
    }

    private fun createFlatButton(text: String, bgColor: Color, fgColor: Color): JButton {
        return JButton(text).apply {
            background = bgColor; foreground = fgColor; font = Font("Monospaced", Font.BOLD, 16)
            isOpaque = true; isBorderPainted = false; isFocusPainted = false; cursor = Cursor(Cursor.HAND_CURSOR)
        }
    }

    private fun findComponentByName(parent: Container, name: String): Component? {
        for (comp in parent.components) {
            if (comp.name == name) return comp
            if (comp is Container) {
                val found = findComponentByName(comp, name)
                if (found != null) return found
            }
        }
        return null
    }
}