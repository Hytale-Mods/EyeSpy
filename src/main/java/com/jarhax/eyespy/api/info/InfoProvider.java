package com.jarhax.eyespy.api.info;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.jarhax.eyespy.api.context.Context;

public interface InfoProvider<CTX extends Context> {

    void updateDescription(CTX context, InfoBuilder info);

    default void modifyUI(CTX context, UICommandBuilder ui) {
        
    }
}