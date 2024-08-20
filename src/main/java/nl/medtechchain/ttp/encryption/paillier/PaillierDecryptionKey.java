package nl.medtechchain.ttp.encryption.paillier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.medtechchain.ttp.encryption.DecryptionKey;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PaillierDecryptionKey implements DecryptionKey<PaillierEncryptionScheme, PaillierEncryptionKey, PaillierDecryptionKey, PaillierKeyPair> {
    private final String p;
    private final String q;

    public PaillierDecryptionKey(BigInteger p, BigInteger q) {
        this.p = p.toString();
        this.q = q.toString();
    }

    public BigInteger p() {
        return new BigInteger(p);
    }

    public BigInteger q() {
        return new BigInteger(q);
    }
}
