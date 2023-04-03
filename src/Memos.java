import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Memos {
    private HashMap<CleanDate,String> memosMap;
    public Memos(String filePath){
        memosMap = new HashMap<CleanDate, String>();
        Scanner input = null;
        try {// different OS have different file separators But I am not sure if there is a better workaround.
            input = new Scanner(new File("assets" + System.getProperty("file.separator") + filePath));
        } catch (FileNotFoundException e) {
            System.out.println("The menu file does not exist.");
            System.exit(0);
        } catch (SecurityException e) {
            try {
                input = new Scanner(new File("assets" + "\\" + filePath));
            } catch (FileNotFoundException e1) {
                System.out.println("The menu file does not exist.");
                System.exit(0);
            }
        }
    }
    public void addMemo(CleanDate date, String msg){
            memosMap.put(date, msg);
    }
    public String getMemo(CleanDate date){
        if (memosMap.get(date) == null) {
            return "";
        }
        return memosMap.get(date);
    }


}
