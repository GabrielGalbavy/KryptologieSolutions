package crypto

import crypto.algorithms.FirstSolution
import crypto.algorithms.SecondSolution
import crypto.models.Solver
import crypto.ui.WelcomeFrame
import javax.swing.SwingUtilities

val availableSolutions: Array<Solver> = arrayOf(
    FirstSolution(),
    SecondSolution()
)
val availableCiphers = availableSolutions.map { it.whoAmI() }.toTypedArray()

fun main() {
    SwingUtilities.invokeLater {
        val window = WelcomeFrame()
        window.isVisible = true
    }
}