package interviewee.Leecode;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class postorderTraversal {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if(root == null){
            return list;
        }
        Stack<TreeNode> stack = new Stack<>();
        push(root.left, stack);
        TreeNode node = stack.pop();
        list.add(node.val);
        push(root.right, stack);
        node = stack.pop();
        list.add(node.val);
        return list;

    }

    private static void push(TreeNode node, Stack<TreeNode> stack){
        if(node.left != null){
            stack.push(node.left);
        }
        if(node.right != null){
            stack.push(node.right);
        }
    }
}
