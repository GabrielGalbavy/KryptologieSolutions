package crypto.models


abstract class Solver {

    //////// essentials ////////
    abstract val name: String
    abstract fun whoAmI(): String
    abstract fun encrypt(input: String): String
    abstract fun decrypt(input: String): String


    //////// utils ////////

    protected val diacriticsMap = mapOf(
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
    protected val keywordsMap = mutableMapOf(
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

    protected fun washTheMessage(notCleanedMessage: String): String {
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

    protected fun unwashTheMessage(notCleanedMessage: String): String {
        var cleanMessage = notCleanedMessage

        keywordsMap.forEach { set ->
            cleanMessage = cleanMessage.replace(set.value, set.key.toString())
        }
        return cleanMessage
    }

    protected fun sliceTheCode(orig: String): String {
        return orig.chunked(5).joinToString(" ")
    }

    protected fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

    protected fun modInverse(keyA: Int, m: Int = 26): Int {
        val normA = (keyA % m + m) % m
        for (x in 1..<m) {
            if ((normA * x) % m == 1) return x
        }
        return 1
    }
}