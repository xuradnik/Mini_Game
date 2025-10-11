package usable.weapons.magic;

import enums.EntityInfo;

/**
 * Magic typ predstavuje magický štít blokujúci útoky so spotrebou many
 */
public class MagicShield extends MagicType {

    private final double manaConsumption;
    /**
     * Inicializuje magický štít s nulovým poškodením a prednastavenou spotrebou many
     */
    public MagicShield() {
        super(0, 0, EntityInfo.MAGIC_SHIELD);
        this.manaConsumption = 15;
    }

    /**
     * Vráti dosah magického štítu (vždy 0)
     *
     * @return dosah zbrane
     */
    @Override
    public double getWeaponRange() {
        return 0;
    }

    /**
     * Použitie magického štítu nevytvára poškodenie (vždy 0)
     *
     * @return poškodenie
     */
    @Override
    public double useWeapon() {
        return 0;
    }

    /**
     * Vráti spotrebu many pre magický štít
     *
     * @return množstvo many potrebné na aktiváciu štítu
     */
    @Override
    public double getManaUsage() {
        return this.manaConsumption;
    }
}
