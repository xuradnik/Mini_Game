package entities;

import enums.AnimationFrames;
import enums.EntityInfo;
import enums.PlayerInput;
import manager.EntitiesManager;
import usable.DroppedItems;
import usable.InventoryItems;

import usable.orbs.HealingOrb;
import usable.orbs.ManaOrb;
import usable.orbs.Orb;
import usable.weapons.Weapons;
import usable.weapons.combat.Pistol;
import usable.weapons.magic.MagicType;

import java.util.List;

/**
 * Trieda reprezentuje hráča spravuje vstup inventár boj a interakciu s položkami
 *
 * @param <E> typ položiek v inventári
 */
public class Player<E extends InventoryItems> extends PlayerObject {

    private final components.Inventory playerInventory;
    private double range;

    /**
     * Inicializuje hráča nastaví obraz animáciu život manu rýchlosť a počiatočnú zbraň
     *
     * @param entitiesManager manažér entít
     * @param xCord počiatočná X súradnica
     * @param yCord počiatočná Y súradnica
     */
    public Player(EntitiesManager entitiesManager, double xCord, double yCord) {
        super(entitiesManager, xCord, yCord);
        super.setEntityImage(EntityInfo.PLAYER);
        super.setEntityFrames(AnimationFrames.PLAYER_ANIMATION);
        super.setHp(100);
        super.setMana(100);
        super.setSpeed(8.5);

        this.playerInventory = new components.Inventory();
        this.playerInventory.addToInventory(new Pistol()); // Nastavenie Hráčovej zbrane
        if (this.playerInventory.getCurrentSlot() instanceof Weapons) {
            Weapons currentSlot = (Weapons)this.playerInventory.getCurrentSlot();
            super.setEntityWeaponImage(currentSlot.getWeaponInfo());
        }


        this.range = 300;
        super.renderEntityImage(0, false);
    }

    /**
     * Spracuje vstup hráča pre pohyb zmena inventára útok zber a odhodenie položky
     *
     * @param playerDirection smer alebo akcia hráča
     */
    @SuppressWarnings("checkstyle:NoWhitespaceAfter")
    public void handlePlayerInput(PlayerInput playerDirection) {
        double newPlayerXCord = this.getXCord();
        double newPlayerYCord = this.getYCord();
        double playerSpeed = this.getSpeed();

        switch (playerDirection) {
            case MOVE_UP -> {
                newPlayerYCord -= playerSpeed;
            }
            case MOVE_DOWN -> {
                newPlayerYCord += playerSpeed;
            }
            case MOVE_RIGHT -> {
                newPlayerXCord += playerSpeed;
            }
            case MOVE_LEFT -> {
                newPlayerXCord -= playerSpeed;
            }
            case CHANGE_INVENTORY_RIGHT -> {
                this.playerInventory.changePlayerInventory(true);
                this.updateEntityWeaponeImage();

                return;
            }
            case CHANGE_INVENTORY_LEFT -> {
                this.playerInventory.changePlayerInventory(false);
                this.updateEntityWeaponeImage();

                return;
            }
            case ATTACK -> {
                this.attack();

                return;
            }
            case PICK_ITEM -> {

                // Ak mám čo zdvihnúť tak pokračujem
                DroppedItems pickedItem = this.pickItem();
                if (pickedItem != null) {
                    if (pickedItem.getInventoryItem() instanceof Weapons) {

                        // Ak mám v inventári miesto tak pokračujem
                        if (this.playerInventory.canAdd()) {

                            // Pridám zbraň do inventára
                            E weapon = (E)pickedItem.getInventoryItem();
                            this.addToInventory(weapon);

                            // Zmenim ikonku zbrane vedla hráča na zdvihnutú zbraň
                            Weapons w = (Weapons)pickedItem.getInventoryItem();
                            super.setEntityWeaponImage(w.getWeaponInfo());

                            // Odstráni zdvihnutú vec zo zeme
                            super.iPicekdThisItem(pickedItem);
                        }
                    } else if (pickedItem.getInventoryItem() instanceof Orb) {

                        // Podla orbu orb použijem
                        Orb useOrb = (Orb)pickedItem.getInventoryItem();
                        if (useOrb instanceof HealingOrb) {
                            super.setNewHPStatus(useOrb.getUseStatus());
                        }
                        if (useOrb instanceof ManaOrb) {
                            super.setNewManaStatus(useOrb.getUseStatus());
                        }
                        // Odstráni zdvihnutú vec zo zeme
                        super.iPicekdThisItem(pickedItem);
                    }

                }

                return;
            }
            case DROP_ITEM -> {
                // Iba zbraň viem vyhodiť z inventáru
                if (this.playerInventory.getCurrentSlot() instanceof Weapons) {
                    Weapons weapon = (Weapons)this.playerInventory.getCurrentSlot();
                    // Vytvorím inštanciu dropu na zobrazenie na mape
                    DroppedItems newDrop =
                            new DroppedItems(
                                    this.getEntitiesManager(),
                                    super.getXCord(),
                                    super.getYCord(),
                                    weapon.getWeaponInfo(),
                                    weapon
                            );
                    // Pridám nový drop do listu v EntityManageri
                    this.getEntitiesManager().addDroppedItem(newDrop);
                }
                // Vyhodím zbraň a vymažem obrázok vedľa hráča
                this.playerInventory.dropItem();
                super.removeEntityWeaponeImage();

                return;
            }
            default -> {
                return;
            }
        }

        // Ak som sa pohol updatnem pozíciu hráča a ak idem do ľava tak aj otočím obrázok
        this.changePlayerCoordinates(
                newPlayerXCord,
                newPlayerYCord,
                playerDirection == PlayerInput.MOVE_LEFT
        );
    }

    /**
     * Odstránim aktualny obrazok zbrane a zmením ho na základe aktuálneho slotu v inventáry
     */
    private void updateEntityWeaponeImage() {
        if (this.playerInventory.getCurrentSlot() instanceof Weapons) {
            Weapons weapon = (Weapons)this.playerInventory.getCurrentSlot();
            super.removeEntityWeaponeImage();
            super.setEntityWeaponImage(weapon.getWeaponInfo());
        }
        super.renderEntityImage(0, false);
    }

    /**
     * Vráti prvú položku v Liste, ktorá je v dosahu
     *
     * @return droppable položku alebo null ak nie je v dosahu
     */
    private DroppedItems pickItem() {
        List<DroppedItems> dropedItemsList = super.getDropItemList();
        for (DroppedItems d : dropedItemsList) {
            if (super.isInRange(d, this.range + d.getRange())) {
                return d;
            }
        }
        return null;
    }

    /**
     * Pridá položku do inventára
     *
     * @param newItem položka na pridanie
     */
    private void addToInventory(E newItem) {
        this.playerInventory.addToInventory(newItem);
    }

    /**
     * Vykoná útok na najbližšieho nepriateľa ak je v dosahu a sú prostriedky
     */
    private void attack() {

        // Pokračujem ak najbližšia entita je nepriateľ
        PlayerObject entity = super.getClosestEntity(this);
        if (entity instanceof Enemy) {

            // Pokračujem ak mám zbraň
            var currentInventoryItem = this.playerInventory.getCurrentSlot();
            if (currentInventoryItem == null) {
                return;
            }
            if (currentInventoryItem instanceof Weapons) {

                // Ak som v dosahu tak použijem svoju zbraň
                Weapons currentWeapon = (Weapons)currentInventoryItem;
                double myRange = this.range + currentWeapon.getWeaponRange();
                if (super.isInRange(entity, myRange)) {
                    double weaponDamage = currentWeapon.useWeapon();

                    // Ak zbraň  je magická tak tak odoberem manu
                    if (currentWeapon instanceof MagicType) {
                        if (this.getMana() * 100 >= currentWeapon.getManaUsage()) {
                            super.setNewManaStatus(currentWeapon.getManaUsage() * -1);
                        } else {
                            return;
                        }
                    }
                    if (weaponDamage > 0) {
                        // Ak zbraň je schopná dať DMG tak odobere HP a ukáže hit
                        super.hitEntity(entity, weaponDamage * -1);
                        if (weaponDamage > 0) {
                            super.renderHitImage(entity, currentWeapon);
                        }
                    }
                }
            }
        }
    }

    /**
     * Zmení súradnice hráča nastaví smer animácie a prekreslí obrázok
     *
     * @param deltaX nová X súradnica
     * @param deltaY nová Y súradnica
     * @param xDirection či sa pohybovalo v osi X zľava doprava
     */
    private void changePlayerCoordinates(double deltaX, double deltaY, boolean xDirection) {
        super.setXYCord(deltaX, deltaY);
        super.getEntityImage().nextFrame();
        super.renderEntityImage(0, xDirection);
    }
}
