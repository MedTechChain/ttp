package nl.tudelft.medtechchain.ttp.encryption.paillier;

import nl.tudelft.medtechchain.ttp.encryption.HomomorphicEncryptionScheme;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PaillierHomomorphicEncryptionScheme implements HomomorphicEncryptionScheme {

    private static final String BINARY_PATH;

    static {
        // Get working directory, root of project
        String currentDir = System.getProperty("user.dir");
        // Path to the rust paillier binary
        BINARY_PATH = currentDir
                + "/paillier-encryption-backend/target/release/paillier-encryption-backend";
    }

    @Override
    public String generateKeypair() {
        String command = BINARY_PATH + " keygen";
        return executeCommand(command);
    }

    @Override
    public String encrypt(long plaintext, String ek) {
        String command = BINARY_PATH + " encrypt --plaintext " + plaintext + " --ek '" + ek + "'";
        return executeCommand(command);
    }

    @Override
    public String decrypt(String ciphertext, String dk) {
        String command = BINARY_PATH + " decrypt --ciphertext '" + ciphertext + "' --dk '" + dk + "'";
        return executeCommand(command);
    }

    private String executeCommand(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
