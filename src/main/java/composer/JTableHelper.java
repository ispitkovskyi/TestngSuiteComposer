package composer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Created by ispitkovskyi on 2/10/2017.
 */
public class JTableHelper {
    private DefaultTableModel defaultTableModel;

    public DefaultTableModel getDefaultTableModel(){
        if(defaultTableModel == null) {
            return new DefaultTableModel(new Object[]{"Name", "Value"}, 0);
        }
        return defaultTableModel;
    }

    /**
     * Gets model of table, including value being edited
     * @param editableTable
     * @return
     */
    public DefaultTableModel getTableModel(JTable editableTable){
        //Consume value being edited:
        if (editableTable.getCellEditor() != null) {
            editableTable.getCellEditor().stopCellEditing();
        }
        return (DefaultTableModel) editableTable.getModel();
    }
}
