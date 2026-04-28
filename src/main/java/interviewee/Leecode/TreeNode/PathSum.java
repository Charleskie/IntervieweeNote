package interviewee.Leecode.TreeNode;

public class PathSum {

    /***
     * 二叉树里节点值之和等于 targetSum 的 路径 的数目
     *
     * @param root      二叉树根节点
     * @param targetSum 目标路径和
     * @return 路径和等于 targetSum 的路径数量
     */
    public int pathSum(TreeNode root, int targetSum) {
        int sum = 0;
        if(root == null){
            return 0;
        }
        sum = sum(root, targetSum);
        sum += sum(root.left, targetSum);
        sum += sum(root.right, targetSum);
        return sum;
    }

    /**
     * 统计从当前节点出发、向下延伸且路径和等于 targetSum 的路径数量。
     *
     * @param root      当前路径起点
     * @param targetSum 剩余目标和
     * @return 从当前节点出发的有效路径数量
     */
    public int sum(TreeNode root, int targetSum){
        if(root == null){
            return 0;
        }
        int sum = 0;
        if(root.val == targetSum){
            sum ++;
        }
        sum += sum(root.left, targetSum - root.val);
        sum += sum(root.right, targetSum - root.val);
        return sum;
    }
}
