package usable.weapons;

import enums.EntityInfo;
import usable.InventoryItems;
import java.util.Random;

/**
 * Abstraktná trieda pre zbrane poskytuje poškodenie dosah cooldown a informácie o obrázku
 */
public abstract class Weapons extends InventoryItems {

    private final double damage;
    private final double range;
    private final Random random;
    private final EntityInfo weaponInfo;
    private double lastUse;


    /**
     * Inicializuje zbraň s poškodením dosahom a informáciou o obrázku
     *
     * @param damage množstvo spôsobeného poškodenia
     * @param range dosah zbrane
     * @param weaponInfo enum s cestou a rozmermi obrázku zbrane
     */
    public Weapons(double damage, double range, EntityInfo weaponInfo) {
        this.damage = damage;
        this.range = range;
        this.weaponInfo = weaponInfo;

        this.random = new Random();
        this.lastUse = 0;
    }

    /**
     * Vráti dosah zbrane
     *
     * @return dosah v pixeloch
     */
    public abstract double getWeaponRange();

    /**
     * Použije zbraň vráti poškodenie alebo špeciálnu hodnotu pri nemožnosti útoku
     *
     * @return množstvo spôsobeného poškodenia alebo špeciálna hodnota
     */
    public abstract double useWeapon();

    /**
     * Vráti spotrebu many pre danú zbraň
     *
     * @return množstvo many potrebné na útok
     */
    public abstract double getManaUsage();

    /**
     * Vráti generátor náhodných čísel pre zbraň
     *
     * @return inštancia Random
     */
    public Random getRandom() {
        return this.random;
    }

    /**
     * Skontroluje cooldown zbrane a aktualizuje čas posledného použitia
     *
     * @param weaponCooldown požadovaný interval cooldownu v milisekundách
     * @return true ak cooldown uplynul false inak
     */
    // AI help, Me + ChatGPT
    public boolean cooldownChecker(double weaponCooldown) {
        double currentTime = System.currentTimeMillis();
        if (currentTime - this.lastUse >= weaponCooldown) {
            this.lastUse = currentTime;
            return true;
        }
        return false;
    }

    /**
     * Vráti základný dosah zbrane
     *
     * @return rozsah útoku
     */
    public double getRange() {
        return this.range;
    }

    /**
     * Vráti základné poškodenie zbrane
     *
     * @return hodnota poškodenia
     */
    public double getDamage() {
        return this.damage;
    }

    /**
     * Vráti informáciu o obrázku zbrane
     *
     * @return enum s cestou a rozmermi obrázku
     */
    public EntityInfo getWeaponInfo() {
        return this.weaponInfo;
    }
}
