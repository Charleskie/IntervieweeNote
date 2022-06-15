package interviewee.Leecode;

import java.util.Iterator;
import java.util.List;

public class NestedIterator implements Iterator<Integer> {
    private List<Integer> list;
    private int index;
    public NestedIterator(List<NestedInteger> nestedList) {
        add(nestedList);
    }
    @Override
    public Integer next() {
        return list.get(index++);
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    public static void add(List<NestedInteger> addList){

    }
}
