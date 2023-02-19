package api.preparedstatment;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PSCURDPart {
    // 测试方法导入junist的测试包
    @Test
    public void testInsert() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "root");
        String sql = "insert into t_user(account,password,nickname) values (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, "test");
        preparedStatement.setObject(2, "test");
        preparedStatement.setObject(3, "test");
        // DML类型
        int rows = preparedStatement.executeUpdate();
        if (rows > 0) {
            System.out.println("数据插入成功！");
        } else {
            System.out.println("数据插入失败！");
        }
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testUpdate() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "root");
        String sql = "update t_user set nickname=? where id=? ;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, "update1");
        preparedStatement.setObject(2, 1);
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testDelete() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "root");
        String sql = "delete from t_user where id=? ;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, 3);
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("删除失败");
        }
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testSelect() throws ClassNotFoundException, SQLException {
        // 查询数据库并放在一个list<Map>中
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jdbc_test", "root", "root");
        String sql = "select id,account,password,nickname from t_user ;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map> list = new ArrayList<>();
        // 纯手动
        //while (resultSet.next()){
        //    HashMap map = new HashMap();
        //    map.put("id",resultSet.getInt("id"));
        //    map.put("account",resultSet.getString("account"));
        //    map.put("password",resultSet.getString("password"));
        //    map.put("nickname",resultSet.getString("nickname"));
        //    list.add(map);
        //}
        // TODO：metaData 获取当前列的信息的对象
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        // 注意从1开始，并且是等于列数
        while (resultSet.next()) {
            Map map = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                // 获取指定列下角标的值 值有关的都用resultSet对象
                Object value = resultSet.getObject(i);
                // 不要使用getColumnName 因为getColumnLabel不仅有名字，还有别名
                String key = metaData.getColumnLabel(i);
                map.put(key, value);
            }
            list.add(map);
        }

        System.out.println(list);
    }
}
