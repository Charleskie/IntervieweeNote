package interviewee.Leecode.listnode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class sortlist {
    public ListNode sortList(ListNode head) {
        if(head == null) return head;
        ListNode node = head;
        List<Integer> a = new ArrayList();
        while(node != null){
            a.add(node.val);
            node = node.next;
        }
        int[] b = new int[a.size()];
        for(int i=0; i<a.size(); i++){
            b[i] = a.get(i);
        }
        Arrays.sort(b);
        ListNode c = new ListNode(0);
        ListNode p = c;
        for (int i = 0; i < b.length ; i++) {
            p.next = new ListNode(b[i]);
            p = p.next;
        }
        return c.next;
    }


    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(3);
        head.next.next = new ListNode(2);
        sortlist s = new sortlist();
        head = s.sortList(head);
    }
}
