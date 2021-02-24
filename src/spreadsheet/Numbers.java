package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;

import java.util.Set;

public class Numbers implements Expression {

  double atom;

  public Numbers(double atom) {
    this.atom = atom;
  }

  @Override
  public double evaluate(EvaluationContext context) {
    return atom;
  }

  @Override
  public void findCellReferences(Set<CellLocation> dependencies) {
  }

  @Override
  public String toString() {
    return String.format("%.1f", atom);
  }
}