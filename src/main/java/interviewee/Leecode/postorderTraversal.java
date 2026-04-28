package interviewee.Leecode;

import interviewee.Leecode.TreeNode.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class postorderTraversal {
    /**
     * 使用栈尝试收集二叉树后序遍历结果。
     *
     * @param root 二叉树根节点
     * @return 后序遍历节点值列表
     */
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

    /**
     * 将当前节点的左右孩子压入栈中。
     *
     * @param node  当前节点
     * @param stack 承接待访问节点的栈
     */
    private static void push(TreeNode node, Stack<TreeNode> stack){
        if(node.left != null){
            stack.push(node.left);
        }
        if(node.right != null){
            stack.push(node.right);
        }
    }
}
