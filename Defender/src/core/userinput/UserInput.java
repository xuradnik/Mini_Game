package core.userinput;


/**
 * Trieda zabezpečuje spracovanie vstupov z klávesnice pre ovládanie hráča.
 */

import core.GameEngine;
import enums.PlayerInput;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UserInput  {


    private final KeyAdapter keyboardAdapter;
    private final GameEngine gameEngine;

    /**
     * Vytvára nový objekt pre spracovanie používateľských vstupov.
     *
     * @param gameEngine inštancia herného enginu, do ktorého sa posielajú vstupy hráča
     */
    public UserInput(GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        this.keyboardAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                UserInput.this.handleKeyPress(e);
            }
        };

    }

    /**
     * Spracuje stlačenie klávesy a odošle príslušný vstup do herného enginu.
     *
     * @param e udalosť stlačenia klávesy
     */
    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                this.gameEngine.newPlayerInput(PlayerInput.MOVE_UP);
            }
            case KeyEvent.VK_S -> {
                this.gameEngine.newPlayerInput(PlayerInput.MOVE_DOWN);
            }
            case KeyEvent.VK_A -> {
                this.gameEngine.newPlayerInput(PlayerInput.MOVE_LEFT);
            }
            case KeyEvent.VK_D -> {
                this.gameEngine.newPlayerInput(PlayerInput.MOVE_RIGHT);
            }
            case KeyEvent.VK_SPACE -> {
                this.gameEngine.newPlayerInput(PlayerInput.ATTACK);
            }
            case KeyEvent.VK_E -> {
                this.gameEngine.newPlayerInput(PlayerInput.CHANGE_INVENTORY_RIGHT);
            }
            case KeyEvent.VK_Q -> {
                this.gameEngine.newPlayerInput(PlayerInput.CHANGE_INVENTORY_LEFT);
            }
            case KeyEvent.VK_R -> {
                this.gameEngine.newPlayerInput(PlayerInput.PICK_ITEM);
            }
            case KeyEvent.VK_X -> {
                this.gameEngine.newPlayerInput(PlayerInput.DROP_ITEM);
            }


            default -> {
                return;
            }
        }
    }

    /**
     * Vráti KeyAdapter pre registráciu na spracovanie vstupov z klávesnice.
     *
     * @return objekt typu KeyAdapter
     */
    public KeyAdapter getKeyboardAdapter() {
        return this.keyboardAdapter;
    }
}
