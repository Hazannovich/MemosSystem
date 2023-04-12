import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Memos {
    private final HashMap<CleanDate, String> memosMap;
    private final File memosFile;

    public Memos(String fileName) throws IOException {
        memosMap = new HashMap<CleanDate, String>();
        Scanner input = null;
        String filePath = "assets";
        fileName = fileName.trim();
        if (!Pattern.matches("[a-zA-Z]+$", fileName)) {
            throw new IllegalArgumentException("illegal File name");
        }
        fileName += ".txt";

        try {// different OS have different file separators But I am not sure if there is a better workaround.
            filePath += System.getProperty("file.separator") + fileName;
        } catch (SecurityException e) {
            filePath += "\\" + fileName;
        }
        memosFile = getOrCreateFile(filePath);
//TODO: Add file lines to memos hashmap in the correct format.

//        if(!memosFile.createNewFile()){// if file exists
//            input = new Scanner(filePath);
//            while (input.hasNextLine()) {
//                Map.Entry<CleanDate,String> memo = null;
//                try {
//                } catch (Exception e) {
//                    System.out.println("Some items have invalid information and have been removed.");
//                }
//            }
//            input.close();
//        }

    }

    public static File getOrCreateFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            // File does not exist, create a new file and return the file object
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + filePath);
                } else {
                    System.out.println("Failed to create file: " + filePath);
                }
            } catch (IOException e) {
                System.out.println("An error occurred while creating file: " + filePath);
                e.printStackTrace();
            }
        }
        return file;
    }

    public void addMemo(CleanDate date, String msg) {
        memosMap.put(date, msg);
    }

    public String getMemo(CleanDate date) {
        if (memosMap.get(date) == null) {
            return "";
        }
        return memosMap.get(date);
    }
}
