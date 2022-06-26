package interviewee.Leecode;

import java.util.ArrayList;
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
}
