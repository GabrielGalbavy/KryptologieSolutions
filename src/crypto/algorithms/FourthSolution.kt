package crypto.algorithms

import crypto.models.Solver
import java.math.BigInteger

class FourthSolution : Solver() {
    override val name: String = "RSA šifra"

    override fun whoAmI(): String {
        return name
    }


    //////// variables ////////




    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        TODO("Not yet implemented")
    }

    override fun decrypt(input: String): String {
        TODO("Not yet implemented")
    }

    override fun filter(input: String): String {
        TODO("Not yet implemented, may not be implemented at all as we use UTF-8 encoding")
    }


    //////// Key-related functions ////////

    private fun generateKeys(): Pair<BigInteger, BigInteger> {
        TODO("damn")
    }

    //////// other functions ////////

    private fun rabinMiller(){
        TODO("required for digital signature down the road")
    }
}