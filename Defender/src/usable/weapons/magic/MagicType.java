package usable.weapons.magic;

import enums.EntityInfo;
import usable.weapons.Weapons;

/**
 * Abstraktná trieda pre magické zbrane poskytuje základy poškodenia dosah a informácie o obrázku
 */
public abstract class MagicType extends Weapons {

    /**
     * Inicializuje magický typ zbrane s poškodením dosahom a informáciou o obrázku
     *
     * @param damage množstvo spôsobeného poškodenia
     * @param range dosah zbrane
     * @param weaponInfo enum obsahujúci cestu a rozmery obrázku zbrane
     */
    public MagicType(
            double damage,
            double range,
            EntityInfo weaponInfo) {
        super(damage, range, weaponInfo);
    }
}
