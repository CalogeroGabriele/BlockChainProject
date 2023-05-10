package com.ITSecurity.BlockChainProject;

import java.util.Arrays;

public class HashingClass {
    public static String createHash(long timestamp, String lastHash, String[] data,int nonce,int difficulty){

        String sha256 = null;
        try {
            String input = timestamp+lastHash+Arrays.toString(data)+nonce+difficulty;
            sha256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
        }catch (Exception e) {
            System.out.println("Errore nella creazione dell'hash");
        }
        return sha256;
    }
}

