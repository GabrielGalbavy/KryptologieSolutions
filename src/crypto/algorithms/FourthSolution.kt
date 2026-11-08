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

    // keys
    var publicKey: Pair<BigInteger, BigInteger>? = null
    var privateKey: Pair<BigInteger, BigInteger>? = null

    // key components
    var n: BigInteger? = null
    var d: BigInteger? = null
    var e: BigInteger = 65537.toBigInteger()

    // prime numbers
    var p: BigInteger? = null
    var q: BigInteger? = null
    var primeLength = 14 // amount of digits in prime number, 14 is minimum
    val messageChuckSize = 8
    val bitsPerChar = 8
    val paddingChar = "00000000" // in case the last chunk is not 8 chars long

    val k: Int = 128 // Rabin Miller test depth, chance of false right is 4^-128 = enough


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

    fun generateKeys() {
        var firstNumberValid = false
        var secondNumberValid = false
        var testedKeys = 0
        do {
            if (!firstNumberValid) {
                setRandomPrimes(1)
                testedKeys++
            }
            if (!secondNumberValid) {
                setRandomPrimes(2)
                testedKeys++
            }

            firstNumberValid = rabinMillerTest(p!!)
            secondNumberValid = rabinMillerTest(q!!)

            if (firstNumberValid && secondNumberValid && p == q) {
                secondNumberValid = false // numbers must be different, chance of hitting this is astronomical
                testedKeys++
            }
        } while (!firstNumberValid || !secondNumberValid) // keep generating until we have two usable numbers

        println(p)
        println(q)
        println(testedKeys)
    }

    //////// other functions ////////

    private fun rabinMillerTest(testedNumber: BigInteger): Boolean {
        /**
         * step 1.) find n-1 = 2^k * m
         */
        val nMinusOne = testedNumber.subtract(BigInteger.ONE)
        var m: BigInteger = nMinusOne
        val source = SecureRandom()
        var s = 0

        while (m.remainder(BigInteger.TWO) == BigInteger.ZERO) { // dividing until we get not whole number
            m = m.divide(BigInteger.TWO)
            s++
        }

        for (i in 1..k) {
            /**
             * step 2.) pick value witness, where witness: 1 < a < n-1
             */
            var witness: BigInteger // a number which considers n is prime/composite number

            do {
                witness = BigInteger(testedNumber.bitLength(), source)
                // generates random number with same bit length as n
            } while (witness <= BigInteger.ONE || witness >= testedNumber.subtract(BigInteger.ONE))

            /** step 3.) b = a^m mod(n) */

            var b = witness.modPow(m, testedNumber)

            if (b == BigInteger.ONE || b == nMinusOne) {
                continue // testedNumber is considered prime for provided witness
            }
            /** b is neither 1/-1, running deeper test loop */
            var isComposite = true // testedNumber is considered composite until proven otherwise

            for (r in 1..<s) {
                b = b.modPow(BigInteger.TWO, testedNumber)
                if (b == BigInteger.ONE) {
                    return false // we can definitely say testedNumber is composite
                } else if (b == nMinusOne) {
                    isComposite = false
                    break // testedNumber is considered prime for provided witness
                }
            }

            if (isComposite) {
                return false
            }
        }
        return true // testedNumber is definitely maybe prime
    }

    private fun setRandomPrimes(mode: Int = 3) {
        when (mode) {
            1 -> p = primeGenerator()
            2 -> q = primeGenerator()
            3 -> {
                p = primeGenerator(); q = primeGenerator()
            }
        }
    }

    private fun primeGenerator(): BigInteger {
        val tailSet = listOf(1, 3, 5, 7, 9)
        val source = SecureRandom()

        val first = (source.nextInt(9) + 1).toString() // first number cant be 0
        val body = (1..(primeLength - 2)).map { source.nextInt(10) }.joinToString("")
        val last = tailSet[source.nextInt(tailSet.size)].toString() // number must be odd

        return "$first$body$last".toBigInteger() // [1-9] + 12..n digits + odd number
    }
}