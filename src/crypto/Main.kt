package crypto

import crypto.algorithms.FirstSolution
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

    val solverObj = ThirdSolution()

//    solverObj.keyword = "keyword"
//    solverObj.seed = 12345
//    solverObj.tableSize = ThirdSolution.TableSizes.LARGE
//    solverObj.tableLanguage = ThirdSolution.Tables.EN
//
//    val openText = "hi this is mý ťěšť íÍ 514287639 šžčýáwwwjjj ,.§/(:)"
//    val ciphertext = solverObj.encrypt(openText)
//    val decryptedText = solverObj.decrypt(ciphertext)
//    val filtered = solverObj.filter(openText)
//
//    println(filtered)
//    println(ciphertext)
//    println(decryptedText)
//    println(solverObj.ourTable.contentDeepToString())

    SwingUtilities.invokeLater {
        val window = WelcomeFrame()
        window.isVisible = true
    }

    return
}