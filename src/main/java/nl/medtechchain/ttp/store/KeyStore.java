package nl.medtechchain.ttp.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import nl.medtechchain.ttp.encryption.EncryptionSchemeType;
import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.encryption.paillier.PaillierKeyPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class KeyStore {

    private final ObjectMapper jackson = new ObjectMapper();

    @Value("${store.dir.path}")
    private String storePath;

    public KeyStore() {
        jackson.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    private void init() {
        File directory = new File(storePath);

        if (!directory.exists()) {
            if (!directory.mkdirs())
                throw new IllegalStateException("Cannot create key store directory");
        }
    }

    public boolean set(KeyFileName keyFileName, KeyPair keyPair) throws IOException {
        Path keyFilePath = Paths.get(storePath, keyFileName.name);
        if (Files.exists(keyFilePath))
            return false;

        String jsonKeypair = jackson.writeValueAsString(keyPair);

        Files.writeString(keyFilePath, jsonKeypair);
        return true;

    }

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


    public static class KeyFileName {
        private final String name;
        private final EncryptionSchemeType encType;

        private KeyFileName(String name) {
            this.name = name;
            this.encType = EncryptionSchemeType.valueOf(name.substring(0, name.indexOf("_")));
        }

        public static KeyFileName make(EncryptionSchemeType type, String... args) {
            return new KeyFileName(type + "_" + String.join("_", args) + ".key");
        }

    }
}
