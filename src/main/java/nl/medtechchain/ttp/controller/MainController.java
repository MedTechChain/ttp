package nl.medtechchain.ttp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.medtechchain.ttp.encryption.EncryptionKey;
import nl.medtechchain.ttp.encryption.EncryptionSchemeType;
import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.encryption.paillier.PaillierDecryptionKey;
import nl.medtechchain.ttp.encryption.paillier.PaillierEncryptionKey;
import nl.medtechchain.ttp.encryption.paillier.PaillierEncryptionScheme;
import nl.medtechchain.ttp.store.KeyStore;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class MainController {

    private final KeyStore keyStore;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public MainController(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @GetMapping("/api/paillier/key")
    public ResponseEntity<EncryptionKey> getEncryptionKey(@RequestParam(required = false, defaultValue = "2048") String bitLengthParam) throws IOException {
        int bitLength = Integer.parseInt(bitLengthParam);

        KeyStore.KeyFileName keyFileName = KeyStore.KeyFileName.make(EncryptionSchemeType.PAILLIER, bitLength + "");

        var kpOpt = keyStore.get(keyFileName);
        if(kpOpt.isPresent())
            return ResponseEntity.ok(kpOpt.get().getEncryptionKey());

        KeyPair kp = new PaillierEncryptionScheme(bitLength).generateKeypair();

        if(!keyStore.set(keyFileName, kp))
            logger.warn("Key pair was not stored");

        return ResponseEntity.ok(kp.getEncryptionKey());
    }

    // This method is just for testing purposes maybe
    @PostMapping("/api/paillier/encrypt")
    public ResponseEntity<String> encrypt(@RequestParam(required = false, defaultValue = "2048") String bitLengthParam, @RequestBody String plaintext) throws IOException {
        int bitLength = Integer.parseInt(bitLengthParam);

        // Parse the value inside plaintext json to pass to encryption binary
        // {"plaintext": 42} -> 42
        ObjectMapper mapper = new ObjectMapper();
        plaintext = mapper.readTree(plaintext).get("plaintext").asText();

        KeyStore.KeyFileName keyFileName = KeyStore.KeyFileName.make(EncryptionSchemeType.PAILLIER, bitLength + "");
        var kpOpt = keyStore.get(keyFileName);

        if(kpOpt.isPresent()) {
            PaillierEncryptionKey ek = (PaillierEncryptionKey) kpOpt.get().getEncryptionKey();
            PaillierEncryptionScheme pes = new PaillierEncryptionScheme(bitLength);
            String ciphertext = pes.encrypt(plaintext, ek);
            return ResponseEntity.ok(ciphertext);
        } else {
            // TODO: handle error
            return null;
        }
    }

    @PostMapping("/api/paillier/decrypt")
    public ResponseEntity<String> decrypt(@RequestParam(required = false, defaultValue = "2048") String bitLengthParam, @RequestBody String ciphertext) throws IOException {
        int bitLength = Integer.parseInt(bitLengthParam);

        KeyStore.KeyFileName keyFileName = KeyStore.KeyFileName.make(EncryptionSchemeType.PAILLIER, bitLength + "");
        var kpOpt = keyStore.get(keyFileName);

        if(kpOpt.isPresent()) {
            PaillierDecryptionKey dk = (PaillierDecryptionKey) kpOpt.get().getDecryptionKey();
            PaillierEncryptionScheme pes = new PaillierEncryptionScheme(bitLength);
            String plaintext = pes.decrypt(ciphertext, dk);
            return ResponseEntity.ok(plaintext);
        } else {
            // TODO: handle error
            return null;
        }
    }
}
