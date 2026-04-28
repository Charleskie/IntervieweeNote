package interviewee.Leecode.listnode;

public class detectCycle {
    /**
     * 使用快慢指针检测链表是否有环，并在相遇后定位入环节点。
     *
     * @param head 链表头节点
     * @return 存在环时返回入环节点，否则返回 null
     */
    public static ListNode detectCycle(ListNode head) {
        ListNode node = head;
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null){
            slow = slow.next;
            if(fast.next != null){
                fast = fast.next.next;
            }else {
                return null;
            }
            if(fast == slow){
                while (node != slow){
                    node = node.next;
                    slow = slow.next;
                }
                return node;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);
        node.next.next.next.next = new ListNode(5);
        node.next.next.next.next.next = new ListNode(6);
        node.next.next.next.next.next.next = new ListNode(7);
        node.next.next.next.next.next.next.next = node.next.next;
        System.out.println(detectCycle(node).val);
    }
}
