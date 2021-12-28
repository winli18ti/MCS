package com.winli.mcs.algorithm

import android.util.Base64
import java.lang.Exception
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AES {
    fun encrypt(input: String, key: String): String {
        val k = generateKey(key)
        val c = Cipher.getInstance("AES")
        c.init(Cipher.ENCRYPT_MODE, k)
        val encVal = c.doFinal(input.toByteArray())
        return Base64.encodeToString(encVal, Base64.DEFAULT)
    }

    fun decrypt(input: String, key: String): String {
        val k = generateKey(key)
        val c = Cipher.getInstance("AES")
        c.init(Cipher.DECRYPT_MODE, k)
        return try {
            val decodedValue = Base64.decode(input, Base64.DEFAULT)
            val decVal = c.doFinal(decodedValue)
            String(decVal)
        } catch (e: Exception) {
            ""
        }
    }

    private fun generateKey(input: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = input.toByteArray()
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }
}