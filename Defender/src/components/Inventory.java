package components;

import usable.InventoryItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Trieda reprezentuje hráčov inventár s obmedzenou kapacitou.
 * Umožňuje pridávať, prepínať a vyhadzovať položky.
 */
public class Inventory<E extends InventoryItems> {

    private final int inventoryCap;
    private int inventorySlot;

    private final List<E> inventory;

    /**
     * Vytvára nový inventár s predvolenou kapacitou troch položiek.
     */
    public Inventory() {
        this.inventoryCap = 3;
        this.inventorySlot = 0;
        this.inventory = new ArrayList<>(0);
    }

    /**
     * Prepne aktívny slot v inventári doľava alebo doprava podľa smeru.
     *
     * @param direction ak je true, prepne doprava; inak doľava
     */
    public void changePlayerInventory(boolean direction) {
        if (direction) {
            int newSlot = (this.inventorySlot + 1) % this.inventoryCap;
            if (newSlot >= this.inventory.size() || this.inventory.get(newSlot) == null) {
                return;
            }
            this.inventorySlot = newSlot;
        } else {
            int newSlot = (this.inventorySlot - 1 + this.inventoryCap) % this.inventoryCap;
            if (newSlot >= this.inventory.size() || this.inventory.get(newSlot) == null) {
                return;
            }
            this.inventorySlot = newSlot;
        }
    }

    /**
     * Vráti aktuálne vybranú položku v inventári.
     *
     * @return aktuálna položka alebo null, ak nie je dostupná
     */
    public InventoryItems getCurrentSlot() {
        if (this.inventory.size() <= this.inventorySlot) {
            return null;
        }
        return this.inventory.get(this.inventorySlot);
    }


    /**
     * Pridá novú položku do inventára, ak ešte nie je plný.
     *
     * @param newItem položka na pridanie
     */
    public void addToInventory(E newItem) {
        if (this.canAdd()) {
            this.inventory.add(newItem);
            return;
        }
        System.out.println("The inventory is full");
    }

    /**
     * Odstráni aktuálne vybranú položku z inventára.
     */
    public void dropItem() {
        if (this.inventorySlot < this.inventory.size()) {
            this.inventory.remove(this.inventorySlot);
        }
    }

    public boolean canAdd() {
        if (this.inventory.size() + 1 <= this.inventoryCap) {
            return true;
        }
        return false;
    }
}
