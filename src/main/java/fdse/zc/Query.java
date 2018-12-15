package fdse.zc;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

public class Query {
  public static void run() {
    Properties properties = new Properties();
    FileInputStream fis = null;
    MysqlDataSource mysqlDS = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    Path propertiesFilePath = Paths.get(System.getProperty("user.dir"), "db.properties");
    try {
      fis = new FileInputStream(propertiesFilePath.toString());
      properties.load(fis);
      mysqlDS = new MysqlDataSource();
      mysqlDS.setURL(properties.getProperty("MYSQL_DB_URL"));
			mysqlDS.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
      mysqlDS.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
      con = mysqlDS.getConnection();
      stmt = con.createStatement();
      rs = stmt.executeQuery("select * from jobs where id = 19");
      rs.next();
      System.out.println("" + rs.getString("commit_compare_url"));
    } catch (Exception e) {
      //TODO: handle exception
      e.printStackTrace();
    } finally {
      try {
        if(rs != null) rs.close();
        if(stmt != null) stmt.close();
        if(con != null) con.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
  }
    
  }
}