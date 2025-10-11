package usable.weapons.combat;

import enums.EntityInfo;
import java.util.Random;

/**
 * Combat typ predstavuje oštep s náhodnou pravdepodobnosťou zlyhania útoku
 */
public class Spear extends CombatType {

    private final double weaponCooldown;
    private final Random random;

    /**
     * Inicializuje oštep nastavením poškodenia, cooldownu a obrázku
     */
    public Spear() {
        super(30, 80, EntityInfo.SPEAR);
        this.weaponCooldown = 1000;
        this.random = super.getRandom();
    }

    /**
     * Vráti dosah oštepu
     *
     * @return dosah zbrane
     */
    @Override
    public double getWeaponRange() {
        return super.getRange();
    }

    /**
     * Pokus o útok oštepom ak cooldown uplynul vráti poškodenie alebo 0 ak útok zlyhal
     *
     * @return množstvo poškodenia alebo 0
     */
    @Override
    public double useWeapon() {
        if (super.cooldownChecker(this.weaponCooldown)) {
            if (this.random.nextInt(100) < 40) {
                return 0;
            }
            return super.getDamage();
        }
        return 0;
    }

    /**
     * Vráti spotrebu many pre oštep (vždy 0)
     *
     * @return spotreba many
     */
    @Override
    public double getManaUsage() {
        return 0;
    }
}
