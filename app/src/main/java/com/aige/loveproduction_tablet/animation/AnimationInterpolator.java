package com.aige.loveproduction_tablet.animation;

import android.view.animation.Interpolator;

public class AnimationInterpolator {

    /**
     * 果冻效果插值器
     */
    public static class JellyInterpolator implements Interpolator {
        private final float factor = 0.15f;

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }
}
