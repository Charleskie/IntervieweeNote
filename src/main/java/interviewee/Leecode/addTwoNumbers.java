package interviewee.Leecode;

public class addTwoNumbers {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(0);
        ListNode res = result;
        while(l1 != null || l2 != null || res.val != 0){
            int value = 0;
            if(l1 != null){
                value = l1.val;
            }
            if(l2 != null){
                value += l2.val;
            }
            calVal(value, res);
            res = res.next;
            l1 = l1.next;
            l2 = l2.next;
        }
        if(res.val == 0 ){
            res = null;
        }
        return result;
    }

    public static void calVal(int value, ListNode res){
        ListNode next = new ListNode();
        if(value>9){
            next.val = 1;
            res.val = value - 10;
        } else {
            res.val += value;
        }
        res.next = next;
    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(8);
        l1.next = new ListNode(2);
        l1.next.next = new ListNode(3);
        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(2);
        l2.next.next = new ListNode(3);
        ListNode val = addTwoNumbers(l1, l2);
        while (val != null) {
            System.out.print(val.val + " -->");
            val = val.next;
        }
    }
}
