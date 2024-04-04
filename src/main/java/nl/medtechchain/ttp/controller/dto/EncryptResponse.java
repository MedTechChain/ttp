package nl.medtechchain.ttp.controller.dto;

public record EncryptResponse(String scheme, String keyId, String ciphertext) {
}
