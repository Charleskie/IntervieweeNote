package interviewee.Leecode.listnode;

// Definition for singly-linked list.
public class Node {
    public int val;
    public Node pre;
    public Node next;
    public Node() {}
    public Node(int val) { this.val = val; }
    Node(int val, Node next) { this.val = val; this.next = next; }
    Node(int val, Node pre, Node next) { this.val = val; this.pre = pre; this.next = next; }
}

