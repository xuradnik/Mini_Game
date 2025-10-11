package main;

/**
 * Hlavná trieda aplikácie spúšťa herný engine
 */
public class Main {

    /**
     * Vstupná metóda aplikácie vytvorí inštanciu Main a spustí ju
     *
     * @param args argumenty príkazového riadka
     */
    public static void main(String[] args) {
        Main instance = new Main();
        instance.run();
    }

    /**
     * Inicializuje a spustí herný engine s preddefinovanými parametrami
     */
    private void run() {
        new core.GameEngine(
                "Defender",
                800,
                800,
                java.awt.Color.BLACK
        );
    }
}
