package nl.medtechchain.ttp.controller.dto;

import nl.medtechchain.ttp.encryption.EncryptionKey;

import java.time.Instant;

public record GetEncryptionKeyResponse(String scheme, String keyId, Instant creationTime, EncryptionKey key) {
}
