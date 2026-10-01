package crypto.algorithms


class FirstSolution() : Solver() {
    private val DIACRITICS_MAP = mapOf(
        'á' to 'A',
        'ä' to 'A',
        'č' to 'C',
        'ď' to 'D',
        'é' to 'E',
        'ě' to 'E',
        'í' to 'I',
        'ĺ' to 'L',
        'ľ' to 'L',
        'ň' to 'N',
        'ó' to 'O',
        'ô' to 'O',
        'ŕ' to 'R',
        'š' to 'S',
        'ť' to 'T',
        'ú' to 'U',
        'ů' to 'U',
        'ý' to 'Y',
        'ž' to 'Z',
        'Á' to 'A',
        'Ä' to 'A',
        'Č' to 'C',
        'Ď' to 'D',
        'É' to 'E',
        'Ě' to 'E',
        'Í' to 'I',
        'Ĺ' to 'L',
        'Ľ' to 'L',
        'Ň' to 'N',
        'Ó' to 'O',
        'Ô' to 'O',
        'Ŕ' to 'R',
        'Š' to 'S',
        'Ť' to 'T',
        'Ú' to 'U',
        'Ů' to 'U',
        'Ý' to 'Y',
        'Ž' to 'Z'
    )
    private val NUMERIC_MAP = mapOf(
        '0' to "XNULAX",
        '1' to "XJEDNAX",
        '2' to "XDVAX",
        '3' to "XTRIX",
        '4' to "XSTYRIX",
        '5' to "XPATX",
        '6' to "XSESTX",
        '7' to "XSEDEMX",
        '8' to "XOSEMX",
        '9' to "XDEVATX"
    )
    private val SPECIAL_MAP = mapOf(
        ' ' to "XMEZERAX",
    )

    private var keyA: Int = 1
    private var keyB: Int = 0


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


    //////// cipher functions ////////

    fun encrypt(input: String): String {
        var output = ""
        val cleanInput = washTheMessage(input)

        cleanInput.forEach { character ->
            val charIndex = character - 'A'
            val encryptedCharIndex = (keyA * charIndex + keyB) % 26
            output += 'A' + encryptedCharIndex
        }

        return output
    }

    fun decrypt(input: String): String {
        return "say something else"
    }

    //////// other functions ////////

    override fun whoAmI(): String {
        return "FirstSolution"
    }

    fun washTheMessage(notCleanedMessage: String): String {
        var cleanMessage = ""

        notCleanedMessage.forEach { character ->
            cleanMessage += when {

                // check for mapped chars
                DIACRITICS_MAP.containsKey(character) -> DIACRITICS_MAP[character]
                NUMERIC_MAP.containsKey(character) -> NUMERIC_MAP[character]
                SPECIAL_MAP.containsKey(character) -> SPECIAL_MAP[character]

                // remaining chars to upper case
                character in 'A'..'Z' || character in 'a'..'z' -> character.uppercaseChar()

                // drop anything else
                else -> ""
            }
        }
        return cleanMessage
    }
}


