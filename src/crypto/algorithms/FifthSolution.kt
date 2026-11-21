package crypto.algorithms

import java.io.File
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.Security
import java.util.*


class FifthSolution {
    val name: String = "DSA generator"
    fun whoAmI(): String {
        return name
    }


    //////// variables ////////

    val keyMaker = FourthSolution()

    data class FileMetadata(
        val name: String,
        val path: String,
        val type: String,
        val sizeBytes: Long,
        val createdAt: String,
        val modifiedAt: String
    )

    var file: File? = null
        private set
    var fileMetadata: FileMetadata? = null
        private set
    var inputFilePath: String = """C:\file.txt""" // paths must be in raw string format """..."""
    var outputFilePath: String = """"C:\UTB\AP3KR\forwardOutputHere\""""

    // all hash algorithms available in local runtime environment
    val supportedHashAlgorithms = Security.getAlgorithms("MessageDigest").sorted()
    var fileHash: String = ""
    var encryptedFileHash: String = ""

    var privateKey: Pair<BigInteger, BigInteger>? = null
    var publicKey: Pair<BigInteger, BigInteger>? = null


    //////// signature functions ////////

    /**
     * Takes a `file of any kind` and signs it with provided `RSA key pair`.
     *
     * @see [crypto.algorithms.FourthSolution]
     */
    fun signThisFile(): Boolean {
        if (loadFile()) {
            if (setupEncryption()) {

                fileHash = getFileHashCode()
                encryptedFileHash = encryptHash(fileHash)

                exportKeys()

            }
        }
        return false
    }

    fun verifySignature() {
        TODO()
    }

    fun exportKeys(): Boolean {
        val pub = publicKey ?: return false
        val private = privateKey ?: return false

        return outputFilePath.isNotBlank() && try {
            val exportDir = File(outputFilePath.trim(), "keys_to_the_kingdom").apply { mkdirs() }
            if (!exportDir.exists() && !exportDir.mkdirs()) {
                return false
            }

            val rawPubKey = "${pub.first} ${pub.second}"
            val rawPrivateKey = "${private.first} ${private.second}"

            val base64PubKey = Base64.getEncoder().encodeToString(rawPubKey.toByteArray(Charsets.UTF_8))
            val base64PrivateKey = Base64.getEncoder().encodeToString(rawPrivateKey.toByteArray(Charsets.UTF_8))

            File(exportDir, "public_key.pub").writeText("RSA $base64PubKey")
            File(exportDir, "private_key.priv").writeText("RSA $base64PrivateKey")
            true
        } catch (e: Exception) {
            false
        }
    }

    //////// file handling functions ////////

    /**
     * Function tries to load a file from [inputFilePath] location.
     *
     * @return true if everything okay, false file does not exist, can not be read or is not a file bad bad bad bad bad bad
     */
    fun loadFile(): Boolean {
        if (inputFilePath.isBlank()) {
            file = null
            fileMetadata = null
            println("no file path provided")
            return false
        }
        val possibleFile = File(inputFilePath.trim())

        if (possibleFile.exists() && possibleFile.isFile && possibleFile.canRead()) {
            file = possibleFile
//            fileMetadata = extractMetadata(file!!)
            println("File loaded successfully amaze amaze amaze")
            return true

        } else {
            file = null
            fileMetadata = null
            println("File not found/is unreadable at: ${possibleFile.absolutePath}")
            return false
        }
    }

    private fun extractMetadata(target: File): FileMetadata {
        TODO()
    }


    //////// HASH functions ////////

    fun getFileHashCode(): String {
        return file!!.inputStream().use { getStreamHash(it) }
    }

    fun getStreamHash(input: InputStream): String {
        val digest = MessageDigest.getInstance("SHA3-512")
        val buffer = ByteArray(8192) // to bypass 2GB size limit for single file we use buffer

        var bytesRead = input.read(buffer)
        while (bytesRead != -1) {
            digest.update(buffer, 0, bytesRead)
            bytesRead = input.read(buffer)
        }

        return digest.digest().joinToString("") { "%02x".format(it) }
    }


    //////// encrypt functions ////////

    fun setupEncryption(): Boolean {
        keyMaker.setEncodingType(FourthSolution.EncodingTypes.UTF8)
        keyMaker.generateKeys()

        privateKey = keyMaker.localPrivateKey
        publicKey = keyMaker.localPublicKey

        keyMaker.remotePublicKey = privateKey // so the encr/decr functions work as intended
        keyMaker.localPrivateKey = publicKey

        if (privateKey != null && publicKey != null) {
            println("keys generated")
            println("privateKey: $privateKey")
            println("publicKey: $publicKey")
            return true
        }

        return false
    }

    fun setupDecryption(): Boolean {
        TODO(
            "setup keyMaker so it uses provided keys for checking hash" + " also setupEncryption must only set the encrypting part"
        )
    }

    fun encryptHash(hash: String): String {
        return keyMaker.encrypt(hash)
    }

    fun decryptHash(encryptedHash: String): String {
        return keyMaker.decrypt(encryptedHash)
    }
}