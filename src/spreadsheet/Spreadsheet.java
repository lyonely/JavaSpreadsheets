package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import java.util.Set;

public class Spreadsheet implements BasicSpreadsheet {
  //
  // start replacing
  //

  /**
   * Construct an empty spreadsheet.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  Spreadsheet() {}

  /**
   * Parse and evaluate an expression, using the spreadsheet as a context.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public double evaluateExpression(String expression) throws InvalidSyntaxException {
    throw new UnsupportedOperationException("Copy over your solution for part 1");
  }

  @Override
  public double getCellValue(CellLocation location) {
    throw new UnsupportedOperationException("Copy over your solution for part 1");
  }

  @Override
  public void setCellExpression(CellLocation location, String input) throws InvalidSyntaxException {
    throw new UnsupportedOperationException("Copy over your solution for part 1");
  }

  //
  // end replacing
  //

  @Override
  public String getCellExpression(CellLocation location) {
    return "";
  }

  @Override
  public String getCellDisplay(CellLocation location) {
    return Double.toString(this.getCellValue(location));
  }

  @Override
  public void addDependency(CellLocation dependent, CellLocation dependency) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void removeDependency(CellLocation dependent, CellLocation dependency) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void recalculate(CellLocation location) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void findCellReferences(CellLocation subject, Set<CellLocation> target) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
