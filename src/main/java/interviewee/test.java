package interviewee;

public class test{
    public static void main(String[] args) {

        Thread t = new Thread()
        {
            public void run () {
            pong();
        }
        } ;
        t.run();
        System.out.print("ping");
    }

    /**
     * 输出线程执行顺序示例中的 pong。
     */
    static void pong() {
        System.out.print("pong");
    }
}
