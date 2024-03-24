package nl.medtechchain.ttp.encryption;

public interface KeyPair {
    EncryptionKey getEncryptionKey();
    DecryptionKey getDecryptionKey();
}
