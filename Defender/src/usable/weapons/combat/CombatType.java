package usable.weapons.combat;

import enums.EntityInfo;
import usable.weapons.Weapons;

/**
 * Abstraktná základná trieda pre všetky typy zbraní určených na boj
 */
public abstract class CombatType extends Weapons {

    /**
     * Vytvorí novú bojovú zbraň
     *
     * @param damage základné poškodenie, ktoré táto zbraň spôsobí pri zásahu
     * @param range efektívny dosah
     * @param weaponInfo EntityInfo o zbrani
     */
    public CombatType(double damage, double range, EntityInfo weaponInfo) {
        super(damage, range, weaponInfo);
    }


}
