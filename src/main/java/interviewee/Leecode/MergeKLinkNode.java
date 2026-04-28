package interviewee.Leecode;

import interviewee.Leecode.listnode.ListNode;

public class MergeKLinkNode {
    /**
     * 逐组合并 K 个升序链表。
     *
     * @param lists 升序链表头节点数组
     * @return 合并后的升序链表头节点
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        if(lists == null || lists.length == 0){
            return null;
        }
        if(lists.length == 1){
            return lists[0];
        }
        ListNode node = new ListNode(-1);
        for(int i=0; i<=lists.length -2; i+=2){
            node.next = merge(lists[i], lists[i+1]);
        }
        return node.next;

    }

    /**
     * 递归合并两个升序链表。
     *
     * @param l1 第一个升序链表
     * @param l2 第二个升序链表
     * @return 合并后的升序链表
     */
    public static ListNode merge(ListNode l1, ListNode l2){
        if(l1 == null) return l2;
        if(l2 == null) return l1;
        ListNode node = l1.val >= l2.val ? l2: l1;
        node.next = merge(node.next,  l1.val >= l2.val ? l1: l2);
        return node;
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(2);
        ListNode node2 = new ListNode(-1);
        ListNode[] lsit = new ListNode[]{node, null, node2};
        mergeKLists(lsit);
        String sd = "";
        sd.toCharArray();
    }
}
