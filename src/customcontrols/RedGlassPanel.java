package customcontrols;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * 🔴 Red Glassmorphism Panel
 * 
 * Used for: Critical alerts, error messages, urgent notifications
 * Example: Low blood stock warning, validation errors, danger actions
 * 
 * Visual: Semi-transparent red frosted glass with glowing border
 */
public class RedGlassPanel extends JPanel {
    private int radius = 25;
    private int alpha = 30;        // Transparency level (0-255)
    private int borderAlpha = 120; // Border transparency
    
    // 🔴 Red color variants (customize as needed)
    private static final Color RED_GLASS = new Color(220, 20, 60);   // Crimson
    private static final Color RED_BORDER = new Color(255, 100, 130); // Soft red glow
    private static final Color RED_HIGHLIGHT = new Color(255, 180, 190); // Light red accent

    public RedGlassPanel() {
        setOpaque(false);
    }
    
    /**
     * Constructor with custom transparency
     * @param alpha Background transparency (0=invisible, 255=solid)
     */
    public RedGlassPanel(int alpha) {
        this();
        this.alpha = Math.max(0, Math.min(255, alpha)); // Clamp value
    }
    
    /**
     * Constructor with custom radius
     * @param radius Corner roundness in pixels
     */
    public RedGlassPanel(int radius, int alpha) {
        this(alpha);
        this.radius = Math.max(0, radius);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 🔴 Glass Background (Semi-transparent Red)
        g2.setColor(new Color(
            RED_GLASS.getRed(), 
            RED_GLASS.getGreen(), 
            RED_GLASS.getBlue(), 
            alpha
        ));
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        // 🔴 Soft Border Glow
        g2.setColor(new Color(
            RED_BORDER.getRed(), 
            RED_BORDER.getGreen(), 
            RED_BORDER.getBlue(), 
            borderAlpha
        ));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        // ✨ Subtle inner highlight (top edge) for depth
        g2.setColor(new Color(255, 255, 255, 20));
        g2.fillRoundRect(2, 2, getWidth() - 5, getHeight() / 4, radius - 2, radius - 2);
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    // 🔧 SETTERS for dynamic styling
    
    public void setRedIntensity(String intensity) {
        switch (intensity.toLowerCase()) {
            case "soft":
                setColors(new Color(220, 20, 60, alpha), new Color(255, 150, 170, borderAlpha));
                break;
            case "bold":
                setColors(new Color(180, 0, 30, alpha), new Color(255, 80, 100, borderAlpha));
                break;
            case "neon":
                setColors(new Color(255, 0, 50, alpha), new Color(255, 100, 200, borderAlpha));
                break;
            default: // crimson (default)
                setColors(RED_GLASS, RED_BORDER);
        }
    }
    
    private void setColors(Color bg, Color border) {
        // Store colors if you want to make them instance variables
        // For now, just a placeholder for future expansion
        repaint();
    }
    
    public void setRadius(int radius) {
        this.radius = Math.max(0, radius);
        repaint();
    }
    
    public void setTransparency(int alpha) {
        this.alpha = Math.max(0, Math.min(255, alpha));
        repaint();
    }
}