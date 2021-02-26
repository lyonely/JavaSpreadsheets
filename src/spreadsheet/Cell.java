package spreadsheet;

import static common.lexer.Lexer.tokenize;
import static spreadsheet.Parser.parse;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import common.api.Expression;
import common.lexer.InvalidTokenException;
import common.lexer.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A single cell in a spreadsheet, tracking the expression, value, and other parts of cell state.
 */
public class Cell {
  private final BasicSpreadsheet spreadsheet;
  CellLocation location;
  List<CellLocation> dependents = new ArrayList<>();
  /**
   * Constructs a new cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet,
   * @param location The location of this cell in the spreadsheet.
   */

  private Expression state = null;
  private Expression prevState = null;
  private double value = 0.0;


  Cell(BasicSpreadsheet spreadsheet, CellLocation location) {
    this.spreadsheet = spreadsheet;
    this.location = location;
  }

  /**
   * Gets the cell's last calculated value.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return the cell's value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Gets the cell's last stored expression, in string form.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return a string that parses to an equivalent expression to that last stored in the cell; if no
   *     expression is stored, we return the empty string.
   */
  public String getExpression() {
    if (state == null) {
      return "";
    } else {
      return state.toString();
    }
  }

  /**
   * Sets the cell's expression from a string.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param input The string representing the new cell expression.
   * @throws InvalidSyntaxException if the string cannot be parsed.
   */
  public void setExpression(String input) throws InvalidSyntaxException {
    if (state != null) {
      try {
        List<CellLocation> dependencies = tokenize(state.toString())
            .stream()
            .filter(t -> t.kind.equals(Token.Kind.CELL_LOCATION))
            .map(t -> t.cellLocationValue)
            .collect(Collectors.toList());
        for (CellLocation dependency : dependencies) {
          spreadsheet.removeDependency(this.location, dependency);
        }
      } catch (InvalidTokenException e) {
        e.printStackTrace();
      }
    }
    prevState = state;

    if (input.equals("")) {
      state = null;
    } else {
      try {
        state = parse(input);
        List<CellLocation> dependencies = tokenize(input)
            .stream()
            .filter(t -> t.kind.equals(Token.Kind.CELL_LOCATION))
            .map(t -> t.cellLocationValue)
            .collect(Collectors.toList());
        if (!dependencies.isEmpty()) {
          for (CellLocation dependency : dependencies) {
            spreadsheet.addDependency(this.location, dependency);
          }
        }
      } catch (InvalidTokenException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns a string representing the value, if any, of this cell.
   *
   * @return a string representing the value, if any, of this cell.
   */
  @Override
  public String toString() {
    if (state == null) {
      return "";
    } else {
      return String.valueOf(value);
    }
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void addDependent(CellLocation location) {
    dependents.add(location);
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void removeDependent(CellLocation location) {
    dependents.remove(location);
  }

  /**
   * Adds this cell's expression's references to a set.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param target The set that will receive the dependencies for this
   */
  public void findCellReferences(Set<CellLocation> target) {
    try {
      if (state != null) {
        List<CellLocation> dependencies = tokenize(state.toString())
            .stream()
            .filter(t -> t.kind.equals(Token.Kind.CELL_LOCATION))
            .map(t -> t.cellLocationValue)
            .collect(Collectors.toList());
        target.addAll(dependencies);
      }
    } catch (InvalidTokenException e) {
      e.printStackTrace();
    }
  }

  /**
   * Recalculates this cell's value based on its expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public void recalculate() {
    if (state == null) {
      value = 0.0;
    } else {
      value = state.evaluate(spreadsheet);
    }
    for (CellLocation dependent : dependents) {
      spreadsheet.recalculate(dependent);
    }
  }

  public void revertState() {
    try {
      if (prevState == null) {
        this.setExpression("");
      } else {
        this.setExpression(prevState.toString());
      }
    } catch (InvalidSyntaxException e) {
      e.printStackTrace();
    }
  }
}
