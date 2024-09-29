package org.main.cryptomanager;
import javax.crypto.Cipher;
//import javax.crypto.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import org.mindrot.jbcrypt.BCrypt;


public class Crypto {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // AES with CBC mode
    private String password;
    private String secret;
    private SecretKeySpec secretKey;
    private IvParameterSpec ivSpec; 
    private Cipher cipherEnrypter; // = Cipher.getInstance(ALGORITHM);
    private Cipher cipherDecrypter;
    public Crypto(String password)
    {
        this.password = password;

    }

    public String encryptTimestamp(long timestamp) {
        try
        {
            String password = this.password;
            while (password.length()<32) {
                password+="*";
            }
            byte[] key = password.getBytes("UTF-8");  
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] iv = new byte[16]; 
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedTimestamp = cipher.doFinal(Long.toString(timestamp).getBytes());
            return Base64.getEncoder().encodeToString(encryptedTimestamp);
        }
        catch(Exception e)
        {
            return "";
        }
    }
    private String decryptTimeStamp(String encryptedDate) {
        try{
            String password = this.password;
            while (password.length()<32) {
                password+="*";
            }
            
            byte[] key = password.getBytes("UTF-8");  
            
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] iv = new byte[16]; 
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

    //        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,secretKey, ivSpec);
            
            // Decode the Base64 string back to bytes
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedDate);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            
            return new String(decryptedBytes, "UTF-8");
        }
        catch(Exception e)
        {
            return "";
        }
    }

    public boolean setSecretkey(String date)
    {
        try
        {
            this.secret = this.decryptTimeStamp(date)+password;
            while (this.secret.length()<32) {
                this.secret+="*";
            }
            System.out.println(this.secret.length());
            byte key[] = this.secret.getBytes("UTF-8");
            this.secretKey = new SecretKeySpec(key, "AES");
            byte[] iv = new byte[16]; 
            // SecureRandom random = new SecureRandom();
            // random.nextBytes(iv);  // Random IV
            this.ivSpec = new IvParameterSpec(iv);
            this.cipherEnrypter = Cipher.getInstance(ALGORITHM);
            this.cipherEnrypter.init(Cipher.ENCRYPT_MODE,secretKey, ivSpec);
            this.cipherDecrypter = Cipher.getInstance(ALGORITHM);
            this.cipherDecrypter.init(Cipher.DECRYPT_MODE, secretKey,ivSpec);
            return true; 
        }
        catch(Exception e)
        {
            System.out.println("Exception: "+e);
            return false;
        }
    }
    public String encryptText(String text) 
    {
        try
        {
            byte[] encryptedTimestamp = this.cipherEnrypter.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encryptedTimestamp);
        }
        catch(Exception e)
        {
            System.err.println("ERROR: "+e);
            return "";
        }
    }
    public String decryptText(String text)
    {
        try
        {
            byte[] decodedBytes = Base64.getDecoder().decode(text);
            byte[] decryptedBytes = cipherDecrypter.doFinal(decodedBytes);
            
            return new String(decryptedBytes, "UTF-8");
        }
        catch(Exception e)
        {
            return "";
        }
    }
    public static void main(String[] args) throws Exception {
        String password = "vishwajeet@123";
        System.out.println(password.length());
        Crypto cr = new Crypto(password);

        long signUpTimestamp = System.currentTimeMillis();  // Use current timestamp as example

        // Encrypt the timestamp
        String encryptedTimestamp = cr.encryptTimestamp(signUpTimestamp);
        System.out.println("Encrypted timeStamp: "+encryptedTimestamp);
        System.out.println("Decrepted timestamp: "+cr.decryptTimeStamp(encryptedTimestamp));
        if(cr.setSecretkey(encryptedTimestamp))
            System.out.println("SECRET KEY SET SUCCESS");
        String enc = cr.encryptText("heelo how are you!");
        System.err.println("Enrypted text: "+enc);
        String dec = cr.decryptText(enc);
        System.out.println("decrypted text: "+dec);

        System.out.println("Encrypted Timestamp: " + encryptedTimestamp);
        // System.out.println("DecyptedData: "+cr.decrypt(encryptedTimestamp,password));
    }
    public  String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify a password against a stored hash
    public  boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}

