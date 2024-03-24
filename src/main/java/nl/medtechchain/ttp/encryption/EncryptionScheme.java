package nl.medtechchain.ttp.encryption;

import java.io.IOException;

public interface EncryptionScheme {
    /**
     * Generates a new keypair and returns it as a single JSON string.
     *
     * @return A JSON string containing both the encryption and decryption keys.
     */
    KeyPair generateKeypair() throws IOException;

    /**
     * Encrypts a given plaintext with the provided encryption key.
     *
     * @param plaintext The plaintext to encrypt.
     * @param ek The encryption key in JSON format.
     * @return The encrypted ciphertext in a serialized form.
     */
    String encrypt(String plaintext, EncryptionKey ek) throws IOException;

    /**
     * Decrypts a given ciphertext with the provided decryption key.
     *
     * @param ciphertext The ciphertext to decrypt in a serialized form.
     * @param dk The decryption key in JSON format.
     * @return The decrypted plaintext.
     */
    String decrypt(String ciphertext, DecryptionKey dk) throws IOException;
}
