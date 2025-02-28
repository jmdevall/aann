package jmdevall.aann;

import java.util.ArrayList;
import java.util.List;

public class ListTrasposer {
	/**
	 * Transpose matrix of list of lists
	 * 
	 * Example:
	 * Input List
	 * 
	 * [
	 *     ["a", "b", "c"],
	 *     ["d", "e", "f"]
	 * ]
	 * 
	 * OutpuList:
	 * [
	 *     ["a", "d"],
	 *     ["b", "e"],
	 *     ["c", "f"]
	 * ]
	 * <
	 * 
	 * @param input matrix to trasponse
	 * @return La trasposed matrix
	 */
    public static List<List<String>> transpose(List<List<String>> input) {
        if (input == null || input.isEmpty()) {
            return new ArrayList<>();
        }

        int numRows = input.size();
        int numCols = input.get(0).size();

        List<List<String>> transposed = new ArrayList<>(numCols);

        for (int col = 0; col < numCols; col++) {
            List<String> newRow = new ArrayList<>(numRows);
            for (int row = 0; row < numRows; row++) {
                newRow.add(input.get(row).get(col));
            }
            transposed.add(newRow);
        }

        return transposed;
    }

}
