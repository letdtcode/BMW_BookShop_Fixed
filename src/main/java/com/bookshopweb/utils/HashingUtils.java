package com.bookshopweb.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashingUtils {
    public static String hash(String password) {
        String hashed = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        boolean passwordMatched = BCrypt.verifyer().verify(password.toCharArray(), hashed).verified;
        return hashed;
    }
}


