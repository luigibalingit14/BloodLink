/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customcontrols;




import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

public class ModernTextbox extends JTextField {

    private String placeholder = "";
    private int radius = 25; // Gaano ka-round ang corners (pwede mong baguhin)

public ModernTextbox() {
        setOpaque(false); 
        setBackground(new Color(0, 0, 0, 0)); // <--- IDAGDAG MO ITO! (0 Alpha = 100% Transparent)
        
        setMargin(new Insets(5, 15, 5, 15)); 
        setForeground(Color.BLACK); // Gawing Black ang text para makita sa light background
        setCaretColor(Color.BLACK); // Gawing Black ang blinking cursor
        
        // Listener para kapag kinlick, mas iilaw ang salamin (Focus effect)
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

    // Para ma-edit ang hint text sa Properties window ng NetBeans
    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    // Para ma-edit kung gaano ka-round ang corners
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Para hindi pixelated at smooth ang curves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // GLASSMORPHISM BACKGROUND (Gradient na puti na transparent)
        // Kapag naka-click (focused) mas malinaw, kapag hindi, mas transparent
        int alphaStart = isFocusOwner() ? 120 : 80;
        int alphaEnd = isFocusOwner() ? 60 : 30;
        
        GradientPaint paint = new GradientPaint(0, 0, new Color(255, 255, 255, alphaStart), 
                                                w, h, new Color(255, 255, 255, alphaEnd));
        g2.setPaint(paint);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        super.paintComponent(g);

        // PLACEHOLDER / HINT TEXT
        if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty()) {
            g2.setColor(new Color(255, 255, 255, 170)); // Semi-transparent white para sa hint
            g2.setFont(getFont().deriveFont(java.awt.Font.ITALIC)); // Naka-italic ang hint
            FontMetrics fm = g2.getFontMetrics();
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, getInsets().left, y);
        }
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // BORDER NG SALAMIN (Gawing puti)
        int borderAlpha = isFocusOwner() ? 255 : 150; // Solid white kung naka-click
        g2.setColor(new Color(255, 255, 255, borderAlpha));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        
        g2.dispose();
    }
}