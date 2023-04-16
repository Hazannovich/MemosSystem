import java.util.Arrays;

public class CleanDate {
    private final String[] date;

    public CleanDate(String day, String month, String year) {
        date = new String[3];
        date[0] = day;
        date[1] = month;
        date[2] = year;
    }


    protected static CleanDate toStringToCleanDate(String date){
        String[] cleanedDate = date.substring(1,date.length()-1).split(", ");
        return new CleanDate(cleanedDate[0], cleanedDate[1], cleanedDate[2]);
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CleanDate)) {
            return false;
        }
        return Arrays.equals(this.date, ((CleanDate) obj).date);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(date);
    }
    @Override
    public String toString() {
        return Arrays.toString(date);
    }
}
