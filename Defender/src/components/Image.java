package components;

import enums.AnimationFrames;
import enums.EntityInfo;

/**
 * Trieda reprezentuje obrázky herných objektov vrátane animácie a rozmerov
 */
public class Image {


    private int rotation;
    private boolean reflection;
    private EntityInfo entityInfo;
    private AnimationFrames animationFrames;
    private int frameCounter;

    /**
     * Vytvára nový objekt typu Image
     */
    public Image() {
        this.rotation = 0;
        this.reflection = false;
        this.entityInfo = null;
        this.animationFrames = null;
        this.frameCounter = 0;
    }


    /**
     * Nastaví informácie o entite obrázku
     *
     * @param entityInfo objekt s informáciami o entite
     */
    public void setEntityInfo(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    /**
     * Nastaví animáciu entity, ak ešte nebola nastavená
     *
     * @param animationFrames objekt s animáciou entity
     */
    public void setEntityAnimationFrames(AnimationFrames animationFrames) {
        if (this.animationFrames == null) {
            this.animationFrames = animationFrames;
        }
    }

    /**
     * Vráti cestu k základnému obrázku entity
     *
     * @return cesta k obrázku ako reťazec
     */
    public String getImagePath() {
        return this.entityInfo.getPath();
    }

    /**
     * Vráti rozmery obrázku entity vo forme poľa int[šírka, výška]
     *
     * @return pole s rozmermi obrázku
     */
    public int[] getImageDimensions() {
        int[] dimensions = new int[2];
        dimensions[0] = this.entityInfo.getImageWidth();
        dimensions[1] = this.entityInfo.getImageHeight();

        return dimensions;
    }

    /**
     * Vráti informácie o entite
     *
     * @return informácie o entite
     */
    public EntityInfo getEntityInfo() {
        return this.entityInfo;
    }

    /**
     * Vráti cestu k aktuálnemu framu animácie, alebo základný obrázok ak animácia nie je nastavená
     *
     * @return cesta k obrázku aktuálneho rámu
     */
    public String getCurrentFramePath() {
        if (this.animationFrames == null) {
            return this.entityInfo.getPath();
        }
        return this.animationFrames.getAnimationFrames()[this.frameCounter];
    }


    /**
     * Posunie animáciu na ďalší rám.
     *
     * Nevracia žiadnu hodnotu.
     */
    public void nextFrame() {
        if (this.animationFrames != null) {
            this.frameCounter = (this.frameCounter + 1) % 3;
        }
    }
}
