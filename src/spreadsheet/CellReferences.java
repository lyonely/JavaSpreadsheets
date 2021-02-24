package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;

import java.util.Set;

public class CellReferences implements Expression {

  CellLocation location;

  public CellReferences(CellLocation location) {
    this.location = location;
  }

  @Override
  public double evaluate(EvaluationContext context) {
    return context.getCellValue(location);
  }

  @Override
  public void findCellReferences(Set<CellLocation> dependencies) {
    dependencies.add(location);
  }

  @Override
  public String toString() {
    return location.toString();
  }
}