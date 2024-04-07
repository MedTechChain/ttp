package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.encryption.EncryptionKey;

/**
 * Represents an encryption key for the Paillier cryptosystem.
 * <p>
 * The Paillier encryption scheme is a public-key cryptosystem known for its homomorphic properties.
 * This class specifically holds the public component 'n' of the Paillier key pair, which is used
 * in the encryption process. The 'n' parameter is a large number obtained from the multiplication
 * of two large prime numbers not exposed by this key component.
 */
public class PaillierEncryptionKey implements EncryptionKey {

    private String n;

    /**
     * Constructs a new PaillierEncryptionKey with the specified 'n' parameter.
     *
     * @param n The 'n' parameter of the Paillier encryption key, as a string.
     */
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
