
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StorageFile {
    private boolean isNewFile;
    private File storageFile;
    private Map<String, String> storageMap;

    public StorageFile(String filePath) throws IOException {
        storageMap = new HashMap<>();
        storageFile = getOrCreateFile(filePath);
        if (!isNewFile) {
            storageMap = StorageToMap(storageToStringList());
        }
    }

    private void insertToStorage(String key, String value) throws IOException {
        String startingChars = "\n";
        try (RandomAccessFile file = new RandomAccessFile(storageFile, "rw")) {
            long location = file.length();
            file.seek(location);
            if (isNewFile) {
                startingChars = "";
                isNewFile = false;
            }
            String jsonString = startingChars + entryToStorageStr(key, value);
            System.out.println(jsonString);
            file.write(jsonString.getBytes());
        }
    }

    public <K, V> void addToStorage(K key, V value) throws Exception {
        String keyStr = key.toString().trim();
        String valStr = value.toString().trim();
        if (keyStr.contains(":") || valStr.contains(":")) {
            throw new IllegalArgumentException("I don't know how to handle this cases:(");
        }
        if (valStr.equals("")) {
            return;
        }
        if (storageMap.containsKey(keyStr)) {
            String lineToRemove = lookupInStorage(keyStr);
            removeLine(lineToRemove);
        }
        insertToStorage(keyStr, valStr);
        storageMap.put(keyStr, valStr);
    }

    private void removeLine(String lineToRemove) throws Exception {
        PathType filePath = new PathType("tempFile", "txt");
        File tempFile = new File(filePath.toString());
        try (RandomAccessFile file = new RandomAccessFile(this.storageFile, "rw");
             RandomAccessFile writer = new RandomAccessFile(tempFile, "rw");) {
            String currentLine;
            while ((currentLine = file.readLine()) != null) {
                if (!currentLine.equals(lineToRemove)) {
                    writer.write(currentLine.getBytes());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (storageFile.delete()) {
            if (!tempFile.renameTo(storageFile)) {
                throw new IOException("Failed to rename temporary file to original file");
            }
        } else {
            throw new IOException("Failed to delete original file");
        }
    }

    private String lookupInStorage(String key) {
        try (RandomAccessFile file = new RandomAccessFile(storageFile, "r")) {
            String line;
            while ((line = file.readLine()) != null) {
                if (key.equals(getLineKey(line))) {
                    return line;
                }
            }
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    private String getLineKey(String line) {
        String[] keyValuePair = line.split(":");
        return keyValuePair[0].trim();
    }

    public <K, V> Map<K, V> readDataFromStorage(Function<String, K> createFormattedKey, Function<String, V> createFormattedValue) throws IOException {
        if (isNewFile) {
            return new HashMap<>();
        }
        Map<String, String> map = StorageToMap(storageToStringList());
        return createFormattedMap(map, createFormattedKey, createFormattedValue);
    }

    private ArrayList<String> storageToStringList() throws IOException {
        ArrayList <String> StorageStringList = new ArrayList<>();
        try (RandomAccessFile file = new RandomAccessFile(storageFile, "r")) {
            String line;
            while ((line = file.readLine()) != null) {
                StorageStringList.add(line);
            }
        }
        return StorageStringList;
    }

    private Map<String, String> StorageToMap(ArrayList<String> storageString) {
        if (isNewFile) {
            return new HashMap<>();
        }
        Map<String, String> map = new HashMap<>();
        for (String keyValuePair : storageString) {
            String[] keyValue = keyValuePair.split(":"); //TODO: FIX CASE WHERE INSIDE A STRING
            String key = keyValue[0].trim().replaceAll("\\\\n","\n");
            String value = keyValue[1].trim().replaceAll("\\\\n","\n");
            map.put(key, value);
        }
        System.out.println(map);
        return map;
    }

    private String entryToStorageStr(String key, String value) {
        return (key + ":" + value).replaceAll("\n", "\\\\n");
    }

    public File getOrCreateFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            isNewFile = false;
        } else if (file.createNewFile()) {
            isNewFile = true;
        } else {
            throw new IOException("");
        }
        return file;
    }

    private <K, V> Map<K, V> createFormattedMap(Map<String, String> dataMap,
                                                Function<String, K> createFormattedKey,
                                                Function<String, V> createFormattedValue) {
        Map<K, V> newMap = new HashMap<>();
        K newKey;
        V newVal;
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if (createFormattedKey != null) {
                newKey = createFormattedKey.apply(entry.getKey());
            } else {
                newKey = (K) entry.getKey();
            }
            if (createFormattedValue != null) {
                newVal = createFormattedValue.apply(entry.getValue());
            } else {
                newVal = (V) entry.getValue();
            }
            newMap.put(newKey, newVal);
        }
        return newMap;
    }
}
