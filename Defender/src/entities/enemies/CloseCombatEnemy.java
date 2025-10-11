package entities.enemies;

import entities.Enemy;
import entities.Player;
import entities.PlayerObject;
import enums.AnimationFrames;
import enums.EntityInfo;
import interfaces.EnemyType;
import manager.EntitiesManager;
import usable.weapons.Weapons;
import usable.weapons.combat.Shield;
import usable.weapons.combat.Hands;
import usable.weapons.combat.Spear;
import usable.weapons.combat.Sword;
import usable.weapons.combat.Pistol;
import java.util.Random;

/**
 * Typ nepriateľa bojuja na blízko
 */
public class CloseCombatEnemy extends Enemy implements EnemyType {

    private final double regenerationCooldown;
    private boolean inRange;
    private double range;

    /**
     * Vytvorí blízkeho nepriateľa nastaví animáciu život rýchlosť a počiatočnú zbraň
     *
     * @param entitiesManager manažér všetkých entít
     * @param xCord počiatočná X súradnica
     * @param yCord počiatočná Y súradnica
     */
    public CloseCombatEnemy(EntitiesManager entitiesManager, double xCord, double yCord) {
        super(entitiesManager, xCord, yCord);
        super.setEntityImage(EntityInfo.CLOSE_COMBAT_ENEMY);
        super.setEntityFrames(AnimationFrames.CLOSE_COMBAT_ENEMY_ANIMATION);
        super.setHp(100);
        super.setSpeed(1);
        this.regenerationCooldown = 1000;
        this.inRange = false;
        this.range = 50;
        this.getNewWeapon();
    }

    /**
     * Volané každý tick regeneruje život útočí a pohybuje sa k hráčovi
     */
    @Override
    public void handleGameTick() {
        this.regen();

        // Kontrola pre istotu
        Weapons weapon = super.getWeapon();
        if (weapon == null) {
            return;
        }

        this.attackPlayer();
        super.moveTowardsPlayer();
    }

    /**
     * Útočí na najbližší cieľ ak je v dosahu a zbraň nie je štít
     */
    private void attackPlayer() {
        PlayerObject entityToAttack = this.getClosestEntity(this);
        Weapons weapon = super.getWeapon();

        if (entityToAttack instanceof Player<?>) {
            double myRange = this.range + weapon.getWeaponRange();

            if (super.isInRange(entityToAttack, myRange)) {
                if (weapon instanceof Shield) {
                    return;
                }
                double damage = weapon.useWeapon();
                if (damage > 0) {
                    super.hitEntity(entityToAttack, damage * -1);
                    super.renderHitImage(entityToAttack, weapon);
                }
            }
        }
    }

    /**
     * Prispôsobí rýchlosť podľa aktuálnej zbrane
     */
    public void benefitFromWeapon() {
        Weapons weapon = super.getWeapon();
        if (weapon instanceof Hands) {
            super.setSpeed(1.5 * super.getSpeed());

        } else if (weapon instanceof Sword) {
            super.setSpeed(0.5 * super.getSpeed());

        } else if (weapon instanceof Pistol) {
            super.setSpeed(0.5 * super.getSpeed());

        } else if (weapon instanceof Spear) {
            super.setSpeed(1.2 * super.getSpeed());

        } else if (weapon instanceof Shield) {
            super.setSpeed(0.1 * super.getSpeed());
        }
    }

    /**
     * Náhodne vyberie novú zbraň na základe pravdepodobnosti
     */
    private void getNewWeapon() {
        Random random = new Random();
        int chance = random.nextInt(100);
        if (chance < 8) {
            super.setWeapon(new Pistol());

        } else if (chance < 15) {
            super.setWeapon(new Shield());

        } else if (chance < 35) {
            super.setWeapon(new Spear());

        } else if (chance < 60) {
            super.setWeapon(new Sword());

        } else {
            super.setWeapon(new Hands());
        }
    }

    /**
     * Regeneruje život ak uplynul cooldown
     */
    private void regen() {
        if (super.cooldownChecker(this.regenerationCooldown)) {
            super.setNewHPStatus(4);
        }
    }
}
