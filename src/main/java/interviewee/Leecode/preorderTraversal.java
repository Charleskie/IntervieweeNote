package interviewee.Leecode;

import interviewee.Leecode.TreeNode.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/***
 * 二叉树前序遍历
 */
public class preorderTraversal {
    List<Integer> preorder = new ArrayList<>();
    public List<Integer> preorderTraversal(TreeNode root) {
        if(root == null){
            return preorder;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        if(!stack.isEmpty()){
            TreeNode node = stack.pop();
            preorder.add(node.val);
            if(node.left != null){
                stack.push(node.left);
            }
            if(node.right != null){
                stack.push(node.left);
            }
        }
        return preorder;
    }

    /**
     * 二叉树前序遍历   根-> 左-> 右
     * @param node    二叉树节点
     */
    public static void preOrderTraveral(TreeNode node){
        if(node == null){
            return;
        }
        System.out.print(node.val+" ");
        preOrderTraveral(node.left);
        preOrderTraveral(node.right);
    }

    /**
     * 二叉树中序遍历   左-> 根-> 右
     * @param node   二叉树节点
     */
    public static void inOrderTraveral(TreeNode node){
        if(node == null){
            return;
        }
        inOrderTraveral(node.left);
        System.out.print(node.val+" ");
        inOrderTraveral(node.right);
    }

    /**
     * 二叉树后序遍历   左-> 右-> 根
     * @param node    二叉树节点
     */
    public static void postOrderTraveral(TreeNode node){
        if(node == null){
            return;
        }
        postOrderTraveral(node.left);
        postOrderTraveral(node.right);
        System.out.print(node.val+" ");
    }

    /***
     * 层序遍历
     */
    public static void levelOrder(TreeNode root){
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            root = queue.pop();
            System.out.print(root.val+" ");
            if(root.left!=null) queue.add(root.left);
            if(root.right!=null) queue.add(root.right);
        }
    }


}
