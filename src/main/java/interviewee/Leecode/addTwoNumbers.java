package interviewee.Leecode;

import interviewee.Leecode.listnode.ListNode;

public class addTwoNumbers {
    /**
     * 两数相加：两个链表按低位到高位存储数字，逐位相加并处理进位。
     *
     * @param l1 第一个数字链表
     * @param l2 第二个数字链表
     * @return 表示相加结果的链表
     */
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

    /**
     * 将当前位的加和写入结果节点，并在需要时把进位写入下一节点。
     *
     * @param value 当前位两个数字节点的加和
     * @param res   当前结果节点
     */
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
