package crypto.algorithms

import crypto.models.Solver

class SecondSolution : Solver() {
    override val name = "Playfair šifra"
    override fun whoAmI(): String {
        return this.name
    }


    //////// variables ////////

    val czTable: Array<CharArray> = arrayOf( // dropping W
        charArrayOf('A', 'B', 'C', 'D', 'E'),
        charArrayOf('F', 'G', 'H', 'I', 'J'),
        charArrayOf('K', 'L', 'M', 'N', 'O'),
        charArrayOf('P', 'Q', 'R', 'S', 'T'),
        charArrayOf('U', 'V', 'X', 'Y', 'Z')
    )
    val enTable: Array<CharArray> = arrayOf( // dropping J
        charArrayOf('A', 'B', 'C', 'D', 'E'),
        charArrayOf('F', 'G', 'H', 'I', 'K'),
        charArrayOf('L', 'M', 'N', 'O', 'P'),
        charArrayOf('Q', 'R', 'S', 'T', 'U'),
        charArrayOf('V', 'W', 'X', 'Y', 'Z')
    )

    val tables = listOf(czTable, enTable)

    var key: String = ""
        private set

    val separators = charArrayOf('X', 'Q')


    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        var output: String = ""
        var input = addSeparators(washTheMessage(input))


        return input
    }

    override fun decrypt(input: String): String {
        TODO("Not yet implemented")
    }


    //////// Key-related functions ////////

    fun setKey(key: String): Boolean { // returns true in case the key was set
        val normalizedKey = normaliseKey(key)
        println(normalizedKey)

        if (normalizedKey.isEmpty()) {
            return false
        } else {
            this.key = normalizedKey
            return true
        }
    }

    private fun normaliseKey(inputKey: String): String {
        // removes special chars, spaces, diacritics and duplicities
        var key = ""

        inputKey.replace(" ", "").forEach { ch ->
            if (ch.isLetter()) {
                val normChar = when {
                    diacriticsMap.containsKey(ch) -> diacriticsMap[ch]

                    ch in 'A'..'Z' || ch in 'a'..'z' -> ch.uppercaseChar()

                    // drop anything else
                    else -> ""
                } as Char

                if (!key.contains(normChar)) {
                    key += normChar
                }
            }
        }

        return if (key.length % 2 != 0) {
            when (key.last()) {
                'X' -> key + this.separators[1].toString()
                else -> key + this.separators[0].toString()
            }
        } else key
    }


    //////// other functions ////////

    fun addSeparators(input: String): String {
        var output: String = ""
        var previousChar: Char? = null

        input.forEach { ch ->
            output += if (ch == previousChar) {
                when (ch) {
                    this.separators[0] -> this.separators[1].toString() + ch.toString()
                    this.separators[1] -> this.separators[0].toString() + ch.toString()

                    // use X for any other character
                    else -> this.separators[0].toString() + ch.toString()
                }
            } else {
                ch.toString()
            }
            previousChar = ch
        }

        return if (output.length % 2 != 0) {
            when (output.last()) {
                'X' -> output + this.separators[1].toString()
                else -> output + this.separators[0].toString()
            }
        } else output
    }
}