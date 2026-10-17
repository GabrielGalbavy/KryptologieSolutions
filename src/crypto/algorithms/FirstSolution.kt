package crypto.algorithms

import crypto.models.Solver

class FirstSolution : Solver() {

    override val name: String = "Afinní šifra"

    override fun whoAmI(): String { // possibly pointless
        return name
    }

    var keyA: Int = 1
        private set
    var keyB: Int = 0
        private set


    //////// cipher functions ////////

    override fun encrypt(input: String): String {
        var output = ""
        val cleanInput = washTheMessage(input)

        cleanInput.forEach { character ->
            val charIndex = character - 'A'
            output += 'A' + (keyA * charIndex + keyB) % 26
        }
        return sliceTheCode(output)
    }

    override fun decrypt(input: String): String {
        val gibberish = input.replace(" ", "") // unite the code blocks
        val message: MutableList<Char> = mutableListOf()
        val aInverse = modInverse(keyA)

        gibberish.forEach { character ->
            val charIndex = character.uppercaseChar() - 'A'

            // D(y) = aInverse * (y - keyB) mod 26
            val rawIndex = (aInverse * (charIndex - keyB)) % 26
            val validIndex = (rawIndex + 26) % 26

            message += 'A' + validIndex
        }

        return unwashTheMessage(message.joinToString(""))
    }

    override fun filter(input: String): String {
        return washTheMessage(input)
    }


    //////// Key-related functions ////////
    fun setKeyA(key: Int): Boolean {
        return validateKeyA(key).also { isValid ->
            if (isValid) {
                this.keyA = (key % 26 + 26) % 26
            } else {
                this.keyA = 1
            }
        }
    }

    fun setKeyB(key: Int): Boolean {
        this.keyB = (key % 26 + 26) % 26
        return true
    }

    fun validateKeyA(a: Int): Boolean {
        val normalizedA = (a % 26 + 26) % 26
        return normalizedA != 0 && gcd(normalizedA, 26) == 1
    }
}