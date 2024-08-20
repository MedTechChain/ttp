package nl.medtechchain.ttp.encryption.paillier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.medtechchain.ttp.encryption.KeyPair;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaillierKeyPair implements KeyPair<PaillierEncryptionScheme, PaillierEncryptionKey, PaillierDecryptionKey, PaillierKeyPair> {
    private PaillierEncryptionKey encryptionKey;
    private PaillierDecryptionKey decryptionKey;
}
