package crypto.algorithms

import crypto.models.Solver
import kotlin.random.Random

class ThirdSolution : Solver() {
    override val name: String = "ADFG(V)X šifra"

    override fun whoAmI(): String {
        return this.name
    }


    //////// variables ////////

    var seed: Int = 123
    var keyword: String? = null
    var ourTable: Array<CharArray>? = null

    var tableLanguage: Tables = Tables.CZ
    var tableSize: TableSizes = TableSizes.SMALL

    val smallHeader = charArrayOf('A', 'D', 'F', 'G', 'X')
    val largeHeader = charArrayOf('A', 'D', 'F', 'G', 'V', 'X')
    val smallAlphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val largeAlphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    var smallTable: Array<CharArray> = Array(5) { CharArray(5) }
    var bigTable: Array<CharArray> = Array(6) { CharArray(6) }

    enum class Tables {
        CZ, // masking W as V
        EN // masking J as I
    }

    enum class TableSizes {
        SMALL, // ADFGX
        LARGE // ADFGVX
    }

    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        TODO("Not yet implemented")
    }

    override fun decrypt(input: String): String {
        TODO("Not yet implemented")
    }

    override fun filter(input: String): String {
        TODO("Not yet implemented")
    }


    //////// Key-related functions ////////

    //////// other functions ////////

    fun tableFactory(): Array<CharArray> {
        val size: Int
        val randomOperator = Random(this.seed)
        val rawAlphabet: String

        when (this.tableSize) {
            TableSizes.SMALL -> {
                size = 5
                rawAlphabet = when (this.tableLanguage) {
                    Tables.CZ -> smallAlphabet.replace("W", "")
                    Tables.EN -> smallAlphabet.replace("J", "")
                }
            }

            TableSizes.LARGE -> {
                size = 6
                rawAlphabet = largeAlphabet
            }
        }

        val shuffledList = rawAlphabet.toList().shuffled(randomOperator)

        // constructing the table with power of math and shi
        return Array(size) { rowIndex ->
            CharArray(size) { colIndex ->
                shuffledList[rowIndex * size + colIndex]
            }
        }
    }


    private fun formFactory(): Map<Char, List<Char>> {
        TODO("Not yet implemented")
    }


    private fun mapMessage(input: String, table: Array<CharArray>): String {
        val output = StringBuilder()
        val usableHeaders = when (this.tableSize) {
            TableSizes.SMALL -> {
                smallHeader
            }

            TableSizes.LARGE -> {
                largeHeader
            }
        }

        input.forEach { ch ->
            val coords = locateTargetCoords(table, ch)
            if (coords != null)
                output.append(usableHeaders[coords.first]).append(usableHeaders[coords.second])
        }
        return output.toString()
    }

    private fun locateTargetCoords(table: Array<CharArray>, target: Char): Pair<Int, Int>? {
        table.forEachIndexed { rowIndex, row ->
            val colIndex = row.indexOf(target)
            if (colIndex != -1) {
                return Pair(rowIndex, colIndex)
            }
        }
        return null
    }


    private fun extractMessage() {
        TODO("Not yet implemented")
    }

    private fun actuallyIAmHereToInterpret(input: String): String {
        val output = when (tableLanguage) {
            Tables.EN -> {
                input.replace('J', 'I')
            }

            Tables.CZ -> {
                input.replace('W', 'V')
            }
        }
        // masking letters could be elevated to dedicated table
        return output
    }
}