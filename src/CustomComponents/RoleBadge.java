
package CustomComponents;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class RoleBadge extends JLabel {
    
    private Color badgeColor = new Color(254, 226, 226);
    private Color textColor = new Color(230, 57, 70);
    private int borderRadius = 20;

    // ✅ IMPORTANT: Public no-arg constructor
    public RoleBadge() {
        super("ADMIN", SwingConstants.CENTER);
        initBadge();
    }
    
    public RoleBadge(String role) {
        super(role.toUpperCase(), SwingConstants.CENTER);
        initBadge();
    }
    
    private void initBadge() {
        setFont(new Font("Segoe UI", Font.BOLD, 11));
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setForeground(textColor);
        setBorder(null);
        updateColors();
    }
    
    private void updateColors() {
        String text = getText();
        if (text != null && text.equalsIgnoreCase("Admin")) {
            badgeColor = new Color(254, 226, 226);
            textColor = new Color(230, 57, 70);
        } else {
            badgeColor = new Color(219, 234, 254);
            textColor = new Color(59, 130, 246);
        }
        setForeground(textColor);
        repaint();
    }
    
    public void setRole(String role) {
        setText(role.toUpperCase());
        updateColors();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(badgeColor);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        
        super.paintComponent(g2);
        g2.dispose();
    }
}