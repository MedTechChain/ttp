package nl.medtechchain.ttp.controller;

import nl.medtechchain.ttp.encryption.EncryptionKey;
import nl.medtechchain.ttp.encryption.EncryptionSchemeType;
import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.encryption.paillier.PaillierEncryptionScheme;
import nl.medtechchain.ttp.store.KeyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {

    private final KeyStore keyStore;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public MainController(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @GetMapping("/api/key/paillier")
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

    // TODO: following the same approaches, create the decryption route
}
