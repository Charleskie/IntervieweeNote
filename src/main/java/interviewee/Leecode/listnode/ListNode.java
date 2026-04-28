package interviewee.Leecode.listnode;

// Definition for singly-linked list.
public class ListNode {
    public int val;
    public ListNode next;

    /**
     * 创建一个默认值节点。
     */
    public ListNode() {}

    /**
     * 创建指定值的链表节点。
     *
     * @param val 节点值
     */
    public ListNode(int val) { this.val = val; }

    /**
     * 创建指定值和后继节点的链表节点。
     *
     * @param val  节点值
     * @param next 后继节点
     */
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
