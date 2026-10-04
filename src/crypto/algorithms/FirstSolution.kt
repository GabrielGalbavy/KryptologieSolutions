package crypto.algorithms

import crypto.models.Solver


class FirstSolution() : Solver() {
    override val name: String = "Afinní šifra"

    override fun whoAmI(): String { // possibly pointless
        return name
    }


    //////// variables ////////

    private val DIACRITICS_MAP = mapOf(
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
    private val KEYWORDS_MAP = mutableMapOf(
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


    private var keyA: Int = 1
    private var keyB: Int = 1


    //////// cipher functions ////////

    fun encrypt(input: String): String {
        var output = ""
        val cleanInput = washTheMessage(input)

        cleanInput.forEach { character ->
            val charIndex = character - 'A'
            output += 'A' + (keyA * charIndex + keyB) % 26
        }
        return sliceTheCode(output)
    }

    fun decrypt(input: String): String {
        val gibberish = input.replace(" ", "")
        val message: MutableList<Char> = mutableListOf()
        val aInverse = modInverse(keyA)

        gibberish.forEach { character ->
            val charIndex = character - 'A'

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
            }
        }
    }

    fun setKeyB(key: Int) {
        this.keyB = (key % 26 + 26) % 26
    }

    fun validateKeyA(a: Int): Boolean {
        return a > 0 && gcd(a, 26) == 1
    }

    private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)


    //////// other functions ////////

    fun washTheMessage(notCleanedMessage: String): String {
        var cleanMessage = ""

        notCleanedMessage.forEach { character ->
            cleanMessage += when {
                DIACRITICS_MAP.containsKey(character) -> DIACRITICS_MAP[character]
                KEYWORDS_MAP.containsKey(character) -> KEYWORDS_MAP[character]

                character in 'A'..'Z' || character in 'a'..'z' -> character.uppercaseChar()

                // drop anything else
                else -> ""
            }
        }
        return cleanMessage
    }

    fun unwashTheMessage(notCleanedMessage: String): String {
        var cleanMessage = notCleanedMessage

        KEYWORDS_MAP.forEach { set ->
            cleanMessage = cleanMessage.replace(set.value, set.key.toString())
        }

        return cleanMessage
    }

    fun sliceTheCode(orig: String): String {
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