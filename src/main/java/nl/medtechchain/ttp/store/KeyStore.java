package nl.medtechchain.ttp.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import nl.medtechchain.ttp.encryption.EncryptionKey;
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
import java.util.UUID;

@Service
public class KeyStore {

    private final ObjectMapper om = new ObjectMapper();

    @Value("${store.dir.path}")
    private String storePath;

    @PostConstruct
    private void init() {
        File directory = new File(storePath);

        if (!directory.exists()) {
            if (!directory.mkdirs())
                throw new IllegalStateException("Cannot create key store directory");
        }
    }

    public boolean set(KeyPair<?, ?, ?, ?> keyPair) throws IOException {
        var keyFileName = UUID.nameUUIDFromBytes(om.writeValueAsBytes(keyPair.getEncryptionKey())).toString();
        Path keyFilePath = Paths.get(storePath, keyFileName);
        if (Files.exists(keyFilePath))
            return false;

        String jsonKeypair = om.writeValueAsString(keyPair);

        Files.writeString(keyFilePath, jsonKeypair);
        return true;

    }

    public Optional<KeyPair<?, ?, ?, ?>> get(EncryptionKey<?, ?, ?, ?> ek, EncryptionSchemeType encType) throws IOException {
        var keyFileName = UUID.nameUUIDFromBytes(om.writeValueAsBytes(ek)).toString();
        Path keyFilePath = Paths.get(storePath, keyFileName);

        if (Files.exists(keyFilePath)) {
            String jsonKeypair = String.join("", Files.readAllLines(keyFilePath));

            KeyPair<?, ?, ?, ?> keyPair = switch (encType) {
                case PAILLIER -> om.readValue(jsonKeypair, PaillierKeyPair.class);
            };

            return Optional.of(keyPair);
        } else {
            return Optional.empty();
        }
    }
}
