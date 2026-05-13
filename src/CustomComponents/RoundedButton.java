
package CustomComponents;



import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class RoundedButton extends JButton {

    private int radius = 10;
    private Color normalColor = new Color(220, 53, 69);
    private Color hoverColor = new Color(190, 43, 59);
    private Color pressedColor = new Color(160, 33, 49);

    // ⚠️ IMPORTANT: Dapat PUBLIC at WALANG PARAMETERS
    public RoundedButton() {
        super(""); // Default text
        initButton();
    }
    
    // Constructor with text parameter
    public RoundedButton(String text) {
        super(text);
        initButton();
    }
    
    private void initButton() {
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(normalColor);
                repaint();
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(pressedColor);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        super.paintComponent(g2);
        g2.dispose();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public void setButtonColor(Color normal, Color hover, Color pressed) {
        this.normalColor = normal;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        setBackground(normal);
    }
}