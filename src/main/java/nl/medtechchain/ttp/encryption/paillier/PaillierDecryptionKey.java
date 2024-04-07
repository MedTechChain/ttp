package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.encryption.DecryptionKey;

/**
 * Represents a decryption key for the Paillier cryptosystem.
 * <p>
 * This class encapsulates the components of a Paillier decryption key, specifically
 * the large prime numbers {@code p} and {@code q}. It provides functionality to serialize
 * the key to a JSON string, facilitating easy storage and transmission.
 */
public class PaillierDecryptionKey implements DecryptionKey {

    String p;
    String q;

    /**
     * Constructs a new PaillierDecryptionKey with specified prime numbers.
     *
     * @param p The first prime number component of the decryption key.
     * @param q The second prime number component of the decryption key.
     */
    @JsonCreator
    public PaillierDecryptionKey(@JsonProperty("p") String p, @JsonProperty("q") String q) {
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
