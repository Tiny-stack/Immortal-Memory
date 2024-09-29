package org.main.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// import org.checkerframework.checker.calledmethods.qual.EnsuresCalledMethods.List;
import org.main.backend.records.*;
import java.util.*;

public class  Operation{

    // SQLite database file path (it will create one if it doesn't exist)
    private static final String DB_URL = "jdbc:sqlite:diary3.db";
    private static Connection connection = null;
    private static Operation operation = null; 

    /**
     * Connect to the SQLite database
     */
     private Operation () throws SQLException{
            connection = DriverManager.getConnection(DB_URL);
    }
    public static Operation initConnection() // to make sure only one connection exist we will use singleton class
    {
        try
        {
            if(operation==null)
            {
                Operation.operation =  new Operation(); 
                operation.createTable();
                return operation;  
            }
            else{
                return operation;
            }
        }
        catch(SQLException e)       {
            System.err.println(e);
            return null;
        }
    }
    private void createTable() {
        String sql1 = """
            CREATE TABLE IF NOT EXISTS diary_entries (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title VARCHAR(100),
                content TEXT NOT NULL,
                date Integer
            );""";

        String sql2 = """
            CREATE TABLE IF NOT EXISTS user
            (
                name VARCHAR(50) NOT NULL,
                nick_name VARCHAR(50),
                password VARCHAR(255) NOT NULL,
                about TEXT,
                date Text
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new record into the diary_entries table
     */
    public long insertRecord(String title, String content,int date) {
        String sql = "INSERT INTO diary_entries(title, content,date) VALUES(?, ?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, date);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1); // The generated ID
                    return id;
                } else {
                    System.out.println("No ID obtained.");
                }
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("INSERTION FAILED");
            System.out.println(e.getMessage());
            return 0;
        }
    }

    /**
     * Read records from the diary_entries table
     */
    private ArrayList<Memory> read(String sql) {
        
        ArrayList<Memory> memories = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and print each record
            while (rs.next()) {
                memories.add(new Memory(rs.getString("title"),rs.getString("content"),rs.getInt("date"),rs.getInt("id")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return memories;
    }
    public ArrayList<Memory> readMemories()
    {
        String sql = "SELECT id, title, content,date FROM diary_entries";
        return read(sql);
    }

    public ArrayList<Memory> readMemories(int limit,int offset,String sortBy)
    {
        String sql = "SELECT id, title, content,date FROM diary_entries ORDER BY "+sortBy+" DESC LIMIT "+limit+" OFFSET "+offset;
        // System.err.println(sql);
        return this.read(sql);
    }
    public Memory readMemory(long id)
    {
        String sql = "SELECT id, title, content,date FROM diary_entries Where id= "+id;
        // System.err.println(sql);
        try{
            return this.read(sql).get(0);
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public Ressponse signUp(String userName,String nickName,String password,String about,String timeStamp)
    {
        String sql = "INSERT INTO user (name, nick_name,password,about,date) VALUES(?, ?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, nickName);
            pstmt.setString(3, password);
            pstmt.setString(4, about);
            pstmt.setString(5, timeStamp);
            pstmt.executeUpdate();
            System.out.println("User Created successfully.");
            return new Ressponse("Record Inserted Successfully", true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Ressponse(e.getMessage(),false);
        }
    }
    public String getUserName()
    {
        String sql = "SELECT name FROM user LIMIT 1";
        try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and print each record
            while (rs.next()) {
               return rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
    public String getSignUpDate()
    {
        String sql = "SELECT date FROM user LIMIT 1";
        try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and print each record
            while (rs.next()) {
               return rs.getString("date");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";

    }
    public String login()
    {
        String sql = "SELECT password FROM user LIMIT 1";
        try (Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and print each record
            while (rs.next()) {
               return rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return "";

    }
    public boolean updateMemo(Memory memo)
    {
        String sql = "UPDATE diary_entries SET title = ?, content = ?, date = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, memo.getTitle());
            pstmt.setString(2, memo.getContent());
            pstmt.setInt(3, memo.getDate());
            pstmt.setLong(4, memo.getId()); // Assuming ID is an integer
        
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }

}