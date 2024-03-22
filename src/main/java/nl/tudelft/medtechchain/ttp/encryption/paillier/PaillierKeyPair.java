package nl.tudelft.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.medtechchain.ttp.encryption.KeyPair;

public class PaillierKeyPair implements KeyPair {

    private final PaillierEncryptionKey encryptionKey;
    private final PaillierDecryptionKey decryptionKey;

    public PaillierKeyPair(String jsonKeypair) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonKeypair);

            JsonNode ekNode = objectMapper.readTree(rootNode.get("encryptionKey").asText());
            JsonNode dkNode = objectMapper.readTree(rootNode.get("decryptionKey").asText());

            this.encryptionKey = new PaillierEncryptionKey(ekNode.get("n").asText());
            this.decryptionKey = new PaillierDecryptionKey(dkNode.get("p").asText(), dkNode.get("q").asText());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse keypair JSON", e);
        }
    }

    @Override
    public PaillierEncryptionKey getEncryptionKey() {
        return this.encryptionKey;
    }

    @Override
    public PaillierDecryptionKey getDecryptionKey() {
        return this.decryptionKey;
    }
}
