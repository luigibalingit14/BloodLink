package CustomComponents; // O customcontrols depende sa package mo

import java.awt.Color;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class SidebarButton extends JButton {

    private boolean isActive = false;
    private Icon originalIcon;
    
    // Colors
    private final Color ACTIVE_BG = new Color(230, 57, 70, 200);  // Red with HIGH opacity
    private final Color ACTIVE_FG = Color.WHITE;
    private final Color INACTIVE_FG = new Color(60, 60, 60);

    public SidebarButton() {
        super("");
        init();
    }

    public SidebarButton(String text) {
        super(text);
        init();
    }

    private void init() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(INACTIVE_FG);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(SwingConstants.LEFT);
        setHorizontalTextPosition(SwingConstants.RIGHT);
        setVerticalTextPosition(SwingConstants.CENTER);
        setIconTextGap(12);
        
        // Hover effect
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!isActive) setBackground(new Color(230, 57, 70, 50));
            }
            public void mouseExited(MouseEvent e) {
                if (!isActive) setBackground(new Color(0, 0, 0, 0));
            }
        });
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        this.originalIcon = icon;
    }

public void setActive(boolean active) {
    if (active) {
        // ✅ ACTIVE STATE: Red background, white text
        setBackground(new java.awt.Color(220, 20, 60)); // Crimson
        setForeground(java.awt.Color.WHITE);
    } else {
        // ❌ INACTIVE STATE: Transparent background, gray text
        setBackground(new java.awt.Color(0, 0, 0, 0)); // Transparent
        setForeground(new java.awt.Color(80, 80, 80)); // Dark gray
    }
    repaint(); // ⚠️ MAHALAGA: Para mag-update ang UI agad!
}

    private Icon createColoredIcon(Icon original, Color color) {
        int w = original.getIconWidth();
        int h = original.getIconHeight();
        
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        original.paintIcon(null, g, 0, 0);
        g.dispose();
        
        BufferedImage colored = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = colored.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.setColor(color);
        g2.fillRect(0, 0, w, h);
        g2.dispose();
        
        return new ImageIcon(colored);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (isActive || getModel().isRollover()) {
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
            
            // White border
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
        }
        
        super.paintComponent(g2);
        g2.dispose();
    }
}