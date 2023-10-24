package interviewee.Leecode;

public class minCount {

    public int minCount(int[] coins) {
        int sum = 0;
        for(int c: coins){
            sum += (c % 2 + c/2);
        }
        return sum;
    }
}
