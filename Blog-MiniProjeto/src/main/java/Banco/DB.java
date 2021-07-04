package Banco;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection conexao = null;

    public static Connection abrirConexao() {
        if (conexao == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conexao = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conexao;
    }

    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream file = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(file);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void fecharStatement (Statement state) {
        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void fecharResultSet (ResultSet result) {
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
