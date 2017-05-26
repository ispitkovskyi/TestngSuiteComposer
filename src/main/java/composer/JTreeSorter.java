package composer;

import common.Constants;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.function.BiPredicate;

/**
 * Created by ispitkovskyi on 2/10/2017.
 */
public class JTreeSorter {

    public void sortAscending(JTree tree){
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        sort(rootNode, (node1, node2) -> node1.compareTo(node2) > 0);
        sort((DefaultMutableTreeNode) rootNode.getFirstChild(), (node1, node2) -> node1.compareTo(node2) > 0); //temporary workaround of method-nodes from first class-node of tree not being sorted
        tree.updateUI();
    }

    public void sortDescending(JTree tree){
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        sort(rootNode, (node1, node2) -> node1.compareTo(node2) < 0);
        sort((DefaultMutableTreeNode) rootNode.getFirstChild(), (node1, node2) -> node1.compareTo(node2) < 0);  //temporary workaround of method-nodes from first class-node of tree not being sorted
        tree.updateUI();
    }

    private DefaultMutableTreeNode sort(DefaultMutableTreeNode node, BiPredicate<String, String> predicate){
        int childCount = node.getChildCount();
        for(int i=0; i < childCount/*-1*/; i++){

            for (int j = 1; j < childCount - i; j++) {
                DefaultMutableTreeNode nodeBubble = (DefaultMutableTreeNode) node.getChildAt(j-1);
                String nodeBubbleNm = nodeBubble.getUserObject().toString();
                int nodeBubbleType = ((CustomMutableTreeNode) nodeBubble).getTestngType();

                if (nodeBubbleType == Constants.TYPE_CLASS) {
                    sort(nodeBubble, predicate);
                }

                DefaultMutableTreeNode nodeCompared = (DefaultMutableTreeNode) node.getChildAt(j);
                String nodeComparedNm = nodeCompared.getUserObject().toString();

                if (predicate.test(nodeBubbleNm, nodeComparedNm)) {
                    node.insert(nodeBubble, j);
                    node.insert(nodeCompared, j - 1);
                }
            }
        }
        return node;
    }
}
