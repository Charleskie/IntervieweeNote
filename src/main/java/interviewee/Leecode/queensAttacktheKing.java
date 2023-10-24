package interviewee.Leecode;

import org.apache.storm.guava.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class queensAttacktheKing {

    //8个方向的向量
    public static final int[][] vector = new int[][]{{1,0}, {0,1}, {1,1}, {-1, 0}, {0, -1}, {-1, -1}, {1, -1}, {-1, 1}};
    public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {
        boolean[][] isQueen = new boolean[8][8];
        for(int[] q: queens){
            isQueen[q[0]][q[1]] = true;
        }
        List<List<Integer>> res = new ArrayList<>();
        for(int[] d: vector){
            int x = king[0]+d[0];
            int y = king[1]+d[1];
            while(x>=0 && x < 8 && y >= 0 && y<8){
                if(isQueen[x][y]){
                    res.add(Arrays.asList(x, y));
                    break;
                }
                x += d[0];
                y += d[1];
            }
        }
        return res;
    }
}
