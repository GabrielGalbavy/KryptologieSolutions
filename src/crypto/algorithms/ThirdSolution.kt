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

    private fun tableFactory(seed: Int? = this.seed): Array<CharArray> {
        TODO("gl lol")
    }
}