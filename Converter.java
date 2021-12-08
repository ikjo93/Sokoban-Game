import java.io.File;
import java.nio.file.Paths;

public class Converter {
    public static void main(String[] args) throws Exception {

        AESCryptoUtil aesCryptoUtil = new AESCryptoUtil();

        String key = "Sokoban is good!";
        String specName = "AES/CBC/PKCS5Padding";
        String ivParameterSpec = "Sokoban is good!";

        File inputFile = Paths.get("C:\\map.txt").toFile();
        File encryptedFile = new File("map_enc.txt");
        aesCryptoUtil.encryptFile(specName, key, ivParameterSpec, inputFile, encryptedFile);
    }
}
