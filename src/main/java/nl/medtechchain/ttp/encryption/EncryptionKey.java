package nl.medtechchain.ttp.encryption;

public interface EncryptionKey<
        S extends EncryptionScheme<S, E, D, P>,
        E extends EncryptionKey<S, E, D, P>,
        D extends DecryptionKey<S, E, D, P>,
        P extends KeyPair<S, E, D, P>
        > {
}
