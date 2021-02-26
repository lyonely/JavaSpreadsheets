package spreadsheet;

import static spreadsheet.Parser.parse;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import common.lexer.InvalidTokenException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Spreadsheet implements BasicSpreadsheet {
  //
  // start replacing
  //

  HashMap<CellLocation, Cell> spreadsheet = new HashMap<>();
  HashMap<CellLocation, List<CellLocation>> dependencyEdge = new HashMap<>();

  /**
   * Construct an empty spreadsheet.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  Spreadsheet() {
  }

  /**
   * Parse and evaluate an expression, using the spreadsheet as a context.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public double evaluateExpression(String expression)
      throws InvalidSyntaxException {
    try {
      return parse(expression).evaluate(this);
    } catch (InvalidTokenException e) {
      e.printStackTrace();
    }
    return 0.0;
  }

  /**
   * Assign an expression to a cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public void setCellExpression(CellLocation location, String input) throws InvalidSyntaxException {
    if (!spreadsheet.containsKey(location)) {
      spreadsheet.put(location, new Cell(this, location));
    }
    spreadsheet.get(location).setExpression(input);
    CycleDetector cycledetector = new CycleDetector(this);
    if (cycledetector.hasCycleFrom(location)) {
      spreadsheet.get(location).revertState();
    }
    spreadsheet.get(location).recalculate();
  }

  @Override
  public double getCellValue(CellLocation location) {
    if (!spreadsheet.containsKey(location)) {
      return 0.0;
    } else {
      return spreadsheet.get(location).getValue();
    }
  }

  //
  // end replacing
  //

  @Override
  public String getCellExpression(CellLocation location) {
    if (!spreadsheet.containsKey(location)) {
      return "";
    } else {
      return spreadsheet.get(location).getExpression();
    }
  }

  @Override
  public String getCellDisplay(CellLocation location) {
    if (!spreadsheet.containsKey(location) ||
        spreadsheet.get(location).getExpression().equals("")) {
      return "";
    } else {
      return Double.toString(this.getCellValue(location));
    }
  }

  @Override
  public void addDependency(CellLocation dependent, CellLocation dependency) {
    if (!dependencyEdge.containsKey(dependent)) {
      dependencyEdge.put(dependent, new ArrayList<>());
    }
    dependencyEdge.get(dependent).add(dependency);
    if (!spreadsheet.containsKey(dependency)) {
      try {
        this.setCellExpression(dependency, "");
      } catch (InvalidSyntaxException e) {
        e.printStackTrace();
      }
    }
    spreadsheet.get(dependency).addDependent(dependent);
  }

  @Override
  public void removeDependency(CellLocation dependent, CellLocation dependency) {
    dependencyEdge.get(dependent).remove(dependency);
    if (dependencyEdge.get(dependent).isEmpty()) {
      dependencyEdge.remove(dependent);
    }
    spreadsheet.get(dependency).removeDependent(dependent);
  }

  @Override
  public void recalculate(CellLocation location) {
    spreadsheet.get(location).recalculate();
  }

  @Override
  public void findCellReferences(CellLocation subject, Set<CellLocation> target) {
    spreadsheet.get(subject).findCellReferences(target);
  }
}
