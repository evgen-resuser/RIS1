package com.evgen.hashCrackWorker.crack;

import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class HashUtils {
    public static String md5(String input) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());

            BigInteger number = new BigInteger(1, hashBytes);
            StringBuilder hexString = new StringBuilder(number.toString(16));

            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }

            hash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return hash;
    }
}
