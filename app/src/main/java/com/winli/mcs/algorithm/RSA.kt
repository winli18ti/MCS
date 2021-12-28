package com.winli.mcs.algorithm

import com.winli.mcs.utils.Hex
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

object RSA {
    lateinit var kpg: KeyPairGenerator
    lateinit var kp: KeyPair
    lateinit var publicKey: PublicKey
    lateinit var privateKey: PrivateKey
    lateinit var encryptedBytes: ByteArray
    lateinit var decryptedBytes: ByteArray
    lateinit var cipher: Cipher
    lateinit var cipher1: Cipher
    lateinit var encrypted: String
    lateinit var decrypted: String

    fun showModulus(): String {
        return publicKey.toString().removePrefix("OpenSSLRSAPublicKey{modulus=")
            .removeSuffix(",publicExponent=10001}")
    }

    fun encrypt(input: String): String {
        kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(1024)
        kp = kpg.genKeyPair()
        publicKey = kp.public
        privateKey = kp.private

        return try {
            cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            encryptedBytes = cipher.doFinal(input.toByteArray())
            encrypted = String(Hex.encodeHex(encryptedBytes))
            encrypted
        } catch (e: Exception) {
            ""
        }
    }

    fun decrypt(input: String): String {
        return try {
            cipher1 = Cipher.getInstance("RSA")
            cipher1.init(Cipher.DECRYPT_MODE, privateKey)
            decryptedBytes = cipher1.doFinal(decodeHexString(input))
            decrypted = String(decryptedBytes)
            decrypted
        } catch (e: Exception) {
            ""
        }
    }

    private fun decodeHexString(hexString: String): ByteArray {
        require(hexString.length % 2 != 1) { "Invalid hexadecimal String supplied." }
        val bytes = ByteArray(hexString.length / 2)
        var i = 0
        while (i < hexString.length) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2))
            i += 2
        }
        return bytes
    }

    private fun hexToByte(hexString: String): Byte {
        val firstDigit: Int = toDigit(hexString[0])
        val secondDigit: Int = toDigit(hexString[1])
        return ((firstDigit shl 4) + secondDigit).toByte()
    }

    private fun toDigit(hexChar: Char): Int {
        val digit = Character.digit(hexChar, 16)
        require(digit != -1) { "Invalid Hexadecimal Character: $hexChar" }
        return digit
    }
}