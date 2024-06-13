import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class matchChars {
    //A 字符串数组
    //B 字符串数组
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
