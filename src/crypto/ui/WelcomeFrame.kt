package crypto.ui

import crypto.availableSolutions
import java.awt.*
import javax.swing.*

class GradientPanel(
    var startColor: Color = Color(18, 20, 40), var endColor: Color = Color(35, 45, 85), var isVertical: Boolean = true
) : JPanel() {

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D

        // Zapnutie vyhladzovania pre kvalitnejší render
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        val x2 = if (isVertical) 0f else width.toFloat()
        val y2 = if (isVertical) height.toFloat() else 0f

        // Vytvorenie prechodu od (0,0) po koniec panela
        val gradient = GradientPaint(0f, 0f, startColor, x2, y2, endColor)

        g2d.paint = gradient
        g2d.fillRect(0, 0, width, height)
    }
}

class WelcomeFrame() : JFrame("Crypto Solver") {


    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(400, 300) // in px
        setLocationRelativeTo(null) // centering window to middle of the screen
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

        ////////// components //////////

        // header label
        val headerLabel = JLabel("Welcome to Crypto Solver", SwingConstants.CENTER).apply {
            foreground = colorPackage.textPrimary
            font = font.deriveFont(Font.BOLD, 20f)
            border = BorderFactory.createEmptyBorder(
                20, 0, 10, 0
            ) // Margins: 20px up, 10px down
        }

        // select menu
        val cipherOptions = availableSolutions
        val cipherComboBox = JComboBox(cipherOptions).apply {
            maximumSize = Dimension(250, 30) // width limit
            alignmentX = CENTER_ALIGNMENT

            background = colorPackage.bgInput
            foreground = colorPackage.textPrimary
            font = AppTheme.fontBody
        }

        // start button
        val startBtn = JButton("Start").apply {
            border = BorderFactory.createEmptyBorder(8, 15, 8, 15)
            alignmentX = CENTER_ALIGNMENT

            background = colorPackage.accent
            foreground = Color.WHITE
            font = AppTheme.fontBody.deriveFont(Font.BOLD)
            isFocusPainted = false

        }
        // button event listener
        startBtn.addActionListener {
            val selectedIndex = cipherComboBox.selectedIndex
            when (selectedIndex) {
                0 -> {
                    val solutionFrame = FirstSolutionFrame()
                    dispose()
                    solutionFrame.isVisible = true
                }

                1 -> {
                    val solutionFrame = SecondSolutionFrame()
                    dispose()
                    solutionFrame.isVisible = true
                }

                else -> JOptionPane.showMessageDialog(
                    this, "Pre túto šifru zatiaľ neexistuje UI."
                )
            }
        }


        ////////// panels //////////

        val centerPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(30, 40, 30, 40)
            background = Color(0, 0, 0, 0)

            add(cipherComboBox)
            add(Box.createRigidArea(Dimension(0, 20))) // 20px vertical space
            add(startBtn)
        }


        ////////// implementation //////////

        add(headerLabel, BorderLayout.NORTH)

        add(centerPanel, BorderLayout.CENTER)
    }
}
