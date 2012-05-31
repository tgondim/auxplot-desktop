package br.com.tg.gui.util;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Nobuo Tamemasa
 * @version 1.0 11/09/98
 */
public class MultiRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    JCheckBox checkBox = new JCheckBox();

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Boolean) { // Boolean
            checkBox.setSelected(((Boolean) value).booleanValue());
            checkBox.setHorizontalAlignment(JLabel.CENTER);
            return checkBox;
        }
        String str = (value == null) ? "" : value.toString();
        return super.getTableCellRendererComponent(table, str, isSelected,
                hasFocus, row, column);
    }
}