package mc.rysty.heliosphereranks.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MathUtils {

    public static int getLargestInt(int... numbers) {
        Integer largest = null;

        for (int number : numbers)
            if (largest == null || number > largest)
                largest = number;

        return largest;
    }

    public static class ValueComparator implements Comparator<String> {
        private Map<String, Integer> base;

        public ValueComparator(HashMap<String, Integer> map) {
            this.base = map;
        }

        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b))
                return -1;
            else
                return 1;
        }
    }
}
