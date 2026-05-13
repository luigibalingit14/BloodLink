/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomComponents;




import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {

    private int topLeftRadius = 20;
    private int topRightRadius = 20;
    private int bottomLeftRadius = 20;
    private int bottomRightRadius = 20;

    // BORDER
    private float borderWidth = 0f;
    private Color borderColor = Color.BLACK;

    // ===== SHADOW SETTINGS =====
    private boolean shadowEnabled = false;
    private int shadowSize = 7;
    private int shadowArc = 30;
    private Color shadowColor = new Color(0, 0, 0, 80); // Keep for compatibility

    public RoundedPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // ================= SHADOW =================
        if (shadowEnabled) {
            drawShadow(g2, w, h);
        }

        // ================= BACKGROUND =================
        Shape outer = createRoundedShape(0, 0, w, h);
        g2.setColor(getBackground());
        g2.fill(outer);

        // ================= BORDER =================
        if (borderWidth > 0f) {
            Shape inner = createRoundedShape(
                    borderWidth,
                    borderWidth,
                    w - 2 * borderWidth,
                    h - 2 * borderWidth
            );

            Area borderArea = new Area(outer);
            borderArea.subtract(new Area(inner));

            g2.setColor(borderColor);
            g2.fill(borderArea);
        }

        g2.dispose();
    }

    // ===== SHADOW DRAWING with color support =====
    private void drawShadow(Graphics2D g2, int w, int h) {
        for (int i = shadowSize; i > 0; i--) {
            float alpha = (float) (0.03 * i);
            float safeAlpha = Math.min(0.5f, Math.max(0.0f, alpha));
            
            // Use shadow color but with our alpha calculation
            g2.setColor(new Color(
                    shadowColor.getRed(),
                    shadowColor.getGreen(),
                    shadowColor.getBlue(),
                    (int) (shadowColor.getAlpha() * safeAlpha)
            ));

            g2.fillRoundRect(
                    i,
                    i,
                    w - (i * 2),
                    h - (i * 2),
                    shadowArc,
                    shadowArc
            );
        }
    }

    // ===== SHAPE =====
    private Shape createRoundedShape(float x, float y, float w, float h) {
        Path2D path = new Path2D.Double();

        path.moveTo(x + topLeftRadius, y);
        path.lineTo(x + w - topRightRadius, y);
        path.quadTo(x + w, y, x + w, y + topRightRadius);
        path.lineTo(x + w, y + h - bottomRightRadius);
        path.quadTo(x + w, y + h, x + w - bottomRightRadius, y + h);
        path.lineTo(x + bottomLeftRadius, y + h);
        path.quadTo(x, y + h, x, y + h - bottomLeftRadius);
        path.lineTo(x, y + topLeftRadius);
        path.quadTo(x, y, x + topLeftRadius, y);
        path.closePath();
        return path;
    }

    // ================= RADIUS =================
    public void setTopLeftRadius(int r) { topLeftRadius = r; repaint(); }
    public void setTopRightRadius(int r) { topRightRadius = r; repaint(); }
    public void setBottomLeftRadius(int r) { bottomLeftRadius = r; repaint(); }
    public void setBottomRightRadius(int r) { bottomRightRadius = r; repaint(); }

    // ================= BORDER =================
    public void setBorderWidth(float width) { borderWidth = width; repaint(); }
    public void setBorderWidth(int width) { borderWidth = width; repaint(); }
    public void setBorderColor(Color c) { borderColor = c; repaint(); }

    // ================= SHADOW API =================
    public void setShadowEnabled(boolean enabled) {
        this.shadowEnabled = enabled;
        repaint();
    }
    
    public void setShadowSize(int size) {
        this.shadowSize = size;
        repaint();
    }
    
    public void setShadowArc(int arc) {
        this.shadowArc = arc;
        repaint();
    }
    
    // Keep for NetBeans compatibility
    public void setShadowColor(Color color) {
        this.shadowColor = color;
        repaint();
    }
    
    public void setShadowOffset(int x, int y) {
        // Shadow offset not used in this style, but keep for compatibility
        // You can ignore this or implement if needed
    }

    // ================= GETTERS =================
    public boolean isShadowEnabled() { return shadowEnabled; }
    public float getBorderWidth() { return borderWidth; }
    public Color getBorderColor() { return borderColor; }
    public int getShadowSize() { return shadowSize; }
    public int getShadowArc() { return shadowArc; }
    public Color getShadowColor() { return shadowColor; }
}
