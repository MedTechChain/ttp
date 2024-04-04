package nl.medtechchain.ttp.controller.dto;

public record EncryptRequest(int bitLength, String plaintext) {
}
