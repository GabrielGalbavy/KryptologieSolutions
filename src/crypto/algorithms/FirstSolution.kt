package crypto.algorithms

import crypto.models.Solver
import kotlin.math.pow


class FirstSolution() : Solver() {
    override val name: String = "Afinní šifra"

    override fun whoAmI(): String {
        return name
    }


    //////// variables ////////
    private val DIACRITICS_MAP = mapOf(
        'á' to 'A', 'ä' to 'A', 'Á' to 'A', 'Ä' to 'A',
        'č' to 'C', 'Č' to 'C',
        'ď' to 'D', 'Ď' to 'D',
        'é' to 'E', 'ě' to 'E', 'É' to 'E', 'Ě' to 'E',
        'í' to 'I', 'Í' to 'I',
        'ĺ' to 'L', 'Ĺ' to 'L', 'ľ' to 'L', 'Ľ' to 'L',
        'ň' to 'N', 'Ň' to 'N',
        'ó' to 'O', 'ô' to 'O', 'Ó' to 'O', 'Ô' to 'O',
        'ŕ' to 'R', 'Ŕ' to 'R',
        'š' to 'S', 'Š' to 'S',
        'ť' to 'T', 'Ť' to 'T',
        'ú' to 'U', 'ů' to 'U', 'Ú' to 'U', 'Ů' to 'U',
        'ý' to 'Y', 'Ý' to 'Y',
        'ž' to 'Z', 'Ž' to 'Z'
    )
    private val NUMERIC_MAP = mutableMapOf(
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
    private val SPECIALS_MAP = mapOf(
        ' ' to "XMEZERAX",
    )

    private var keyA: Int = 1
    private var keyB: Int = 0


    //////// cipher functions ////////

    fun encrypt(input: String, reversed: Boolean): String {
        var output = ""
        var cleanInput = input

        if (!reversed) {
            cleanInput = washTheMessage(input)
        }

        cleanInput.forEach { character ->
            val charIndex = character - 'A'

            output +=
                if (!reversed) {
                    'A' + (keyA * charIndex + keyB) % 26
                } else {
                    'A' + (keyA
                        .toDouble()
                        .pow(-1)
                        .toInt() * (charIndex - keyB)) % 26
                }
        }
        return if (reversed) {
            output
        } else {
            sliceTheCode(output)
        }
    }

    fun decrypt(input: String): String {
        return "say something else"
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

                // check for mapped chars
                DIACRITICS_MAP.containsKey(character) -> DIACRITICS_MAP[character]
                NUMERIC_MAP.containsKey(character) -> NUMERIC_MAP[character]
                SPECIALS_MAP.containsKey(character) -> SPECIALS_MAP[character]

                // remaining chars to upper case
                character in 'A'..'Z' || character in 'a'..'z' -> character.uppercaseChar()

                // drop anything else
                else -> ""
            }
        }
        return cleanMessage
    }

    fun sliceTheCode(orig: String): String {
        var counter = 1
        var output = ""
        orig.forEach { character ->
            output += character
            if (counter % 5 == 0) output += " "
            counter++
        }
        return output
    }
}


