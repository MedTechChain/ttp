package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.medtechchain.ttp.encryption.KeyPair;

public class PaillierKeyPair implements KeyPair {

    private final PaillierEncryptionKey encryptionKey;
    private final PaillierDecryptionKey decryptionKey;

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
