package nl.medtechchain.ttp.encryption;

import java.time.Instant;

public interface KeyPair {
    Instant creationTime();
    String id();
    EncryptionKey encryptionKey();
    DecryptionKey decryptionKey();
}
