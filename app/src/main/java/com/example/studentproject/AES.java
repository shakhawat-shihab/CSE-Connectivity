package com.example.studentproject;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
//import java.util.Base64;
//for android we can't use java.util.Base64
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")),Base64.NO_WRAP);

        }
        catch (Exception e)
        {
         e.printStackTrace();
        }
        //return the exact password when unable to encrypt
        return strToEncrypt;
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            //return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            return new String(cipher.doFinal(Base64.decode(strToDecrypt,Base64.NO_WRAP)));
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
        //return the exact password when unable to encrypt
        return strToDecrypt;
    }

}
