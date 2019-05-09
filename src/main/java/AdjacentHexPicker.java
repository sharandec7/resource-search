import java.util.List;
import java.util.Map;

public class AdjacentHexPicker {

    public static int findCeil(int[] a, int r, int lo, int hi) {
        int mid;
        while (lo < hi) {
            mid = lo + (hi - lo) / 2;
            if (a[mid] < r) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return a[lo] >= r ? lo : -1;
    }

    public static int pickTheBest(int[] a, Map<String, Integer> adjacentHexagonWeights, List<String> neighbours) {

        int[] prefix = new int[a.length];
        prefix[0] = adjacentHexagonWeights.getOrDefault(neighbours.get(0), 0);

        for (int i = 1; i < a.length; i++) {
            prefix[i] = adjacentHexagonWeights.get(neighbours.get(i - 1)) + adjacentHexagonWeights.get(neighbours.get(i));
        }

        int r = (int) (Math.random() * prefix[a.length - 1]) + prefix[0];

        int indexCeil = findCeil(prefix, r, 0, a.length - 1);
        if(indexCeil>=a.length)
            return a[a.length-1];
        else if(indexCeil<=0)
            return a[0];
        return a[indexCeil];
    }
}


