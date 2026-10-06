package crypto.ui

import java.awt.Color
import java.awt.Font

sealed class ColorPalette {                 // use this layout for other themes

    abstract val bgPrimary: Color           // Hlavné pozadie okna (#121428)
    abstract val bgSecondary: Color         // Pozadie pre kartičky a panely
    abstract val bgInput: Color             // Pozadie pre JTextArea / JTextField
    abstract val textPrimary: Color         // Hlavný text (#D4E6E6)
    abstract val textMuted: Color           // Popisky, titulky, nápovedy
    abstract val accent: Color              // Hlavné tlačidlo / aktívny prvok
    abstract val border: Color              // Ohraničenia vstupov a panelov
}

data class ColorPaletteDark(
    val bgPrimary: Color = Color(18, 20, 40),
    val bgSecondary: Color = Color(28, 32, 60),
    val bgInput: Color = Color(35, 40, 75),
    val textPrimary: Color = Color(212, 230, 230),
    val textMuted: Color = Color(130, 145, 175),
    val accent: Color = Color(0, 150, 255),
    val border: Color = Color(45, 52, 90)
)

//data class ColorPaletteWhite (
//
//)


object AppTheme {
    val dark = ColorPaletteDark()
    val fontTitle = Font("SansSerif", Font.BOLD, 22)
    val fontBody = Font("SansSerif", Font.PLAIN, 14)
}