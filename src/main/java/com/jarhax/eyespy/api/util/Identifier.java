package com.jarhax.eyespy.api.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Objects;

/**
 * An Identifier represents an ID that belongs within a specific namespace. Identifiers can be used as globally unique
 * id values.
 * <p>
 * For example, if your name was Timmy1234 and you made a mod called EyeSpyPlus, and you had an Armor property, your
 * identifier would be Timmy1234:EyeSpyPlus:Armor. This would not conflict with Jimmy67:YetAnotherAddon:Armor.
 */
public final class Identifier {

    /**
     * A thread-safe SHA256 instance used to hash the components of the Identifier.
     */
    private static final ThreadLocal<MessageDigest> SHA256 = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("SHA-256");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    });

    /**
     * The group component of the identifier. This is usually the name of the developer or publisher.
     */
    private final String group;

    /**
     * The name component of the identifier. This is usually the name of the project.
     */
    private final String name;

    /**
     * The entry component of the identifier. This is usually the name of a specific entry, like Stone or Dirt.
     */
    private final String entry;

    /**
     * A representation of the identifier that is safe to use in Hytale UI files.
     */
    private final String uiName;

    /**
     * @param group The name of the group or individual that owns the project. This should be the same as the string in
     *              your manifest.json.
     * @param name  The name of the project. This should be the same as the string in your manifest.json.
     * @param entry The name of the actual value.
     */
    public Identifier(String group, String name, String entry) {
        this.group = group;
        this.name = name;
        this.entry = entry;

        // UI element IDs have strict requirements. They must always start with
        // an uppercase letter, never start with a number, and only include
        // alphanumeric characters. Modifying the string to meet requirements
        // is a lossy process that will lead to conflicts. We use hex encoded
        // SHA-256 hashes to achieve this instead.
        this.uiName = "EyeSpy" + HexFormat.of().formatHex(SHA256.get().digest(this.toString().getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Produces identifiers within the EyeSpy group and name. This should only be used for built-in identifiers. New
     * elements added by addons should use their own group and name.
     */
    public static final Factory EYE_SPY = factory("JarHax", "EyeSpy");

    /**
     * Produces identifiers within the Hytale group and name.
     */
    public static final Factory HYTALE = factory("Hytale", "Hytale");

    /**
     * Provides a unique string representation of the identifier that can be used inside of UI files.
     *
     * @return A unique string representation of the identifier.
     */
    public String ui() {
        return this.uiName;
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s", this.group, this.name, this.entry);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Identifier otherId && Objects.equals(this.group, otherId.group) && Objects.equals(this.name, otherId.name) && Objects.equals(this.entry, otherId.entry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.group, this.name, this.entry);
    }

    /**
     * Creates a factory with a predefined group and name.
     *
     * @param group The predefined group. This is usually the name of the developer or publisher.
     * @param name  The predefined name. This is usually the name of the project.
     * @return A factory that produces identifiers with the group and name predefined.
     */
    public static Factory factory(String group, String name) {
        return entry -> new Identifier(group, name, entry);
    }

    /**
     * Produces identifiers with a predefined group and name.
     */
    @FunctionalInterface
    public interface Factory {

        /**
         * Creates an identifier with a given entry parameter. The group and name are predefined.
         *
         * @param entry The name of the entry, like Dirt or Cobblestone_Basalt.
         * @return An identifier with the predefined group and name, and the provided entry.
         */
        Identifier of(String entry);
    }
}