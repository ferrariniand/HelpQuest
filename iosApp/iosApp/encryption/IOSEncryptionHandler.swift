//TODO TEST IF IT IS RIGHT
EncryptionHandler().encrypt { data in
        // iOS AES encryption logic
        return data.encryptString
}
EncryptionHandler().decrypt { encryptedData in
        //iOS decryption logic NSString is my class in
        //swift responsible for encryption/decryption
        return NSString.decryptData((encryptedData as! String), withKey: Services.mobOil)
}