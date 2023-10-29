import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Table {
    public class MySQLJDBCUtil {
        public static Connection getConnection() throws SQLException {
            Connection conn = null;
            try (FileInputStream f = new FileInputStream("db.properties")) {

                // load the properties file
                Properties pros = new Properties();
                pros.load(f);

                // assign db parameters
                String url = pros.getProperty("url");
                String user = pros.getProperty("user");
                String password = pros.getProperty("password");


                conn = DriverManager.getConnection(url, user, password);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }
    }

    public class Main {
        public static void main(String[] args) {
            try (Connection conn = Table.MySQLJDBCUtil.getConnection()) {
                System.out.println(String.format("Connected to database %s successfully.", conn.getCatalog()));

                createStudentsTable(conn);

                insertStudents(conn);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        private static void createStudentsTable(Connection conn) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
                    "student_id INT(10) NOT NULL AUTO_INCREMENT, " +
                    "last_name VARCHAR(30), " +
                    "first_name VARCHAR(30), " +
                    "PRIMARY KEY (student_id)" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
                System.out.println("Table 'students' created or already exists.");
            } catch (SQLException ex) {
                System.out.println("Table creation error: " + ex.getMessage());
            }
        }

        private static void insertStudents(Connection conn) {
            String[] lastNames = {"Rossi", "Verdi", "Bianchi", "Esposito"};
            String[] firstNames = {"Antonio", "Luca", "Dario", "Ciro"};


            try (Statement stmt = conn.createStatement()) {
                for (int i = 0; i < 4; i++) {
                    String lastName = lastNames[i];

                    String firstName = firstNames[i];

                    String insertSQL = "INSERT INTO students (last_name, first_name) VALUES " +
                            "('" + lastName + "', '" + firstName + "')";

                    stmt.executeUpdate(insertSQL);
                    System.out.println("Inserted student: " + firstName + " " + lastName);
                }
            } catch (SQLException ex) {
                System.out.println("Error inserting students: " + ex.getMessage());
            }
        }
    }
}