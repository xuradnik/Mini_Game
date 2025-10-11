package enums;

/**
 * Obsahuje cesty k obrázkom pre animácie hráča a nepriateľov
 */
public enum AnimationFrames {
    PLAYER_ANIMATION(
            "images/Player/Player_Idle.png",
            "images/Player/Player_walking_f1.png",
            "images/Player/Player_walking_f2.png"),
    CLOSE_COMBAT_ENEMY_ANIMATION(
            "images/Enemy/CloseCombatEnemy/CloseCombatEnemy_idle.png",
            "images/Enemy/CloseCombatEnemy/CloseCombatEnemy_f1.png",
            "images/Enemy/CloseCombatEnemy/CloseCombatEnemy_f2.png"),
    MAGIC_COMBAT_ENEMY_ANIMATION(
            "images/Enemy/MagicCombatEnemy/MagicCombatEnemy_idle.png",
            "images/Enemy/MagicCombatEnemy/MagicCombatEnemy_f1.png",
            "images/Enemy/MagicCombatEnemy/MagicCombatEnemy_f2.png");

    private final String idle;
    private final String f1;
    private final String f2;

    /**
     * Inicializuje rámce animácie s tromi snímkami
     *
     * @param idle cesta k snímke v kľudovej animácii
     * @param f1 cesta k prvej snímke animácie
     * @param f2 cesta k druhej snímke animácie
     */
    AnimationFrames(String idle, String f1, String f2) {
        this.idle = idle;
        this.f1 = f1;
        this.f2 = f2;
    }

    /**
     * Vráti pole ciest k snímkam animácie
     *
     * @return pole reťazcov s cestami k obrázkom snímok
     */
    public String[] getAnimationFrames() {
        String[] toReturn = new String[]{this.idle, this.f1, this.f2};
        return toReturn;
    }
}
