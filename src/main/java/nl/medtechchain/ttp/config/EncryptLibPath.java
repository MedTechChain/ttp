package nl.medtechchain.ttp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Component to manage and validate the path to the encryption library binary.
 * <p>
 * This component is responsible for loading and verifying the path to an external encryption library binary,
 * specified through application configuration. It ensures that the binary exists at the specified location
 * at the time of application initialization and provides a global access point to this path for other components
 * within the application.
 */
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
