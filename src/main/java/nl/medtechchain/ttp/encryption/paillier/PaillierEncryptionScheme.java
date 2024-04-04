package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.config.EncryptLibPath;
import nl.medtechchain.ttp.encryption.DecryptionKey;
import nl.medtechchain.ttp.encryption.EncryptionKey;
import nl.medtechchain.ttp.encryption.EncryptionScheme;
import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.util.SubprocessCall;

import java.io.IOException;
import java.time.Instant;

public class PaillierEncryptionScheme implements EncryptionScheme {

    private final int bitLength;

    public PaillierEncryptionScheme(int bitLength) {
        this.bitLength = bitLength;
    }

    @Override
    public KeyPair generateKeypair() throws IOException {
        String command = String.format("%s keygen --bit-length %d", EncryptLibPath.get(), bitLength);
        String jsonKeypair = SubprocessCall.execute(command);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonKeypair);

            JsonNode ekNode = objectMapper.readTree(rootNode.get("encryptionKey").asText());
            JsonNode dkNode = objectMapper.readTree(rootNode.get("decryptionKey").asText());

            String n = ekNode.get("n").asText();
            String p = dkNode.get("p").asText();
            String q = dkNode.get("q").asText();

            return new PaillierKeyPair(Instant.now(), new PaillierEncryptionKey(n), new PaillierDecryptionKey(p, q));
        } catch (Exception e) {
            throw new IOException("Could not generate keys: Failed to parse keypair JSON", e);
        }
    }

    @Override
    public String encrypt(String plaintext, EncryptionKey ek) throws IOException {
        String command = String.format("%s encrypt --plaintext %s --ek '%s'", EncryptLibPath.get(), plaintext, ek.json());
        return SubprocessCall.execute(command);
    }

    @Override
    public String decrypt(String ciphertext, DecryptionKey dk) throws IOException {
        String command = String.format("%s decrypt --ciphertext '%s' --dk '%s'", EncryptLibPath.get(), ciphertext, dk.json());
        return SubprocessCall.execute(command);
    }
}
