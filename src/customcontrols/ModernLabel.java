/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customcontrols;



import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ModernLabel extends JLabel {

    private int radius = 25; // Gaano ka-round ang corners ng picture at label

    public ModernLabel() {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0)); // Transparent base
        setHorizontalAlignment(CENTER); // Gitna ang text kung walang picture
    }

    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // 1. I-drawing muna ang Glass Background (In case walang picture na nilagay)
        GradientPaint paint = new GradientPaint(0, 0, new Color(255, 255, 255, 80), 
                                                w, h, new Color(255, 255, 255, 30));
        g2.setPaint(paint);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        // 2. Kung may "icon" (Image) na nilagay sa Properties, i-drawing nang Auto-Fit at Rounded
        Icon currentIcon = getIcon();
        if (currentIcon != null && currentIcon instanceof ImageIcon) {
            Image img = ((ImageIcon) currentIcon).getImage();
            
            // Ito yung magic para ma-cut ang picture na maging rounded!
            g2.setClip(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radius, radius));
            
            // I-stretch/fit ang picture sa buong lapad at taas ng label
            g2.drawImage(img, 0, 0, w, h, this);
            
            g2.setClip(null); // Tanggalin ang clip para hindi maapektuhan ang iba
        }

        // 3. I-drawing ang Text (Kung meron)
        setIcon(null); // Pansamantalang itago ang default na image para di dumoble
        super.paintComponent(g2); 
        setIcon(currentIcon); // Ibalik ang image property

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Puti at glass-like na border ng label
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}