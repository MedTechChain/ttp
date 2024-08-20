package nl.medtechchain.ttp.dto.encrypt.paillier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaillierEncryptionKeyResponse {
    private String encryptionKey;
}
