package crypto.ui

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class SecondSolutionFrame : JFrame() {
    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(1200, 700)
        setLocationRelativeTo(null)
        layout = BorderLayout()


        ////////// style //////////

        val colorPackage = AppTheme.light


        ////////// backend and logic //////////

        ////////// components //////////

        // labels

        val headerLabel = JLabel("PlayFair šifra", SwingConstants.CENTER).apply {
        }


        val backBtn = JButton("Menu").apply {
            background = colorPackage.menuPurple
            foreground = colorPackage.textWhite

            font = font.deriveFont(Font.BOLD, 16f)

            addActionListener {
                val welcomeFrame = WelcomeFrame()
                welcomeFrame.isVisible = true
                dispose()
            }
        }


        ////////// panels //////////

        // center structure

        val bodyLeftSubPanel = JPanel(BorderLayout()).apply {
            background = Color.CYAN
            preferredSize = Dimension(400, 0)
        }

        val bodyCenterSubPanel = JPanel(BorderLayout()).apply {
            background = Color.RED
        }

        val bodyRightSubPanel = JPanel(BorderLayout()).apply {
            background = Color.BLUE
            preferredSize = Dimension(400, 0)
        }

        // window structure

        val headerPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(0, 80)
            background = Color.DARK_GRAY
        }

        val bodyPanel = JPanel(BorderLayout()).apply {


        }

        val footerPanel = JPanel(BorderLayout()).apply {
            preferredSize = Dimension(0, 220)
            background = Color(0, 0, 0)
        }


        ////////// implementation //////////

        bodyPanel.apply {
            add(bodyLeftSubPanel, BorderLayout.WEST)
            add(bodyCenterSubPanel, BorderLayout.CENTER)
            add(bodyRightSubPanel, BorderLayout.EAST)
        }

        this.apply {
            add(headerPanel, BorderLayout.NORTH)
            add(bodyPanel, BorderLayout.CENTER)
            add(footerPanel, BorderLayout.SOUTH)
        }
    }
}