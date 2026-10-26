package crypto.algorithms

import crypto.models.Solver

class ThirdSolution : Solver() {
    override val name: String = "ADFG(V)X šifra"

    override fun whoAmI(): String {
        return this.name
    }


    //////// variables ////////

    var seed: Int? = null

    enum class Tables {
        CZ, // masking W as V
        EN // masking J as I
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

        // whatever
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
        TODO()
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