package entities;

import interfaces.EnemyType;
import manager.EntitiesManager;
import usable.weapons.Weapons;

/**
 * Základná trieda nepriateľa poskytuje pohyb správu zbrane a životné zdroje
 */
public abstract class Enemy extends PlayerObject implements EnemyType {

    private Weapons weapon;

    /**
     * Inicializuje nepriateľa s odkazom na manažéra entít a počiatočnými súradnicami
     *
     * @param entitiesManager manažér všetkých entít
     * @param xCord počiatočná X súradnica
     * @param yCord počiatočná Y súradnica
     */
    public Enemy(EntitiesManager entitiesManager, double xCord, double yCord) {
        super(entitiesManager, xCord, yCord);
    }

    /**
     * Pohne entitou smerom k hráčovi
     */
    public void moveTowardsPlayer() {
        PlayerObject player = super.getClosestEntity(this);

        if (player == null) {
            return;
        }

        double deltaX = player.getXCord() - super.getXCord();
        double deltaY = player.getYCord() - super.getYCord();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance == 0) {
            return;
        }

        double newX = super.getXCord() + (deltaX / distance) * super.getSpeed();
        double newY = super.getYCord() + (deltaY / distance) * super.getSpeed();

        boolean flipImage = false;

        super.getEntityImage().nextFrame();

        // Ak sa pohybujem dolava tak otočím obrázok
        if (newX < super.getXCord()) {
            flipImage = true;
        }

        super.setXYCord(newX, newY);
        super.renderEntityImage(0, flipImage);
    }

    /**
     * Nastaví obrázok novej zbrane nepriateľa
     *
     * @param newWeapon zbraň na nastavenie
     */
    public void setWeapon(Weapons newWeapon) {
        if (this.weapon == null) {
            this.weapon = newWeapon;
            super.setEntityWeaponImage(this.weapon.getWeaponInfo());
        }
    }

    /**
     * Getter pre aktuálnu zbraň čo nepriateľ má
     *
     * @return aktuálna zbraň
     */
    public Weapons getWeapon() {
        return this.weapon;
    }
}
