package crypto.algorithms

import crypto.models.Solver
import java.math.BigInteger

class FourthSolution : Solver() {
    override val name: String = "RSA šifra"

    override fun whoAmI(): String {
        return name
    }


    //////// variables ////////

    var p: BigInteger? = null
    var q: BigInteger? = null

    var e: BigInteger = 65537.toBigInteger()
    val k: Int = 40 // Rabin Miller test depth, chance of false right is 4^-40 = enough
    var primeLength = 14 // amount of digits in prime number, 14 is minimum


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