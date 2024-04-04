package nl.medtechchain.ttp.encryption.paillier;

import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.util.StringToUUID;

import java.time.Instant;

public record PaillierKeyPair(Instant creationTime, PaillierEncryptionKey encryptionKey,
                              PaillierDecryptionKey decryptionKey) implements KeyPair {
    @Override
    public Instant creationTime() {
        return creationTime;
    }

    @Override
    public String id() {
        return StringToUUID.stringToUUID(this.toString()).toString();
    }
}
