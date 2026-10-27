package crypto.models


abstract class Solver {

    //////// essentials ////////
    abstract val name: String

    /**
     * Returns `name` in `String` form of this class instance.
     */
    abstract fun whoAmI(): String

    /**
     * Turns `Open Text` in `String` form into `Cipher Text` by using provided `Key`.
     */
    abstract fun encrypt(input: String): String

    /**
     * Turns `Cipher Text` in `String` form into `Open Text` by using provided `Key`.
     */
    abstract fun decrypt(input: String): String

    /**
     * Turns Open Text in `String` form into `normalized` text format that is ready for encryption.
     */
    abstract fun filter(input: String): String


    //////// utils ////////
    /**
     * Used to `remove diacritics` from `Open Text`.
     */
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

    /**
     * Masks all provided `Keys` for assigned `Placeholders`.
     * Must have `1:1 key-value relation`, as it is also used in reverse.
     */
    protected val keywordsExtMap = mapOf(
        '0' to "QNULAQ",
        '1' to "QIEDNAQ",
        '2' to "QDVAQ",
        '3' to "QTRIQ",
        '4' to "QSTYRIQ",
        '5' to "QPATQ",
        '6' to "QSESTQ",
        '7' to "QSEDEMQ",
        '8' to "QOSEMQ",
        '9' to "QDEVATQ",

        ' ' to "QMEZERAQ",
        '-' to "QCARKAQ",
    )

    /**
     * Short version of keywordsExtMap.
     * Masks all provided `Keys` for assigned `Placeholders`.
     * Must have `1:1 key-value relation`, as it is also used in reverse.
     */
    protected val keywordsShortMap = mapOf(
        ' ' to "QMEZERAQ",
        '-' to "QCARKAQ",
    )

    /**
     * Takes `Open Text` as a `String` and turns it into `normalized text form` ready for encryption. Removes all `diacritics`,
     * `special characters`, and `masks defined characters` for `keywords` by using `keywordsMap.
     */
    protected fun washTheMessage(notCleanedMessage: String): String {
        var cleanMessage = ""
        notCleanedMessage.forEach { character ->
            cleanMessage += when {
                diacriticsMap.containsKey(character) -> diacriticsMap[character]
                keywordsExtMap.containsKey(character) -> keywordsExtMap[character]

                character in 'A'..'Z' || character in 'a'..'z' -> character.uppercaseChar()

                // drop anything else
                else -> ""
            }
        }
        return cleanMessage
    }

    /**
     * Takes `Normalized text` as a `String` and turns it into `Open Text`.
     * Basically reverts changes done by `washTheMessage` function. Can not bring back removed characters like `"!@%^$*&"`.
     */
    protected fun unwashTheMessage(notCleanedMessage: String): String {
        var cleanMessage = notCleanedMessage

        keywordsExtMap.forEach { set ->
            cleanMessage = cleanMessage.replace(set.value, set.key.toString())
        }
        return cleanMessage
    }

    protected fun sliceTheCode(orig: String, n: Int = 5): String {
        return orig.chunked(n).joinToString(" ")
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