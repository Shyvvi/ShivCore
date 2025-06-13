package fr.stormer3428.shivcore.entities.particle;

import fr.stormer3428.shivcore.ShivCoreMain;
import fr.stormer3428.shivcore.entities.AbstractTickingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ParticleFrame {
    int customModelData;
    float frameDisplaySize;
    Vector frameDisplaySizeVector;
    public ParticleFrame(int customModelData) {
        this.customModelData = customModelData;
        this.frameDisplaySize = 1;
        this.frameDisplaySizeVector = null;
    }
    /// frameDisplaySize controls the size of the Item Display when this frame is played, by default it is 1
    public ParticleFrame(int customModelData, float frameDisplaySize) {
        this.customModelData = customModelData;
        this.frameDisplaySize = frameDisplaySize;
        this.frameDisplaySizeVector = null;
    }

    /// Alternative for if you want to change the size of the Item Display along x, y and z
    public ParticleFrame(int customModelData, Vector frameDisplaySizeVector) {
        this.customModelData = customModelData;
        this.frameDisplaySize = 0;
        this.frameDisplaySizeVector = frameDisplaySizeVector;
    }
    public int getCustomModelData() {
        return customModelData;
    }

    public boolean usesSizeVector() {
        return this.frameDisplaySizeVector != null;
    }

    public float getFrameDisplaySize() {
        return this.frameDisplaySize;
    }
    public Vector getFrameDisplaySizeVector() {
        return this.frameDisplaySizeVector;
    }
}
