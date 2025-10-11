package usable.orbs;

import usable.InventoryItems;

/**
 * Abstraktná trieda reprezentuje orbu poskytujúcu obnovu zdrojov
 */
public abstract class Orb extends InventoryItems {

    private boolean used;

    /**
     * Vráti hodnotu obnovy (napr. HP alebo many) a označí orbu za použitú
     *
     * @return množstvo obnovenej hodnoty
     */
    public abstract double getUseStatus();



    /**
     * Skontroluje, či bol Orb použitý
     *
     * @return true ak bola použitá inak false
     */
    public boolean getUsed() {
        return this.used;
    }

    /**
     * Nastaví stav orby na použitý alebo nepoužitý
     *
     * @param status true pre použitú orbu false pre nepoužitú
     */
    public void setUsed(boolean status) {
        this.used = status;
    }
}
