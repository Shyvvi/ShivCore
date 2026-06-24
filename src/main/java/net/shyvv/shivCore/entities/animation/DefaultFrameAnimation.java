package net.shyvv.shivCore.entities.animation;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.function.Predicate;

public class DefaultFrameAnimation extends FrameAnimation {
    public DefaultFrameAnimation(Builder builder, int frameDuration, boolean facesCamera, int brightness, Vector fixedSize, Predicate<Entity> ap) {
        super(builder, frameDuration, facesCamera, brightness, fixedSize, ap);
    }

    public static class Builder extends FrameAnimation.Builder {
        public Builder(int frameDuration) {
            super(frameDuration, entity -> true);
        }
    }
}
