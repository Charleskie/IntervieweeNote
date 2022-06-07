package interviewee.Leecode;

import java.util.Map;
import java.util.TreeMap;


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
class MyCalendar {

    TreeMap<Integer, Integer> treeMap;
    public MyCalendar() {
        treeMap = new TreeMap<>();
    }

    public boolean book(int start, int end) {
        Map.Entry<Integer, Integer> event = treeMap.floorEntry(start);
        if(event != null && event.getValue() > start) return false;

        event = treeMap.ceilingEntry(start);
        if(event != null && event.getKey() < end) return false;

        treeMap.put(start, end);

        return true;
    }

    public static void main(String[] args) {
        MyCalendar calendar = new MyCalendar();
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
