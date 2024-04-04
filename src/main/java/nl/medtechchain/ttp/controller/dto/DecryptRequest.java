package nl.medtechchain.ttp.controller.dto;

public record DecryptRequest(int bitLength, String ciphertext) {
}
