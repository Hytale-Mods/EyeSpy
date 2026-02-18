package com.jarhax.eyespy.api;

import com.jarhax.eyespy.api.ui.AnchorProperties;
import com.jarhax.eyespy.api.ui.LayoutMode;

public interface EyeSpyConfig {

    AnchorProperties position();

    LayoutMode layoutMode();

    boolean visible();

    boolean showContainers();

    boolean showProcessingTimes();

    boolean showInBackground();
}
