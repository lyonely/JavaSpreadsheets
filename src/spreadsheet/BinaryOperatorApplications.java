package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;

import java.util.Set;

public class BinaryOperatorApplications implements Expression {

  Expression first;
  Expression second;
  String binaryOperator;

  public BinaryOperatorApplications(Expression second, Expression first, String binaryOperator) {
    this.first = first;
    this.second = second;
    this.binaryOperator = binaryOperator;
  }

  @Override
  public double evaluate(EvaluationContext context) {
    return switch (binaryOperator) {
      case "+" -> first.evaluate(context) + second.evaluate(context);
      case "-" -> first.evaluate(context) - second.evaluate(context);
      case "*" -> first.evaluate(context) * second.evaluate(context);
      case "/" -> first.evaluate(context) / second.evaluate(context);
      case "^" -> Math.pow(first.evaluate(context), second.evaluate(context));
      default -> 0;
    };
  }

  @Override
  public void findCellReferences(Set<CellLocation> dependencies) {
    first.findCellReferences(dependencies);
    second.findCellReferences(dependencies);
  }

  @Override
  public String toString() {
    return "(" + first + " " + binaryOperator + " " + second + ")";
  }


}