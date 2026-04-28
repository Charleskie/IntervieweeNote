package interviewee.Leecode;

import java.util.Arrays;

/****
 * 给你一个用字符数组 tasks 表示的 CPU 需要执行的任务列表。其中每个字母表示一种不同种类的任务。任务可以以任意顺序执行，并且每个任务都可以在 1 个单位时间内执行完。在任何一个单位时间，CPU 可以完成一个任务，或者处于待命状态。
 *
 * 然而，两个 相同种类 的任务之间必须有长度为整数 n 的冷却时间，因此至少有连续 n 个单位时间内 CPU 在执行不同的任务，或者在待命状态。
 *
 * 你需要计算完成所有任务所需要的 最短时间 。
 *
 */
public class leastInterval {
    /**
     * 计算带冷却时间的任务调度最短执行时间。
     *
     * 解题思路：
     * 1、将任务按类型分组，A-Z 可以用一个 int[26] 保存任务次数。
     * 2、优先考虑出现次数最多的任务，形成 (maxCount - 1) * (n + 1) + 1 的基础框架。
     * 3、如果还有同样出现 maxCount 次的任务，末尾位置需要继续补齐。
     * 4、当其他任务足够填满所有空位时，总任务数就是最短时间。
     *
     * @param tasks 任务类型数组
     * @param n     相同任务之间的冷却时间
     * @return 完成全部任务的最短时间
     */
    public int leastInterval(char[] tasks, int n) {
        if (tasks.length <= 1 || n < 1) return tasks.length;
        //步骤1
        int[] counts = new int[26];
        for (int i = 0; i < tasks.length; i++) {
            counts[tasks[i] - 'A']++;
        }
        //步骤2
        Arrays.sort(counts);
        int maxCount = counts[25];
        int retCount = (maxCount - 1) * (n + 1) + 1;
        int i = 24;
        //步骤3
        while (i >= 0 && counts[i] == maxCount) {
            retCount++;
            i--;
        }
        //步骤4
        return Math.max(retCount, tasks.length);
    }
}
