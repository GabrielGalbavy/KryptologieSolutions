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


    enum class EncodingTypes {
        UTF8, UTF16
    }

    // keys

    var remotePublicKey: Pair<BigInteger, BigInteger>? = null

    var localPublicKey: Pair<BigInteger, BigInteger>? = null
    var localPrivateKey: Pair<BigInteger, BigInteger>? = null
    var testedKeys: Int = 0 // counts how many numbers were tested before keys were successfully generated

    // key components
    var n: BigInteger? = null
    var d: BigInteger? = null
    var e: BigInteger = 65537.toBigInteger()

    // prime numbers
    var p: BigInteger? = null
    var q: BigInteger? = null

    // encoding params

    var encodingStandard = EncodingTypes.UTF8
        private set
    var filerForUTF8 = false // idk if this is required
        private set
    var primeLength = 14 // amount of digits in prime number, 14 is minimum
    var messageChuckSize = 8
        private set
    var bitsPerChar = 8
        private set
    var bitsPerChunk = messageChuckSize * bitsPerChar // should be always 64
        private set
    var paddingCharStr = "00000000" // in case the last chunk is not 8 chars long
        private set
    val paddingCharCh = '\u0000'

    val k: Int = 128 // Rabin-Miller test depth, chance of false right is 4^-128 = enough


//////// cipher functions ////////

    override fun encrypt(input: String): String {
        if (remotePublicKey != null) {
            var input = input
            if (filerForUTF8) {
                input = washTheMessage(input)
            }

            val chunkedInput = input.chunked(messageChuckSize)
            val chunkedInputBin = bitCaster(chunkedInput)

            val chunkedInputBigInt = bigIntCaster(chunkedInputBin)

            val encryptedSets = chunkedInputBigInt.map { m ->
                m.modPow(remotePublicKey!!.second, remotePublicKey!!.first)
            }

            return encryptedSets.joinToString(" ")
        }
        return ""
    }

    override fun decrypt(input: String): String {
        if (localPrivateKey != null) {
            val encryptedChunkedInputBigInt = input.split(" ").map { it.toBigInteger() }
            val decryptedChunkedInputBigInt = encryptedChunkedInputBigInt.map { c ->
                c.modPow(localPrivateKey!!.second, localPrivateKey!!.first)
            }
            val decryptedChunkedInputBin = decryptedChunkedInputBigInt.map {
                it.toString(2).padStart(bitsPerChunk, '0')
            }.toMutableList()


            val decryptedMessage = StringBuilder()

            decryptedChunkedInputBin.joinToString("").chunked(bitsPerChar).forEach { binChar ->
                val ch = binChar.toInt(2).toChar()

                if (ch != paddingCharCh) { // filtering out end padding
                    decryptedMessage.append(ch)
                }
            }

//            println(decryptedChunkedInputBin.joinToString("").chunked(bitsPerChar).joinToString(" "))

            return decryptedMessage.toString()
        }
        return ""
    }


    override fun filter(input: String): String {
        TODO("Not yet implemented, may not be implemented at all as we can encode virtually anything")
    }


//////// Key-related functions ////////

    fun generateKeys() {
        var firstNumberValid = false
        var secondNumberValid = false
        testedKeys = 0
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

        n = p!!.multiply(q!!) // n = p x q
        val phi = p!!.subtract(BigInteger.ONE).multiply(q!!.subtract(BigInteger.ONE))
        d = e.modInverse(phi)

        localPublicKey = Pair(n!!, e)
        localPrivateKey = Pair(n!!, d!!)
    }


//////// other functions ////////
    /**
     * Switches whole backend logic to support selected encoding standard
     *
     * @param option Anything from `EncodingTypes enum class`
     * @return `true` if successful
     */
    fun setEncodingType(option: EncodingTypes): Boolean {
        when (option) {
            EncodingTypes.UTF8 -> {
                encodingStandard = EncodingTypes.UTF8
                bitsPerChar = 8
                messageChuckSize = 8
//                filerForUTF8 = true
                paddingCharStr = "00000000"
            }

            EncodingTypes.UTF16 -> {
                encodingStandard = EncodingTypes.UTF16
                bitsPerChar = 16
                messageChuckSize = 8
//                filerForUTF8 = false
                paddingCharStr = "0000000000000000"
            }
        }
        bitsPerChunk = messageChuckSize * bitsPerChar
        return true
    }

    /**
     * Takes chunks of message and transforms them into their binary representation.
     *
     * Frontal padding is applied so each char is the same length defined by `bitsPerChar length`
     * All `chars` which are `out of range` of `defined bitsPerChar length` will be masked for `'?'`
     */
    private fun bitCaster(chunks: List<String>): MutableList<String> {
        val bitStream: MutableList<String> = mutableListOf()
        val placeholderChar = '?'.code.toString(2).padStart(bitsPerChar, '0')
        chunks.forEach { chunk ->
            val chunkInBits = StringBuilder()

            chunk.forEach { char ->
                val unsafeChar = char.code.toString(2).padStart(bitsPerChar, '0')
                val safeChar = if (unsafeChar.length > bitsPerChar) {
                    placeholderChar
                } else unsafeChar
                chunkInBits.append(safeChar)
            }

            if (chunk.length < messageChuckSize) {
                (0..<(messageChuckSize - chunk.length)).forEach { _ ->
                    chunkInBits.append(paddingCharStr)
                }
            }
            bitStream.add(chunkInBits.toString())
        }
        return bitStream
    }

    /**
     * Essentially the same thing as `bitCaster()`, but transforms `binary numbers` for their `Big Integer` representation.
     */
    private fun bigIntCaster(chunks: List<String>): MutableList<BigInteger> {
        val bigIntStream: MutableList<BigInteger> = mutableListOf()
        chunks.forEach { chunk ->
            bigIntStream.add(chunk.toBigInteger(2)) // we are working with bin numbers
        }
        return bigIntStream
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

    private fun rabinMillerTest(testedNumber: BigInteger): Boolean {
        /**
         * explained in dedicated .pdf file (they are in gitignore so good lock everyone else)
         *
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

        for (i in 1..k) { // could be .forEach{} but then it looks bad
            /**
             * step 2.) pick value witness, where witness: 1 < witness < n-1
             * variables naming is totally random stitched from multiple sources
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
}