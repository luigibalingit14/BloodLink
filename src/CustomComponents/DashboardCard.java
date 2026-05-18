/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomComponents;



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class DashboardCard extends GlassPanel {
    
    private JLabel titleLabel;
    private JLabel valueLabel;
    private JLabel iconLabel;
    
    public DashboardCard(String title, String value, javax.swing.Icon icon) {
        setLayout(null);
        setPreferredSize(new java.awt.Dimension(200, 120));
        setBorderRadius(15);
        setGlassColor(new Color(255, 255, 255, 200));
        
        // Icon
        iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setBounds(15, 15, 40, 40);
        add(iconLabel);
        
        // Title
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(100, 116, 139));
        titleLabel.setBounds(15, 60, 170, 20);
        add(titleLabel);
        
        // Value
        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(30, 41, 59));
        valueLabel.setBounds(15, 80, 170, 30);
        add(valueLabel);
    }
    
    public void setValue(String value) {
        valueLabel.setText(value);
    }
    
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}