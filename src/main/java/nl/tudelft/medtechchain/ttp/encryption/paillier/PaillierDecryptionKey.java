package nl.tudelft.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tudelft.medtechchain.ttp.encryption.DecryptionKey;

public class PaillierDecryptionKey implements DecryptionKey {

    String p;
    String q;

    public PaillierDecryptionKey(String p, String q) {
        this.p = p;
        this.q = q;
    }

    public String getP() {
        return this.p;
    }

    public String getQ() {
        return this.q;
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
