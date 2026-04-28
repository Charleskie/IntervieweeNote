package interviewee.Algorithm;

public class KMP {
    /**
     * 使用已经构建好的 next 数组在主串中查找模式串第一次出现的位置。
     *
     * @param str  主串
     * @param dest 模式串
     * @param next 模式串的 KMP 部分匹配表
     * @return 匹配成功时返回起始下标，否则返回 -1
     */
    private int mykmp(String str, String dest,int[] next) {
        int strlen = str.length();
        int destlen = dest.length();
        int i=0,j=0;
        while(i<strlen && j<destlen){
            if(j==-1 || str.charAt(i)==dest.charAt(j)){
                ++i;
                ++j;
            }else{
                j=next[j];
            }
        }
        if(j==destlen){
            return i-j;
        }
        return -1;
    }

    /**
     * 构造 KMP 的 next 数组，用于在失配时决定模式串回退位置。
     *
     * @param dest 模式串
     * @return next 数组
     */
    private int[] myNext(String dest) {
        int pLen = dest.length();
        int[] next = new int[pLen];
        next[0]=-1;
        int k=-1;
        int j=0;
        while(j<pLen-1){
            if(k==-1 || dest.charAt(j)==dest.charAt(k)){
                ++k;
                ++j;
                next[j]=k;
            }else{
                k=next[k];
            }
        }
        return next;
    }

    public static void main(String[] args){
        String a = "abaa";
        String b = "ssdfgasdbabaaba";
        KMP temp = new KMP();
        int[] next = temp.myNext(a);
        int res = temp.mykmp(b, a, next);
        System.out.println(res);
        for(int i = 0; i < next.length; i++){
            System.out.print(next[i]+" ");
        }
        System.out.println();
        System.out.println(next.length);
    }
}
