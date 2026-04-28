package interviewee.Leecode.listnode;

public class deleteDuplicates {

    /**
     * 删除升序链表中所有重复出现的节点，只保留原链表中没有重复的值。
     *
     * @param head 升序链表头节点
     * @return 去重后的链表头节点
     */
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) {
            return head;
        }

        ListNode dummy = new ListNode(0, head);

        ListNode cur = dummy;
        while (cur.next != null && cur.next.next != null) {
            if (cur.next.val == cur.next.next.val) {
                int x = cur.next.val;
                while (cur.next != null && cur.next.val == x) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }

        return dummy.next;
    }


    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        node.next = new ListNode(1);
        node.next.next = new ListNode(1);
        node.next.next.next = new ListNode(2);
        node.next.next.next.next = new ListNode(3);
        node.next.next.next.next.next = new ListNode(3);
        deleteDuplicates deleteDuplicates = new deleteDuplicates();
        deleteDuplicates.deleteDuplicates(node);
    }
}
