package com.helpquest.core.data.encryption

actual object Crypto {
    actual fun encrypt(data: String): String {
        return EncryptionHandler.encryptionCallback?.invoke(data).orEmpty()
    }

    actual fun decrypt(encryptedData: String): String {
        return EncryptionHandler.decryptionCallback?.invoke(encryptedData).orEmpty()
    }
}

//TODO WHERE SHOULD BE CALLED THIS PART???? maybe on iOS app?
//EncryptionHandler().encrypt { data in
//        // iOS AES encryption logic
//        return data.encryptString
//}
//EncryptionHandler().decrypt { encryptedData in
////iOS decryption logic NSString is my class in
////swift responsible for encryption/decryption
//        return NSString.decryptData((encryptedData as! String), withKey: Services.mobOil)
//}