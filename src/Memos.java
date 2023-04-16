import java.io.*;
//import java.util.Scanner;
import java.util.Map;


public class Memos {
    private Map<CleanDate, String> memosMap;
    StorageFile memosFile;

    public Memos(String fileName) throws IOException {
        PathType filePath = new PathType(fileName, "txt");
        memosFile = new StorageFile(filePath.toString());
        memosMap = memosFile.readDataFromStorage(CleanDate::toStringToCleanDate,null);
    }

    public void addMemo(CleanDate date, String msg) throws Exception {
        memosMap.put(date, msg);
        memosFile.addToStorage(date, msg);
    }

    public String getMemo(CleanDate date) {
        if (memosMap.get(date) == null) {
            return "";
        }
        return memosMap.get(date);
    }

}
