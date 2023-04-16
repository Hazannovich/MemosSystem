
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StorageFile {
    private boolean isNewFile;
    private final File storageFile;
    private int splitLimit;
    private Map<String, String> storageMap;

    public StorageFile(String filePath) throws IOException {
        storageMap = new HashMap<>();
        storageFile = getOrCreateFile(filePath);
        splitLimit = 2;
        if (!isNewFile) {
            storageMap = StorageToMap(storageToStringList());
        }
    }

    private void insertToStorage(String key, String value) throws IOException {
        String startingChars = "\n";
        if (value.equals("")) {
            return;
        }
        try (RandomAccessFile file = new RandomAccessFile(storageFile, "rw")) {
            long location = file.length();
            file.seek(location);
            if (isNewFile) {
                startingChars = "";
                isNewFile = false;
            }
            String jsonString = startingChars + entryToStorageStr(key, value);
            file.write(jsonString.getBytes());
        }
    }

    public <K, V> void addToStorage(K key, V value) throws Exception {
        String keyStr = key.toString().trim();
        String valStr = value.toString().trim();
        getSplitLimit(keyStr);
        if (storageMap.containsKey(keyStr)) {
            String lineToRemove = lookupInStorage(keyStr);
            if (lineToRemove.equals("")) {
                throw new IllegalArgumentException("You are holding it wrong.");
            }
            removeLine(lineToRemove);
        }
        insertToStorage(keyStr, valStr);
        storageMap.put(keyStr, valStr);
    }

    private void removeLine(String lineToRemove) throws Exception {
        PathType filePath = new PathType("tempFile", "txt");
        File tempFile = new File(filePath.toString());
        try (RandomAccessFile file = new RandomAccessFile(this.storageFile, "rw");
             RandomAccessFile writer = new RandomAccessFile(tempFile, "rw")) {
            String currentLine = file.readLine();
            while ((currentLine) != null) {
                if (!currentLine.equals(lineToRemove)) {
                    String lineToAdd = currentLine;
                    currentLine = file.readLine();
                    if (currentLine != null && !currentLine.equals(lineToRemove)) {
                        lineToAdd += "\n";
                    } else if (currentLine != null) {
                        currentLine = file.readLine();
                        if (currentLine != null) {
                            lineToAdd += "\n";
                        }
                    }
                    writer.write(lineToAdd.getBytes());
                }
                else {
                    currentLine = file.readLine();
                }
            }
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
                int start = getSplitLimitFromLine(line);
                String cleanedLine = line.substring(start);
                if (key.equals(getLineKey(cleanedLine))) {
                    return line;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    private String getLineKey(String line) {
        StringBuilder key = new StringBuilder();
        String[] keyValuePair = line.split(":", splitLimit);
        for (int i = 0; i < splitLimit - 1; i++) {
            key.append(keyValuePair[i]);
        }
        return key.toString();
    }

    private void getSplitLimit(String key) {
        int countMatches = 2;
        for (int i = 0; i < key.length(); i++)
            if (key.charAt(i) == ':') {
                countMatches++;
            }
        splitLimit = countMatches;
    }

    public <K, V> Map<K, V> readDataFromStorage(Function<String, K> createFormattedKey, Function<String, V> createFormattedValue) throws IOException {
        if (isNewFile) {
            return new HashMap<>();
        }
        Map<String, String> map = StorageToMap(storageToStringList());
        return createFormattedMap(map, createFormattedKey, createFormattedValue);
    }

    private ArrayList<String> storageToStringList() throws IOException {
        ArrayList<String> StorageStringList = new ArrayList<>();
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
            int keyStartIndex = getSplitLimitFromLine(keyValuePair);
            keyValuePair = keyValuePair.substring(keyStartIndex);
            System.out.println(keyValuePair);
            String key = getLineKey(keyValuePair).replaceAll("\\\\n", "\n");
            String value = getLineValue(keyValuePair).replaceAll("\\\\n", "\n");
            map.put(key, value);
        }
        System.out.println(map);
        return map;
    }

    private int getSplitLimitFromLine(String keyValuePair) {
        StringBuilder limit = new StringBuilder();
        int keyStartIndex = 1;
        for (int i = 1; i < keyValuePair.length(); i++) {
            char c = keyValuePair.charAt(i);
            if (c == ']') {
                keyStartIndex = i + 1;
                break;
            }
            limit.append(c);
        }
        splitLimit = Integer.parseInt(limit.toString());
        return keyStartIndex;
    }

    private String getLineValue(String line) {
        String[] keyValuePair = line.split(":", splitLimit);
        System.out.println(splitLimit);
        System.out.println(Arrays.toString(keyValuePair));
        return keyValuePair[splitLimit - 1];
    }

    private String entryToStorageStr(String key, String value) {
        return ("[" + splitLimit + "]" + key + ":" + value).replaceAll("\n", "\\\\n");
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
