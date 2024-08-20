package nl.medtechchain.ttp.dto.encrypt.paillier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaillierEncryptRequest {
    private String encryptionKey;
    private String plaintext;
}
