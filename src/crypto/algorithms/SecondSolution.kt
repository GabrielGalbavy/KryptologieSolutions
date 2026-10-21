package crypto.algorithms

import crypto.models.Solver


class SecondSolution : Solver() {
    override val name = "Playfair šifra"
    override fun whoAmI(): String {
        return this.name
    }


    //////// variables ////////

    enum class Tables {
        CZ, // masking W as V
        EN // masking J as I
    }

    data class Point(val row: Int, val col: Int)

    var key: String? = null
        private set
    var tableType: Tables = Tables.CZ
    var table: Array<CharArray> = arrayOf(charArrayOf())
    val separators = charArrayOf('X', 'Q')


    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        val output = StringBuilder()
        val normInput = addSeparators(washTheMessage(input))
        val pairList: List<Pair<Char, Char>> = splitToPairs(normInput)

        this.table = tableFactory() // generates table with current key applied

        for (pair in pairList) {
            val outputChars = shiftLetters(pair, 1)

            output.append(outputChars.first).append(outputChars.second).append(' ')
        }

        return output.toString().trim()
    }

    override fun decrypt(input: String): String {
        val output = StringBuilder()
        val normInput = input.replace(" ", "")
        val pairList: List<Pair<Char, Char>> = splitToPairs(normInput)

        this.table = tableFactory()

        for (pair in pairList) {
            val outputChars = shiftLetters(pair, -1)

            output.append(outputChars.first).append(outputChars.second)
        }

        val cleanOutput = unwashTheMessage(findSeparators(output.toString().trim()))

        return cleanOutput
    }

    override fun filter(input: String): String {
        return addSeparators(washTheMessage(input))
    }


    //////// Key-related functions ////////

    fun setKey(key: String): Boolean { // returns true in case the key was set
        val normalizedKey = normaliseKey(key)
        if (normalizedKey.isEmpty()) {
            this.key = null
            return false
        } else {
            this.key = normalizedKey
            return true
        }
    }

    private fun normaliseKey(inputKey: String): String {
        // removes special chars, spaces, diacritics and duplicities
        val key = StringBuilder() // more memory friendly than string + string

        inputKey.uppercase().forEach { ch ->
            var normChar = when {
                diacriticsMap.containsKey(ch) -> diacriticsMap[ch]
                ch in 'A'..'Z' -> ch
                else -> null
            }

            if (normChar != null) {
                normChar = when (tableType) {
                    Tables.CZ -> {
                        if (normChar == 'W') 'V' else normChar
                    }

                    Tables.EN -> {
                        if (normChar == 'J') 'I' else normChar
                    }
                }
            }

            if (normChar != null && !key.contains(normChar)) {
                key.append(normChar)
            }
        }

        return key.toString()
    }

    fun setTableLanguage(language: String): Boolean { // returns true in case the language was set
        when (language) {
            "czech" -> {
                this.tableType = Tables.CZ
                return true
            }

            "english" -> {
                this.tableType = Tables.EN
                return true
            }

            else -> return false
        }
    }


    //////// other functions ////////

    private fun addSeparators(input: String): String {
        var output = ""
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

    private fun tableFactory(): Array<CharArray> {
        var keyImprint = this.key
        var alphabetIndex = 0
        val matrix = Array(5) { CharArray(5) }

        for (row in matrix) {
            row.forEachIndexed { i, _ ->
                if (!keyImprint.isNullOrBlank()) { // first add the key
                    row[i] = keyImprint.first()
                    keyImprint = keyImprint.drop(1)
                } else {
                    while (row[i] == '\u0000') { // each array char is set to this on init
                        val possibleChar = 'A' + alphabetIndex

                        val currentChar = when (tableType) {
                            Tables.CZ -> if (possibleChar == 'W') 'V' else possibleChar
                            Tables.EN -> if (possibleChar == 'J') 'I' else possibleChar
                        }

                        if (!matrix.any { row -> row.contains(currentChar) }) { // prevent duplicates
                            row[i] = currentChar
                        }
                        alphabetIndex++
                    }
                }
            }
        }

        return matrix
    }

    private fun locateTargets(pair: Pair<Char, Char>): Pair<Point, Point> {
        var col1 = 0
        var col2 = 0
        var row1 = 0
        var row2 = 0

        this.table.forEachIndexed { i, row ->
            row.forEachIndexed { j, ch ->
                when (ch) {
                    pair.first -> {
                        row1 = i
                        col1 = j
                    }

                    pair.second -> {
                        row2 = i
                        col2 = j
                    }
                }
            }
        }
        return Pair(Point(row1, col1), Point(row2, col2))
    }

    private fun shiftLetters(pair: Pair<Char, Char>, direction: Int): Pair<Char, Char> {
        val coords = locateTargets(pair)
        var outputChars: Pair<Char, Char>

        val directionFix = direction + 5 // Adding 5 so we don't run out of bounds

        when {
            coords.first.row == coords.second.row -> { // chars share row
                outputChars = Pair(
                    this.table[coords.first.row][(coords.first.col + directionFix) % 5],
                    this.table[coords.second.row][(coords.second.col + directionFix) % 5]
                )
            }

            coords.first.col == coords.second.col -> { // chars share column
                outputChars = Pair(
                    this.table[(coords.first.row + directionFix) % 5][coords.first.col],
                    this.table[(coords.second.row + directionFix) % 5][coords.second.col]
                )
            }

            else -> { // chars form a sub-table
                outputChars = Pair(
                    this.table[coords.first.row][coords.second.col], this.table[coords.second.row][coords.first.col]
                )
            }
        }
        return outputChars
    }

    private fun findSeparators(input: String): String {
        val regexPatternX = Regex("([^X])X\\1") // any 3 letter pattern with X in the middle
        val regexPatternQ = Regex("(X)Q\\1") // XQX

        val output = regexPatternX
            .replace(input, "$1$1")
            .replace(regexPatternQ, "$1$1")

        return output
    }

    private fun splitToPairs(input: String): List<Pair<Char, Char>> {
        return input.chunked(2) { chunk ->
            val first = chunk.first()
            val second = chunk.last()
            Pair(first, second)
        }
    }
}