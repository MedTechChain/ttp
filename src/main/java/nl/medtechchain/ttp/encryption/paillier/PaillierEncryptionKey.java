package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.encryption.EncryptionKey;

public class PaillierEncryptionKey implements EncryptionKey {

    private String n;

    @JsonCreator
    public PaillierEncryptionKey(@JsonProperty("n") String n) {
        this.n = n;
    }

    public String getN() {
        return this.n;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
