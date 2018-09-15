package interviewee.wangyi;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class HelpTicket {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        while(in.hasNext()){
            int N=in.nextInt();
            int M=in.nextInt();
            getTicket[] NNren=new getTicket[N];
            int  a;int b;
            int[] nums=new int[M+1];
            for(int i=0;i<N;i++){
                a=in.nextInt();
                b=in.nextInt();
                NNren[i]=new getTicket(a, b);
                nums[a]++;
            }
            int max=1;
            for(int i=0;i<=M;i++){
                max=nums[i]>nums[max]?i:max;
            }
            if(max==1){
                System.out.println(0);
            }
            Arrays.sort(NNren,new Comparator<getTicket>() {

                @Override
                public int compare(getTicket o1, getTicket o2) {
                    return o1.money-o2.money;
                }
            });
            int res=0;
            int support=nums[max];
            int mySup=nums[1];
            for(getTicket someone:NNren){
                if(someone.aim==1){
                    continue;
                }
                if(someone.aim!=max){
                    res+=someone.money;
                    mySup++;
                }else{
                    res+=someone.money;
                    mySup++;
                    support--;
                }
                if(mySup>support){
                    break;
                }
            }
            System.out.println(res);
        }
    }
}
class getTicket{
    int aim;
    int money;
    public getTicket(int aim,int money){
        this.aim=aim;
        this.money=money;
    }

}