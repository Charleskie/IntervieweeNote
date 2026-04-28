package interviewee.Leecode.listnode;

import java.util.Stack;

public class removeNthFromEnd {

    /**
     * 删除链表倒数第 n 个节点，思路是用栈回溯定位待删除节点的前驱。
     *
     * @param head 链表头节点
     * @param n    倒数位置，从 1 开始
     * @return 删除目标节点后的链表头节点
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode pre = new ListNode(0);
        pre.next = head;
        Stack<ListNode> stack = new Stack<>();
        ListNode cur = pre.next;
        while (cur != null){
            stack.push(cur);
        }
        for (int i = 0; i < n; i++) {
            stack.pop();
        }
        ListNode nPre = stack.peek();
        nPre.next = nPre.next.next;
        return pre.next;
    }
}
