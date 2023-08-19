package interviewee.Leecode.listnode;

public class RevertListNode {

    public ListNode reverseList(ListNode head) {
        ListNode node = null;
        ListNode next = head;
        while(head.next != null){
            ListNode pre = head.next;
            pre.next = node;
            pre = 
            head = head.next;
        }
        return node;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        RevertListNode v = new RevertListNode();
        v.reverseList(head);
    }
}
