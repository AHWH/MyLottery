package sg.reddotdev.sharkfin.data.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Calendar;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;
import sg.reddotdev.sharkfin.data.typeconverter.CalendarTypeConverter;

/**
 * Created by weiho on 3/7/2017.
 */
@Table(database = LotteryDatabase.class)
public class LotteryResult extends BaseModel {
    @PrimaryKey
    private int lotteryID;
    @PrimaryKey
    private Calendar date;

    public LotteryResult() {}

    public LotteryResult(int lotteryID, Calendar date) {
        this.lotteryID = lotteryID;
        this.date = date;
    }

    public int getLotteryID() {
        return lotteryID;
    }

    public void setLotteryID(int lotteryID) {
        this.lotteryID = lotteryID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "===4D Results\n"
                + "Draw No: " + getLotteryID()
                + ", Date: "
                + date.get(Calendar.DAY_OF_MONTH) + " "
                + date.get(Calendar.MONTH) + " "
                + date.get(Calendar.YEAR) + ", "
                + date.get(Calendar.DAY_OF_WEEK) + "\n";
    }
}
