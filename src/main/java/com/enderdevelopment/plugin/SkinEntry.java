package com.enderdevelopment.plugin;

/**
 * Represents a single predefined morphing skin.
 *
 * @param name      The unique name / identifier of the skin.
 * @param display   The display name shown in the GUI.
 * @param material  The material used for the GUI icon.
 * @param texture   The base64 skin texture value.
 * @param signature The optional skin signature, may be null.
 */
public record SkinEntry(String name, String display, String material, String texture, String signature) {

    public boolean hasSignature() {
        return signature != null && !signature.isBlank();
    }
}
