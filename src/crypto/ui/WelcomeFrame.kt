package crypto.ui

import crypto.availableSolutions
import java.awt.*
import javax.swing.*

class WelcomeFrame() : JFrame("Crypto Solver") {

    val colorPackage = AppTheme.dark

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(400, 300) // in px
        setLocationRelativeTo(null) // centering window to middle of the screen
        layout = BorderLayout()

        ////////// components //////////

        // header label
        val headerLabel = JLabel("Welcome to Crypto Solver", SwingConstants.CENTER).apply {
            font = font.deriveFont(Font.BOLD, 20f)
            border = BorderFactory.createEmptyBorder(
                20, 0, 10, 0
            ) // Margins: 20px up, 10px down
        }

        // select menu
        val cipherOptions = availableSolutions
        val cipherComboBox = JComboBox(cipherOptions).apply {
            maximumSize = Dimension(250, 30) // width limit
            alignmentX = Component.CENTER_ALIGNMENT
        }

        // start button
        val startBtn = JButton("Start").apply {
            alignmentX = Component.CENTER_ALIGNMENT
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

            add(cipherComboBox)
            add(Box.createRigidArea(Dimension(0, 20))) // 20px vertical space
            add(startBtn)
        }


        ////////// implementation //////////

        add(headerLabel, BorderLayout.NORTH)

        add(centerPanel, BorderLayout.CENTER)
    }

}