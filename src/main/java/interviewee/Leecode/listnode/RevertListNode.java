package interviewee.Leecode.listnode;

public class RevertListNode {

    /**
     * 迭代反转整个单链表。
     *
     * @param head 原链表头节点
     * @return 反转后的新头节点
     */
    public ListNode reverseList(ListNode head) {

        // 也可以使用递归反转一个链表
        ListNode pre = null;
        ListNode cur = head;

        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        RevertListNode v = new RevertListNode();
       // v.reverseList(head);
        //revert(head);
        v.reverseBetween(head, 2, 4);
    }


    /**
     * 静态版本的链表整体反转方法，逐个节点修改 next 指向。
     *
     * @param node 原链表头节点
     * @return 反转后的新头节点
     */
    public static ListNode revert(ListNode node){
        ListNode pre = null;
        ListNode cur = node;
        while (cur != null){
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    /**
     * 反转链表中从 left 到 right 的闭区间，使用头插法把区间内节点依次移到子链表头部。
     *
     * @param head  原链表头节点
     * @param left  反转区间左端位置，从 1 开始
     * @param right 反转区间右端位置，从 1 开始
     * @return 局部反转后的链表头节点
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {

        ListNode result = new ListNode(-1);
        result.next = head;
        ListNode pre = result;
        for (int i = 0; i < left - 1; i++) {
            pre = pre.next;
        }
        ListNode cur = pre.next;
        ListNode next;
        for (int i = 0; i < right - left; i++) {
            next = cur.next;
            cur.next = next.next;
            next.next = pre.next;
            pre.next = next;
        }
        return result.next;
    }
}
