package api.tranctionV2;

import api.utils.JdbcUtilsV2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BankDao {
    public void add(String account, int money) throws ClassNotFoundException, SQLException {
        Connection connection = JdbcUtilsV2.getConnection();
        String sql = "update t_bank set money = money + ? where account = ? ;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, money);
        statement.setObject(2, account);
        int i = statement.executeUpdate();
        statement.close();
        System.out.println("加钱成功");
    }

    public void sub(String account, int money) throws ClassNotFoundException, SQLException {
        Connection connection = JdbcUtilsV2.getConnection();
        String sql = "update t_bank set money = money - ? where account = ? ;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setObject(1, money);
        statement.setObject(2, account);
        int i = statement.executeUpdate();
        statement.close();
        System.out.println("减钱成功");
    }
}
