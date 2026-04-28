package interviewee.Leecode.array;

public class numberOfWeeks {

    /**
     * 计算不连续做同一项目时最多可工作的周数；当前方法体仍是待补全草稿。
     *
     * @param milestones 每个项目的里程碑数量
     * @return 当前草稿固定返回 0
     */
    public long numberOfWeeks(int[] milestones) {
        int sum = 0, max = 0;
        for (int i = 0; i < milestones.length; i++) {
            sum += milestones[i];
            max = Math.max(max, milestones[i]);
        }
        return 0L;
    }
}
