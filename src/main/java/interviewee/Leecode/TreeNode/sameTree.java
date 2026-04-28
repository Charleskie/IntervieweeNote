package interviewee.Leecode.TreeNode;

public class sameTree {

    /**
     * 递归判断两棵二叉树的结构和值是否完全相同。
     *
     * @param p 第一棵树根节点
     * @param q 第二棵树根节点
     * @return 结构和值都相同时返回 true
     */
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if(p == null && q == null){
            return true;
        }
        if(p == null || q == null){
            return false;
        }

        return p.val==q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    public static void main(String[] args) {
        TreeNode a = new TreeNode(1);
        a.left = new TreeNode(2);
        a.right = new TreeNode(3);
        TreeNode b = new TreeNode(1);
        b.left = new TreeNode(2);
        b.right = new TreeNode(3);
        sameTree sameTree = new sameTree();
        System.out.println(sameTree.isSameTree(a, b));
    }
}
