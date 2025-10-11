package usable.weapons.combat;

import enums.EntityInfo;
import java.util.Random;

/**
 * Combat typ predstavuje strelnú zbraň pištoľ s náhodným zlyhaním zásahu
 */
public class Pistol extends CombatType {

    private final double weaponCooldown;
    private final Random random;

    /**
     * Inicializuje pištoľ nastavením poškodenia, cooldownu a obrázku
     */
    public Pistol() {
        super(30, 500, EntityInfo.PISTOL);
        this.weaponCooldown = 00;
        this.random = super.getRandom();
    }

    /**
     * Vráti dosah pištole
     *
     * @return dosah zbrane
     */
    @Override
    public double getWeaponRange() {
        return super.getRange();
    }

    /**
     * Použije pištoľ ak cooldown uplynul vráti poškodenie alebo 0 ak výstrel zlyhal
     *
     * @return množstvo poškodenia alebo 0
     */
    @Override
    public double useWeapon() {
        if (super.cooldownChecker(this.weaponCooldown)) {
            if (this.random.nextInt(100) < 60) {
                return 0;
            }
            return super.getDamage();
        }
        return 0;
    }

    /**
     * Vráti spotrebu many pre pištoľ (0 pretože strelné zbrane manu nevyžadujú)
     *
     * @return 0
     */
    @Override
    public double getManaUsage() {
        return 0;
    }
}
