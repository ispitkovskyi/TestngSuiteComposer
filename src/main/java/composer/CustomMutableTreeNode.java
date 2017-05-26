package composer;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import java.util.Vector;


/**
 * Created by ispitkovskyi on 12/29/2016.
 */
public class CustomMutableTreeNode extends DefaultMutableTreeNode {
    private DefaultTableModel parameters;
    private int testngType;

    public CustomMutableTreeNode(String name){
        this(name, 0, null);
    }

    public CustomMutableTreeNode(String name, int testngType){
        this(name, testngType, null);
    }

    public CustomMutableTreeNode(String name, int testngType, DefaultTableModel parameters){
        super(name);

        this.testngType = testngType;

        if(parameters != null) {
            this.parameters = parameters;
        }else{
            resetParameters();
        }
    }

    public void setParameters(DefaultTableModel parametersTableModel){
        parameters = parametersTableModel;
    }

    public DefaultTableModel getParameters(){
        //return this.parameters;
        String pNameHeader = this.parameters.getColumnName(0);
        String pValHeader = this.parameters.getColumnName(1);

        Vector parametersData = cloneVector(this.parameters.getDataVector());

        return new DefaultTableModel(
                parametersData,
                new Vector(){{
                    add(pNameHeader);
                    add(pValHeader);
                }});
    }

    //Deep cloning of Vector. Default Vector.clone() Java function leaves persistent links to data array inside clonned Vector, because of that changes made in clone affect data in source.
    private Vector cloneVector(Vector sourceVector){
        Vector parametersData = new Vector();

        Enumeration paramsEnum = sourceVector.elements();
        while(paramsEnum.hasMoreElements()){
            Vector item = (Vector)paramsEnum.nextElement();
            parametersData.add(new Vector(){{add(item.get(0)); add(item.get(1));}});
        }

        return parametersData;
    }

    public int getTestngType(){
        return this.testngType;
    }

    public void resetParameters(){
        parameters = new DefaultTableModel(new Object[]{"Name", "Value"}, 0);
    }

    public void addParameterRow(Object key){
        addParameterRow(key, "");
    }

    public void addParameterRow(Object key, Object value){
        parameters.addRow(new Object[]{key, value});
        //((DefaultTableModel)paramsTable.getModel()).addRow(new Object[]{paramNode.toString(), ""});
    }

}
