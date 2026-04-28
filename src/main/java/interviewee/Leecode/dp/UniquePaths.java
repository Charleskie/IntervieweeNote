package interviewee.Leecode.dp;

import java.util.ArrayList;
import java.util.List;

import static interviewee.Algorithm.quicksort.quickSort;

public class UniquePaths {

    /**
     * 计算 m x n 网格中从左上角到右下角的不同路径数，只允许向右或向下移动。
     *
     * @param m 行数
     * @param n 列数
     * @return 不同路径总数
     */
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

    /**
     * 计算从左上角到右下角的最小路径和，直接在 grid 上累加动态规划结果。
     *
     * @param grid 非负整数网格
     * @return 最小路径和
     */
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

    /**
     * 合并重叠区间，复用快排先按区间起点排序。
     *
     * @param intervals 区间数组
     * @return 合并后的区间数组
     */
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
