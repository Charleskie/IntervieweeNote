package interviewee.Leecode.listnode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListNode2Array {
    public int[] toArray(Node node) {
        List<Integer> list = new ArrayList<>();
        dp(node, list);
        Node next = node.next;
        while (next != null) {
            list.add(next.val);
            next = next.next;
        }
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            res[i] = list.get(i);
        }
        return res;
    }

    public void dp(Node node, List<Integer> pre) {
        if(node.pre != null){
            dp(node.pre, pre);
        }
        pre.add(node.val);
    }

    public static void main(String[] args) {
        ListNode2Array listNode2Array = new ListNode2Array();
        Node node = new Node(1);
        node.next = new Node(2);
        int[] arr = listNode2Array.toArray(node);
        System.out.println(Arrays.toString(arr));
    }
}
