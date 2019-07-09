package sqllite;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class OpSqliteDB {

	static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

	private static final String Class_Name = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:E:\\FaceDatabase.db";

	public static void main(String args[]) {
		// load the sqlite-JDBC driver using the current class loader
		Connection connection = null;
		try {
			connection = createConnection();
			func1(connection);
			System.out.println("Success!");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	// 创建Sqlite数据库连接
	public static Connection createConnection() throws SQLException, ClassNotFoundException {
		Class.forName(Class_Name);
		return DriverManager.getConnection(DB_URL);
	}

	public static void func1(Connection connection) throws Exception {
		OpMysqlDB mysqlDB = new OpMysqlDB();
        Statement statement = connection.createStatement();
        Statement statement1 = connection.createStatement();
        statement.setQueryTimeout(30); // set timeout to 30 sec.
        // 执行查询语句

        ResultSet rs = statement.executeQuery("select * from t_FacialFeature");
//		Map<String, Object> map = null;
		String imagePath = "E:/feature/";
        while (rs.next()) {
			String col1 = rs.getString("id");
			byte[] bytes = rs.getBytes(4);
			System.out.println(bytes.length);
			String feature = new BASE64Encoder().encode(bytes).replace("\r\n", "");
//			map = new HashMap<String, Object>();
			mysqlDB.add(Integer.parseInt(col1), feature);
//			map.put("id", col1);
//			map.put("feature", feature);
//			base64StringToImage(encoder.encodeBuffer(image).trim(), imagePath + col1 + ".jpg");
        }
		// true表示在文件末尾追加
    }

	static void base64StringToImage(String base64String, String imagePth) {
		try {
			byte[] bytes1 = decoder.decodeBuffer(base64String);

			ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
			BufferedImage bi1 = ImageIO.read(bais);
			File w2 = new File(imagePth);// 可以是jpg,png,gif格式
			ImageIO.write(bi1, "jpg", w2);// 不管输出什么格式图片，此处不需改动
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}