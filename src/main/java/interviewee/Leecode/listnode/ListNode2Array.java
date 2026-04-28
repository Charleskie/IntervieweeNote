package interviewee.Leecode.listnode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListNode2Array {
    /**
     * 将带前驱和后继指针的节点链转换为数组，先回溯前驱链，再顺着后继链补齐。
     *
     * @param node 链表中的任意节点
     * @return 从最前节点到最后节点的值数组
     */
    public int[] toArray(Node node) {
        List<Integer> list = new ArrayList<>();
        dp(node, list);
        Node next = node.next;
        while (next != null) {
            list.add(next.val);
            next = next.next;
        }
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    /**
     * 递归收集当前节点之前的所有前驱节点值。
     *
     * @param node 当前节点
     * @param pre  用于承接前驱节点值的列表
     */
    public void dp(Node node, List<Integer> pre) {
        if(node.pre != null){
            dp(node.pre, pre);
        }
        pre.add(node.val);
    }

    public static void main(String[] args) {
        ListNode2Array listNode2Array = new ListNode2Array();
        Node node = new Node(1);
        node.next = new Node(2);
        int[] arr = listNode2Array.toArray(node);
        System.out.println(Arrays.toString(arr));
    }
}
