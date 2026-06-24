package net.shyvv.shivCore.entities.animation;

import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class AnimationController {
    List<FrameAnimation> animations;
    DefaultFrameAnimation defaultAnimation;
    public AnimationController(List<FrameAnimation> animations, DefaultFrameAnimation defaultAnimation) {
        this.animations = animations;
        this.defaultAnimation = defaultAnimation;
    }

    public void tickAnimations(Entity e) {
        boolean nonDefaultAnimRunning = false;
        for(FrameAnimation pa : animations) {
            if(pa.isActive(e)) {
                pa.tickFrames(e);
            }
        }
        if(!nonDefaultAnimRunning && defaultAnimation != null) {
            defaultAnimation.tickFrames(e);
        }
    }

    public static class Builder {
        List<FrameAnimation> animations = new ArrayList<>();
        DefaultFrameAnimation defaultAnimation = null;
        public Builder addAnimation(FrameAnimation pa) {
            animations.add(pa);
            return this;
        }

        public Builder addDefaultAnimation(DefaultFrameAnimation pa) {
            defaultAnimation = pa;
            return this;
        }

        public AnimationController build() {
            return new AnimationController(animations, defaultAnimation);
        }
    }
}
