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
    println("key A:")
    val keyA = readln().toInt()
    firstSolver.setKeyA(keyA)

    println("key B:")
    val keyB = readln().toInt()
    firstSolver.setKeyB(keyB)

    println("your message:")
    val ourMsg = readln()

    val res = firstSolver.encrypt(ourMsg)

    println("output:")
    println(res)
    println(firstSolver.decrypt(res))

//    var x = readln()
//    val firstSolver = FirstSolution()
//    println(firstSolver.decrypt(x))

}
