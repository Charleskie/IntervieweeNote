package interviewee.Leecode.listnode;

public class SwapPairs {

    /**
     * 递归交换链表中相邻的两个节点。
     *
     * @param head 链表头节点
     * @return 两两交换后的新头节点
     */
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = head.next;
        head.next = swapPairs(newHead.next);
        newHead.next = head;
        return newHead;
    }

}
