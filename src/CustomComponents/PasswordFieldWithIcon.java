/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomComponents;



import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.Icon;

public class PasswordFieldWithIcon extends JPasswordField {

    private String placeholder = "";
    private Icon leftIcon;
    private Icon eyeOpenIcon;
    private Icon eyeClosedIcon;
    private boolean showPassword = false;
    private int radius = 8;
    private Color backgroundColor = new Color(250, 250, 250);
    private Color borderColor = new Color(230, 230, 230);
    private Color focusColor = new Color(220, 53, 69);
    private JLabel toggleButton;

    public PasswordFieldWithIcon() {
        setOpaque(false);
        setMargin(new Insets(10, 40, 10, 45));
        setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        setForeground(new Color(80, 80, 80));
        setCaretColor(new Color(80, 80, 80));
        setEchoChar('\u2022');

        // Create eye toggle button
        toggleButton = new JLabel();
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPassword = !showPassword;
                if (showPassword) {
                    setEchoChar((char) 0);
                    if (eyeOpenIcon != null) {
                        toggleButton.setIcon(eyeOpenIcon);
                    }
                } else {
                    setEchoChar('\u2022');
                    if (eyeClosedIcon != null) {
                        toggleButton.setIcon(eyeClosedIcon);
                    }
                }
            }
        });

        setLayout(null);
    }

    public void setLeftIcon(Icon icon) {
        this.leftIcon = icon;
        repaint();
    }

    public void setEyeIcons(Icon eyeOpen, Icon eyeClosed) {
        this.eyeOpenIcon = eyeOpen;
        this.eyeClosedIcon = eyeClosed;
        if (toggleButton != null && eyeClosed != null) {
            toggleButton.setIcon(eyeClosed);
            int iconWidth = eyeClosed.getIconWidth();
            int iconHeight = eyeClosed.getIconHeight();
            toggleButton.setBounds(getWidth() - iconWidth - 12, (getHeight() - iconHeight) / 2, iconWidth, iconHeight);
            add(toggleButton);
        }
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
        if (getPassword().length == 0 && placeholder != null && !placeholder.isEmpty()) {
            g2.setColor(new Color(150, 150, 150));
            g2.setFont(getFont().deriveFont(java.awt.Font.PLAIN));
            FontMetrics fm = g2.getFontMetrics();
            int iconWidth = leftIcon != null ? leftIcon.getIconWidth() + 8 : 0;
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

        // Border
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
        if (leftIcon != null) {
            int iconY = (getHeight() - leftIcon.getIconHeight()) / 2;
            leftIcon.paintIcon(this, g, 12, iconY);
        }
    }

    @Override
    public void doLayout() {
        super.doLayout();
        if (toggleButton != null && eyeClosedIcon != null) {
            int iconWidth = eyeClosedIcon.getIconWidth();
            int iconHeight = eyeClosedIcon.getIconHeight();
            toggleButton.setBounds(getWidth() - iconWidth - 12, (getHeight() - iconHeight) / 2, iconWidth, iconHeight);
        }
    }
}