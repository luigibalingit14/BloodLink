package customcontrols;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class GlassTable extends JTable {

    public GlassTable() {
        // Table Body Settings
        setOpaque(false);
        setShowGrid(false);
        setBackground(new Color(0, 0, 0, 0));
        setForeground(new Color(245, 245, 245)); 
        setRowHeight(40); 
        setIntercellSpacing(new java.awt.Dimension(0, 0));

        // Data Rows Renderer
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((DefaultTableCellRenderer) c).setOpaque(false);
                ((DefaultTableCellRenderer) c).setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                
                if (isSelected) {
                    c.setBackground(new Color(255, 255, 255, 30)); 
                    ((DefaultTableCellRenderer) c).setOpaque(true);
                }
                return c;
            }
        });

        // ==========================================
        // PURE TRANSPARENT HEADER
        // ==========================================
        getTableHeader().setUI(new javax.swing.plaf.basic.BasicTableHeaderUI());
        getTableHeader().setOpaque(false);
        getTableHeader().setBackground(new Color(0, 0, 0, 0));
        getTableHeader().setReorderingAllowed(false);
        
        // Gagamit tayo ng raw JLabel para walang background na sisingit
        getTableHeader().setDefaultRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel header = new JLabel(value != null ? value.toString() : "");
                header.setOpaque(false); // SIGURADONG TRANSPARENT
                header.setBackground(new Color(0, 0, 0, 0));
                header.setForeground(Color.BLACK); // Kulay ng text
                header.setFont(new Font("SansSerif", Font.BOLD, 14));
                
                // Styling sa ilalim ng header text
                header.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 70)), 
                    BorderFactory.createEmptyBorder(15, 15, 15, 15) 
                ));
                
                return header;
            }
        });
    }
}