/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomComponents;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class RoundedButton2 extends JButton {
    
    private int borderRadius = 10;
    private Color normalColor = new Color(230, 57, 70); // #E63946
    private Color hoverColor = new Color(193, 18, 31);  // #C1121F
    private Color pressedColor = new Color(160, 15, 25);
    
    public RoundedButton2(String text) {
        super(text);
        initButton();
    }
    
    private void initButton() {
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(Color.WHITE);
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(normalColor);
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(pressedColor);
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }
        });
    }
    
    public void setButtonColor(Color normal, Color hover, Color pressed) {
        this.normalColor = normal;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        setBackground(normal);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        
        super.paintComponent(g2);
        g2.dispose();
    }
}
