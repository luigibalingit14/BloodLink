package CustomComponents;
        
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class GlassPanel extends JPanel {
    
    private int borderRadius = 15;
    private Color glassColor = new Color(255, 255, 255, 180);
    private Color borderColor = new Color(255, 255, 255, 100);
    private boolean hasShadow = true;
    
    public GlassPanel() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }
    
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }
    
    public void setGlassColor(Color color) {
        this.glassColor = color;
        repaint();
    }
    
    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw shadow
        if (hasShadow) {
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, borderRadius, borderRadius);
        }
        
        // Draw glass background
        g2.setColor(glassColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        
        // Draw border
        g2.setColor(borderColor);
        g2.setStroke(new java.awt.BasicStroke(1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        
        g2.dispose();
    }
}