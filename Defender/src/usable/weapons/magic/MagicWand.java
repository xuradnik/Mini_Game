package usable.weapons.magic;

import enums.EntityInfo;
import java.util.Random;

/**
 * Magic typ predstavuje magickú paličku vrhajúcu kúzla so spotrebou many a cooldownom
 */
public class MagicWand extends MagicType {

    private final Random random;
    private final double weaponCooldown;
    private final double manaConsumption;

    /**
     * Inicializuje magickú paličku nastaví poškodenie cooldown spotrebu many a náhodný generátor
     */
    public MagicWand() {
        super(20, 50, EntityInfo.MAGIC_WAND);

        this.weaponCooldown = 1000;
        this.manaConsumption = 40;
        this.random = super.getRandom();
    }

    /**
     * Vráti dosah magickej paličky
     *
     * @return dosah zbrane
     */
    @Override
    public double getWeaponRange() {
        return super.getRange();
    }

    /**
     * Použije magickú paličku ak cooldown uplynul vráti poškodenie alebo -1 ak cooldown neuplynul
     *
     * @return množstvo poškodenia alebo -1 pri nedostupnom útoku
     */
    @Override
    public double useWeapon() {
        if (super.cooldownChecker(this.weaponCooldown)) {
            if (this.random.nextInt(100) < 20) {
                return 0;
            }
            return super.getDamage();
        }
        return -1;
    }

    /**
     * Vráti spotrebu many pre magickú paličku
     *
     * @return množstvo many potrebné na útok
     */
    @Override
    public double getManaUsage() {
        return this.manaConsumption;
    }
}
