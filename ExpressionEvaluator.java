import java.util.ArrayList;
import java.util.Arrays;

public class ExpressionEvaluator {


    public static double evaluateExpression(Node root){
        //post-order traversal
        if(root == null){
            return 0;
        }

        //return leaf node
        if(root.left == null && root.right == null){
            return Double.parseDouble(root.token.getValue());
        }
        //evaluate left tree
        double leftNode = evaluateExpression(root.left);
        //evaluate right tree
        double rightNode = evaluateExpression(root.right);
        //evaluate left and right node
        switch(root.token.getValue()){
            case "+" -> {
                return leftNode + rightNode;
            }
            case "-" -> {
                return leftNode - rightNode;
            }
            case "*" -> {
                return leftNode * rightNode;
            }
            case "/" -> {
                return leftNode / rightNode;
            }
            case "++" -> {
                return leftNode + 1;
            }
            case "--" -> {
                return leftNode - 1;
            }
        }
        return Double.parseDouble(root.token.getValue());
    }



}
