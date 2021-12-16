import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCryptoUtil {

    public void encryptFile(String specName, String key, String ivParameterSpec,
                                   File inputFile, File outputFile) throws Exception {

        Key secrtKey = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameterSpec.getBytes(StandardCharsets.UTF_8));
        Cipher cipher = Cipher.getInstance(specName);
        cipher.init(Cipher.ENCRYPT_MODE, secrtKey, iv);

        try (FileOutputStream output = new FileOutputStream(outputFile);
             CipherOutputStream cipherOutput = new CipherOutputStream(output, cipher)) {

            BufferedReader reader = new BufferedReader(new FileReader(inputFile.toString()));
            String data;
            while((data = reader.readLine()) != null) {
                data += "\n";
                cipherOutput.write(data.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public void decryptFile(String specName, String key, String ivParameterSpec,
                                   File encryptedFile, File decryptedFile) throws Exception {

        Key secrtKey = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameterSpec.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(specName);
        cipher.init(Cipher.DECRYPT_MODE, secrtKey, iv);

        try (
                CipherInputStream cipherInput = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
                InputStreamReader inputStream = new InputStreamReader(cipherInput);
                BufferedReader reader = new BufferedReader(inputStream);
                FileOutputStream fileOutput = new FileOutputStream(decryptedFile)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                fileOutput.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                sb.delete(0, sb.length());
            }

        }
    }

}
