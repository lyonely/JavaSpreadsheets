package spreadsheet;

import common.api.Expression;
import common.lexer.InvalidTokenException;
import common.lexer.Token;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import static common.lexer.Lexer.tokenize;

public class Parser {
  private static final Map<Token.Kind, String> associativity = Map.of(
      Token.Kind.LPARENTHESIS, "NEUTRAL",
      Token.Kind.RPARENTHESIS, "NEUTRAL",
      Token.Kind.PLUS, "LEFT",
      Token.Kind.MINUS, "LEFT",
      Token.Kind.STAR, "LEFT",
      Token.Kind.SLASH, "LEFT",
      Token.Kind.CARET, "RIGHT");
  private static final Map<Token.Kind, Integer> precedence = Map.of(
      Token.Kind.LPARENTHESIS, 0,
      Token.Kind.RPARENTHESIS, 0,
      Token.Kind.PLUS, 1,
      Token.Kind.MINUS, 1,
      Token.Kind.STAR, 2,
      Token.Kind.SLASH, 2,
      Token.Kind.CARET, 3);

  /**
   * Parse a string into an Expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  static Expression parse(String input) throws InvalidSyntaxException, InvalidTokenException {
    List<Token> tokens = tokenize(input);
    Stack<Expression> operands = new Stack<>();
    Stack<Token> operators = new Stack<>();
    Map<Token.Kind, String> binaryOperators =
        Map.of(Token.Kind.PLUS, "+",
            Token.Kind.MINUS, "-",
            Token.Kind.STAR, "*",
            Token.Kind.SLASH, "/",
            Token.Kind.CARET, "^");
    while (!tokens.isEmpty()) {
      if (tokens.get(0).kind == Token.Kind.LANGLE || tokens.get(0).kind == Token.Kind.RANGLE) {
        throw new InvalidSyntaxException("Invalid Expression");
      } else if (tokens.get(0).kind == Token.Kind.RPARENTHESIS
          && operators.peek().kind == Token.Kind.LPARENTHESIS) {
        operators.pop();
        tokens.remove(0);
      } else if (tokens.get(0).kind == Token.Kind.NUMBER) {
        operands.push(new Numbers(tokens.get(0).numberValue));
        tokens.remove(0);
      } else if (tokens.get(0).kind == Token.Kind.CELL_LOCATION) {
        operands.push(new CellReferences(tokens.get(0).cellLocationValue));
        tokens.remove(0);
      } else if (operands.size() >= 2 && !operators.empty()
          && supersedes(operators.peek(), tokens.get(0))) {
        operands.push(new BinaryOperatorApplications(operands.pop(), operands.pop(),
            binaryOperators.get(operators.pop().kind)));
      } else {
        operators.push(tokens.get(0));
        tokens.remove(0);
      }
    }
    while (!operators.isEmpty()) {
      BinaryOperatorApplications bin =
          new BinaryOperatorApplications(operands.pop(), operands.pop(),
              binaryOperators.get(operators.pop().kind));
      operands.push(bin);
    }

    return operands.pop();
  }

  private static boolean supersedes(Token token1, Token token2) {
    return (precedence.get(token1.kind) > precedence.get(token2.kind)
        || (precedence.get(token1.kind).equals(precedence.get(token2.kind))
        && associativity.get(token1.kind).equals("LEFT")));
  }
}
