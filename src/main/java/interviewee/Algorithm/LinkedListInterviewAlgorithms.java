package interviewee.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 面试中常见的链表算法集合。
 *
 * 约定：方法默认复用原链表节点，只在题目语义需要时创建新节点。
 */
public final class LinkedListInterviewAlgorithms {

    /**
     * 单链表节点定义，便于本类中的链表算法独立运行。
     */
    public static final class ListNode {
        public int val;
        public ListNode next;

        /**
         * 创建指定值的节点。
         *
         * @param val 节点值
         */
        public ListNode(int val) {
            this.val = val;
        }

        /**
         * 创建指定值和后继节点的节点。
         *
         * @param val  节点值
         * @param next 后继节点
         */
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    /**
     * 工具类不需要实例化。
     */
    private LinkedListInterviewAlgorithms() {
    }

    /**
     * 反转整个链表。
     *
     * @param head 原链表头节点
     * @return 反转后的新头节点
     */
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    /**
     * 反转链表中 [left, right] 位置的节点，位置从 1 开始。
     *
     * @param head  原链表头节点
     * @param left  反转区间左端
     * @param right 反转区间右端
     * @return 局部反转后的链表头节点
     */
    public static ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || left >= right) {
            return head;
        }
        ListNode dummy = new ListNode(0, head);
        ListNode prev = dummy;
        for (int i = 1; i < left; i++) {
            prev = prev.next;
        }
        ListNode cur = prev.next;
        for (int i = 0; i < right - left; i++) {
            ListNode next = cur.next;
            cur.next = next.next;
            next.next = prev.next;
            prev.next = next;
        }
        return dummy.next;
    }

    /**
     * 合并两个升序链表。
     *
     * @param l1 第一个升序链表
     * @param l2 第二个升序链表
     * @return 合并后的升序链表
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }
        tail.next = l1 != null ? l1 : l2;
        return dummy.next;
    }

    /**
     * 两两交换链表中的相邻节点。
     *
     * @param head 原链表头节点
     * @return 交换后的链表头节点
     */
    public static ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode prev = dummy;
        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = first.next;
            first.next = second.next;
            second.next = first;
            prev.next = second;
            prev = first;
        }
        return dummy.next;
    }

    /**
     * 删除链表倒数第 n 个节点。
     *
     * @param head 原链表头节点
     * @param n    倒数位置，从 1 开始
     * @return 删除后的链表头节点
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (n <= 0) {
            return head;
        }
        ListNode dummy = new ListNode(0, head);
        ListNode fast = dummy;
        ListNode slow = dummy;
        for (int i = 0; i < n && fast != null; i++) {
            fast = fast.next;
        }
        if (fast == null) {
            return head;
        }
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return dummy.next;
    }

    /**
     * 返回链表中间节点，偶数长度时返回第二个中间节点。
     *
     * @param head 链表头节点
     * @return 中间节点
     */
    public static ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 查找链表倒数第 k 个节点。
     *
     * @param head 链表头节点
     * @param k    倒数位置，从 1 开始
     * @return 倒数第 k 个节点；不存在时返回 null
     */
    public static ListNode kthFromEnd(ListNode head, int k) {
        if (k <= 0) {
            return null;
        }
        ListNode fast = head;
        ListNode slow = head;
        for (int i = 0; i < k; i++) {
            if (fast == null) {
                return null;
            }
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * 判断链表是否存在环。
     *
     * @param head 链表头节点
     * @return 有环时返回 true
     */
    public static boolean hasCycle(ListNode head) {
        return detectCycle(head) != null;
    }

    /**
     * 检测链表入环节点。
     *
     * @param head 链表头节点
     * @return 存在环时返回入环节点，否则返回 null
     */
    public static ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                ListNode cur = head;
                while (cur != slow) {
                    cur = cur.next;
                    slow = slow.next;
                }
                return cur;
            }
        }
        return null;
    }

    /**
     * 查找两个链表的第一个相交节点。
     *
     * @param headA 第一个链表头节点
     * @param headB 第二个链表头节点
     * @return 相交节点；不相交时返回 null
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode p = headA;
        ListNode q = headB;
        while (p != q) {
            p = p == null ? headB : p.next;
            q = q == null ? headA : q.next;
        }
        return p;
    }

    /**
     * 判断链表是否为回文链表。
     *
     * @param head 链表头节点
     * @return 正反读取一致时返回 true
     */
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        ListNode firstHalfEnd = endOfFirstHalf(head);
        ListNode secondHalfStart = reverseList(firstHalfEnd.next);
        ListNode p1 = head;
        ListNode p2 = secondHalfStart;
        boolean result = true;
        while (result && p2 != null) {
            if (p1.val != p2.val) {
                result = false;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        firstHalfEnd.next = reverseList(secondHalfStart);
        return result;
    }

    /**
     * 两数相加：链表按低位到高位保存数字。
     *
     * @param l1 第一个数字链表
     * @param l2 第二个数字链表
     * @return 表示相加结果的链表
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            tail.next = new ListNode(sum % 10);
            tail = tail.next;
            carry = sum / 10;
        }
        return dummy.next;
    }

    /**
     * 重排链表为 L0 -> Ln -> L1 -> Ln-1 的顺序。
     *
     * @param head 链表头节点
     */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }
        ListNode firstHalfEnd = endOfFirstHalf(head);
        ListNode second = reverseList(firstHalfEnd.next);
        firstHalfEnd.next = null;
        ListNode first = head;
        while (second != null) {
            ListNode nextFirst = first.next;
            ListNode nextSecond = second.next;
            first.next = second;
            second.next = nextFirst;
            first = nextFirst;
            second = nextSecond;
        }
    }

    /**
     * 使用归并排序对链表按节点值升序排序。
     *
     * @param head 原链表头节点
     * @return 排序后的链表头节点
     */
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode mid = splitMiddle(head);
        ListNode right = mid.next;
        mid.next = null;
        return mergeTwoLists(sortList(head), sortList(right));
    }

    /**
     * 删除升序链表中的重复节点，每个值只保留一个。
     *
     * @param head 升序链表头节点
     * @return 去重后的链表头节点
     */
    public static ListNode deleteDuplicates(ListNode head) {
        ListNode cur = head;
        while (cur != null && cur.next != null) {
            if (cur.val == cur.next.val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

    /**
     * 删除升序链表中所有重复出现的值，只保留完全不重复的节点。
     *
     * @param head 升序链表头节点
     * @return 删除重复值后的链表头节点
     */
    public static ListNode deleteAllDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode prev = dummy;
        while (prev.next != null && prev.next.next != null) {
            if (prev.next.val == prev.next.next.val) {
                int value = prev.next.val;
                while (prev.next != null && prev.next.val == value) {
                    prev.next = prev.next.next;
                }
            } else {
                prev = prev.next;
            }
        }
        return dummy.next;
    }

    /**
     * 找到链表前半段的尾节点。
     *
     * @param head 链表头节点
     * @return 前半段尾节点
     */
    private static ListNode endOfFirstHalf(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 将链表从中间位置断开前，返回左半段尾节点。
     *
     * @param head 链表头节点
     * @return 左半段尾节点
     */
    private static ListNode splitMiddle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 根据数组构造链表。
     *
     * @param values 节点值数组
     * @return 链表头节点
     */
    private static ListNode listOf(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }
        return dummy.next;
    }

    /**
     * 将链表转成数组，便于测试断言。
     *
     * @param head 链表头节点
     * @return 节点值数组
     */
    private static int[] toArray(ListNode head) {
        List<Integer> values = new ArrayList<>();
        while (head != null) {
            values.add(head.val);
            head = head.next;
        }
        int[] result = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    /**
     * 断言链表结果等于期望数组。
     *
     * @param name     用例名称
     * @param actual   实际链表
     * @param expected 期望数组
     */
    private static void assertListEquals(String name, ListNode actual, int[] expected) {
        int[] actualArray = toArray(actual);
        if (!Arrays.equals(actualArray, expected)) {
            throw new IllegalStateException(name + " 结果错误: " + Arrays.toString(actualArray));
        }
    }

    /**
     * 断言 int 结果相等。
     *
     * @param name     用例名称
     * @param actual   实际结果
     * @param expected 期望结果
     */
    private static void assertEquals(String name, int actual, int expected) {
        if (actual != expected) {
            throw new IllegalStateException(name + " 结果错误: " + actual + ", expected: " + expected);
        }
    }

    /**
     * 断言 boolean 结果相等。
     *
     * @param name     用例名称
     * @param actual   实际结果
     * @param expected 期望结果
     */
    private static void assertEquals(String name, boolean actual, boolean expected) {
        if (actual != expected) {
            throw new IllegalStateException(name + " 结果错误: " + actual + ", expected: " + expected);
        }
    }

    public static void main(String[] args) {
        assertListEquals("reverseList", reverseList(listOf(1, 2, 3, 4)), new int[]{4, 3, 2, 1});
        assertListEquals("reverseBetween", reverseBetween(listOf(1, 2, 3, 4, 5), 2, 4), new int[]{1, 4, 3, 2, 5});
        assertListEquals("mergeTwoLists", mergeTwoLists(listOf(1, 2, 4), listOf(1, 3, 4)), new int[]{1, 1, 2, 3, 4, 4});
        assertListEquals("swapPairs", swapPairs(listOf(1, 2, 3, 4)), new int[]{2, 1, 4, 3});
        assertListEquals("removeNthFromEnd", removeNthFromEnd(listOf(1, 2, 3, 4, 5), 2), new int[]{1, 2, 3, 5});
        assertEquals("middleNode", middleNode(listOf(1, 2, 3, 4, 5, 6)).val, 4);
        assertEquals("kthFromEnd", kthFromEnd(listOf(1, 2, 3, 4, 5), 2).val, 4);

        ListNode cycle = listOf(3, 2, 0, -4);
        cycle.next.next.next.next = cycle.next;
        assertEquals("hasCycle", hasCycle(cycle), true);
        assertEquals("detectCycle", detectCycle(cycle).val, 2);

        ListNode common = listOf(8, 4, 5);
        ListNode a = new ListNode(4, new ListNode(1, common));
        ListNode b = new ListNode(5, new ListNode(6, new ListNode(1, common)));
        assertEquals("getIntersectionNode", getIntersectionNode(a, b).val, 8);

        assertEquals("isPalindrome", isPalindrome(listOf(1, 2, 2, 1)), true);
        assertListEquals("addTwoNumbers", addTwoNumbers(listOf(2, 4, 3), listOf(5, 6, 4)), new int[]{7, 0, 8});
        ListNode reorder = listOf(1, 2, 3, 4, 5);
        reorderList(reorder);
        assertListEquals("reorderList", reorder, new int[]{1, 5, 2, 4, 3});
        assertListEquals("sortList", sortList(listOf(4, 2, 1, 3)), new int[]{1, 2, 3, 4});
        assertListEquals("deleteDuplicates", deleteDuplicates(listOf(1, 1, 2, 3, 3)), new int[]{1, 2, 3});
        assertListEquals("deleteAllDuplicates", deleteAllDuplicates(listOf(1, 1, 2, 3, 3)), new int[]{2});

        System.out.println("All linked list algorithms passed.");
    }
}
