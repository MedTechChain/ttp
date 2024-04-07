package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.medtechchain.ttp.encryption.KeyPair;

/**
 * Represents a key pair for the Paillier encryption scheme.
 * <p>
 * This class holds both components of a key pair used in Paillier encryption: the encryption key
 * and the decryption key. It provides a concrete implementation of the {@link KeyPair} interface,
 * tailored specifically to the Paillier cryptosystem's requirements.
 */
public class PaillierKeyPair implements KeyPair {

    private final PaillierEncryptionKey encryptionKey;
    private final PaillierDecryptionKey decryptionKey;

    /**
     * Constructs a PaillierKeyPair with specified encryption and decryption keys.
     *
     * @param encryptionKey The Paillier encryption key.
     * @param decryptionKey The Paillier decryption key.
     */
    @JsonCreator
    public PaillierKeyPair(@JsonProperty("encryptionKey") PaillierEncryptionKey encryptionKey,
                           @JsonProperty("decryptionKey") PaillierDecryptionKey decryptionKey) {
        this.encryptionKey = encryptionKey;
        this.decryptionKey = decryptionKey;
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
