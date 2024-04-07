package nl.medtechchain.ttp.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.encryption.EncryptionSchemeType;
import nl.medtechchain.ttp.encryption.paillier.PaillierKeyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Service for storing and retrieving cryptographic keys.
 * <p>
 * This service provides functionality to store and retrieve encryption keys,
 * specifically designed to work with different encryption schemes, with initial
 * support for the Paillier encryption scheme. The keys are stored in the file system
 * as JSON objects, in a directory specified by the {@code store.dir.path} property.
 */
@Service
public class KeyStore {

    private final ObjectMapper jackson = new ObjectMapper();

    @Value("${store.dir.path}")
    private String storePath;

    /**
     * Initializes the key store service, ensuring the existence of the storage directory.
     */
    @PostConstruct
    private void init() {
        File directory = new File(storePath);

        if (!directory.exists()) {
            if (!directory.mkdirs())
                throw new IllegalStateException("Cannot create key store directory");
        }
    }

    /**
     * Stores a given key pair in the key store.
     *
     * @param keyFileName The name of the key file.
     * @param keyPair     The key pair to store.
     * @return {@code true} if the key pair was successfully stored, {@code false} if a key with the same name already exists.
     * @throws IOException if an I/O error occurs while writing the key pair to the file system.
     */
    public boolean set(KeyFileName keyFileName, KeyPair keyPair) throws IOException {
        Path keyFilePath = Paths.get(storePath, keyFileName.name);
        if (Files.exists(keyFilePath))
            return false;

        String jsonKeypair = jackson.writeValueAsString(keyPair);

        Files.writeString(keyFilePath, jsonKeypair);
        return true;

    }

    /**
     * Retrieves a key pair from the key store.
     *
     * @param keyFileName The name of the key file to retrieve.
     * @return An {@code Optional} containing the key pair if found, or an empty {@code Optional} if no such key exists.
     * @throws IOException if an I/O error occurs while reading the key pair from the file system.
     */
    public Optional<KeyPair> get(KeyFileName keyFileName) throws IOException {
        Path keyFilePath = Paths.get(storePath, keyFileName.name);

        if (Files.exists(keyFilePath)) {
            String jsonKeypair = String.join("", Files.readAllLines(keyFilePath));

            KeyPair keyPair = switch (keyFileName.encType) {
                case PAILLIER -> jackson.readValue(jsonKeypair, PaillierKeyPair.class);
            };

            return  Optional.of(keyPair);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Represents a file name for storing a key pair, including the encryption scheme and additional identifiers.
     */
    public static class KeyFileName {
        private final String name;
        private final EncryptionSchemeType encType;

        private KeyFileName(String name) {
            this.name = name;
            this.encType = EncryptionSchemeType.valueOf(name.substring(0, name.indexOf("_")));
        }

        /**
         * Creates a new {@code KeyFileName} instance for a given encryption scheme and identifiers.
         *
         * @param type The encryption scheme type.
         * @param args Additional identifiers for the key file name.
         * @return A new {@code KeyFileName} instance.
         */
        public static KeyFileName make(EncryptionSchemeType type, String... args) {
            return new KeyFileName(type + "_" + String.join("_", args) + ".key");
        }

    }
}
