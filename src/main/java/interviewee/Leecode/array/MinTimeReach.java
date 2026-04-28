package interviewee.Leecode.array;

public class MinTimeReach {
    /**
     * 计算到达右下角的最短时间；当前方法体仍是待补全草稿。
     *
     * @param moveTime 每个格子的最早可进入时间
     * @return 当前草稿固定返回 1
     */
    public int minTimeToReach(int[][] moveTime) {
        int time = moveTime[0][0];
        for (int i = 0; i < moveTime.length; i++) {
            for (int j = 0; j < moveTime[i].length; j++) {
                if (moveTime[i][j] <= time) {

                }else {
//                    time
                }
            }
        }
        return 1;
    }

    /**
     * 递归/动态规划辅助入口；当前方法体仍是待补全草稿。
     *
     * @param i    当前行
     * @param j    当前列
     * @param grid 网格数据
     * @return 当前草稿固定返回 1
     */
    public int dp(int i, int j, int[][] grid) {
        return 1;
    }
}
