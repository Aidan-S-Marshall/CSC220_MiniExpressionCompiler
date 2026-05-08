# CSC220_MiniExpressionCompiler

Summary:
  This project aims to simulate how a compiler would accept an expression and interpret it. It began with first accepting the input of the expression. From there it was passed to a tokenizer function that would only pass valid characters into an arraylist. If any invalid charcters were found, it would error out and not procede any further. If the expression was tokenized properly, it would be passed through to a parser. The parser's job was to validate or invalidate the grammar of the expression. It would do this via recursive descent parsing, starting by validated all numbers and parentheses. From there if no problems were found, it was returned back to validate the grammar of any multiplies or divides. From there with the same idea it checked all pluses or minuses. If no grammar violations were found it was passed to an AST generator. The AST generator used the same logic of the parser to split each expression into subtree with the parent being the operator between the two operands. After the AST was generated, a post-order traversal was run in order to evaluate the expression. To print the tree a reverse in-order traversal was used. After all was done the results were printed.

Setup Instructions:
  Download the java files provided and run the Main.java file through the command prompt or any IDE preffered. As long as the files are there it should work.

Example Inputs:
  Input these into the console to see the token list, if it was successful, a parse tree, and an evaluated result.
-   (5+3)-2+5
-   ((3+2)-((1)))
-   ++(5+2)*(5-1)
-   --5*(4/2)+3
