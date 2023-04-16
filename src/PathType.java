import java.util.regex.Pattern;

public class PathType {
    // I feel like I need a class to find paths.
    // I do it mostly for myself less because of the project.
    private String filePath;
    public PathType(String fileName, String fileType){
        filePath = "assets";
        fileName = fileName.trim();
        if (!Pattern.matches("[a-zA-Z]+$", fileName)) {
            throw new IllegalArgumentException("illegal File name");
        }
        fileName += "." + fileType;
        try {// different OS have different file separators But I am not sure if there is a better workaround.
            filePath += System.getProperty("file.separator") + fileName;
        } catch (SecurityException e) {
            filePath += "\\" + fileName;
        }
    }
    @Override
    public String toString(){
        return filePath;
    }
}
