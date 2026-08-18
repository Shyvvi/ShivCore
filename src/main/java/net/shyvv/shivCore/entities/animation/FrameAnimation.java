package net.shyvv.shivCore.entities.animation;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FrameAnimation {
    private final List<Frame> customModelDatas;
    private final int frameDuration;
    private final Vector fixedSize;
    private final boolean facesCamera;
    private final int brightness;
    private final Predicate<Entity> activePredicate;

    private final String activeFrameID = "activeFrame";
    private final String frameTimeID = "frameTime";

    public FrameAnimation(Builder builder, Predicate<Entity> ap) {
        this.customModelDatas = builder.customModelDatas;
        this.fixedSize = builder.fixedSize;
        this.facesCamera = builder.facesCamera;
        this.brightness = builder.brightness;
        this.frameDuration = builder.frameDuration;
        this.activePredicate = ap;
    }

    public void tickFrames(Entity e) {
        ItemDisplay ie = (ItemDisplay) e;
        int s = customModelDatas.size();

        ie.setBrightness(new Display.Brightness(brightness, brightness));
        ie.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);

        if (facesCamera) {
            ie.setBillboard(Display.Billboard.CENTER);
        }

        int fd = incrementInt(ie, frameTimeID);

        if (fd >= frameDuration) {
            setInt(ie, 0, frameTimeID);

            int af = getInt(ie, activeFrameID);

            if (af + 1 < s) {
                af++;
                setInt(ie, af, activeFrameID);

                Frame frame = customModelDatas.get(af);

                ItemStack is = ie.getItemStack();
                ItemMeta im = is.getItemMeta();

                im.setCustomModelData(frame.getCustomModelData());
                is.setItemMeta(im);
                ie.setItemStack(is);

                float sizeX;
                float sizeY;
                float sizeZ;

                if (fixedSize == null) {
                    if (frame.usesSizeVector()) {
                        Vector sizeVector = frame.getFrameDisplaySizeVector();
                        sizeX = (float) sizeVector.getX();
                        sizeY = (float) sizeVector.getY();
                        sizeZ = (float) sizeVector.getZ();
                    } else {
                        float size = frame.getFrameDisplaySize();
                        sizeX = size;
                        sizeY = size;
                        sizeZ = size;
                    }
                } else {
                    sizeX = (float) fixedSize.getX();
                    sizeY = (float) fixedSize.getY();
                    sizeZ = (float) fixedSize.getZ();
                }

                float sizeModifier = getSizeModifier(e);

                sizeX += sizeModifier;
                sizeY += sizeModifier;
                sizeZ += sizeModifier;

                if (facesCamera) {
                    sizeZ = 1;
                }

                Transformation transformation = ie.getTransformation();

                ie.setTransformation(new Transformation(
                        transformation.getTranslation(),
                        transformation.getLeftRotation(),
                        new Vector3f(sizeX, sizeY, sizeZ),
                        transformation.getRightRotation()
                ));
            }
        }
    }

    public Frame getFrame(int frame) {
        try {
            return customModelDatas.get(frame);
        } catch (Exception ignored) {}
        return null;
    }

    public Frame getActiveFrame(Entity e) {
        int af = getInt(e, activeFrameID);
        for(int i = 0; i < customModelDatas.size(); i++) {
            if (af == i) {
                return customModelDatas.get(i);
            }
        }
        return null;
    }

    public boolean isActive(Entity e) {
        return this.activePredicate.test(e);
    }

    public int getFrameAmount() {
        return customModelDatas.size();
    }

    public int getFrameDuration() {
        return this.frameDuration;
    }

    public void setSizeModifier(Entity entity, float value) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), "sizeModifier"), PersistentDataType.FLOAT, value);
    }


    @SuppressWarnings("DataFlowIssue")
    private float getSizeModifier(Entity entity) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), "sizeModifier"))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "sizeModifier"), PersistentDataType.FLOAT);
        }
        return 0;
    }

    public void init(Entity e) {
        setInt(e, 99999999, frameTimeID);
    }

    // *cough cough* (I'm lazy)
    private int incrementInt(Entity entity, String id) {
        int i = getInt(entity, id);
        setInt(entity, i+1, id);
        return i;
    }
    private void setInt(Entity entity, int value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.INTEGER, value);
    }
    @SuppressWarnings("DataFlowIssue")
    private int getInt(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCore.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), id), PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static class Builder {
        private final List<Frame> customModelDatas = new ArrayList<>();
        private int brightness;
        private boolean facesCamera;
        private Vector fixedSize;
        private final int frameDuration;
        private final Predicate<Entity> activePredicate;
        public Builder(int frameDuration, Predicate<Entity> activePredicate) {
            this.frameDuration = frameDuration;
            this.activePredicate = activePredicate;
        }

        public Builder setFixedSize(Vector size) {
            this.fixedSize = size;
            return this;
        }

        public Builder setFacesCamera(boolean facesCamera) {
            this.facesCamera = facesCamera;
            return this;
        }

        public Builder setBrightness(int brightness) {
            this.brightness = brightness;
            //Makin' sure no exceptions are called by input exceeding normal brightness levels
            if(brightness > 15) {
                this.brightness = 15;
            }
            if(brightness < 0) {
                this.brightness = 0;
            }
            return this;
        }

        public Builder addFrame(Frame frame) {
            customModelDatas.add(frame);
            return this;
        }

        public FrameAnimation build() {
            return new FrameAnimation(this, activePredicate);
        }
    }
}