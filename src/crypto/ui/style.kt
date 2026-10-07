package crypto.ui

import crypto.models.ColorPalette
import crypto.models.ThemeModel
import java.awt.Color
import java.awt.Font

data class ColorPaletteDark(
    override val bgPrimary: Color = Color(18, 20, 40),
    override val bgSecondary: Color = Color(28, 32, 60),
    override val bgInput: Color = Color(35, 40, 75),
    override val textPrimary: Color = Color(212, 230, 230),
    override val textSecondary: Color = Color(50, 100, 200),
    override val textMuted: Color = Color(130, 145, 175),
    override val accent: Color = Color(0, 150, 255),
    override val border: Color = Color(45, 52, 90)
) : ColorPalette

//data class ColorPaletteWhite (
//
//)


object AppTheme : ThemeModel() {
    override val dark = ColorPaletteDark()
    override val fontTitle = Font("SansSerif", Font.BOLD, 22)
    override val fontBody = Font("SansSerif", Font.PLAIN, 14)

    val gradientColors = arrayOf(intArrayOf(80, 8, 100), intArrayOf(20, 50, 100))
}