package usable.orbs;

import components.Image;
import enums.EntityInfo;

/**
 * Trieda reprezentuje ManaOrb obnovujúcu hráčovu manu
 */
public class ManaOrb extends Orb {

    private final double mana;
    private final Image magicOrbImage;

    /**
     * Inicializuje ManaOrbu nastaví hodnotu many a obrázok
     */
    public ManaOrb() {
        this.mana = 40;
        this.magicOrbImage = new Image();
        this.magicOrbImage.setEntityInfo(EntityInfo.MANA_ORB);
        super.setUsed(false);
    }

    /**
     * Vráti množstvo obnovenej many a označí orbu za použitú
     *
     * @return množstvo obnovenej many
     */
    @Override
    public double getUseStatus() {
        super.setUsed(true);
        return this.mana;
    }
}
