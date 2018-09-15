package interviewee.Algorithm;

public class KMP {
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