package interviewee.Leecode;

/****
 * 请实现一个 MyCalendar 类来存放你的日程安排。如果要添加的时间内没有其他安排，则可以存储这个新的日程安排。
 *
 * MyCalendar 有一个 book(int start, int end)方法。它意味着在 start 到 end 时间内增加一个日程安排，注意，这里的时间是半开区间，即 [start, end), 实数 x 的范围为，  start <= x < end。
 *
 * 当两个日程安排有一些时间上的交叉时（例如两个日程安排都在同一时间内），就会产生重复预订。
 *
 * 每次调用 MyCalendar.book方法时，如果可以将日程安排成功添加到日历中而不会导致重复预订，返回 true。否则，返回 false 并且不要将该日程安排添加到日历中。
 *
 * 请按照以下步骤调用 MyCalendar 类: MyCalendar cal = new MyCalendar(); MyCalendar.book(start, end)
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/fi9suh
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
class Node{
    int value;
    int lazy;
    Node leftchild;
    Node rightchild;
}
class MyCalendarV2 {
    private int N;
    private Node root;
    public MyCalendarV2() {
        N = (int)1e9;
        root = new Node();
    }

    public boolean book(int start, int end) {
        int dep = query(root, 0, N, start, end-1);
        if(dep>=1) return false;
        update(root, 0, N, start, end-1);
        return true;
    }

    private int query(Node root, int lr, int rr, int l, int r){
        if(l<=lr && rr<=r) return root.value;
        pushdown(root);
        int mid = (lr+rr)>>1, ans = 0;
        if(l<=mid)
            ans = query(root.leftchild, lr, mid, l, r);
        if(r>mid)
            ans = Math.max(ans, query(root.rightchild, mid+1, rr, l, r));
        return ans;
    }

    private void update(Node root, int lr, int rr, int l, int r){
        if(l<=lr && rr<=r){
            root.value++;
            root.lazy++;
            return;
        }
        pushdown(root);
        int mid = (lr+rr)>>1;
        if(l<=mid)
            update(root.leftchild, lr, mid, l, r);
        if(r>mid)
            update(root.rightchild, mid+1, rr, l, r);
        pushup(root);
    }

    private void pushup(Node root){
        root.value = Math.max(root.leftchild.value, root.rightchild.value);
    }

    private void pushdown(Node root){
        if(root.leftchild == null) root.leftchild = new Node();
        if(root.rightchild == null) root.rightchild = new Node();
        if(root.lazy == 0) return;
        root.leftchild.value += root.lazy;
        root.leftchild.lazy += root.lazy;
        root.rightchild.value += root.lazy;
        root.rightchild.lazy += root.lazy;
        root.lazy = 0;
    }

    public static void main(String[] args) {
        MyCalendarV2 calendar = new MyCalendarV2();
        calendar.book(1,23);
        System.out.println(calendar.book(20, 23));
        System.out.println(calendar.book(23, 32));
        System.out.println(calendar.book(56, 70));
        System.out.println(calendar.book(34, 36));
        System.out.println(calendar.book(55, 57));

    }
}

/**
 * Your MyCalendar object will be instantiated and called as such:
 * MyCalendar obj = new MyCalendar();
 * boolean param_1 = obj.book(start,end);
 */
