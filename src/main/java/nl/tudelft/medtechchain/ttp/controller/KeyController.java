package nl.tudelft.medtechchain.ttp.controller;

import nl.tudelft.medtechchain.ttp.encryption.EncryptionKey;
import nl.tudelft.medtechchain.ttp.encryption.KeyPair;
import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierEncryptionKey;
import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/key")
public class KeyController {

    private final KeyPair keyPair;

    @Autowired
    public KeyController(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @GetMapping
    public ResponseEntity<EncryptionKey> getEncryptionKey() {
        return ResponseEntity.ok(keyPair.getEncryptionKey());
    }

}
