package interviewee.Leecode.array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class maxDistance {
    public int maxDistance1(List<List<Integer>> arrays) {
        List<Integer> list = arrays.get(0);
        int max = Integer.MIN_VALUE;
        for (int i = 1; i < arrays.size(); i++) {
            int temp = Math.max(Math.abs(list.get(0) - arrays.get(i).get(arrays.get(i).size() - 1)),
                    Math.abs(list.get(list.size() - 1) - arrays.get(i).get(0)));
            max = Math.max(max, temp);
            for (int j = i + 1; j < arrays.size(); j++) {
                temp = Math.max(Math.abs(arrays.get(i).get(0) - arrays.get(j).get(arrays.get(j).size() - 1)),
                        Math.abs(arrays.get(i).get(arrays.get(i).size() - 1) - arrays.get(j).get(0)));
                max = Math.max(temp, max);
            }
        }
        return max;
    }

    public int maxDistance(List<List<Integer>> arrays) {
        List<Integer> list = arrays.get(0);
        int max = Integer.MIN_VALUE;
        int left = list.get(0);
        int right = list.get(list.size() - 1);
        for (int i = 1; i < arrays.size(); i++) {
            max = Math.max(max, Math.max(Math.abs(left - arrays.get(i).get(arrays.get(i).size() - 1)),
                    Math.abs(right - arrays.get(i).get(0))));
            left = Math.min(left, arrays.get(i).get(0));
            right = Math.max(right, arrays.get(i).get(arrays.get(i).size() - 1));

        }
        return max;
    }

    public static void main(String[] args) {
        List<List<Integer>> arr = new ArrayList<>();

        arr.add(Arrays.asList(1,2,3));
        arr.add(Arrays.asList(4,5));
        arr.add(Arrays.asList(1,2,3));
        maxDistance m = new maxDistance();
        System.out.println(m.maxDistance(arr));
    }
}
