package sg.reddotdev.sharkfin.data.model;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.threeten.bp.ZonedDateTime;

import sg.reddotdev.sharkfin.data.database.LotteryDatabase;

/**
 * Created by weiho on 3/7/2017.
 */
@Table(database = LotteryDatabase.class)
public class LotteryResult extends BaseModel {
    @PrimaryKey
    private int lotteryID;
    @PrimaryKey
    private ZonedDateTime date;

    public LotteryResult() {}

    public LotteryResult(int lotteryID, ZonedDateTime date) {
        this.lotteryID = lotteryID;
        this.date = date;
    }

    public int getLotteryID() {
        return lotteryID;
    }

    public void setLotteryID(int lotteryID) {
        this.lotteryID = lotteryID;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "===4D Results\n"
                + "Draw No: " + getLotteryID()
                + ", Date: "
                + date.getDayOfMonth() + " "
                + date.getMonth().getValue() + " "
                + date.getYear() + ", "
                + date.getDayOfWeek().getValue() + "\n";
    }
}
