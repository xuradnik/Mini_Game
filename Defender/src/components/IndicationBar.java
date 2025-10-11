package components;

import entities.PlayerObject;
import enums.EntityInfo;

/**
 * Trieda reprezentuje HP a Manu
 */
public class IndicationBar {

    private final PlayerObject playerObject;

    private Image indicationBarImage;

    private double maxValue;
    private double currentValue;
    private final EntityInfo entityInfo;

    /**
     * Vytvára indikátor pre hráča
     *
     * @param playerObject objekt hráča, ku ktorému indikátor patrí
     * @param maxValue maximálna hodnota indikátora
     * @param entityInfo informácia o grafike obrázka
     */
    public IndicationBar (
            PlayerObject playerObject,
            double maxValue,
            EntityInfo entityInfo ) {

        this.playerObject = playerObject;

        this.entityInfo = entityInfo;
        this.indicationBarImage = new Image();
        this.indicationBarImage.setEntityInfo(entityInfo);

        this.maxValue = maxValue;
        this.currentValue = maxValue;

        this.indicationBarImage.setEntityInfo(entityInfo);
        this.playerObject.renderEntityImage(0, false);
    }

    /**
     * Aktualizuje hodnotu indikátora
     *
     * @param delta zmena hodnoty indikátora
     */
    public void updateIndicatorBarValue(double delta) {
        if (this.currentValue <= 0 && delta < 0) {
            return;
        }
        this.currentValue += delta;

        if (this.currentValue > this.maxValue) {
            this.currentValue = this.maxValue;
        }

        if (this.currentValue < 0) {
            this.currentValue = 0;
        }
    }

    /**
     * Vráti rozmery obrázka vo forme poľa int[šírka, výška]
     *
     * @return pole s rozmermi obrázka
     */
    public int[] getInicatorBarDimensions() {
        int[] indicationBarDimensions = new int[] {this.entityInfo.getImageWidth(), this.entityInfo.getImageHeight()};

        return indicationBarDimensions;
    }

    /**
     * Vráti aktuálnu hodnotu indikátora, prepočítanú na percentuálnu hodnotu
     *
     * @return hodnota indikátora ako desatinné číslo
     */
    public double getCurrentValue() {
        return this.currentValue / 100;
    }

    /**
     * Vráti cestu k obrázku indikátorovej lišty
     *
     * @return cesta k obrázku
     */
    public String getImagePath() {
        return this.indicationBarImage.getImagePath();
    }
}
