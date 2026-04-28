package interviewee.Leecode.listnode;

// Definition for singly-linked list.
public class Node {
    public int val;
    public Node pre;
    public Node next;

    /**
     * 创建一个默认值的双向链表节点。
     */
    public Node() {}

    /**
     * 创建指定值的双向链表节点。
     *
     * @param val 节点值
     */
    public Node(int val) { this.val = val; }

    /**
     * 创建指定值和后继节点的双向链表节点。
     *
     * @param val  节点值
     * @param next 后继节点
     */
    Node(int val, Node next) { this.val = val; this.next = next; }

    /**
     * 创建指定值、前驱节点和后继节点的双向链表节点。
     *
     * @param val  节点值
     * @param pre  前驱节点
     * @param next 后继节点
     */
    Node(int val, Node pre, Node next) { this.val = val; this.pre = pre; this.next = next; }
}
