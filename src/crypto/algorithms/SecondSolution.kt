package crypto.algorithms

import crypto.models.Solver

class SecondSolution : Solver() {
    override val name = "Playfair šifra"
    override fun whoAmI(): String {
        return this.name
    }


    //////// variables ////////

    val czTable: Array<CharArray> = arrayOf( // dropping W
        charArrayOf('A', 'B', 'C', 'D', 'E'),
        charArrayOf('F', 'G', 'H', 'I', 'J'),
        charArrayOf('K', 'L', 'M', 'N', 'O'),
        charArrayOf('P', 'Q', 'R', 'S', 'T'),
        charArrayOf('U', 'V', 'X', 'Y', 'Z')
    )
    val enTable: Array<CharArray> = arrayOf( // dropping J
        charArrayOf('A', 'B', 'C', 'D', 'E'),
        charArrayOf('F', 'G', 'H', 'I', 'K'),
        charArrayOf('L', 'M', 'N', 'O', 'P'),
        charArrayOf('Q', 'R', 'S', 'T', 'U'),
        charArrayOf('V', 'W', 'X', 'Y', 'Z')
    )

    val tables = listOf(czTable, enTable)

    var key: String = ""
        private set


    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        TODO("Not yet implemented")
    }

    override fun decrypt(input: String): String {
        TODO("Not yet implemented")
    }

}