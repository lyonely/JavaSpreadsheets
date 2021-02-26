package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Detects dependency cycles.
 */
public class CycleDetector {
  private final BasicSpreadsheet spreadsheet;

  /**
   * Constructs a new cycle detector.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet, used for resolving cell locations.
   */
  CycleDetector(BasicSpreadsheet spreadsheet) {
    this.spreadsheet = spreadsheet;
  }

  /**
   * Checks for a cycle in the spreadsheet, starting at a particular cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param start The cell location where cycle detection should start.
   * @return Whether a cycle was detected in the dependency graph starting at the given cell.
   */
  public boolean hasCycleFrom(CellLocation start) {
    List<CellLocation> visited = new ArrayList<>();
    visited.add(start);
    List<CellLocation> recStack = new ArrayList<>();
    recStack.add(start);
    Set<CellLocation> adj = new HashSet<>();
    spreadsheet.findCellReferences(start, adj);
    for (CellLocation location : adj) {
      if (isCyclic(location, visited, recStack)) {
        return true;
      }
    }
    return false;
  }

  private boolean isCyclic(CellLocation start,
                           List<CellLocation> visited,
                           List<CellLocation> recStack) {
    if (recStack.contains(start)) {
      return true;
    }
    if (visited.contains(start)) {
      return false;
    }
    visited.add(start);
    recStack.add(start);
    Set<CellLocation> adj = new HashSet<>();
    spreadsheet.findCellReferences(start, adj);
    for (CellLocation location : adj) {
      if (isCyclic(location, visited, recStack)) {
        return true;
      }
    }
    recStack.remove(start);
    return false;
  }
}
