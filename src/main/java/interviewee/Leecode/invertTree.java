package interviewee.Leecode;

import interviewee.Leecode.TreeNode.TreeNode;

public class invertTree {

    /**
     * 递归翻转二叉树，交换每个节点的左右子树。
     *
     * @param root 二叉树根节点
     * @return 翻转后的根节点
     */
    public TreeNode invertTree(TreeNode root) {
        if(root == null){
            return null;
        }
        TreeNode temp = root.right;
        root.right = root.left;
        root.left = temp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }
}
