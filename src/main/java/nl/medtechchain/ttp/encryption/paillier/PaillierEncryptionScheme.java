package nl.medtechchain.ttp.encryption.paillier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.config.EncryptLibPath;
import nl.medtechchain.ttp.encryption.DecryptionKey;
import nl.medtechchain.ttp.encryption.EncryptionKey;
import nl.medtechchain.ttp.encryption.EncryptionScheme;
import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.subprocess.SubprocessCall;

import java.io.IOException;

/**
 * Implementation of the Paillier encryption scheme.
 * <p>
 * This class provides methods for generating key pairs, encrypting plaintext, and decrypting ciphertext
 * using the Paillier cryptosystem. It leverages external commands for cryptographic operations,
 * which are accessible through the path specified by {@link EncryptLibPath}.
 */
public class PaillierEncryptionScheme implements EncryptionScheme {

    private final int bitLength;

    /**
     * Constructs a new PaillierEncryptionScheme with the specified bit length for key generation.
     *
     * @param bitLength The bit length for the Paillier key pair. This typically determines the security level
     *                  of the cryptographic operations.
     */
    public PaillierEncryptionScheme(int bitLength) {
        this.bitLength = bitLength;
    }

    /**
     * Generates a Paillier key pair.
     * <p>
     * Executes an external command to generate a Paillier key pair, then parses the JSON output to
     * construct and return a {@link KeyPair} instance.
     *
     * @return A {@link KeyPair} containing the generated Paillier encryption and decryption keys.
     * @throws IOException If there's an error executing the command or parsing the output.
     */
    @Override
    public KeyPair generateKeypair() throws IOException {
        String command = String.format("%s keygen --bit-length %d", EncryptLibPath.get(), bitLength);
        String jsonKeypair = SubprocessCall.execute(command);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonKeypair);

            JsonNode ekNode = objectMapper.readTree(rootNode.get("encryptionKey").asText());
            JsonNode dkNode = objectMapper.readTree(rootNode.get("decryptionKey").asText());

            String n = ekNode.get("n").asText();
            String p = dkNode.get("p").asText();
            String q = dkNode.get("q").asText();

            return new PaillierKeyPair(new PaillierEncryptionKey(n), new PaillierDecryptionKey(p ,q));
        } catch (Exception e) {
            throw new IOException("Could not generate keys: Failed to parse keypair JSON", e);
        }
    }

    /**
     * Encrypts a plaintext string using a given encryption key.
     * <p>
     * Executes an external command to encrypt the given plaintext using the Paillier encryption key.
     *
     * @param plaintext The plaintext to encrypt.
     * @param ek        The encryption key to use.
     * @return The encrypted ciphertext as a string.
     * @throws IOException If there's an error executing the encryption command.
     */
    @Override
    public String encrypt(String plaintext, EncryptionKey ek) throws IOException {
        String command = String.format("%s encrypt --plaintext %s --ek '%s'", EncryptLibPath.get(), plaintext, ek);
        return SubprocessCall.execute(command);
    }

    /**
     * Decrypts a ciphertext string using a given decryption key.
     * <p>
     * Executes an external command to decrypt the given ciphertext using the Paillier decryption key.
     *
     * @param ciphertext The ciphertext to decrypt.
     * @param dk         The decryption key to use.
     * @return The decrypted plaintext as a string.
     * @throws IOException If there's an error executing the decryption command.
     */
    @Override
    public String decrypt(String ciphertext, DecryptionKey dk) throws IOException {
        //String command = EncryptLibPath.get() + " decrypt --ciphertext '" + ciphertext + "' --dk '" + dk + "'";
        String command = String.format("%s decrypt --ciphertext '%s' --dk '%s'", EncryptLibPath.get(), ciphertext, dk);
        return SubprocessCall.execute(command);
    }
}
