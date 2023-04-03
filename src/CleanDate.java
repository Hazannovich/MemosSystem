import java.util.Date;

public class CleanDate {
    private String date;

    public CleanDate() {
        String[] fullDate = new Date().toString().split(" ");
        date = fullDate[2] + " ";
        date += fullDate[1] + " ";
        date += fullDate[5];
        System.out.println(date);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CleanDate)){
            return false;
        }
        return this.date.equals(((CleanDate) obj).date);
    }

}
