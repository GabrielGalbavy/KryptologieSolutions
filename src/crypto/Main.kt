package crypto

import crypto.algorithms.FirstSolution

val WORKING_ALGORITHMS = arrayOf("test", "test_two")

// This is where the fun begins
fun main() {
    /*
    SwingUtilities.invokeLater {
        val frame = MainFrame()
        frame.btnProcess.addActionListener {
            val text = frame.inputField.text
            frame.outputField.text = text
        }

        frame.isVisible = true
    }
    */
    println("Solve for One:")
    val firstSolver = FirstSolution()
    println((firstSolver.washTheMessage("toto je moja sprava @%*^(#$%*(@)_^# !@ %@ %$ %@$ FGSDG 4231F")))
}
