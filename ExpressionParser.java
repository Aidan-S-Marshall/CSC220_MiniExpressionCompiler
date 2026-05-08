import java.util.ArrayList;
import java.util.Scanner;

public class ExpressionParser {

    private static int position = 0;
    private static ArrayList<Token> tokens;

    public static ArrayList<Token> tokenize(String input) throws IllegalAccessException {

        int i = 0;
        ArrayList<Token> tokens = new ArrayList<>();

        while(i < input.length()){
            char curr = input.charAt(i);

            //skip whitespace
            if(Character.isWhitespace(curr)){
                i++;
                continue;
            }

            String num = "";
            //number token
            if(Character.isDigit(curr)){
                while(i < input.length() && Character.isDigit(input.charAt(i))){
                    num += input.charAt(i);
                    i++;
                }
                tokens.add(new Token("NUMBER", num));
                continue;
            }

            switch(curr){
                //increment/plus token
                case '+' -> {
                    //peek for another plus
                    if(i + 1 < input.length() && input.charAt(i+1) == '+'){
                        //check if previous is number
                        if(!tokens.isEmpty() && tokens.get(tokens.size()-1).getType().equals("NUMBER")){
                            tokens.add(new Token("POST_INCREMENT", "++"));
                        }else{
                            tokens.add(new Token("PRE_INCREMENT", "++"));
                        }
                        i+=2;
                    }else{
                        tokens.add(new Token("PLUS", "+"));
                        i++;
                    }
                }
                //decrement/minus token
                case '-' -> {
                    //peek for another minus
                    if(i+1 < input.length() && input.charAt(i+1) == '-'){
                        //check if previous is number
                        if(!tokens.isEmpty() && tokens.get(tokens.size()-1).getType().equals("NUMBER")){
                            tokens.add(new Token("POST_DECREMENT", "--"));
                        }else{
                            tokens.add(new Token("PRE_DECREMENT", "--"));
                        }
                        i+=2;
                    }
                    //negative number
                    else if(i + 1 < input.length() && Character.isDigit(input.charAt(i+1))
                            && isUnaryDigits(tokens)){
                        num = "-";
                        i++;
                        //multi-digit negative
                        while(i < input.length() && Character.isDigit(input.charAt(i))){
                            num += input.charAt(i);
                            i++;
                        }
                        tokens.add(new Token("NUMBER", num));
                    }
                    //minus
                    else{
                        tokens.add(new Token("MINUS", "-"));
                        i++;
                    }
                }
                //other tokens
                case '*' -> {tokens.add(new Token("MULTIPLY", "*"));i++;}
                case '/' -> {tokens.add(new Token("DIVIDE", "/"));i++;}
                case '(' -> {tokens.add(new Token("LPAREN", "("));i++;}
                case ')' -> {tokens.add(new Token("RPAREN", ")"));i++;}
                default -> {System.out.println("Unknown token '" + curr + "' at position " + (i+1));return null;}
            }
        }
        return tokens;
    }

    private static boolean isUnaryDigits(ArrayList<Token> tokens){
        //check if start of expression
        if(tokens.isEmpty()){
            return true;
        }
        //check if there is valid operand left of operator
        String last = tokens.get(tokens.size()-1).getType();
        if(last.equals("PLUS") || last.equals("MINUS") ||
            last.equals("MULTIPLY") || last.equals("DIVIDE") ||
            last.equals("LPAREN")){
            return true;
        }

        return false;
    }


    public static boolean validateExpression(ArrayList<Token> tokenList){
        tokens = tokenList;
        //start at beginning whenever called
        position = 0;
        if(!validateE()){
            return false;
        }
        //check if any tokens left
        if(position != tokens.size()){
            System.out.println("Unexpected token '" + tokens.get(position).getValue() + "' at position " + (position+1));
            return false;
        }
        return position == tokens.size();
    }


    private static boolean validateF(){
        //check if missing operand
        if(position >= tokens.size()){
            System.out.println("Unexpected end of expression");
            return false;
        }
        String type = tokens.get(position).getType();

        //check for unary minus
        if(type.equals("MINUS")){
            position++;
            if(position >= tokens.size()){
                System.out.println("Invalid unary operand at position " + (position+1));
                return false;
            }
            return validateF();
        }

        //check for pre-unary operator
        if(type.equals("PRE_INCREMENT") || type.equals("PRE_DECREMENT")){
            position++;
            if(position >= tokens.size()){
                System.out.println("Invalid unary operand at position " + (position+1));
                return false;
            }
            //check for valid expression after unary operator
            if(!tokens.get(position).getType().equals("NUMBER")
            && !tokens.get(position).getType().equals("LPAREN") && !tokens.get(position).getType().equals("PRE_INCREMENT")
            && !tokens.get(position).getType().equals("PRE_DECREMENT")){
                System.out.println("Invalid unary operand at position " + position);
                return false;
            }
            return validateF();
        }
        //check for post-unary operator
        if(type.equals("NUMBER")){
            position++;
            if(position < tokens.size()){
                String next = tokens.get(position).getType();
                if(next.equals("POST_INCREMENT") || next.equals("POST_DECREMENT")){
                    position++;
                    //check for additional post-unary operators
                    if(position < tokens.size()){
                        String nextNext = tokens.get(position).getType();
                        if(nextNext.equals("NUMBER") || nextNext.equals("LPAREN") ||
                            nextNext.contains("DECREMENT") || nextNext.contains("INCREMENT")){
                            System.out.println("Missing unary operator/operand at position " + (position+1));
                            return false;
                        }
                    }
                }
            }
            return true;
        }


        //validate parentheses
        if(type.equals("LPAREN")){
            position++;
            if(!validateE()){
                return false;
            }
            //no closing parentheses
            if(position >= tokens.size() || !tokens.get(position).getType().equals("RPAREN")){
                System.out.println("Missing ')' at position " + (position+1));
                return false;
            }
            position++;
            return true;
        }
        System.out.println("Unexpected Token '" + tokens.get(position).getValue() + "' at position " + (position+1));
        return false;
    }

    private static boolean validateT(){
        //validate left operand
        if(!validateF()){
            return false;
        }
        while(position < tokens.size()){
            String type = tokens.get(position).getType();
            //validate operator
            if(type.equals("MULTIPLY") || type.equals("DIVIDE")){
                position++;
                //validate right operand
                if(!validateF()){
                    return false;
                }
                //check if missing operator
            }else if(type.equals("NUMBER") || type.equals("LPAREN")){
                System.out.println("Missing operator at position " + (position+1));
                return false;
            }
            else{
                break;
            }
        }
        return true;
    }

    private static boolean validateE(){
        //validate left operand
        if(!validateT()){
            return false;
        }
        while(position < tokens.size()){
            String type = tokens.get(position).getType();
            //check if valid operator
            if(type.equals("PLUS") || type.equals("MINUS")){
                position++;
                //validate right operand
                if(!validateT()){
                    return false;
                }
                //check if missing operator
            }else if(type.equals("NUMBER") || type.equals("LPAREN")){
                System.out.println("Missing operator at position " + (position+1));
                return false;
            }
            else{
                break;
            }
        }
        return true;
    }

}
