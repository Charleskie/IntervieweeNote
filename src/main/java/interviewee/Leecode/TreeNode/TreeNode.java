package interviewee.Leecode.TreeNode;

public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    /**
     * 创建默认二叉树节点。
     */
    TreeNode() {}

    /**
     * 创建指定值的二叉树节点。
     *
     * @param val 节点值
     */
    TreeNode(int val) { this.val = val; }

    /**
     * 创建指定值和左右孩子的二叉树节点。
     *
     * @param val   节点值
     * @param left  左子节点
     * @param right 右子节点
     */
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
