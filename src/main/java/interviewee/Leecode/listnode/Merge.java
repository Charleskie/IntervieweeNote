package interviewee.Leecode.listnode;

public class Merge {

    /**
     * 递归合并两个升序链表，每次取当前较小节点作为结果链表的下一个节点。
     *
     * @param l1 第一个升序链表头节点
     * @param l2 第二个升序链表头节点
     * @return 合并后的升序链表头节点
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

}
