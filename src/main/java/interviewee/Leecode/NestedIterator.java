package interviewee.Leecode;

import java.util.Iterator;
import java.util.List;

public class NestedIterator implements Iterator<Integer> {
    private List<Integer> list;
    private int index;

    /**
     * 初始化扁平化迭代器，目标是把嵌套列表展开到 list 中。
     *
     * @param nestedList 嵌套整数列表
     */
    public NestedIterator(List<NestedInteger> nestedList) {
        add(nestedList);
    }

    /**
     * 返回扁平化序列中的下一个整数。
     *
     * @return 下一个整数
     */
    @Override
    public Integer next() {
        return list.get(index++);
    }

    /**
     * 判断扁平化序列是否还有未遍历的整数。
     *
     * @return 还有元素时返回 true
     */
    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    /**
     * 将嵌套结构递归展开到内部列表；当前方法体仍是待补全草稿。
     *
     * @param addList 待展开的嵌套列表
     */
    public static void add(List<NestedInteger> addList){

    }
}
