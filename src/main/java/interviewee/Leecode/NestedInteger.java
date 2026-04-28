package interviewee.Leecode;

import java.util.List;

public interface NestedInteger {

    /**
     * 判断当前对象是否保存单个整数，而不是嵌套列表。
     *
     * @return 保存单个整数时返回 true
     */
    public boolean isInteger();

    /**
     * 获取当前对象保存的单个整数。
     *
     * @return 保存整数时返回该整数；保存嵌套列表时返回 null
     */
    public Integer getInteger();

    /**
     * 获取当前对象保存的嵌套列表。
     *
     * @return 保存嵌套列表时返回列表；保存单个整数时返回空列表
     */
    public List<NestedInteger> getList();
}
