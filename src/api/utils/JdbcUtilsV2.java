package api.utils;

import api.druid.DruidUsePart;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
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

    TODO 利用线程本地变量，存储连接信息！确保一个线程的多个方法可以获取同一个connection
     优势：事务操作的时候，service和dao属于同一个线程 不用再传递参数了
     大家都可以调用getConnection自动获取的是相同的连接
 */
public class JdbcUtilsV2 {
    private static DataSource dataSource = null;
    private static ThreadLocal<Connection> t1 = new ThreadLocal<>();

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
        // 查看线程本地变量中是否存在
        Connection connection = t1.get();
        if (connection == null){
            connection = dataSource.getConnection();
            t1.set(connection);
        }
        return connection;
    }

    public static void freeConnection() throws SQLException {
        Connection connection = t1.get();
        if (connection != null){
            t1.remove(); // 清空本地变量数据
            connection.setAutoCommit(true);// 是无状态回归false到true
            connection.close();
        }
    }
}
