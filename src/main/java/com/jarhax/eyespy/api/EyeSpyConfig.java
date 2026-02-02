package com.jarhax.eyespy.api;

import com.jarhax.eyespy.api.info.AnchorBuilder;
import com.jarhax.eyespy.impl.hud.LayoutMode;

public interface EyeSpyConfig {

    AnchorBuilder position();

    LayoutMode layoutMode();

    boolean visible();

    boolean showContainers();

    boolean showProcessingTimes();

    boolean showInBackground();
}
