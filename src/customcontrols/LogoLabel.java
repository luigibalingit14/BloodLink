/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customcontrols;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * LogoLabel: Isang custom JLabel na binuo specifically para sa mga logos.
 * Features:
 * - Awtomatikong pinagkakasya (fits) ang BUONG image sa loob ng label bounds proportionially (hindi naii-stretch).
 * - Naka-center ang image.
 * - Walang border na pini-paint.
 * - Transparent background by default para tanging icon lang ang makita.
 */
public class LogoLabel extends JLabel {

    public LogoLabel() {
        super();
        
        // --- Mga default properties para sa 'Logo Only' look ---
        
        // 1. Siguraduhing walang border. User requested "walang border".
        // Bagaman default na 'null' ang border sa JLabel, pinipwersa natin dito.
        setBorder(null); 
        
        // 2. Gawing transparent ang background by default para icon lang ang lumutang.
        // Pwede itong baguhin ng user sa Properties (gawing Opaque at lagyan ng Color).
        setOpaque(false);
        
        // 3. I-setup ang default alignments para sa safe-keepings,
        // bagaman manual nating kino-compute ang centering sa paintComponent.
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        
        // Opsyonal: Pwede mong burahin ang default text dito kung gusto mo.
        // setText(""); 
    }

    /**
     * Override paintComponent para manual nating kontrolin ang pag-drawing ng image.
     * Dito natin ilalagay ang logic para sa proportional scaling at centering.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Kung ang component ay ginawang Opaque ng user (naka-check ang opaque property sa NetBeans),
        // kailangan nating i-paint ang background color muna.
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // TANDAAN: HINDI nating tinatawag ang super.paintComponent(g).
        // Kung tatawagin natin ito, ang standard JLabel drawing logic ay tatakbo,
        // at i-drawing din nito ang icon sa standard size (baka ma-crop o ma-stretch depende sa layout).
        // Kaya HINDI natin ito tinatawag. Manual nating ida-draw ang image.
        // super.paintComponent(g); 

        // Kunin ang kasalukuyang Icon na set sa label (via Properties o Code)
        Icon currentIcon = getIcon();

        // Lamang proceed kung may icon at ito ay isang ImageIcon (nag-contain ng Image)
        if (currentIcon != null && currentIcon instanceof ImageIcon) {
            Graphics2D g2 = (Graphics2D) g.create();
            
            // I-set ang rendering hints para sa smoother scaling ng image (mas magandang quality)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            Image img = ((ImageIcon) currentIcon).getImage();
            
            // Kunin ang original dimensions ng image
            int imgW = img.getWidth(this);
            int imgH = img.getHeight(this);

            // Kunin ang dimensions ng kasalukuyang component
            int labelW = getWidth();
            int labelH = getHeight();

            // Prevent division by zero kung hindi pa fully laid out ang component o maliit ang image
            if (labelW > 0 && labelH > 0 && imgW > 0 && imgH > 0) {
                
                // --- PROPORTIONAL SCALING LOGIC ---
                
                // Kalkulahin ang scale factors para magkasya (fit) sa component bounds.
                // We calculate scaling factor separately for width and height.
                double scaleW = (double) labelW / imgW;
                double scaleH = (double) labelH / imgH;
                
                // Kunin ang PINAKAMALIIT na scaling factor. Ito ang gagamitin natin para i-scale ang image.
                // Sa paraang ito, ang BUONG image ay laging nakikita sa loob ng bounds,
                // proportionially scaled, at hindi naii-stretch o naku-crop.
                double scale = Math.min(scaleW, scaleH);

                // Kalkulahin ang final dimensions ng image pagkatapos ng scaling.
                int targetW = (int) (imgW * scale);
                int targetH = (int) (imgH * scale);

                // --- CENTERING LOGIC ---
                
                // Kalkulahin ang (x, y) coordinates para naka-center ang image sa loob ng label component.
                int x = (labelW - targetW) / 2;
                int y = (labelH - targetH) / 2;

                // --- DRAWING ---
                
                // I-draw ang scaled at centered image.
                // Ang built-in drawImage handles smooth scaling kung specified sa RenderingHints.
                g2.drawImage(img, x, y, targetW, targetH, this);
            }
            g2.dispose();
        }
        
        // TANDAAN: Dahil hindi natin tinawag ang super.paintComponent(g), hindi rin i-drawing ng JLabel
        // ang text property nito by default. Ito ay okay dahil ang purpose ng LogoLabel ay ipakita ang logo lamang.
        // Kung kailangan mo rin ng text support na naka-align sa image, kailangan mo itong manual na i-paint
        // after drawing the image.
    }
}