import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IllegalAccessException {

        //User input
        Scanner input = new Scanner(System.in);
        System.out.print("Expression: ");
        //Parsing expression
        String expression = input.nextLine();
        ArrayList<Token> tokens = ExpressionParser.tokenize(expression);
        if(tokens == null){
        }else{
            //Printing results
            if (ExpressionParser.validateExpression(tokens)){
                System.out.print("Token(s): [" + tokens.get(0));
                for(int i = 1; i < tokens.size(); i++) {
                    System.out.print(", " + tokens.get(i));
                }
                System.out.println("]");

                System.out.println("Success");

                System.out.println("Parse Tree:");
                ExpressionTree tree = new ExpressionTree();
                tree.generateTree(tokens);
                Node root = tree.getRoot();
                printTree(root, 0);
                System.out.println("Expression Result: " + ExpressionEvaluator.evaluateExpression(root));
            }
        }

    }

    public static void printTree(Node root, int depth){
        if(root == null){
            return;
        }
        //right subtree
        printTree(root.right, depth+1);
        //indent
        for(int i = 0; i < depth; i++){
            System.out.print("    ");
        }
        //current node
        System.out.println(root.token.getValue());
        //left subtree
        printTree(root.left, depth+1);
    }

}