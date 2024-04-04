package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.encryption.DecryptionKey;

public record PaillierDecryptionKey(String p, String q) implements DecryptionKey {

    public String json() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
