package crypto.algorithms


class FifthSolution {
    val name: String = "DSA generator"
    fun whoAmI(): String {
        return name
    }


    //////// variables ////////

    val keyMaker = FourthSolution()

    var inputFilePath: String = ""
    var outputFilePath: String = ""


    //////// signature functions ////////

    /**
     * Takes a `file of any kind` and signs it with provided `RSA key pair`.
     *
     * @see [crypto.algorithms.FourthSolution]
     */
    fun signThisFile() {
        TODO()
    }

    fun verifySignature() {
        TODO()
    }


    //////// other functions ////////


}