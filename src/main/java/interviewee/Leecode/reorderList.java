package interviewee.Leecode;

/***
 * 给定一个单链表 L 的头节点 head ，单链表 L 表示为：
 *
 * L0 → L1 → … → Ln - 1 → Ln
 * 请将其重新排列后变为：
 *
 * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
 * 不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/reorder-list
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class reorderList {
    public static void reorderList(ListNode head) {
        if(head == null || head.next == null){
            return;
        }
        ListNode tem = head;
        while(tem.next.next != null){
            tem = tem.next;
        }
        ListNode p = tem.next;
        tem.next = null;
        p.next = head.next;
        head.next = p;
        reorderList(p.next);
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);
        reorderList(node);
        int[] n = new int[5];
    while (node != null){
            System.out.print(node.val + "-->");
            System.out.print(8/10);
            System.out.print(1%10);
            System.out.print(176/100);
            node = node.next;
        }
    }
}
