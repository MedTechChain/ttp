package nl.medtechchain.ttp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class EncryptLibPath {

    private static EncryptLibPath instance;

    @Value("${encrypt.binary}")
    private String encryptBinaryPath;

    @PostConstruct
    private void init() {
        instance = this;

        if (!Files.exists(Paths.get(encryptBinaryPath)))
            throw new IllegalStateException(String.format("Encrypt binary not found at path '%s'.", encryptBinaryPath));
    }

    public static String get() {
        return instance.encryptBinaryPath;
    }
}
