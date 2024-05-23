package interviewee.Leecode.string;

public class Permutation {
    /***
     * 无重复字符串的排列组合。编写一种方法，计算某字符串的所有排列组合，字符串每个字符均不相同。
     */
    public void solution(String str){
        char[] ch = str.toCharArray();

    }

    public void permutation(char[] arr, int index){

    }

    public void permutate(char[] arr, int first) {
        if (first == arr.length - 1) {
            System.out.println(new String(arr));
            return;
        }
        for (int i = first; i < arr.length; i++) {
            swap(arr, first, i);
            permutate(arr, first + 1);
            swap(arr, first, i);
        }
    }

    public void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        String s = "abc";
        Permutation p = new Permutation();
        p.permutate(s.toCharArray(), 0);
    }

}
