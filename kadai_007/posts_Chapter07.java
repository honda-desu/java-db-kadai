package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class posts_Chapter07 {

	public static void main(String[] args) {
		
		Connection con = null;
		Statement statement = null;
		
		String[][] postList = {
				{"1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13"},
				{"1002", "2023-02-08", "お疲れ様です！", "12"},
				{"1003", "2023-02-09", "今日も頑張ります！", "18"},
				{"1001", "2023-02-09", "無理は禁物ですよ！", "17"},
				{"1002", "2023-02-10", "明日から連休ですね！", "20"}
		};
		
		try {
			con = DriverManager.getConnection(
				"jdbc:mysql://localhost/challenge_java",
				"root",
				"password"
			);
			
			System.out.println("データベース接続成功:" + con);
			
			// SQLクエリを準備
			String psql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
			PreparedStatement pstmt = con.prepareStatement(psql);
			
			int totalRowCnt = 0;
			//リストを順番に読み込む
			int rowCnt = 0;
			for(int i = 0; i < postList.length; i++) {
				//?部分をリストのデータに置き換える
				pstmt.setString(1, postList[i][0]);
				pstmt.setString(2, postList[i][1]);
				pstmt.setString(3, postList[i][2]);
				pstmt.setString(4, postList[i][3]);
				
				//SQLクエリを実行（DBMSに送信）
			    //System.out.println("レコード追加：" + pstmt.toString());
			    int row = pstmt.executeUpdate(); // 引数は不要
			    //System.out.println(row + "件のレコードが追加されました");
				totalRowCnt += row;
			}
			System.out.println("レコードを実行します");
			System.out.println(totalRowCnt + "件のレコードが追加されました");
			
			//検索対象User ID
			int userID = 1002;
			//SQLクエリを準備
			statement = con.createStatement();
			String gsql = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = " + userID + ";";
			
			//クエリ実行
			ResultSet result = statement.executeQuery(gsql);
			System.out.println("ユーザーIDが" + userID + "のレコードを検索しました");
			//結果を抽出
			while(result.next()) {
				Date posted_at = result.getDate("posted_at");
				String content = result.getString("post_content");
				int likes = result.getInt("likes");
				
				System.out.println(result.getRow() + "件目：投稿日時=" + posted_at 
						+ "/投稿内容=" + content + "/いいね数=" + likes);
				
			}
		} catch(SQLException e) {
			System.out.println("エラー発生：" + e.getMessage());
		} finally {
			if (statement != null) {
				try {statement.close();}catch(SQLException ignore) {}
			}
			if(con != null) {
				try {con.close();}catch(SQLException ignore) {}
			}
		}

		

	}

}
