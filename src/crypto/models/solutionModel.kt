package crypto.models


abstract class Solver {
    abstract val name: String
    abstract fun whoAmI(): String

    // abstract fun encrypt(input: String): String
    // abstract fun decrypt(input: String): String

// problem with passing different types of params, solve later
}