package crypto.models

import java.awt.Font

abstract class ThemeModel {
    abstract val dark: ColorPalette
    abstract val fontTitle: Font
    abstract val fontBody: Font
}