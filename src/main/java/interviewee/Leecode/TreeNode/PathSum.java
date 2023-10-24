package interviewee.Leecode.TreeNode;

public class PathSum {

    /***
     * 二叉树里节点值之和等于 targetSum 的 路径 的数目
     * @param root
     * @param targetSum
     * @return
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
