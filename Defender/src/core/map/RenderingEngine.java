package core.map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Klása vytvorená pomocou AI (ChatGPT) a mňa
 * Slúži na vykreslovanie grafiky pre hru
 */
public class RenderingEngine {

    private final JFrame window;
    private final CanvasPanel canvas;
    private BufferedImage buffer;
    private Graphics2D graphics;
    private final Color backgroundColor;
    private BufferedImage backgroundImage;
    private volatile boolean redrawPending = false;

    private final Map<Object, ShapeDetails> drawables ;



    private double scale;
    private double translateX;
    private double translateY;

    /**
     * Inicializuje renderovací engine s oknom, plátnom a buffrom
     */
    public RenderingEngine(String title, int width, int height, Color bgColor) {
        this.backgroundColor = bgColor;
        this.drawables = new ConcurrentHashMap<>();

        this.window = new JFrame(title);
        this.canvas = new CanvasPanel();
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.add(this.canvas);
        this.window.setSize(width, height);
        this.window.setLocationRelativeTo(null);

        this.initBuffer(width, height);

        this.window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                RenderingEngine.this.initBuffer(RenderingEngine.this.canvas.getWidth(), RenderingEngine.this.canvas.getHeight());
                RenderingEngine.this.redraw();
            }
        });

        this.window.setVisible(true);

        this.scale = 1.0;
        this.translateX = 0.0;
        this.translateY = 0.0;

        this.window.setFocusable(true);
        this.window.requestFocus();
    }

    /**
     * Pripojí ovládanie používateľa k oknu
     */
    public void setUserInput(core.userinput.UserInput userInput) {
        this.window.addKeyListener(userInput.getKeyboardAdapter());

    }
    /**
     * Odpojí ovládanie používateľa z okna
     */
    public void killUserInput(core.userinput.UserInput userInput) {
        this.window.removeKeyListener(userInput.getKeyboardAdapter());

    }

    /**
     * Pridá tvar na vykreslenie
     */
    public void addShape(Object key, Shape shape, Color color, BasicStroke stroke, boolean fill) {
        ShapeDetails details = new ShapeDetails(shape, color, stroke, fill);
        this.drawables.put(key, details);
    }

    /**
     * Pridá obrázok s danou pozíciou
     */
    public void addImage(Object key, String filePath, double x, double y) {
        if (this.drawables.containsKey(key)) {
            ShapeDetails existing = this.drawables.get(key);
            this.updateImage(key, x, y, existing.drawWidth, existing.drawHeight, existing.rotation, existing.reflection);
            return;
        }
        BufferedImage img = this.loadImage(filePath);
        if (img != null) {
            ShapeDetails details = new ShapeDetails(img, filePath, x, y);
            this.drawables.put(key, details);
            this.redraw();
        }
    }

    /**
     * Pridá alebo aktualizuje obrázok s pozíciou, veľkosťou, rotáciou a odrazom
     */
    public void addImage(Object key, String filePath, double x, double y, double width, double height, double rotation, boolean reflection) {
        ShapeDetails existing = this.drawables.get(key);
        if (existing != null) {
            // Check if image path has changed
            if (!existing.imagePath.equals(filePath)) {
                BufferedImage newImg = this.loadImage(filePath);
                if (newImg != null) {
                    existing.image = newImg;
                    existing.imagePath = filePath;
                }
            }
            // Update parameters
            existing.x = x;
            existing.y = y;
            existing.drawWidth = width;
            existing.drawHeight = height;
            existing.rotation = rotation;
            existing.reflection = reflection;
            this.redraw();
            return;
        }

        // New entry
        BufferedImage img = this.loadImage(filePath);
        if (img != null) {
            ShapeDetails details = new ShapeDetails(img, filePath, x, y, width, height, rotation, reflection);
            this.drawables.put(key, details);
            this.redraw();
        }
    }

    /**
     * Aktualizuje parametre existujúceho obrázku
     */
    public void updateImage(Object key, double x, double y, double width, double height, double rotation, boolean reflection) {
        ShapeDetails details = this.drawables.get(key);
        if (details != null && details.image != null) {
            details.x = x;
            details.y = y;
            details.drawWidth = width;
            details.drawHeight = height;
            details.rotation = rotation;
            details.reflection = reflection;
            this.redraw();
        }
    }

    /**
     * Nastaví faktor orezania obrázku
     */
    public void updateImageCrop(Object key, double cropFactor) {
        ShapeDetails details = this.drawables.get(key);
        if (details != null && details.image != null) {
            details.setCropFactor(cropFactor);
            this.redraw();
        }
    }

    /**
     * Nastaví pozadie z obrázku
     */
    public void setBackgroundImage(String filePath) {
        BufferedImage img = this.loadImage(filePath);
        if (img != null) {
            this.backgroundImage = img;
            this.redraw();
        }
    }


    /**
     * Odstráni tvar alebo obrázok podľa kľúča
     */
    public void removeShape(Object key) {
        this.drawables.remove(key);
        this.redraw();
    }

    /**
     * Vymaže všetky tvary a obrázky
     */
    public void clearShapes() {
        this.drawables.clear();
    }


    /**
     * Inicializuje buffer s danou veľkosťou
     */
    private void initBuffer(int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.graphics = this.buffer.createGraphics();
        this.graphics.setColor(this.backgroundColor);
        this.graphics.fillRect(0, 0, width, height);
    }

    /**
     * Načíta obrázok zo súboru
     */
    private BufferedImage loadImage(String filePath) {
        try {
            return ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println("Error loading image [" + filePath + "]: " + e.getMessage());
            return null;
        }
    }

    /**
     * Vyrenderuje celý obsah na buffer a spustí prekreslenie plátna
     */
    public void redraw() {
        if (!this.redrawPending) {
            this.redrawPending = true;
            javax.swing.SwingUtilities.invokeLater(() -> {
                this.graphics.setTransform(new AffineTransform());
                this.graphics.setColor(this.backgroundColor);
                this.graphics.fillRect(0, 0, this.buffer.getWidth(), this.buffer.getHeight());

                if (this.backgroundImage != null) {
                    this.graphics.drawImage(this.backgroundImage, 0, 0, this.buffer.getWidth(), this.buffer.getHeight(), null);
                }

                int cx = this.canvas.getWidth() / 2;
                int cy = this.canvas.getHeight() / 2;
                AffineTransform sceneTransform = new AffineTransform();
                sceneTransform.translate(cx + this.translateX, cy + this.translateY);
                sceneTransform.scale(this.scale, this.scale);
                sceneTransform.translate(-cx, -cy);
                this.graphics.setTransform(sceneTransform);

                // Create a copy of values to avoid ConcurrentModificationException
                List<ShapeDetails> drawablesCopy = new ArrayList<>(this.drawables.values());
                for (ShapeDetails details : drawablesCopy) {
                    if (details.image != null) {
                        AffineTransform old = this.graphics.getTransform();

                        int croppedWidth = (int)(details.image.getWidth() * details.cropFactor);
                        BufferedImage croppedImage = details.image.getSubimage(0, 0, croppedWidth, details.image.getHeight());

                        AffineTransform at = new AffineTransform();
                        at.translate(details.x + details.drawWidth / 2.0, details.y + details.drawHeight / 2.0);
                        at.rotate(details.rotation);
                        if (details.reflection) {
                            at.scale(-1, 1);
                        }
                        double scaleX = details.drawWidth / (double)details.image.getWidth();
                        double scaleY = details.drawHeight / (double)details.image.getHeight();
                        at.scale(scaleX, scaleY);
                        at.translate(-details.image.getWidth() / 2.0, -details.image.getHeight() / 2.0);

                        this.graphics.drawImage(croppedImage, at, null);
                        this.graphics.setTransform(old);
                    } else {
                        this.graphics.setColor(details.color);
                        if (details.fill) {
                            this.graphics.fill(details.shape);
                        } else {
                            this.graphics.setStroke(details.stroke);
                            this.graphics.draw(details.shape);
                        }
                    }
                }

                this.graphics.setTransform(new AffineTransform());
                this.canvas.repaint();
                this.redrawPending = false;
            });
        }
    }

    private class CanvasPanel extends JPanel {
        /**
         * Vytvára nové plátno s nastavením
         */
        CanvasPanel() {
            setOpaque(false);
            setDoubleBuffered(false);
            setFocusable(true);
            requestFocusInWindow();
        }

        /**
         * Vykreslí buffer na plátno
         */
        @Override
        protected void paintComponent(Graphics g) {
            if (RenderingEngine.this.buffer != null) {
                g.drawImage(RenderingEngine.this.buffer, 0, 0, null);
            }
        }
    }

    public static class ShapeDetails {
        // Shape properties
        private Shape shape;
        private Color color;
        private BasicStroke stroke;
        private boolean fill;

        // Image properties
        private BufferedImage image;
        private String imagePath;
        private double x;
        private double y;
        private double drawWidth;
        private double drawHeight;
        private double rotation;
        private boolean reflection;
        private double cropFactor = 1.0;

        /**
         * Vytvára detail pre vykreslenie tvaru
         */
        public ShapeDetails(Shape shape, Color color, BasicStroke stroke, boolean fill) {
            this.shape = shape;
            this.color = color;
            this.stroke = stroke;
            this.fill = fill;
        }

        /**
         * Vytvára detail pre vykreslenie obrázku (bez veľkosti a rotácie)
         */
        public ShapeDetails(BufferedImage image, String imagePath, double x, double y) {
            this(image, imagePath, x, y, image.getWidth(), image.getHeight(), 0.0, false);
        }

        /**
         * Vytvára detail pre vykreslenie obrázku s veľkosťou, rotáciou a odrazom
         */
        public ShapeDetails(BufferedImage image, String imagePath, double x, double y, double drawWidth, double drawHeight, double rotation, boolean reflection) {
            this.image = image;
            this.imagePath = imagePath;
            this.x = x;
            this.y = y;
            this.drawWidth = drawWidth;
            this.drawHeight = drawHeight;
            this.rotation = rotation;
            this.reflection = reflection;
        }

        /**
         * Nastaví orezávací faktor pre obrázok
         */
        public void setCropFactor(double cropFactor) {
            this.cropFactor = Math.max(0.0, Math.min(1.0, cropFactor));
        }
    }
}