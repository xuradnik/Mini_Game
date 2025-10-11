package core;

import java.awt.Color;
import core.map.RenderingEngine;
import core.userinput.UserInput;
import enums.PlayerInput;
import manager.EntitiesManager;


/**
 * Trieda zabezpečuje hlavnú logiku hry a koordinuje interakcie medzi hernými komponentmi.
 */
public class GameEngine {

    private final RenderingEngine renderingEngine;
    private final GameTime gameTime;
    private  UserInput userInput;
    private  EntitiesManager entityManager;

    /**
     * Inicializuje herný engine vrátane grafiky, vstupu, entít a času
     *
     * @param gameSceneName názov scény
     * @param width šírka herného okna
     * @param height výška herného okna
     * @param color farba pozadia
     */
    public GameEngine(String gameSceneName, int width, int height, Color color) {
        this.userInput = new UserInput(this);
        this.renderingEngine = new RenderingEngine(gameSceneName, width, height, color);
        this.renderingEngine.setUserInput(this.userInput);
        this.entityManager = new EntitiesManager(this);
        this.gameTime = new GameTime(this);
        this.gameTime.start();

        this.renderingEngine.setBackgroundImage("images/background.png");
    }

    /**
     * Zastaví hru a uvoľní objekty
     */
    public void stopTheGame() {
        this.gameTime.stop();
        this.entityManager = null;
        this.renderingEngine.killUserInput(this.userInput);
        this.userInput = null;
    }

    /**
     * Vykoná ďalší krok logiky hry tick
     */
    public void nextGameStep() {
        this.entityManager.handleGameTickAll();
    }

    /**
     * Spracuje nový vstup hráča
     *
     * @param playerInput vstup hráča
     */
    public void newPlayerInput(PlayerInput playerInput) {
        this.entityManager.newPlayerInput(playerInput);
    }

    /**
     * Vykreslí obrázok entity na obrazovku.
     *
     * @param entityID identifikátor entity
     * @param entityImagePath cesta k obrázku entity
     * @param entityXCord X-ová súradnica
     * @param entityYCord Y-ová súradnica
     * @param width šírka obrázku
     * @param height výška obrázku
     * @param rotation rotácia obrázku
     * @param flip či sa má obrázok zrkadlovo otočiť
     */
    public void renderEntityImage(
            String entityID,
            String entityImagePath,
            double entityXCord,
            double entityYCord,
            int width,
            int height,
            int rotation,
            boolean flip ) {
        this.renderingEngine.addImage(entityID, entityImagePath, entityXCord, entityYCord, width, height, rotation, flip);
    }

    /**
     * Odstráni obrázok entity z obrazovky.
     *
     * @param entityID identifikátor entity
     */
    public void removeEntityImage(String entityID) {
        this.renderingEngine.removeShape(entityID);
    }

    /**
     * Orezáva obrázok entity o danú hodnotu.
     *
     * @param entityID identifikátor entity
     * @param cropDelta hodnota orezu
     */
    public void cropEntityImage(String entityID, double cropDelta) {
        this.renderingEngine.updateImageCrop(entityID, cropDelta);
    }

    /**
     * Aktualizuje hernú scénu prekreslením všetkých prvkov.
     */
    public void updateGameScene() {
        this.renderingEngine.redraw();
    }
}
