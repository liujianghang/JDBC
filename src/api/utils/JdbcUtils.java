package api.utils;

import api.druid.DruidUsePart;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/*
    包含一个连接池对象，并且对外提供获取连接和回收连接的方法
    工具类的方法，应该写成静态，外部调用会更加的方便

    实现：
    属性：连接和对象(只实例化一次)
        单例模式||静态代码块
    方法： 对外提供连接的方法 回收外部传入连接方法
 */
public class JdbcUtils {
    private static DataSource dataSource = null;

    static {
        // 初始化连接池对象
        Properties properties = new Properties();
        InputStream ips = DruidUsePart.class.getClassLoader().getResourceAsStream("api/druid/druid.properties");
        try {
            properties.load(ips);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
        对外提供链接的方法
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void freeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
