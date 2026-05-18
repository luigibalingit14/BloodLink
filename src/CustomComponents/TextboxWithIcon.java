package CustomComponents;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.Icon;

public class TextboxWithIcon extends JTextField {

    private String placeholder = "";
    private Icon icon;
    private int radius = 8;
    private Color backgroundColor = new Color(250, 250, 250);
    private Color borderColor = new Color(230, 230, 230);
    private Color focusColor = new Color(220, 53, 69);

    public TextboxWithIcon() {
        setOpaque(false);
        setMargin(new Insets(10, 40, 10, 15));
        setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        setForeground(new Color(80, 80, 80));
        setCaretColor(new Color(80, 80, 80));
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }
            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        super.paintComponent(g2);

        // Placeholder
        if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty()) {
            g2.setColor(new Color(150, 150, 150));
            g2.setFont(getFont().deriveFont(java.awt.Font.PLAIN));
            FontMetrics fm = g2.getFontMetrics();
            int iconWidth = icon != null ? icon.getIconWidth() + 8 : 0;
            int x = getInsets().left + iconWidth;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, x, y);
        }
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Border color - red kapag focused
        Color border = isFocusOwner() ? focusColor : borderColor;
        int borderWidth = isFocusOwner() ? 2 : 1;
        
        g2.setColor(border);
        for (int i = 0; i < borderWidth; i++) {
            g2.drawRoundRect(i, i, getWidth() - 1 - (i * 2), getHeight() - 1 - (i * 2), radius, radius);
        }
        
        g2.dispose();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (icon != null) {
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g, 12, iconY);
        }
    }

    public Object getTextField() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}