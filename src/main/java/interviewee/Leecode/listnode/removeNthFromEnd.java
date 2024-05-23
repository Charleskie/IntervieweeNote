package interviewee.Leecode.listnode;

import java.util.Stack;

public class removeNthFromEnd {

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
