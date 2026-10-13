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

    var key: String = ""
        private set

    val tableType: Tables = Tables.CZ
    var table: Array<CharArray> = arrayOf(charArrayOf())

    val separators = charArrayOf('X', 'Q')


    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        val output = StringBuilder()
        val normInput = addSeparators(washTheMessage(input))

        this.table = tableFactory() // generates table with current key applied


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

    fun tableFactory(): Array<CharArray> {
        var keyImprint = this.key
        var alphabetIndex = 0
        val matrix = Array(5) { CharArray(5) }

        for (row in matrix) {
            row.forEachIndexed { i, _ ->
                if (keyImprint.isNotEmpty()) { // first add the key
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

//    fun resolveFormation(charPair: Pair<Char, Char>): Formations {
//        if (this.table.any { row -> charPair.first in row && charPair.second in row }) {
//            return Formations.ROW
//        } else {
//            var indexFirst: Int
//            var indexSecond: Int
//            this.table.forEach { row -> if (row.contains(charPair.first)) indexFirst = row.indexOf(charPair.first) }
//            this.table.forEach { row -> if (row.contains(charPair.second)) indexSecond = row.indexOf(charPair.second) }
//        }
//    }

    fun locateTargets(pair: Pair<Char, Char>): Pair<Point, Point> {
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
}