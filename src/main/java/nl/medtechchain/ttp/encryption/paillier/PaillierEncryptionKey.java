package nl.medtechchain.ttp.encryption.paillier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.medtechchain.ttp.encryption.EncryptionKey;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PaillierEncryptionKey implements EncryptionKey<PaillierEncryptionScheme, PaillierEncryptionKey, PaillierDecryptionKey, PaillierKeyPair> {
    private final String n;

    public PaillierEncryptionKey(BigInteger n) {
        this.n = n.toString();
    }

    public BigInteger n() {
        return new BigInteger(n);
    }
}
