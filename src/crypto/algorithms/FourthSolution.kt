package crypto.algorithms

import crypto.models.Solver
import java.math.BigInteger
import java.security.SecureRandom

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

    private fun rabinMiller(n: BigInteger): Boolean {
        TODO("required for digital signature down the road")
    }

    private fun setRandomPrimes() {
        this.p = primeGenerator()
        this.q = primeGenerator()
    }

    private fun primeGenerator(): BigInteger {
        val tailSet = listOf(1, 3, 5, 7, 9)
        val source = SecureRandom()

        val first = (source.nextInt(9) + 1).toString() // first number cant be 0
        val body = (1..this.primeLength - 2).map { source.nextInt(10) }.joinToString("")
        val last = tailSet[source.nextInt(tailSet.size)].toString() // number must be odd

        return "$first$body$last".toBigInteger() // [1-9] + 12..n digits + odd number
    }
}