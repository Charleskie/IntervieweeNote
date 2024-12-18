package interviewee.Leecode.string;


import java.util.Arrays;
import java.util.HashSet;

public class ReportSpam {

    /***
     * 举报垃圾信息
     * @param message
     * @param bannedWords
     * @return
     */
    public boolean reportSpam(String[] message, String[] bannedWords) {
        int flag = 0;
        HashSet<String> set = new HashSet<>(Arrays.asList(bannedWords));
        for (String word : message) {
            if(set.contains(word)){
                flag++;
            }
            if(flag >= 2){
                return true;
            }
        }
        return false;
    }
}
