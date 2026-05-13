/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomComponents;



import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

public class ModernTextField extends JTextField {
    
    private String placeholder = "";
    private int borderRadius = 10;
    private Color borderColor = new Color(226, 232, 240);
    private Color focusColor = new Color(230, 57, 70);
    
    public ModernTextField() {
        initField();
    }
    
    private void initField() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(new Color(50, 50, 50));
        setCaretColor(focusColor);
        setBorder(null);
        setOpaque(false);
        setMargin(new Insets(10, 15, 10, 15));
        
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
    
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        
        // Border
        g2.setColor(isFocusOwner() ? focusColor : borderColor);
        g2.setStroke(new java.awt.BasicStroke(isFocusOwner() ? 2 : 1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        
        super.paintComponent(g2);
        
        // Placeholder
        if (getText().isEmpty() && !placeholder.isEmpty()) {
            g2.setColor(new Color(148, 163, 184));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, x, y);
        }
        
        g2.dispose();
    }
}