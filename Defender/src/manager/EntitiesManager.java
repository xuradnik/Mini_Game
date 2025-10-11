package manager;

import core.GameEngine;
import entities.Enemy;
import entities.Player;
import entities.PlayerObject;
import entities.enemies.CloseCombatEnemy;
import entities.enemies.MagicCombatEnemy;
import enums.EntityInfo;
import enums.PlayerInput;
import usable.DroppedItems;
import usable.InventoryItems;
import usable.orbs.HealingOrb;
import usable.orbs.ManaOrb;
import usable.orbs.Orb;
import usable.weapons.combat.Shield;
import usable.weapons.magic.MagicShield;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

/**
 * Manažér entít spravuje hráča nepriateľov a predmety vo svete hry
 */
public class EntitiesManager {

    private final GameEngine gameEngine;
    private final List<PlayerObject> entities;
    private final List<DroppedItems> dropped;
    private final int enemySpawn;

    /**
     * Inicializuje manažéra entít s odkazom na herný engine
     *
     * @param gameEngine inštancia herného engine
     */
    public EntitiesManager(GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        this.enemySpawn = 1;
        this.entities = new ArrayList<>(0);
        this.dropped = new ArrayList<>(0);

        this.entities.add(new Player(
                this,
                100,
                100));

        this.spawnEnemy(this.enemySpawn - (this.entities.size() - 1));
    }

    /**
     * Spustí spracovanie ticku pre všetky entity a odstráni mŕtve a použité objekty
     */
    public void handleGameTickAll() {
        // Ak v liste sa nachádza dakto okrem hráča
        if (this.entities != null && this.entities.size() >= 1) {
            // Prečistím mapu
            this.removeAllDeadEnemies();
            this.removeAllItems();

            for (PlayerObject o : this.entities) {
                if (o instanceof Player<?>) {
                    // Skontrolujem či hráč žije
                    Player<?> player = (Player<?>)o;
                    if (player.getHp() == 0) {
                        System.out.println("Player died Game Over");
                        this.gameEngine.stopTheGame();
                    }
                }
                // Každému nepriatelovy pošlem správu na spracovanie pre akciu
                if (o instanceof Enemy) {
                    ((Enemy)o).handleGameTick();
                }
            }
        }
    }

    /**
     * Vráti nemodifikovateľný zoznam predmetov na zemi
     *
     * @return zoznam predmetov na zemi alebo null ak prázdny
     */
    public List<DroppedItems> getDropItemList() {
        if (this.dropped != null) {
            return Collections.unmodifiableList(this.dropped);
        }
        return null;
    }

    /**
     * Spracuje zdvihnutú položku z herného sveta vymazaním z mapy a zoznamu
     *
     * @param item položka, ktorá bola zdvihnutá
     */
    public void handlePickItem(InventoryItems item) {
        if (this.dropped != null && item instanceof DroppedItems) {
            DroppedItems droppedItem = (DroppedItems)item;
            int removeIndex = -1;
            // Prejdem celým listom a ak som našiel item v liste tak mu pošlem správu na odstránenie
            // a vymažem ho zo zoznamu
            for (int i = 0; i < this.dropped.size(); i++) {
                if (this.dropped.get(i) == droppedItem) {
                    droppedItem.removeDropped();
                    removeIndex = i;
                }
            }
            if (removeIndex >= 0) {
                this.dropped.remove(removeIndex);
            }
        }
    }

    /**
     * Nájde najbližšiu entitu k entite, ktorá sa to pýta
     *
     * @param callingEntity entita, ktorá hľadá cieľ
     * @return najbližšia entita alebo null
     */
    public PlayerObject getClosestEntity(PlayerObject callingEntity) {
        // Ak som hráč tak pokračujem
        if (callingEntity instanceof Player<?>)  {
            Enemy closest = null;
            Player player = null;
            double relativePosition = Integer.MAX_VALUE;

            // Ak je aspoň jeden nepriatel v liste tak pokračujem inak vrátim null
            // Hneď na prvej pozícii v liste je hráč [0]
            if (this.entities.size() <= 1) {
                return null;
            } else {
                for (PlayerObject o : this.entities) {
                    Enemy enemy = null;

                    // Sám seba nehladám tak sa preskočím
                    if (o instanceof Player<?>) {
                        player = (Player)o;
                        continue;
                    } else if ( o instanceof Enemy) {
                        enemy = (Enemy)o;
                    }

                    // Vzdialenost = ((X_2 - X_1)^2) + ((Y_2 - Y_1)^2)
                    // X_1 = Hráč
                    // X_2 = Nepriateľ
                    double relativeCalculatedPosition =
                            Math.sqrt(
                                    Math.pow(
                                            (enemy.getXCord() - player.getXCord()), 2)
                                            +
                                            Math.pow(
                                                    (enemy.getYCord() - player.getYCord()), 2)
                            );

                    // Kontorla či nepriateľa čo som našiel je bližšie ako ten ktorého som si uložil naposledy
                    if (relativeCalculatedPosition <= relativePosition) {
                        relativePosition = relativeCalculatedPosition;
                        closest = enemy;
                    }
                }
            }

            return closest;
        } else {
            // Ak som nepriatel čo sa pýta tak vrátim hráča
            if (this.entities != null && this.entities.size() >= 1) {
                Player player = (Player)this.entities.get(0);

                return player;
            }
        }
        return null;
    }



    /**
     * Aplikuje poškodenie na zadanú entitu
     *
     * @param entityToDamage cieľ poškodenia
     * @param damage množstvo poškodenia
     */
    public void delaDamageToEntity(PlayerObject entityToDamage, double damage) {
        Player player = null;
        if (this.entities != null  && this.entities.size() >= 1) {
            player = (Player)this.entities.get(0);
        } else {
            return;
        }


        if (entityToDamage instanceof Player<?>) {

            // Ak idem zraniť hráča
            player.setNewHPStatus(damage);
        } else if (entityToDamage instanceof Enemy) {

            // Ak idem zraniť nepriateľa
            for (PlayerObject o : this.entities) {
                if (o instanceof Enemy) {


                    Enemy enemy = (Enemy)o;
                    if (enemy == entityToDamage) {

                        // Ak som našiel nepriateľa ktorého chcem zraniť v liste
                        if (enemy.getWeapon() instanceof Shield) {
                            enemy.setNewHPStatus(damage * 0.2); // Zredukujem DMG o 80% keď má Shield
                            break;

                        } else if (enemy.getWeapon() instanceof MagicShield) {

                            // Ak ide o Magic_Shield tak skontrolujem či má nepriateľ dosť many na obranu
                            // a ak áno tak mu zoberem manu a dám mu 10% DMG, inak mu zoberem HP podla DMG
                            double shieldManaConsuption = enemy.getWeapon().getManaUsage() / 100; // Konvertuje mi z celého čísla na percentá
                            double enemyMana = enemy.getMana();

                            if (enemyMana >= shieldManaConsuption) {

                                // Konvertujem manu na celéčisla a pridelím jej negatívnu hodnotu
                                enemy.setNewManaStatus((shieldManaConsuption * 100) * -1);
                                enemy.setNewHPStatus(damage * 0.1);

                            } else {
                                enemy.setNewHPStatus(damage);
                            }
                            break;
                        }
                        // Obyčajný nepriateľ dostane DMG
                        enemy.setNewHPStatus(damage);
                        break;
                    }
                }
            }

        }

        // Render kaźdú entitu aby bolo vidno ubudok HP ak sa entita nehybe
        if (this.entities != null) {
            for (PlayerObject o : this.entities) {
                o.renderEntityImage(0, false);
            }
        }
    }

    /**
     * Spracuje vstup hráča a odovzdá ho hráčovej entite
     *
     * @param playerInput vstup hráča
     */
    public void newPlayerInput(PlayerInput playerInput) {
        if (this.entities != null && this.entities.size() >= 1) {
            Player<?> player = (Player<?>)this.entities.get(0);
            player.handlePlayerInput(playerInput);
        }
    }

    /**
     * Vykreslí obrázok entity cez herný engine
     *
     * @param entityID kľúč entity
     * @param entityImagePath cesta k obrázku
     * @param entityXCord X súradnica
     * @param entityYCord Y súradnica
     * @param width šírka obrázku
     * @param height výška obrázku
     * @param rotation uhol rotácie
     * @param flip či obrázok má byť zrkadlený
     */
    public void renderEntityImage(
            String entityID,
            String entityImagePath,
            double entityXCord,
            double entityYCord,
            int width,
            int height,
            int rotation,
            boolean flip) {

        this.gameEngine.renderEntityImage(
                entityID,
                entityImagePath,
                entityXCord,
                entityYCord,
                width,
                height,
                rotation,
                flip
        );
    }

    /**
     * Orezáva obrazok
     *
     * @param entityID kľúč entity
     * @param cropDelta veľkosť orezu
     */
    public void cropEntityImage(String entityID, double cropDelta) {
        // Orezáva sa HP a Mana Bar
        this.gameEngine.cropEntityImage(entityID, cropDelta);
    }

    /**
     * Odstráni obrázok na základe klúču pre RenderEngine (HashMap)
     *
     * @param key kľúč entity
     */
    public void removeImage(String key) {
        this.gameEngine.removeEntityImage(key);
        this.gameEngine.updateGameScene();
    }

    /**
     * Pridá nový drop do listu pre dropy
     *
     * @param newDrop inštancia DropedItems
     */
    public void addDroppedItem(DroppedItems newDrop) {
        this.dropped.add(newDrop);
    }

    /**
     * Vytvorí zadaný počet nepriateľov na náhodných pozíciách
     *
     * @param howMany počet nepriateľov na vytvorenie
     */
    private void spawnEnemy(int howMany) {
        for (int i = 0; i < howMany; i++) {
            // Náhodne buď vytvorim CloseCombatEnemy alebo MagicCombatEnemy
            Random random = new Random();
            PlayerObject enemy;

            switch (random.nextInt(2)) {
                case 0 -> {
                    enemy = new CloseCombatEnemy(
                            this,
                            random.nextInt(800),
                            random.nextInt(800));

                    ((CloseCombatEnemy)enemy).benefitFromWeapon();
                }
                default -> {
                    enemy = new MagicCombatEnemy(
                            this,
                            random.nextInt(800),
                            random.nextInt(800));

                    ((MagicCombatEnemy)enemy).benefitFromWeapon();
                }
            }
            this.entities.add(enemy);
        }
    }

    /**
     * Odstráni všetkých mŕtvych nepriateľov a vyhodí z nich predmety
     */
    private void removeAllDeadEnemies() {
        if (this.entities != null && this.entities.size() >= 1) {

            // Pôjdem cez každý prvok
            Iterator<PlayerObject> iterator = this.entities.iterator();
            while (iterator.hasNext()) {
                PlayerObject o = iterator.next();
                if (o instanceof Enemy) {
                    Enemy enemy = (Enemy)o;
                    // Ak má nepriateľ <= 0 HP
                    if (enemy.getHp() <= 0) {
                        // Získam všetky klúče pre obrázky na vymazanie
                        String entityKey = "" + enemy;
                        String hpKey = enemy.getHPKey();
                        String manaKey = enemy.getManaKey();
                        if (this.dropped != null) {
                            // Ak nepriateľ drží zbraň ktorú môže zhodiť
                            if (enemy.getWeapon().getWeaponInfo() != EntityInfo.NOT_DROPPABLE
                                    && enemy.getWeapon().getWeaponInfo() != EntityInfo.MAGIC_SHIELD
                                    && enemy.getWeapon().getWeaponInfo() != EntityInfo.SHIELD) {
                                // Vytvorí drop na súradniciach jeho vlastných
                                DroppedItems drop = new DroppedItems(
                                        enemy.getEntitiesManager(),
                                        enemy.getXCord(),
                                        enemy.getYCord(),
                                        enemy.getWeapon().getWeaponInfo(),
                                        enemy.getWeapon());
                                // Pridám drop do listu
                                this.addDroppedItem(drop);
                            }
                            // 50% šanca na drop HP orbu a Magic Orbu
                            Random r = new Random();
                            if (r.nextInt(100) < 50) {
                                this.addDroppedItem(new DroppedItems(
                                        enemy.getEntitiesManager(),
                                        enemy.getXCord() + 30,
                                        enemy.getYCord() + 30,
                                        EntityInfo.HEALING_ORB,
                                        new HealingOrb()));
                            } else {
                                this.addDroppedItem(new DroppedItems(
                                        enemy.getEntitiesManager(),
                                        enemy.getXCord() + 30,
                                        enemy.getYCord() + 30,
                                        EntityInfo.MANA_ORB,
                                        new ManaOrb()));
                            }
                        }
                        // Odstránim obrázky z hernej plochy
                        this.gameEngine.removeEntityImage(entityKey);
                        this.gameEngine.removeEntityImage(hpKey);
                        this.gameEngine.removeEntityImage(manaKey);
                        this.gameEngine.removeEntityImage(enemy.getEntityWeaponKey());
                        // Odstránim entitu z listu
                        iterator.remove();
                    }
                }
            }
        }
        // Spawnem novú entitu aby ich bolo vždy podla enemySpawn čo je konštanta
        this.spawnEnemy(this.enemySpawn - (this.entities.size() - 1));
    }

    /**
     * Odstráni použité orby z sveta
     */
    private void removeAllItems() {
        if (this.dropped != null) {
            for (int i = 0; i < this.dropped.size(); i++) {
                DroppedItems d = this.dropped.get(i);
                // Ak v liste nájdem drop, ktorý už je použitý tak ho odstránim
                if (d.getInventoryItem() instanceof Orb orb && orb.getUsed()) {
                    d.removeDropped();
                    this.dropped.remove(i);
                    break;
                }
            }
        }
    }
}
