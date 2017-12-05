package com.decipherzone.dropwizard.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public enum PasswordManager
{
    INSTANCE;

    private static final int ITERATION = 20 * 1000;
    private static final int SALTLEN = 32;
    private static final int DESIREDKEYLEN = 256;

    /**
     * Computes a salted PBKDF2 hash of given plaintext password
     * suitable for storing in a database.
     * Empty passwords are not supported.
     */
    public String getSaltedHash(String password)
    {
        String saltedPassword = password;
        try
        {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SALTLEN);
            saltedPassword = Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return saltedPassword;
    }

    /**
     * Checks whether given plaintext password corresponds
     * to a stored salted hash of the password.
     */
    public boolean check(String password, String stored)
    {
        String hashOfInput = "";
        String[] saltAndPass = stored.split("\\$");
        if (saltAndPass.length != 2)
        {
            throw new IllegalStateException("The stored password have the form 'salt$hash'");
        }
        try
        {
            hashOfInput = hash(password, Base64.getDecoder().decode(saltAndPass[0]));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return hashOfInput.equals(saltAndPass[1]);
    }

    /**
     * Getting hash of password
     * @param password
     * @param salt
     * @return
     * @throws Exception
     */
    private String hash(String password, byte[] salt) throws Exception
    {
        if (password == null || password.length() == 0)
        {
            throw new IllegalArgumentException("Empty passwords are not supported.");
        }
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, ITERATION, DESIREDKEYLEN));
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
