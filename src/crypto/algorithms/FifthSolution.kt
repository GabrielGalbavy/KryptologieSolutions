package crypto.algorithms

import java.io.File
import java.io.InputStream
import java.math.BigInteger
import java.nio.file.Files
import java.security.MessageDigest
import java.security.Security
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.createDirectories


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


            }
        }
        return false
    }

    fun verifySignature() {
        TODO()
    }


    fun exportKeys(): Boolean {
        return outputFilePath.isNotBlank() && try {
            val keysToTheKingdom = keyGen()

            val cleanPath = outputFilePath.trim().removeSurrounding("\"")
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss"))
            val updatedPath = """$cleanPath\key_export_$currentDateTime"""
            Path(updatedPath).createDirectories()

            generateKeyFile(
                "priv", updatedPath, keysToTheKingdom.first
            ) && generateKeyFile(
                "pub", updatedPath, keysToTheKingdom.second
            )

        } catch (e: Exception) {
            println("exportKeys() Error: ${e.message}")
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


    fun generateKeyFile(keyType: String, path: String, dedicatedKey: Pair<BigInteger, BigInteger>): Boolean {
        if (path.isBlank() || keyType.isBlank()) return false // technically redundant, better safe than sorry tho

        val fileContent = "RSA " + Base64.getEncoder()
            .encodeToString("${dedicatedKey.first} ${dedicatedKey.second}".toByteArray(Charsets.UTF_8))

        return try {
            val cleanPath = path.trim().removeSurrounding("\"")
            val target = File(cleanPath)
            val keyFile = if (target.isDirectory) {
                File(target, "key.$keyType")
            } else {
                File(cleanPath.removeSuffix(".$keyType") + ".$keyType")
            }

            keyFile.parentFile?.let { Files.createDirectories(it.toPath()) }
            keyFile.writeText(fileContent, Charsets.UTF_8)
            true
        } catch (e: Exception) {
            println("generateKeyFile() Error: ${e.message}")
            false
        }
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

    fun extractKeyFromFile(targer: String): Pair<BigInteger, BigInteger>? {
        TODO()
    }

    fun setupEncryption(specifiedPrivateKey: String = ""): Boolean {
        if (specifiedPrivateKey.isBlank()) {
            val generatedKeys = keyGen()
            privateKey = generatedKeys.first
        } else {
            val cleanPrivateKey = specifiedPrivateKey.trim().split(" ").map { it.toBigInteger() }

            privateKey = (cleanPrivateKey.first() to cleanPrivateKey.last())
            // keyMaker uses public key in encrypt, so we set it to OUR private key
            keyMaker.localPublicKey = (cleanPrivateKey.first() to cleanPrivateKey.last())
        }

        if (privateKey != null && publicKey != null) {
            println("privateKey set: $privateKey")
            return true
        }
        return false
    }

    fun setupDecryption(specifiedPublicKey: String): Boolean {
        if (specifiedPublicKey.isNotBlank()) {
            val cleanPublicKey = specifiedPublicKey.trim().split(" ").map { it.toBigInteger() }
        }
        return false
    }

    fun keyGen(): Pair<Pair<BigInteger, BigInteger>, Pair<BigInteger, BigInteger>> {
        keyMaker.setEncodingType(FourthSolution.EncodingTypes.UTF8)
        keyMaker.generateKeys()
        return (keyMaker.localPrivateKey!! to keyMaker.localPublicKey!!)
    }

    fun encryptHash(hash: String): String {
        return keyMaker.encrypt(hash)
    }

    fun decryptHash(encryptedHash: String): String {
        return keyMaker.decrypt(encryptedHash)
    }
}