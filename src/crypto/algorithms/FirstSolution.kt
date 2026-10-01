package crypto.algorithms

import java.math.BigInteger


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

    override fun whoAmI(): String {
        return "FirstSolution"
    }

    fun encrypt(input: String, keyA: Int, keyB: Int): String? {

        if (validateKeyA(keyA).not()) {
            return null
        }

        return "input"
    }

    fun decrypt(input: String): String {
        return "say something else"
    }

    fun validateKeyA(a: Int): Boolean {
        // "číslo a musí být nesoudělné s číslem 26." this
        return a % 2 != 0 && a % 13 != 0
    }

    fun washTheMessage(dirtyMessage: String): String {
        var cleanMessage = ""

        dirtyMessage.forEach { character ->
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


