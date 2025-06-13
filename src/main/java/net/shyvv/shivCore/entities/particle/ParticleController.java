package fr.stormer3428.shivcore.entities.particle;

import fr.stormer3428.shivcore.ShivCoreMain;
import fr.stormer3428.shivcore.util.MiscUtil;
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

public class ParticleController {
    private final List<ParticleFrame> customModelDatas;
    private final int frameDuration;
    private final Vector fixedSize;
    private final boolean facesCamera;
    private final int brightness;

    private final String activeFrameID = "activeFrame";
    private final String frameTimeID = "frameTime";

    //Only used by the ParticleController builder, hence why it's private
    private ParticleController(Builder builder, int frameDuration, boolean facesCamera, int brightness, Vector fixedSize) {
        this.customModelDatas = builder.customModelDatas;
        this.fixedSize = fixedSize;
        this.facesCamera = facesCamera;
        this.brightness = brightness;
        this.frameDuration = frameDuration;
    }

    public void init(Entity e) {
        setInt(e, 99999999, frameTimeID);
    }

    public void tickFrames(Entity e) {
        ItemDisplay ie = (ItemDisplay) e;
        int s = customModelDatas.size();
        int af = getInt(ie, activeFrameID);
        int fd = incrementInt(ie, frameTimeID);

        ie.setBrightness(new Display.Brightness(brightness, brightness));

        if(this.facesCamera) {
            ie.setBillboard(Display.Billboard.CENTER);
        }

        //Frame is being changed
        if(fd > frameDuration) {
            incrementInt(ie, activeFrameID);
            setInt(ie, 0, frameTimeID);

            //Setting the next frame back to 0
            if(af > s) {
                setInt(ie, 0, activeFrameID);
            }

            //Changing the texture of the Item Display and the size
            ItemStack is = ie.getItemStack();
            assert is != null;
            ItemMeta im = is.getItemMeta();
            for(int i = 0; i < s; i++) {
                if(af == i) {
                    ParticleFrame frame = customModelDatas.get(i);
                    float sizeX;
                    float sizeY;
                    float sizeZ;

                    // cmd stuffs!
                    assert im != null;
                    im.setCustomModelData(frame.getCustomModelData());
                    is.setItemMeta(im);
                    ie.setItemStack(is);

                    // Size stuffs!
                    if(fixedSize == null) {
                        if (frame.usesSizeVector()) {
                            Vector sizeVector = frame.getFrameDisplaySizeVector();
                            sizeX = (float) sizeVector.getX();
                            sizeY = (float) sizeVector.getY();
                            sizeZ = (float) sizeVector.getZ();
                        } else {
                            //Looking at "size" this much makes me dyslexic
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

                    sizeX += getSizeModifier(e);
                    sizeY += getSizeModifier(e);
                    sizeZ += getSizeModifier(e);

                    if(facesCamera) {
                        sizeZ = 1;
                    }

                    //Setting the changed size values
                    Transformation defaultTransformation = ie.getTransformation();
                    ie.setTransformation(new Transformation(
                            defaultTransformation.getTranslation(),
                            defaultTransformation.getLeftRotation(),
                            // we only want to change the size here, right?
                            new Vector3f(sizeX, sizeY, sizeZ),
                            defaultTransformation.getRightRotation())
                    );
                }
            }
        }
    }

    public ParticleFrame getFrame(int frame) {
        try {
            return customModelDatas.get(frame);
        } catch (Exception ignored) {}
        return null;
    }

    public ParticleFrame getActiveFrame(Entity e) {
        int af = getInt(e, activeFrameID);
        for(int i = 0; i < customModelDatas.size(); i++) {
            if (af == i) {
                return customModelDatas.get(i);
            }
        }
        return null;
    }

    public int getFrameAmount() {
        return customModelDatas.size();
    }

    public int getFrameDuration() {
        return this.frameDuration;
    }

    public void setSizeModifier(Entity entity, float value) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), "sizeModifier"), PersistentDataType.FLOAT, value);
    }


    @SuppressWarnings("DataFlowIssue")
    private float getSizeModifier(Entity entity) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), "sizeModifier"))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), "sizeModifier"), PersistentDataType.FLOAT);
        }
        return 0;
    }

    // *cough cough* (I'm lazy)
    private int incrementInt(Entity entity, String id) {
        int i = getInt(entity, id);
        setInt(entity, i+1, id);
        return i;
    }
    private void setInt(Entity entity, int value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.INTEGER, value);
    }
    @SuppressWarnings("DataFlowIssue")
    private int getInt(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.INTEGER);
        }
        return 0;
    }

    public static class Builder {
        private final List<ParticleFrame> customModelDatas = new ArrayList<>();
        private int brightness;
        private boolean facesCamera;
        private Vector fixedSize;
        private int frameDuration;
        public Builder(int frameDuration) {
            this.frameDuration = frameDuration;
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

        public Builder addFrame(ParticleFrame frame) {
            customModelDatas.add(frame);
            return this;
        }

        public ParticleController build() {
            return new ParticleController(this, frameDuration, facesCamera, brightness, fixedSize);
        }
    }
}