package usable.orbs;

import components.Image;
import enums.EntityInfo;

/**
 * Trieda reprezentuje Healing orb, obnovujúcu hráčovo zdravie
 */
public class HealingOrb extends Orb {
    private final double healing;
    private final Image healingOrbImage;

    /**
     * Inicializuje Healing Orb nastaví hodnotu liečenia a obrázok
     */
    public HealingOrb() {
        super();
        super.setUsed(false);

        this.healing = 40;
        this.healingOrbImage = new Image();
        this.healingOrbImage.setEntityInfo(EntityInfo.HEALING_ORB);

    }

    /**
     * Vráti množstvo healu a označí orbu za použitý
     *
     * @return množstvo obnovenej životnej hodnoty
     */
    @Override
    public double getUseStatus() {
        super.setUsed(true);
        return this.healing;
    }
}
