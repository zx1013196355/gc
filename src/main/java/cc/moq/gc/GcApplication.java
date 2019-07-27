package cc.moq.gc;

import cc.moq.gc.creator.base.TableConfig;
import cc.moq.gc.creator.jdbc.DBConnProperty;
import cc.moq.gc.creator.jdbc.JDBCUtils;
import cc.moq.gc.creator.util.DBUtils;
import cc.moq.gc.utils.StrUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.util.List;

@SpringBootApplication
public class GcApplication {

    private static Connection conn;

    public static void befor() {
        DBConnProperty dbPropety = new DBConnProperty("127.0.0.1", "3306", "monitor", "root", "root");
        conn = JDBCUtils.getConnection(
                JDBCUtils.createMySqlConnectStr(dbPropety),
                "com.mysql.jdbc.Driver");
    }


    public static void main(String[] args) {
        befor();

        String savePath = "C:\\用户\\MePlusYou\\Desktop\\code\\";
        try {

            List<String> tabls = DBUtils.getTables(conn);


            for (String tableName : tabls) {
//                if(tableName.startsWith("ticket_")){
                System.out.println(tableName);
                String en = tableName;
                en = StrUtils.underlineToCamelhump(en);
                en = StrUtils.firstCharToUpper(en);
                TableConfig tableConfig = new TableConfig(tableName,
                        en, "io.starteos.monitor.controller", "io.starteos.monitor.model",
                        "io.starteos.monitor.mapper",
                        "io.starteos.monitor.service", savePath, "t", conn);
                tableConfig.create(true, true, true, true);
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        after();
    }

    public static void after() {
        JDBCUtils.release(conn, null, null);
    }

}
