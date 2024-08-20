package nl.medtechchain.ttp.controller;

import nl.medtechchain.ttp.dto.encrypt.paillier.*;
import nl.medtechchain.ttp.encryption.EncryptionSchemeType;
import nl.medtechchain.ttp.encryption.paillier.PaillierDecryptionKey;
import nl.medtechchain.ttp.encryption.paillier.PaillierEncryptionKey;
import nl.medtechchain.ttp.encryption.paillier.PaillierEncryptionScheme;
import nl.medtechchain.ttp.store.KeyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;

@RestController
public class MainController {

    private final KeyStore keyStore;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    public MainController(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @GetMapping("/api/paillier/key")
    public ResponseEntity<PaillierEncryptionKeyResponse> getEncryptionKey(@RequestParam(required = false, defaultValue = "2048") String bitLength) throws IOException {
        var kp = new PaillierEncryptionScheme().generateKeypair(Integer.parseInt(bitLength));

        if (!keyStore.set(kp))
            logger.warn("Key pair was not stored");

        return ResponseEntity.ok(new PaillierEncryptionKeyResponse(kp.getEncryptionKey().getN()));
    }

    @PostMapping("/api/paillier/encrypt")
    public ResponseEntity<PaillierEncryptResponse> encrypt(@RequestBody PaillierEncryptRequest request) throws IOException {
        PaillierEncryptionScheme pes = new PaillierEncryptionScheme();
        String ciphertext = pes.encrypt(new BigInteger(request.getPlaintext()).toString(), new PaillierEncryptionKey(new BigInteger(request.getEncryptionKey())));
        return ResponseEntity.ok(new PaillierEncryptResponse(new BigInteger(ciphertext).toString()));
    }

    @PostMapping("/api/paillier/decrypt")
    public ResponseEntity<PaillierDecryptResponse> decrypt(@RequestBody PaillierDecryptRequest request) throws IOException {
        var kpOpt = keyStore.get(new PaillierEncryptionKey(request.getEncryptionKey()), EncryptionSchemeType.PAILLIER);

        if (kpOpt.isPresent()) {
            PaillierDecryptionKey dk = (PaillierDecryptionKey) kpOpt.get().getDecryptionKey();
            PaillierEncryptionScheme pes = new PaillierEncryptionScheme();
            String plaintext = pes.decrypt(new BigInteger(request.getCiphertext()).toString(), dk);
            return ResponseEntity.ok(new PaillierDecryptResponse(new BigInteger(plaintext).toString()));
        } else {
            throw new IllegalArgumentException("Key not known to the server");
        }
    }
}
