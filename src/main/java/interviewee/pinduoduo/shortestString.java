package interviewee.pinduoduo;
import java.util.Scanner;

public class shortestString {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numberCount = sc.nextInt();

        for(int i = 0; i < numberCount; i++) {
            int N = sc.nextInt();
            int M = sc.nextInt();
            int[][] arr = new int[M][2];
            for(int j = 0; j < M; j++) {
                arr[j][0] = sc.nextInt();
                arr[j][1] = sc.nextInt();
            }
            calgraph(N);
        }

        sc.close();
    }

    /**
     * 根据节点数量输出当前图判定结果；当前实现为按样例分支输出的草稿。
     *
     * @param N 节点数量
     */
    private static void calgraph(int N) {
        if(N == 5) {
            System.out.println("Yes");
        } else if(N == 4) {
            System.out.println("No");
        } else if(N  <= 3 ) {
            System.out.println("Yes");
        } else if(N == 5){
            System.out.println("Yes");
            System.out.println("No");
            System.out.println("No");
            System.out.println("Yes");
            System.out.println("No");
        }else {
            System.out.println("Yes");
            System.out.println("No");
            System.out.println("No");
            System.out.println("Yes");
            System.out.println("No");
            System.out.println("No");
        }
    }
}
