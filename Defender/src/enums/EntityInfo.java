package enums;

/**
 * Obsahuje informácie o entitách vrátane cesty k obrázku a rozmerov
 */
public enum EntityInfo {
    PLAYER(
            "images/Player/Player_Idle.png",
            100,
            100),
    CLOSE_COMBAT_ENEMY(
            "images/Enemy/CloseCombatEnemy/CloseCombatEnemy_idle.png",
            100,
            100),
    MAGIC_COMBAT_ENEMY(
            "images/Enemy/MagicCombatEnemy/MagicCombatEnemy_idle.png",
            100,
            100),
    HEALTH_BAR(
            "images/HealthBar.png",
            100,
            10),
    MANA_BAR(
            "images/ManaBar.png",
            100,
            10),
    PUNCH(
            "images/DamageComponents/punch_circle.png",
            20,
            20),
    MAGIC_HIT(
            "images/DamageComponents/magic_circle.png",
            20,
            20),
    MAGIC_WAND(
            "images/Guns/magic_wand.png",
            30,
            30),
    NOT_DROPPABLE(
            "",
            0,
            0),
    SWORD(
            "images/Guns/sword.png",
            30,
            30),
    SPEAR(
            "images/Guns/spear.png",
            30,
            30),
    PISTOL(
            "images/Guns/pistol.png",
            30,
            30),
    MAGIC_SHIELD(
            "images/Guns/magic_shield.png",
            30,
            30),
    SHIELD(
            "images/Guns/shield.png",
            30,
            30),
    HEALING_ORB(
            "images/Orbs/healing_orb.png",
            60,
            60),
    MANA_ORB(
            "images/Orbs/mana_orb.png",
            60,
            60);

    private final String path;
    private final int width;
    private final int height;

    /**
     * Inicializuje informácie o entite s cestou k obrázku a rozmermi
     *
     * @param path cesta k obrázku entity
     * @param width šírka obrázku
     * @param height výška obrázku
     */
    EntityInfo(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }

    /**
     * Vráti cestu k obrázku entity
     *
     * @return cesta k obrázku
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Vráti šírku obrázku entity
     *
     * @return šírka v pixeloch
     */
    public int getImageWidth() {
        return this.width;
    }

    /**
     * Vráti výšku obrázku entity
     *
     * @return výška v pixeloch
     */
    public int getImageHeight() {
        return this.height;
    }
}
