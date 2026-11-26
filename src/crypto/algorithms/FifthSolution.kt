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
    var keyFilePath: String = """"""

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
            if (setupSignature(keyFilePath)) {

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
        val cleanPath = outputFilePath.trim().removeSurrounding("\"")

        return cleanPath.isNotBlank() && try {
            val keysToTheKingdom = keyGen()
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss"))
            val exportDir = Path(cleanPath, "key_export_$currentDateTime").createDirectories().toString()

            generateKeyFile("priv", exportDir, keysToTheKingdom.first) && generateKeyFile(
                "pub", exportDir, keysToTheKingdom.second
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

    fun setupSignature(keyDirPath: String): Boolean {
        val cleanPath = keyDirPath.trim().removeSurrounding("\"")

        return cleanPath.isNotBlank() && try {
            val dir = File(cleanPath)
            if (!dir.exists() || !dir.isDirectory) return false

            val extPrivateKey =
                File(dir, "key.priv").readText(Charsets.UTF_8).trim().drop(4).trim() // .drop(4) -> "RSA " seq
            val extPublicKey = File(dir, "key.pub").readText(Charsets.UTF_8).trim().drop(4).trim()

            val digestedPrivateKey =
                Base64.getDecoder().decode(extPrivateKey).decodeToString().trim().split(" ").map { it.toBigInteger() }
            privateKey = digestedPrivateKey[0] to digestedPrivateKey[1]
            keyMaker.remotePublicKey = privateKey

            val digestedPublicKey =
                Base64.getDecoder().decode(extPublicKey).decodeToString().trim().split(" ").map { it.toBigInteger() }
            publicKey = digestedPublicKey[0] to digestedPublicKey[1]

            keyMaker.setEncodingType(FourthSolution.EncodingTypes.UTF8)

            privateKey != null && publicKey != null
        } catch (e: Exception) {
            println("setupSignature() Error: ${e.message}")
            false
        }
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