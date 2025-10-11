package entities.enemies;

import entities.Enemy;
import entities.PlayerObject;
import enums.AnimationFrames;
import enums.EntityInfo;
import interfaces.EnemyType;
import manager.EntitiesManager;
import usable.weapons.Weapons;
import usable.weapons.combat.Hands;
import usable.weapons.magic.MagicShield;
import usable.weapons.magic.MagicWand;
import java.util.Random;

/**
 * Typ nepriateľa bojuje na diaľku
 */
public class MagicCombatEnemy extends Enemy implements EnemyType {

    private final double regenerationCooldown;
    private boolean inRange;
    private double range;

    /**
     * Vytvorí magického nepriateľa nastaví animáciu život manu rýchlosť a počiatočnú zbraň
     *
     * @param entitiesManager manažér všetkých entít
     * @param xCord počiatočná X súradnica
     * @param yCord počiatočná Y súradnica
     */
    public MagicCombatEnemy(EntitiesManager entitiesManager, double xCord, double yCord) {

        super(entitiesManager, xCord, yCord);
        super.setEntityImage(EntityInfo.MAGIC_COMBAT_ENEMY);
        super.setEntityFrames(AnimationFrames.MAGIC_COMBAT_ENEMY_ANIMATION);
        super.setHp(100);
        super.setMana(100);
        super.setSpeed(2);

        this.regenerationCooldown = 10000;
        this.inRange = false;
        this.range = 250;
        this.getNewWeapon();
    }

    /**
     * Volané každý tick regeneruje život a manu, útočí alebo sa približuje k hráčovi
     */
    @Override
    public void handleGameTick() {
        this.regen();

        // Kontrola pre istotu
        Weapons weapon = super.getWeapon();
        if (weapon == null) {
            return;
        }

        // Ak som v dosahu nebudem sa približovať
        PlayerObject entityToAttack = this.getClosestEntity(this);
        double myRange = this.range + weapon.getWeaponRange();
        if (super.isInRange(entityToAttack, myRange)) {
            this.attackPlayer();
            return;
        }

        super.moveTowardsPlayer();
    }

    /**
     * Útočí na najbližší cieľ ak má dostatok many a zbraň nie je štít
     */
    private void attackPlayer() {

        PlayerObject entityToAttack = this.getClosestEntity(this);
        Weapons weapon = super.getWeapon();
        double myMana = super.getMana();
        double weaponMana = weapon.getManaUsage() / 100;

        if (myMana >= weaponMana) {
            double result = weapon.useWeapon();
            if (result == -1) {
                return;
            } else {
                if (weapon instanceof MagicShield) {
                    return;
                }
                super.setNewManaStatus(weapon.getManaUsage() * -1);
                if (result > 0) {
                    super.renderHitImage(entityToAttack, weapon);
                    super.hitEntity(entityToAttack, result * -1);
                }
            }
        }
    }

    /**
     * Prispôsobí rýchlosť podľa aktuálnej magickej zbrane
     */
    public void benefitFromWeapon() {
        Weapons weapon = super.getWeapon();
        if (weapon instanceof Hands) {
            super.setSpeed(1.5 * super.getSpeed());

        } else if (weapon instanceof MagicWand) {
            super.setSpeed(0.5 * super.getSpeed());

        } else if (weapon instanceof MagicShield) {
            super.setSpeed(0.3 * super.getSpeed());
        }
    }

    /**
     * Náhodne vyberie novú magickú zbraň na základe pravdepodobnosti
     */
    private void getNewWeapon() {
        Random random = new Random();
        int chance = random.nextInt(100);

        if (chance < 1) {
            super.setWeapon(new Hands());

        } else if (chance < 30) {
            super.setWeapon(new MagicShield());

        } else {
            super.setWeapon(new MagicWand());
        }
    }

    /**
     * Regeneruje život a manu ak uplynul cooldown
     */
    private void regen() {
        if (super.cooldownChecker(this.regenerationCooldown)) {
            super.setNewHPStatus(3);
            super.setNewManaStatus(8);
        }
    }
}
