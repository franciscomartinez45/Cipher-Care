
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.*;

public class CipherCareEncryption{ 

    private static String publicKey="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ4eVIFfdvHTvtOADFAbGzlt1dQijiglpFvGVAjz150SH8FOxame3I7XigX8wxCAtjmcGiZS2Xp7/Keouy+bm00CAwEAAQ==";
    private static String privateKey="MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAnh5UgV928dO+04AMUBsbOW3V1CKOKCWkW8ZUCPPXnRIfwU7FqZ7cjteKBfzDEIC2OZwaJlLZenv8p6i7L5ubTQIDAQABAkALQTug2KOis5pHlSILPHeAuOJs26eeKib1VPwohcbV4+9uf3xr/VSUixKnHCChkOiRsru+Mh73htGz0UQcCALnAiEAnv8Tosw8AU1+pvL1JF1HB70aQpY7nUGex+WttHV0KgMCIQD+liK4THeYs2Wm3mRW4yBG07xKTjTRBV92dKBbAkHMbwIgA7gZwHwghzNF3CfgvhoEeFTfEWGsQVBwo2Jm/sqkjfsCIBgExBvNIw9mBXYbzy2fWgE0orQD8tmxfxMm56YObXTDAiB8g6vtMBkFKAQR1jspRKyK4SL2jt8a63L+FJMe59kK5w=="; 

    public static void main(String[] args){
        
    }

    public static String encrypt(String message) throws Exception {
        byte[] pubKeyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        PublicKey publicKey =  keyFactory.generatePublic(pubKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
    }
 
    public static String decrypt(String encryptedMessage) throws Exception {
        byte[] priKeyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey= keyFactory.generatePrivate(priKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);
    }
 }


