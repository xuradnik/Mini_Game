package usable.weapons.combat;

import enums.EntityInfo;
import java.util.Random;

/**
 * Combat typ predstavuje útok holými rukami bez spotreby many
 */
public class Hands extends CombatType {

    private final Random random;
    private final double weaponCooldown;

    /**
     * Inicializuje ruky nastaví poškodenie rozsah obrázok a cooldown
     */
    public Hands() {
        super(3, 5, EntityInfo.NOT_DROPPABLE);
        this.weaponCooldown = 500;
        this.random = super.getRandom();
    }

    /**
     * Vráti dosah útoku holými rukami
     *
     * @return dosah zbrane
     */
    @Override
    public double getWeaponRange() {
        return super.getRange();
    }

    /**
     * Použije zbraň ak cooldown uplynul vráti poškodenie inak 0
     *
     * @return množstvo poškodenia alebo 0 ak cooldown neuplynul alebo útok zlyhal
     */
    @Override
    public double useWeapon() {
        if (super.cooldownChecker(this.weaponCooldown)) {
            if (this.random.nextInt(100) < 20) {
                return 0;
            }
            return super.getDamage();
        }
        return 0;
    }

    /**
     * Vráti spotrebu many pre útok holými rukami
     *
     * @return 0 vždy, pretože útok rúk nevyžaduje manu
     */
    @Override
    public double getManaUsage() {
        return 0;
    }
}
