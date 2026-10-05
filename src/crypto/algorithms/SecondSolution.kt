package crypto.algorithms

import crypto.models.Solver

class SecondSolution : Solver() {
    override val name = "Second solution"
    override fun whoAmI(): String {
        return this.name
    }

    override fun encrypt(input: String): String {
        TODO("Not yet implemented")
    }

    override fun decrypt(input: String): String {
        TODO("Not yet implemented")
    }

}