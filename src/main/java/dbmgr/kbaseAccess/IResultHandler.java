package dbmgr.kbaseAccess;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultHandler {
    void handle(ResultSet resultSet) throws SQLException, IOException;
    boolean isSuccess();
}

