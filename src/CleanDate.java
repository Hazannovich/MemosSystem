import java.util.Arrays;

public class CleanDate {
    private final String[] date;

    public CleanDate(String day, String month, String year) {
        date = new String[3];
        date[0] = day;
        date[1] = month;
        date[2] = year;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CleanDate)){
            return false;
        }
        return Arrays.equals(this.date, ((CleanDate) obj).date);
    }
    @Override
    public int hashCode() {
        return 0;
    }
    public String toString(){
        return Arrays.toString(date);
    }

}
