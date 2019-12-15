package com.adenon.sp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {

    public Connection        conn;
    public Statement         st;
    public PreparedStatement ps;
    public ResultSet         rs;

    public static Dao newDao() {
        return new Dao(null);
    }

    public static Dao newDao(Connection conn) {
        return new Dao(conn);
    }

    private Dao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Close all resources, including connection.
     */
    public void close() {
        this.close(true);
    }

    /**
     * Close resources, deciding for connection with parameter.
     */
    public void close(boolean closeConnection) {
        try {
            if (this.rs != null) {
                this.rs.close();
            }
        } catch (SQLException e) {
        }
        try {
            if (this.st != null) {
                this.st.close();
            }
        } catch (SQLException e) {
        }
        try {
            if (this.ps != null) {
                this.ps.close();
            }
        } catch (SQLException e) {
        }
        if (closeConnection) {
            try {
                if (this.conn != null) {
                    this.conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

}
