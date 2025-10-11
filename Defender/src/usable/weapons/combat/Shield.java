package usable.weapons.combat;

import enums.EntityInfo;

/**
 * Combat typ predstavuje štít blokujúci útoky
 */
public class Shield extends CombatType {

    /**
     * Inicializuje štít s nulovým poškodením a cooldownom
     */
    public Shield() {
        super(0, 0, EntityInfo.SHIELD);
    }

    /**
     * Vráti dosah štítu (vždy 0)
     *
     * @return dosah zbrane
     */
    @Override
    public double getWeaponRange() {
        return 0;
    }

    /**
     * Použitie štítu nevytvára poškodenie (vždy 0)
     *
     * @return poškodenie
     */
    @Override
    public double useWeapon() {
        return 0;
    }

    /**
     * Vráti spotrebu many pre štít (vždy 0)
     *
     * @return spotreba many
     */
    @Override
    public double getManaUsage() {
        return 0;
    }
}
