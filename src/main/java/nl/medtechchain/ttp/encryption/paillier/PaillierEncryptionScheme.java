package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.config.EncryptLibPath;
import nl.medtechchain.ttp.encryption.EncryptionScheme;
import nl.medtechchain.ttp.subprocess.SubprocessCall;

import java.io.IOException;
import java.math.BigInteger;

public class PaillierEncryptionScheme implements EncryptionScheme<PaillierEncryptionScheme, PaillierEncryptionKey, PaillierDecryptionKey, PaillierKeyPair> {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    public PaillierKeyPair generateKeypair(int bitLength) throws IOException {
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

            return new PaillierKeyPair(new PaillierEncryptionKey(new BigInteger(n)), new PaillierDecryptionKey(new BigInteger(p), new BigInteger(q)));
        } catch (Exception e) {
            throw new IOException("Could not generate keys: Failed to parse keypair JSON", e);
        }
    }

    @Override
    public String encrypt(String plaintext, PaillierEncryptionKey ek) throws IOException {
        String command = String.format("%s encrypt --plaintext %s --ek '%s'", EncryptLibPath.get(), plaintext, om.writeValueAsString(ek));
        return SubprocessCall.execute(command);
    }

    @Override
    public String decrypt(String ciphertext, PaillierDecryptionKey dk) throws IOException {
        String command = String.format("%s decrypt --ciphertext '%s' --dk '%s'", EncryptLibPath.get(), ciphertext, om.writeValueAsString(dk));
        return SubprocessCall.execute(command);
    }
}
