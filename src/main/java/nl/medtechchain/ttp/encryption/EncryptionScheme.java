package nl.medtechchain.ttp.encryption;

import java.io.IOException;

public interface EncryptionScheme<
        S extends EncryptionScheme<S, E, D, P>,
        E extends EncryptionKey<S, E, D, P>,
        D extends DecryptionKey<S, E, D, P>,
        P extends KeyPair<S, E, D, P>
        > {

    P generateKeypair(int bitLength) throws IOException;

    String encrypt(String plaintext, E ek) throws IOException;

    String decrypt(String ciphertext, D dk) throws IOException;
}
