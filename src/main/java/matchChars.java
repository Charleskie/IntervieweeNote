import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class matchChars {
    /**
     * 从 A 中筛出能够覆盖 B 中每个字符串字符需求的单词。
     *
     * @param A 候选单词数组
     * @param B 约束单词数组
     * @return 满足所有约束的候选单词列表
     */
    public static List<String> sout(String[] A, String[] B){
        List<String> out = new ArrayList<>();
        for (int i = 0; i < A.length; i++) {
            String a = A[i];
            boolean flag = false;
            for (int j = 0; j < B.length; j++) {
                String b = B[j];
                char[] bc = b.toCharArray();
                flag = containsAB(a, bc);
                if(!flag){
                    break;
                }
            }
            if(flag){
                out.add(a);
            }
        }
        return out;
    }

    /**
     * 判断字符串 a 中是否包含字符数组 bc 所要求的字符数量。
     *
     * @param a  候选字符串
     * @param bc 需要覆盖的字符数组
     * @return 当前实现只要某个字符数量满足就返回 true
     */
    public static boolean containsAB(String a, char[] bc){
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < bc.length; i++) {
            int index = map.getOrDefault(bc[i], 0);
            index += 1;
            map.put(bc[i], index);
        }
        for (int i = 0; i < bc.length; i++) {
            int tem = 0;
            for(int j = 0; j < a.length(); j++){
                if(a.charAt(j) == bc[i]){
                    tem ++;
                }
            }
            if(tem >= map.get(bc[i])){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String[] A = {"amazon","apple","facebook","google"};
        String[] B = {"le","oo"};
        sout(A, B).forEach(s -> System.out.println(s));
    }
}
