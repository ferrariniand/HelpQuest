package com.helpquest.core.data.encryption

expect object Crypto {

    fun encrypt(data: String): String
    fun decrypt(encryptedData: String): String

}

object EncryptionHandler {
    var encryptionCallback: ((String) -> String)? = null
    var decryptionCallback: ((String) -> String)? = null

    fun encrypt(callback: (String) -> String) {
        encryptionCallback = callback
    }

    fun decrypt(callback: (String) -> String) {
        decryptionCallback = callback
    }
}