package com.uniflow.service;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QRCodeGeneratorServiceTest {

    private final QRCodeGeneratorService qrCodeGeneratorService = new QRCodeGeneratorService();

    @Test
    void generateQrCodeBase64ShouldReturnValidPngBase64() {
        String payload = "bookingId=10|venueName=PST1|equipmentOfficeName=PST Equipment Office|scheduledEndTime=2026-04-10T16:00";

        String base64 = qrCodeGeneratorService.generateQrCodeBase64(payload);

        assertFalse(base64.isBlank());
        byte[] decoded = Base64.getDecoder().decode(base64);
        byte[] pngHeader = new byte[] {(byte) 0x89, 0x50, 0x4E, 0x47};
        assertArrayEquals(pngHeader, new byte[] {decoded[0], decoded[1], decoded[2], decoded[3]});
    }

    @Test
    void generateQrCodeBase64ShouldThrowForBlankPayload() {
        assertThrows(IllegalArgumentException.class, () -> qrCodeGeneratorService.generateQrCodeBase64("  "));
    }
}
