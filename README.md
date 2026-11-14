# ShivCore
> Minecraft Plugins Made Modular
<img width="400" height="291" alt="2026-11-14_00-30-14" src="https://github.com/user-attachments/assets/ed84a28f-fa36-43e2-8a71-808bae395d6a" />

### What is ShivCore?
ShivCore serves as a general utility library focusing heavily on making the development of content more efficient through modularity.<br>
___
In the design philosophy behind ShivCore, modularity is the top priority, in which it provides the following tools for developers:
|         Builders            |      General Systems        |
|-----------------------------|-----------------------------|
| ● Raycast Builders          | ● Player Trust System       |
| ● Automatic Config Builder  | ● Status Effect System      |
| ● Particle Builders         | ● Command System            |
| ● Animated Particle Builder | ● True Damage System        |
| ● GUI Builders              | ● Segment Based Rope System |
| ● Entity Behavior Builders  | ● Custom Ticking Entity System| 

## What is Made With ShivCore?
### Bliss SMP - Auratus Gem
The Auratus Gem is a commission which acts as a canvas for content creators to build narratives off of, where multiple YouTubers have reached **hundreds of thousands** of views from
videos based around the Auratus Gem. Additionally, as a result of these videos there are multiple fan projects online for public download which attempt to mimic the plugin.

<img width="326" height="230" alt="image" src="https://github.com/user-attachments/assets/f864986b-5030-4426-82d7-bfcf9ca4c809" />
<img width="326" height="230" alt="image" src="https://github.com/user-attachments/assets/f9f75ccc-a729-477e-b175-6486ded073df" /> <br>

> YouTubers: DumbIsDumb & Pinity

___

### Ocular SMP
OcularSMP is an in-development project with the goal of adding the perfect ability based combat where the design philosophy prioritizes fun and fairness among all else. It is an SMP ability plugin where the player is provided 3 slots and *(eventually)* 20 abilities to pick and choose from.

<img width="400" height="244" alt="2026-11-14_00-44-25" src="https://github.com/user-attachments/assets/c0086412-7395-49d3-a6de-0477c53bc8f0" />
<img width="400" height="240" alt="2026-08-17_13-23-44" src="https://github.com/user-attachments/assets/65bcc72a-b43c-49b3-9348-5c2263fb3a82" />

### Custom Ticking Entities
**Creating Ticking Entities**
For creating a custom entity, all that is needed is to create a new class extending AbstractTickingEntity with the generic parameter of the vanilla base entity that you're going to be using (eg: ItemDisplay, Marker, Zombie, etc). All of the entity's ticking logic goes inside of `tick(Entity e)` while all of the spawning logic goes inside of `spawnArgs(Entity e)`. 
<br><br>
**Adding Conditional Behaviors**<br>
Custom behaviors allow for you to create additional conditional logic separate from the entity's base class. New Behaviors can be created by adding a static nested class into the base class of your TickingEntity which extends Behavior, with the generic parameters of the base class of the TickingEntity and your base vanilla entity.
```
public class TestEntity extends AbstractTickingEntity<ItemDisplay> {
    public TestEntity(EntityType baseEntity, String entityID, BehaviorController behaviorController) {
        super(baseEntity, entityID, behaviorController);
    }

    // CONSTANT tickingLogic for the entity
    @Override
    public void tick(ItemDisplay entity) {
        // base logic goes here
    }

    @Override
    protected void spawnArgs(ItemDisplay entity) {
        // spawning / creation logic goes here
    }

    public static class Default extends Behavior<ItemDisplay, TestEntity> {
        @Override
        public TestEntity entityClass() {
            // reference to a static initialization of the entity's base class
            return Entities.TEST_ENTITY;
        }

        @Override
        public void tick(ItemDisplay e) {
            // the behavior's logic goes here
            e.getWorld().spawnParticle(Particle.CRIT, e.getLocation(), 1);
            MiscUtil.debugMessage("default behavior");
        }
    }

    public static class Blaze extends Behavior<ItemDisplay, TestEntity> {
        public Blaze(String behaviorID, Predicate<Entity> activePredicate) {
            super(behaviorID, activePredicate);
        }

        @Override
        public TestEntity entityClass() {
            return Entities.TEST_ENTITY;
        }

        @Override
        public void tick(ItemDisplay e) {
            e.getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 1);
            e.getWorld().spawnParticle(Particle.SMOKE, e.getLocation(), 1);
            MiscUtil.debugMessage("fire behavior");
        }
    }

    // as many behaviors as you want can be added, in this case, the Breeze Behavior would do something different than the Blaze Behavior
    public static class Breeze extends Behavior<ItemDisplay, TestEntity> {...}
}
```
