package interviewee.Leecode.array;

public class numberOfWeeks {

    public long numberOfWeeks(int[] milestones) {
        int sum = 0, max = 0;
        for (int i = 0; i < milestones.length; i++) {
            sum += milestones[i];
            max = Math.max(max, milestones[i]);
        }
        return 0L;
    }
}
