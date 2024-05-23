package interviewee.Leecode.dp;

import java.util.ArrayList;
import java.util.List;

import static interviewee.Algorithm.quicksort.quickSort;

public class UniquePaths {

    public int uniquePaths(int m, int n) {
        int[][] g = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if(i==0 && j ==0){
                    continue;
                }else if(i==0 || j==0){
                    g[i][j] = 1;
                }else {
                    g[i][j] = g[i-1][j] + g[i][j-1];
                }
            }
        }
        return g[m-1][n-1];
    }

    public int minPathSum(int[][] grid) {
        if(grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if(i==0 && j ==0){
                    continue;
                }else if(i==0){
                    grid[i][j] = grid[i][j] + grid[i][j-1];
                }else if(j == 0){
                    grid[i][j] = grid[i][j] + grid[i-1][j];
                }
                else {
                    grid[i][j] = Math.min(grid[i][j] + grid[i][j-1],
                            grid[i][j] + grid[i-1][j]);
                }
            }
        }
        return grid[grid.length - 1][grid[0].length-1];
    }

    public int[][] merge(int[][] intervals) {
        quickSort(intervals, 0, intervals.length-1);
        List<int[]> cp = new ArrayList<>();
        for(int i=0; i<=intervals.length-1; i++){
            int L = intervals[i][0], R = intervals[i][1];
            if(cp.size() == 0 || cp.get(cp.size()-1)[1] < L){
                cp.add(new int[]{L, R});
            }else{
                cp.get(cp.size()-1)[1] =Math.max(cp.get(cp.size()-1)[1], R);
            }

        }
        return cp.toArray(new int[cp.size()][]);
    }

    public static void main(String[] args) {
        UniquePaths u = new UniquePaths();
        System.out.println(u.uniquePaths(7,3));
    }
}
