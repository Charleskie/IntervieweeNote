package interviewee.iqiyi;

import java.util.Scanner;

public class food {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        int P = sc.nextInt();
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = sc.nextInt();
        }
        for (int i = 0; i < M; i++) {
            String s = sc.next();
            int inedx = sc.nextInt();
            if (s.equals("A")) {
                arr[inedx - 1] += 1;
            } else if (s.equals("B")) {
                arr[inedx - 1] -= 1;
            }
        }

        int sum = 1;
        for(int i = 0; i < N; i++){
            if(arr[P-1] < arr[i]){
                sum++;
            }
        }
        System.out.print(sum);

    }
}
