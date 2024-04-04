package nl.medtechchain.ttp.controller;

import nl.medtechchain.ttp.controller.dto.*;
import nl.medtechchain.ttp.encryption.EncryptionSchemeType;
import nl.medtechchain.ttp.encryption.KeyPair;
import nl.medtechchain.ttp.encryption.paillier.PaillierEncryptionScheme;
import nl.medtechchain.ttp.encryption.paillier.PaillierKeyPair;
import nl.medtechchain.ttp.store.KeyStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {

    private final KeyStore keyStore;

    public MainController(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @PostMapping("/api/paillier/key")
    public ResponseEntity<GetEncryptionKeyResponse> getEncryptionKey(@RequestBody GetEncryptionKeyRequest request) throws IOException {
        KeyStore.KeyFileName keyFileName = KeyStore.KeyFileName.make(EncryptionSchemeType.PAILLIER, request.bitLength() + "");

        var kpOpt = keyStore.get(keyFileName);

        KeyPair kp;

        if (kpOpt.isPresent())
            kp = kpOpt.get();
        else {
            kp = new PaillierEncryptionScheme(request.bitLength()).generateKeypair();

            if (!keyStore.set(keyFileName, kp))
                throw new IllegalStateException("Error occurred on server: Could not store key");
        }

        return ResponseEntity.ok(new GetEncryptionKeyResponse(
                EncryptionSchemeType.PAILLIER + "_" + request.bitLength(),
                kp.id(),
                kp.creationTime(),
                kp.encryptionKey()
        ));
    }

    // This method is just for testing purposes maybe
    @PostMapping("/api/paillier/encrypt")
    public ResponseEntity<EncryptResponse> encrypt(@RequestBody EncryptRequest request) throws IOException {
        KeyStore.KeyFileName keyFileName = KeyStore.KeyFileName.make(EncryptionSchemeType.PAILLIER, request.bitLength() + "");

        var kpOpt = keyStore.get(keyFileName);

        if (kpOpt.isPresent()) {
            PaillierKeyPair kp = (PaillierKeyPair) kpOpt.get();
            PaillierEncryptionScheme pes = new PaillierEncryptionScheme(request.bitLength());
            String ciphertext = pes.encrypt(request.plaintext(), kp.encryptionKey());
            return ResponseEntity.ok(new EncryptResponse(
                    EncryptionSchemeType.PAILLIER + "_" + request.bitLength(),
                    kp.id(),
                    ciphertext
            ));
        } else {
            throw new IllegalStateException("Key not set!");
        }
    }

    @PostMapping("/api/paillier/decrypt")
    public ResponseEntity<DecryptResponse> decrypt(@RequestBody DecryptRequest request) throws IOException {
        KeyStore.KeyFileName keyFileName = KeyStore.KeyFileName.make(EncryptionSchemeType.PAILLIER, request.bitLength() + "");

        var kpOpt = keyStore.get(keyFileName);

        if (kpOpt.isPresent()) {
            PaillierKeyPair kp = (PaillierKeyPair) kpOpt.get();
            PaillierEncryptionScheme pes = new PaillierEncryptionScheme(request.bitLength());
            String ciphertext = pes.decrypt(request.ciphertext(), kp.decryptionKey());
            return ResponseEntity.ok(new DecryptResponse(
                    EncryptionSchemeType.PAILLIER + "_" + request.bitLength(),
                    kp.id(),
                    ciphertext
            ));
        } else {
            throw new IllegalStateException("Key not set!");
        }
    }
}
