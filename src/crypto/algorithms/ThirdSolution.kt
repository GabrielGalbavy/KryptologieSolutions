package crypto.algorithms

import crypto.models.Solver
import kotlin.math.ceil
import kotlin.random.Random

class ThirdSolution : Solver() {
    override val name: String = "ADFG(V)X šifra"

    override fun whoAmI(): String {
        return this.name
    }


    //////// variables ////////

    var seed: Int = 123
    var keyword: String = ""
    var ourTable: Array<CharArray>? = null

    var tableLanguage: Tables = Tables.CZ
    var tableSize: TableSizes = TableSizes.SMALL

    val smallHeader = charArrayOf('A', 'D', 'F', 'G', 'X')
    val largeHeader = charArrayOf('A', 'D', 'F', 'G', 'V', 'X')
    val smallAlphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val largeAlphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    enum class Tables {
        CZ, // masking W as V
        EN // masking J as I
    }

    enum class TableSizes {
        SMALL, // ADFGX
        LARGE // ADFGVX
    }


    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        val extendedKeyMap = when (tableSize) {
            TableSizes.SMALL -> true; TableSizes.LARGE -> false
        }
        this.ourTable = tableFactory()

        val filteredInput = actuallyIAmHereToInterpret(washTheMessage(input, extendedKeyMap))
        println(filteredInput)
        val mappedMessage = mapMessage(filteredInput)
        val messageForm = formFactory(mappedMessage)
        val sortedForm = sortFormColumns(messageForm)
        var cipherText = extractMessage(sortedForm).replace(" ", "")
        cipherText = sliceTheCode(cipherText)
        // too many variables, wasting RAM like it isn't worth more than gold right now

        return cipherText
    }

    override fun decrypt(input: String): String {
        val formDepth = ceil((input.length / this.keyword.length).toDouble()).toInt()
        TODO("Not yet implemented")
    }

    override fun filter(input: String): String {
        TODO("Not yet implemented")
    }


    //////// Key-related functions ////////

//    public fun setKeyword(keyword: String) {
//        TODO()
//    }
//
//    private fun validateKeyword() {
//        TODO()
//    }

    //////// other functions ////////

    fun tableFactory(): Array<CharArray> {
        val size: Int
        val randomOperator = Random(this.seed)
        val rawAlphabet: String

        when (this.tableSize) {
            TableSizes.SMALL -> {
                size = 5
                rawAlphabet = when (this.tableLanguage) {
                    Tables.CZ -> smallAlphabet.replace("W", "")
                    Tables.EN -> smallAlphabet.replace("J", "")
                }
            }

            TableSizes.LARGE -> {
                size = 6
                rawAlphabet = largeAlphabet
            }
        }

        val shuffledList = rawAlphabet.toList().shuffled(randomOperator)

        // constructing the table with power of math and shi
        return Array(size) { rowIndex ->
            CharArray(size) { colIndex ->
                shuffledList[rowIndex * size + colIndex]
            }
        }
    }


    private fun formFactory(input: String): List<List<String>> {
        val key = keyword
        val output = mutableListOf<MutableList<String>>()
        var pointer = 0

        key.forEach { ch ->
            output.add(mutableListOf(ch.toString()))
            // we create mutable list for each keyword character
        }
        while (pointer < input.length) {
            output.forEach { verticalColumn ->
                if (pointer < input.length) verticalColumn.add(input[pointer++].toString())
                else verticalColumn.add(" ")
            }
        }

        return output
    }

    private fun mapMessage(input: String): String {
        val output = StringBuilder()
        val usableHeaders = when (this.tableSize) {
            TableSizes.SMALL -> smallHeader
            TableSizes.LARGE -> largeHeader
        }

        input.forEach { ch ->
            val coords = locateTargetCoords(this.ourTable!!, ch)
            if (coords != null) output.append(usableHeaders[coords.first]).append(usableHeaders[coords.second])
        }
        return output.toString()
    }

    private fun locateTargetCoords(table: Array<CharArray>, target: Char): Pair<Int, Int>? {
        table.forEachIndexed { rowIndex, row ->
            val colIndex = row.indexOf(target)
            if (colIndex != -1) {
                return Pair(rowIndex, colIndex)
            }
        }
        return null
    }

    private fun sortFormColumns(input: List<List<String>>): List<List<String>> {
        return input.sortedBy { column -> column.first() }
    }

    private fun desortFormColumns(cipherText: String): List<List<String>>? {
        val key = keyword.ifEmpty { return null }
        val c = key.length
        val l = cipherText.length
        val baseRows = l / c
        val remainder = l % c
        val indexedKey = key.withIndex().sortedWith(compareBy({ it.value }, { it.index }))
        val sortedColumns = mutableListOf<MutableList<String>>()
        var cipherIndex = 0
        for ((origIndex, ch) in indexedKey) {
            val colLength = if (origIndex < remainder) baseRows + 1 else baseRows
            val colData = mutableListOf<String>()
            colData.add(ch.toString())
            repeat(colLength) {
                if (cipherIndex < cipherText.length) {
                    colData.add(cipherText[cipherIndex++].toString())
                }
            }
            sortedColumns.add(colData)
        }
        val originalColumns = arrayOfNulls<MutableList<String>>(c)
        for (i in sortedColumns.indices) {
            val origIndex = indexedKey[i].index
            originalColumns[origIndex] = sortedColumns[i]
        }

        return originalColumns.filterNotNull()
    }

    private fun rebuildMessage(inputForm: List<List<String>>): String {
        val output = StringBuilder()
        val numCols = inputForm.size

        val maxRows = inputForm.maxOfOrNull { it.size } ?: 0

        for (rowIndex in 1..<maxRows) {
            for (colIndex in 0..<numCols) {
                val column = inputForm[colIndex]

                if (rowIndex < column.size) {
                    output.append(column[rowIndex])
                }
            }
        }

        return output.toString().trimEnd()
    }

    private fun resolveMessage(input: String): String {
        val output = StringBuilder()
        val cleanInput = input.replace("\\s+".toRegex(), "")
        val inputPairs = cleanInput.chunked(2)

        val usableHeaders = when (this.tableSize) {
            TableSizes.SMALL -> smallHeader
            TableSizes.LARGE -> largeHeader
        }

        for (chunk in inputPairs) {
            if (chunk.length < 2) break

            val x = usableHeaders.indexOf(chunk[0])
            val y = usableHeaders.indexOf(chunk[1])

            if (x != -1 && y != -1) {
                output.append(this.ourTable!![x][y])
            }
        }

        return output.toString()
    }


    private fun extractMessage(input: List<List<String>>): String {
        val output = StringBuilder()
        for (column in input) {
            output.append(column.drop(1).joinToString(""))
        }
        return output.toString()
    }

    private fun actuallyIAmHereToInterpret(input: String): String {
        val output = when (tableLanguage) {
            Tables.EN -> input.replace('J', 'I')
            Tables.CZ -> input.replace('W', 'V')
        }
        // masking letters could be elevated to dedicated table
        return output
    }
}