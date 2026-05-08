import java.util.ArrayList;

public class ExpressionTree {
    private int position;
    private ArrayList<Token> tokens;

    Node root = null;;

    public void generateTree(ArrayList<Token> tokenList) {
        tokens = tokenList;
        position = 0;
        root = buildE();
    }

    public Node getRoot() {
        return root;
    }

    public Node buildF(){
        //end of tree
        if(position >= tokens.size()){
            return null;
        }
        Token curr = tokens.get(position);

        //check for pre-unary operator
        if(curr.getType().equals("PRE_INCREMENT") || curr.getType().equals("PRE_DECREMENT")){
            position++;
            Node operand = buildF();
            return new Node(curr, operand, null);
        }
        //check for post-unary operator
        if (curr.getType().equals("NUMBER")) {
            position++;
            if (position < tokens.size()) {
                String next = tokens.get(position).getType();
                //peek for next operator
                if (next.equals("POST_INCREMENT") || next.equals("POST_DECREMENT")) {
                    Token op = tokens.get(position);
                    position++;
                    return new Node(op, new Node(curr), null);
                }
            }
            return new Node(curr);
        }

        //store leaf nodes
        if(curr.getType().equals("NUMBER")){
            position++;
            return new Node(curr);
        }
        //evaluate expression in parentheses
        if(curr.getType().equals("LPAREN")){
            position++;
            Node node = buildE();
            if(position < tokens.size() && tokens.get(position).getType().equals("RPAREN")){
                position++;
            }
            return node;
        }
        //none of the above found
        return null;
    }


    public Node buildT(){
        //get left operand
        Node left = buildF();
        //look for * or /
        while(position < tokens.size()){
            Token operator = tokens.get(position);
            if(operator.getType().equals("MULTIPLY") || operator.getType().equals("DIVIDE")){
                position++;
                //get right operand
                Node right = buildF();
                //store in left for future iterations
                left = new Node(operator, left, right);
            }else{
                break;
            }
        }
        //return when out of * or /
        return left;
    }

    public Node buildE(){
        //get left operand
        Node left = buildT();
        //look for + or -
        while(position < tokens.size()){
            Token operator = tokens.get(position);
            if(operator.getType().equals("PLUS") || operator.getType().equals("MINUS")){
                position++;
                //get right operand
                Node right = buildT();
                //store in left for future iterations
                left = new Node(operator, left, right);
            }else{
                break;
            }
        }
        //return when out of + or -
        return left;
    }

}
