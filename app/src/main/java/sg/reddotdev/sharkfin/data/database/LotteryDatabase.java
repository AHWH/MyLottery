package sg.reddotdev.sharkfin.data.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by weiho on 3/7/2017.
 */
@Database(name = LotteryDatabase.NAME, version = LotteryDatabase.VERSION)
public class LotteryDatabase {
    public static final String NAME = "LotteryDB";
    public static final int VERSION = 18;
}
