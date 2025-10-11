
package core;

/**
 * Trieda zabezpečuje časovanie hry a pravidelné vykonávanie herných krokov (tickov)
 */

public class GameTime {

    private final GameEngine gameEngine;
    private volatile int gameTick; // Ai help
    private volatile boolean running; // New flag - Ai help

    /**
     * Inicializuje objekt GameTime s priradeným herným enginom
     *
     * @param gameEngine herný engine, ktorý bude informovaný o každom ticku
     */
    public GameTime(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.gameTick = 0;
        this.running = false; // Initial state - Ai help
    }

    /**
     * Spustí herný čas a začne vykonávať herné ticky v samostatnom vlákne
     */
    public void start() {
        this.running = true; // Set to true when starting - Ai help
        new Thread(this::gameRun).start();
    }

    /**
     * Zastaví vykonávanie herných tickov
     */
    public void stop() {
        this.running = false; // Stop the loop - Ai help
    }

    /**
     * Hlavná loopka herného času, ktorá opakovane spúšťa herné ticky
     * Obsahuje spánok medzi tickmi a resetovanie počítadla pri hodnote 255
     */
    private void gameRun() {
        while (this.running) { // Check the running flag - Ai help
            if (this.gameTick == 255) {
                this.gameTick = 0;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            this.gameEngine.nextGameStep();
            this.gameTick++;
        }
    }
}