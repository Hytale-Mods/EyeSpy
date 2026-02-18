package com.jarhax.eyespy.api.info;

import com.jarhax.eyespy.api.context.Context;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Provides information that will be added to the EyeSpy HUD.
 *
 * @param <CTX> The type of context processed by the provider.
 */
@FunctionalInterface
public interface InfoProvider<CTX extends Context> {

    /**
     * The color used by new lines of info added to the HUD.
     */
    Color INFO_COLOR = new Color(170, 170, 170);

    /**
     * The color used for the mod/owner text added to the HUD.
     */
    Color OWNER_COLOR = new Color(85, 85, 255);

    /**
     * Formats decimals, rounding them to one place of precision, or removing them entirely if the decimal would be
     * zero.
     */
    DecimalFormat ROUNDED_1 = new DecimalFormat("#.#");

    /**
     * Provides additional information about the current context.
     *
     * @param context Context about the game, the observer, and the thing being observed.
     * @param info    A builder that collects provided info and assembles it into the HUD UI.
     */
    void provideInfo(CTX context, InfoBuilder info);
}