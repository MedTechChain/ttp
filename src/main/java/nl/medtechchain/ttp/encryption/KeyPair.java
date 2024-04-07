package nl.medtechchain.ttp.encryption;

/**
 * Represents a generic interface for a key pair used in encryption operations.
 * <p>
 * This interface defines the contract for encryption-related classes that manage
 * key pairs. A key pair consists of an encryption key and a decryption key. Implementations of this
 * interface are expected to provide mechanisms to access both types of keys.
 */
public interface KeyPair {
    EncryptionKey getEncryptionKey();
    DecryptionKey getDecryptionKey();
}
