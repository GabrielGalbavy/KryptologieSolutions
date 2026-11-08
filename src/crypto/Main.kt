package crypto

import crypto.algorithms.FirstSolution
import crypto.algorithms.FourthSolution
import crypto.algorithms.SecondSolution
import crypto.algorithms.ThirdSolution
import crypto.models.Solver
import crypto.ui.WelcomeFrame
import javax.swing.SwingUtilities

val availableSolutions: Array<Solver> = arrayOf(
    FirstSolution(), SecondSolution(), ThirdSolution()
)
val availableCiphers = availableSolutions.map { it.whoAmI() }.toTypedArray()


fun main() {

    FourthSolution().generateKeys()

    SwingUtilities.invokeLater { val window = WelcomeFrame(); window.isVisible = true }; return
}