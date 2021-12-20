package com.botdiril.userdata.metrics;

import com.botdiril.framework.sql.DBConnection;
import com.botdiril.framework.sql.SqlFoundation;
import com.botdiril.userdata.UserInventory;

public class UserMetrics
{
    public static final String TABLE_USER_METRICS = "metrics";

    public static void initTable(DBConnection db)
    {
        var tabExists = SqlFoundation.checkTableExists(db, TABLE_USER_METRICS);

        if (!tabExists)
        {
            db.simpleExecute("""
             CREATE TABLE metrics
             (
                 fk_us_id INT NOT NULL,
                 um_commandid INT NOT NULL AUTO_INCREMENT,
                 um_coins BIGINT NOT NULL,
                 um_keks BIGINT NOT NULL,
                 um_tokens BIGINT NOT NULL,
                 um_keys BIGINT NOT NULL,
                 um_mega BIGINT NOT NULL,
                 um_dust BIGINT NOT NULL,
                 um_level INT NOT NULL,
                 um_xp BIGINT NOT NULL,
                 um_timestamp TIMESTAMP NOT NULL DEFAULT UTC_TIMESTAMP(),
                 
                 PRIMARY KEY (fk_us_id, um_commandid),
                 FOREIGN KEY (fk_us_id) REFERENCES users(us_id)
             ) ENGINE = MyISAM;
             """);
        }
    }

    public static void updateMetrics(DBConnection db, UserInventory ui)
    {
        var uiObj = ui.getUserDataObj();

        db.simpleUpdate("""
            INSERT INTO metrics
            (fk_us_id, um_coins, um_keks, um_tokens, um_keys, um_mega, um_dust, um_level, um_xp)
            VALUES
            (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, ui.getFID(), uiObj.coins(), uiObj.keks(), uiObj.tokens(), uiObj.keys(), uiObj.megakeks(), uiObj.dust(), uiObj.level(), uiObj.xp());
    }
}
