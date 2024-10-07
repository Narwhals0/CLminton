package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {
	private final String USERNAME = "root"; //root = username default device
    //pake kapital semua supaya keliatannya aja biasanya kalau final kek gt
    private final String PASSWORD = "";
    private final String DATABASE = "clminton"; //sesuai dengan nama database yg dibuat
    private final String HOST = "localhost:3306"; //sesuain sm port MySQL di xampp

    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    private static Connection con;
    //import dari java.sql
    private Statement st; //untuk eksekusi query ke database kita

    public static ResultSet rs; //untuk nampung hsl query
    public ResultSetMetaData rsm; //untuk nampung informasi ttg hasil query
    public static PreparedStatement ps; 

    //singleton -> biar cuma ada 1 connection buat keseluruhan jalannya program (pakai static)
    private static Connect connect;
    //Connect yg pake C gede itu sesuai dengan kelas Connect kita

    //membuat method untuk dapetin objek connect nya
    public static Connect getInstance() {
        if(connect == null) {
            //kalau connect blm pernah dibuat
            return new Connect(); //kalau connect blm pernah dibuat
        } else{
            return connect; //kalau object connectnya  udh ada
        }
    }

    //contructor
    private Connect() {
        try {
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();
            ps = null; 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method buat jalanin query (SELECT)
    public static ResultSet execQuery(String query, String... params) {
        try {
            PreparedStatement preparedStatement = getPreparedStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i]);
            }
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static void execUpdate(String query, Object... params) {
        try {
            ps = con.prepareStatement(query);

       
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

  
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
    
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static PreparedStatement getPreparedStatement(String query) {
        try {
            return con.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
