package nl.tudelft.medtechchain.ttp.encryption;

import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierDecryptionKey;
import nl.tudelft.medtechchain.ttp.encryption.paillier.PaillierEncryptionKey;

public interface KeyPair {
    EncryptionKey getEncryptionKey();
    DecryptionKey getDecryptionKey();
}
