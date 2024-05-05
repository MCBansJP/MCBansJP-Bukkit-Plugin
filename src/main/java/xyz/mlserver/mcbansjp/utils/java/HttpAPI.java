package xyz.mlserver.mcbansjp.utils.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpAPI {

    public static String postResult(String result, String json) throws IOException {

        //1.接続するための設定をする

        // URL に対して openConnection メソッドを呼び出すし、接続オブジェクトを生成
        URL url = new URL(result);
        HttpURLConnection conn = getHttpURLConnection(url, json);

        //HttpURLConnectionからInputStreamを取得し、読み出す
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        // 5.InputStreamを閉じる
        br.close();

        //結果は呼び出し元に返しておく
        return sb.toString();
    }

    private static HttpURLConnection getHttpURLConnection(URL url, String json) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // HttpURLConnectionの各種設定
        //HTTPのメソッドをPOSTに設定
        conn.setRequestMethod("POST");
        //リクエストボディへの書き込みを許可
        conn.setDoInput(true);
        //レスポンスボディの取得を許可
        conn.setDoOutput(true);
        //リクエスト形式をJsonに指定
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2.接続を確立する
        conn.connect();

        // 3.リクエスとボディに書き込みを行う
        //HttpURLConnectionからOutputStreamを取得し、json文字列を書き込む
        PrintStream ps = new PrintStream(conn.getOutputStream());
        ps.print(json);
        ps.close();


        // 4.レスポンスを受け取る
        //正常終了時はHttpStatusCode 200が返ってくる
        if (conn.getResponseCode() != 200) {
            //エラー処理
        }
        return conn;
    }

}
