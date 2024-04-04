package nl.medtechchain.ttp.controller.dto;

public record DecryptResponse(String scheme, String keyId, String plaintext) {
}
