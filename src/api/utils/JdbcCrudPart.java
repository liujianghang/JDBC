package api.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcCrudPart extends BaseDao{
    public void testInsert() throws SQLException {
//        Connection connection = JdbcUtils.getConnection();
//        JdbcUtils.freeConnection(connection);
        String sql = "update t_bank set money = money + ? where account = ? ;";
        int i = executeUpdate(sql,"测试12333","123","测试贵3");
    }
}
