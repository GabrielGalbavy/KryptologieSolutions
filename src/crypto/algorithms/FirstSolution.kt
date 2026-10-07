package crypto.algorithms

import crypto.models.Solver


class FirstSolution : Solver() {
    override val name: String = "Afinní šifra - 1. ukol"

    override fun whoAmI(): String { // possibly pointless
        return name
    }


    //////// variables ////////

    private val diacriticsMap = mapOf(
        'á' to 'A',
        'ä' to 'A',
        'Á' to 'A',
        'Ä' to 'A',
        'č' to 'C',
        'Č' to 'C',
        'ď' to 'D',
        'Ď' to 'D',
        'é' to 'E',
        'ě' to 'E',
        'É' to 'E',
        'Ě' to 'E',
        'í' to 'I',
        'Í' to 'I',
        'ĺ' to 'L',
        'Ĺ' to 'L',
        'ľ' to 'L',
        'Ľ' to 'L',
        'ň' to 'N',
        'Ň' to 'N',
        'ó' to 'O',
        'ô' to 'O',
        'Ó' to 'O',
        'Ô' to 'O',
        'ŕ' to 'R',
        'Ŕ' to 'R',
        'š' to 'S',
        'Š' to 'S',
        'ť' to 'T',
        'Ť' to 'T',
        'ú' to 'U',
        'ů' to 'U',
        'Ú' to 'U',
        'Ů' to 'U',
        'ý' to 'Y',
        'Ý' to 'Y',
        'ž' to 'Z',
        'Ž' to 'Z'
    )
    private val keywordsMap = mutableMapOf(
        // must have 1:1 key-value relation for decryption
        '0' to "XNULAX",
        '1' to "XJEDNAX",
        '2' to "XDVAX",
        '3' to "XTRIX",
        '4' to "XSTYRIX",
        '5' to "XPATX",
        '6' to "XSESTX",
        '7' to "XSEDEMX",
        '8' to "XOSEMX",
        '9' to "XDEVATX",

        ' ' to "XMEZERAX",
        '-' to "XCARKAX",
    )


    var keyA: Int = 1
        private set
    var keyB: Int = 0
        private set


    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        var output = ""
        val cleanInput = washTheMessage(input)

        cleanInput.forEach { character ->
            val charIndex = character - 'A'
            output += 'A' + (keyA * charIndex + keyB) % 26
        }
        return sliceTheCode(output)
    }

    override fun decrypt(input: String): String {
        val gibberish = input.replace(" ", "") // unite the code blocks
        val message: MutableList<Char> = mutableListOf()
        val aInverse = modInverse(keyA)

        gibberish.forEach { character ->
            val charIndex = character.uppercaseChar() - 'A'

            // D(y) = aInverse * (y - keyB) mod 26
            val rawIndex = (aInverse * (charIndex - keyB)) % 26
            val validIndex = (rawIndex + 26) % 26

            message += 'A' + validIndex
        }

        return unwashTheMessage(message.joinToString(""))
    }


    //////// Key-related functions ////////

    fun setKeyA(key: Int): Boolean {
        return validateKeyA(key).also { isValid ->
            if (isValid) {
                this.keyA = (key % 26 + 26) % 26
                println("Key A: " + this.keyA)
            } else {
                println("KEY A INVALID, setting to 1")
                this.keyA = 1
            }
        }
    }

    fun setKeyB(key: Int): Boolean {
        this.keyB = (key % 26 + 26) % 26
        println("Key B:" + this.keyB)
        return true
    }

    fun validateKeyA(a: Int): Boolean {
        val normalizedA = (a % 26 + 26) % 26
        return normalizedA != 0 && gcd(normalizedA, 26) == 1
    }

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)


    //////// other functions ////////

    private fun washTheMessage(notCleanedMessage: String): String {
        var cleanMessage = ""
        notCleanedMessage.forEach { character ->
            cleanMessage += when {
                diacriticsMap.containsKey(character) -> diacriticsMap[character]
                keywordsMap.containsKey(character) -> keywordsMap[character]

                character in 'A'..'Z' || character in 'a'..'z' -> character.uppercaseChar()

                // drop anything else
                else -> ""
            }
        }
        return cleanMessage
    }

    private fun unwashTheMessage(notCleanedMessage: String): String {
        var cleanMessage = notCleanedMessage

        keywordsMap.forEach { set ->
            cleanMessage = cleanMessage.replace(set.value, set.key.toString())
        }
        return cleanMessage
    }

    private fun sliceTheCode(orig: String): String {
        return orig.chunked(5).joinToString(" ")
    }

    private fun modInverse(keyA: Int, m: Int = 26): Int {
        val normA = (keyA % m + m) % m
        for (x in 1..<m) {
            if ((normA * x) % m == 1) return x
        }
        return 1
    }
}