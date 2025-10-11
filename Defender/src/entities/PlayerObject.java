package entities;

import enums.AnimationFrames;
import enums.EntityInfo;
import interfaces.PlayerInventory;
import manager.EntitiesManager;
import usable.DroppedItems;
import usable.InventoryItems;

import components.Image;
import components.IndicationBar;
import usable.weapons.Weapons;
import usable.weapons.combat.CombatType;
import usable.weapons.magic.MagicType;

import java.util.List;

public abstract class PlayerObject implements PlayerInventory {

    private final EntitiesManager entitiesManager;
    private final Image entityImage;
    private Image currentWeaponImage;


    private double xCord;
    private double yCord;
    private double speed;

    private IndicationBar hp;
    private IndicationBar mana;

    private double lastRegen;

    public PlayerObject(EntitiesManager entitiesManager, double xCord, double yCord) {
        this.entitiesManager = entitiesManager;
        this.entityImage = new Image();
        this.currentWeaponImage = new Image();

        this.xCord = xCord;
        this.yCord = yCord;
        this.speed = 1;

        this.hp = null;
        this.mana = null;
        this.lastRegen = 0;
    }

    @Override
    public double getXCord() {
        return this.xCord;
    }

    @Override
    public double getYCord() {
        return this.yCord;
    }

    public EntitiesManager getEntitiesManager() {
        return this.entitiesManager;
    }

    public Image getEntityImage() {
        return this.entityImage;
    }

    public double getSpeed() {
        return this.speed;
    }
    public double getHp() {
        return this.hp.getCurrentValue();
    }
    public double getMana() {
        if (this.mana != null) {
            return this.mana.getCurrentValue();
        }

        return -1;
    }
    public String getHPKey() {
        if (this.hp != null) {
            return "" + this.hp;
        }

        return "";
    }

    public String getManaKey() {
        if ( this.mana != null) {
            return "" + this.mana;
        }
        return "";

    }

    public void setEntityImage(EntityInfo newEntityInfo) {
        this.entityImage.setEntityInfo(newEntityInfo);
    }
    public void setEntityWeaponImage(EntityInfo weaponTypePath) {
        this.currentWeaponImage.setEntityInfo(weaponTypePath);
    }
    public void setEntityFrames(AnimationFrames animationFrames) {
        this.entityImage.setEntityAnimationFrames(animationFrames);
    }
    public void setXYCord(double newXCord, double newYCord) {
        this.xCord = newXCord;
        this.yCord = newYCord;
    }
    public void setSpeed(double newSpeed) {
        this.speed = newSpeed;
    }
    public void setHp(double maxSize) {
        if (this.hp == null) {
            this.hp = new IndicationBar(this, maxSize, EntityInfo.HEALTH_BAR);
        }

    }
    public void setMana(double maxSize) {
        if (this.mana == null) {
            this.mana = new IndicationBar(this, maxSize, EntityInfo.MANA_BAR);
        }


    }

    public List<DroppedItems> getDropItemList() {
        return this.entitiesManager.getDropItemList();
    }


    public void renderEntityImage(int rotation, boolean flip) {

        String entityID = "" + this;
        int[] dimensionsOfEntity = this.entityImage.getImageDimensions();


        String currentFramePath = this.entityImage.getCurrentFramePath();

        this.entitiesManager.renderEntityImage(
                entityID,
                currentFramePath,
                this.xCord,
                this.yCord,
                dimensionsOfEntity[0],
                dimensionsOfEntity[1],
                rotation,
                flip
        );


        this.renderEntityIndicator();
        this.renderEntityCurentInventorySlot();


    }

    public PlayerObject getClosestEntity(PlayerObject entity) {
        return this.entitiesManager.getClosestEntity(entity);
    }

    public void hitEntity(PlayerObject entity, double damageToProcess) {
        this.entitiesManager.delaDamageToEntity(entity, damageToProcess);
    }

    public void setNewHPStatus(double newDelta) {
        this.hp.updateIndicatorBarValue(newDelta);
        if (this.hp.getCurrentValue() <= 1) {
            this.renderEntityIndicator();
        }
    }

    public void setNewManaStatus(double newDelta) {
        this.mana.updateIndicatorBarValue(newDelta);

        if (this.mana.getCurrentValue() <= 0) {
            this.mana.updateIndicatorBarValue(1);
        }
        this.renderEntityIndicator();
    }

    public boolean cooldownChecker(double entityRegenCooldown) {
        double currentTime = System.currentTimeMillis();
        if (currentTime - this.lastRegen >= entityRegenCooldown) {

            this.lastRegen = currentTime;
            return true;
        }

        return false;
    }

    public void iPicekdThisItem(InventoryItems i) {
        this.entitiesManager.handlePickItem(i);
    }

    public boolean isInRange(PlayerInventory entityToAskIfClose, double attackingEntityRange) {
        double currentRange = attackingEntityRange;
        double distance = Math.sqrt(Math.pow(this.getXCord() - entityToAskIfClose.getXCord(), 2) + Math.pow(this.getYCord() - entityToAskIfClose.getYCord(), 2));

        if (distance <= currentRange) {
            return true;
        }
        return false;
    }

    public void renderHitImage(PlayerObject entotyToRenderImageAt, Weapons weapon) {
        double renderXCord = entotyToRenderImageAt.getXCord();
        double renderYCord = entotyToRenderImageAt.getYCord();

        String entityAttackAnimationKey = "" + this + weapon;

        if (weapon instanceof CombatType) {
            this.entitiesManager.renderEntityImage(
                    entityAttackAnimationKey,
                    EntityInfo.PUNCH.getPath(),
                    renderXCord + 0,
                    renderYCord + 0,
                    EntityInfo.PUNCH.getImageWidth(),
                    EntityInfo.PUNCH.getImageHeight(),
                    0,
                    false
            );
        }
        if (weapon instanceof MagicType) {
            this.entitiesManager.renderEntityImage(
                    entityAttackAnimationKey,
                    EntityInfo.MAGIC_HIT.getPath(),
                    renderXCord + 0,
                    renderYCord + 30,
                    EntityInfo.PUNCH.getImageWidth(),
                    EntityInfo.PUNCH.getImageHeight(),
                    0,
                    false
            );
        }


        // Help From AI to render the image and delete it but not in the same game tick
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        PlayerObject.this.entitiesManager.removeImage(entityAttackAnimationKey);
                    }
                },
                300
        );
    }

    private void renderEntityIndicator() {
        if (this.hp != null) {
            String hpID = "" + this.hp;
            int[] dimensions = this.hp.getInicatorBarDimensions();
            double hpValue = this.hp.getCurrentValue();


            if (hpValue > 0) {
                this.entitiesManager.renderEntityImage(
                        hpID,
                        this.hp.getImagePath(),
                        this.xCord,
                        this.yCord - 10,
                        dimensions[0],
                        dimensions[1],
                        0,
                        false);
                this.entitiesManager.cropEntityImage(hpID, hpValue);
            }

            if (this.mana != null) {
                String manaID = "" + this.mana;
                dimensions = this.mana.getInicatorBarDimensions();


                double manaValue = this.mana.getCurrentValue();
                if (manaValue > 0) {
                    this.entitiesManager.renderEntityImage(manaID, this.mana.getImagePath(), this.xCord, this.yCord + this.entityImage.getImageDimensions()[1], dimensions[0], dimensions[1], 0, false);
                    this.entitiesManager.cropEntityImage(manaID, manaValue);
                }
            }
        }
    }

    private void renderEntityCurentInventorySlot() {
        if (
                this.currentWeaponImage != null
                && this.currentWeaponImage.getEntityInfo() != null
                && this.currentWeaponImage.getEntityInfo() != EntityInfo.NOT_DROPPABLE) {

            String key = "" + this + this.currentWeaponImage;
            int[] dimensions = this.currentWeaponImage.getImageDimensions();

            this.entitiesManager.renderEntityImage(
                    key,
                    this.currentWeaponImage.getImagePath(),
                    this.xCord + this.entityImage.getImageDimensions()[1] - 40,
                    this.yCord + this.entityImage.getImageDimensions()[1] - 70,
                    dimensions[0] + 20,
                    dimensions[1] + 20,
                    0,
                    false);
        }
    }

    public void removeEntityWeaponeImage() {
        if (this.currentWeaponImage.getEntityInfo() != EntityInfo.NOT_DROPPABLE) {
            String key = "" + this + this.currentWeaponImage;
            this.entitiesManager.removeImage(key);
            this.currentWeaponImage.setEntityInfo(EntityInfo.NOT_DROPPABLE);
        }

    }


    public String getEntityWeaponKey() {
        return  ("" + this + this.currentWeaponImage);
    }
}
