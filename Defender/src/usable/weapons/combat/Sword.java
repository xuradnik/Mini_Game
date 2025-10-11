package usable.weapons.combat;

import enums.EntityInfo;
import java.util.Random;

/**
 * Combat typ predstavuje meč s náhodnou pravdepodobnosťou zlyhania útoku
 */
public class Sword extends CombatType {

    private final double weaponCooldown;
    private final Random random;

    /**
     * Inicializuje meč nastavením poškodenia, cooldownu a obrázku
     */
    public Sword() {
        super(30, 10, EntityInfo.SWORD);
        this.weaponCooldown = 800;
        this.random = super.getRandom();
    }

    /**
     * Vráti dosah útoku mečom
     *
     * @return dosah zbrane
     */
    @Override
    public double getWeaponRange() {
        return super.getRange();
    }

    /**
     * Pokus o útok mečom ak cooldown uplynul vráti poškodenie alebo 0 ak útok zlyhal
     *
     * @return množstvo poškodenia alebo 0
     */
    @Override
    public double useWeapon() {
        if (super.cooldownChecker(this.weaponCooldown)) {
            if (this.random.nextInt(100) < 15) {
                return 0;
            }
            return super.getDamage();
        }
        return 0;
    }

    /**
     * Vráti spotrebu many pre meč (vždy 0)
     *
     * @return spotreba many
     */
    @Override
    public double getManaUsage() {
        return 0;
    }
}
