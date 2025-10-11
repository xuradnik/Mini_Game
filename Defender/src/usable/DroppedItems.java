package usable;

import components.Image;
import enums.EntityInfo;
import interfaces.PlayerInventory;
import manager.EntitiesManager;

/**
 * Trieda reprezentuje znehodnotené predmety zanechané v svete hry
 */
public class DroppedItems extends InventoryItems implements PlayerInventory {

    private final Image image;
    private final double xCord;
    private final double yCord;
    private final double radius;
    private final EntitiesManager entitiesManager;
    private final InventoryItems inventoryItem;

    /**
     * Inicializuje spadnutého predmetu, nastaví pozíciu obrázok a pridá ho na obrazovku
     *
     * @param entitiesManager manažér entít na vykresľovanie predmetu
     * @param xCord X súradnica predmetu
     * @param yCord Y súradnica predmetu
     * @param imageInfo informácie o obrázku predmetu
     * @param inventoryItem uložená položka inventára
     */
    public DroppedItems(
            EntitiesManager entitiesManager,
            double xCord,
            double yCord,
            EntityInfo imageInfo,
            InventoryItems inventoryItem)  {

        this.entitiesManager = entitiesManager;
        this.xCord = xCord;
        this.yCord = yCord;
        this.image = new Image();
        this.image.setEntityInfo(imageInfo);
        this.inventoryItem = inventoryItem;
        this.radius = 20;

        if (this.entitiesManager != null) {
            this.entitiesManager.renderEntityImage(
                    "" + this,
                    this.image.getImagePath(),
                    xCord,
                    yCord,
                    imageInfo.getImageWidth(),
                    imageInfo.getImageHeight(),
                    0,
                    false
            );
        }
    }

    /**
     * Odstráni predmet z hernej scény
     */
    public void removeDropped() {
        this.entitiesManager.removeImage("" + this);
    }

    /**
     * Vráti X súradnicu predmetu
     *
     * @return X súradnica
     */
    @Override
    public double getXCord() {
        return this.xCord;
    }

    /**
     * Vráti Y súradnicu predmetu
     *
     * @return Y súradnica
     */
    @Override
    public double getYCord() {
        return this.yCord;
    }

    /**
     * Vráti dosah, v akom predmet viem zdvihnúť
     *
     * @return polomer v pixeloch
     */
    public double getRange() {
        return this.radius;
    }

    /**
     * Vráti položku ktorú drop reprezentuje zbraň/orb
     *
     * @return položka inventára
     */
    public InventoryItems getInventoryItem() {
        return this.inventoryItem;
    }
}
