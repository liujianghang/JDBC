package api.utils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    TODO 封装两个方法
        一个简化DQL 一个简化非DQL
 */
public abstract class BaseDao {
    /*
        封装简化非DQL语句
        params是占位符的值
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        Connection connection = JdbcUtilsV2.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 1; i <= params.length; i++) {
            statement.setObject(i, params[i - 1]);
        }
        int i = statement.executeUpdate();
        statement.close();
        // 是否回收连接需要考虑是不是事务
        if (connection.getAutoCommit()) {
            // true = 没有开启事务 就回收
            JdbcUtilsV2.freeConnection();
        }
        return i;
    }

    /*
        java与mysql 表中-> 一行 -> java类的一个对象 -> 多行 ->List<Java实体类> list;
        DQL语句的方法 返回值是 List<T> list (返回的类型应该是某一个类的实体类集合)
     */
    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object... params) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Connection connection = JdbcUtilsV2.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        if (params != null && params.length != 0) {
            for (int i = 1; i < params.length; i++) {
                statement.setObject(i, params[i - 1]);
            }
        }
        // 执行
        ResultSet resultSet = statement.executeQuery();
        ArrayList<T> list = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 注意从1开始，并且是等于列数
        while (resultSet.next()) {
            // 一行数据 对应一个T实例的对象
            T t = clazz.newInstance();
            // 循环获取对象的属性值
            for (int i = 1; i <= columnCount; i++) {
                // 获取指定列下角标的值 值有关的都用resultSet对象
                Object value = resultSet.getObject(i);
                String propertyName = metaData.getColumnLabel(i);
                // 反射，给对象的属性值赋值
                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true); // 属性可以被设置 打破private的限制
                field.set(t, value);
            }
            list.add(t);
        }
        resultSet.close();
        statement.close();

        // 是否回收连接需要考虑是不是事务
        if (connection.getAutoCommit()) {
            // true = 没有开启事务 就回收
            JdbcUtilsV2.freeConnection();
        }
        return list;
    }
}
