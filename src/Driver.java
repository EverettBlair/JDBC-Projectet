
import java.sql.*;
import java.util.Collections;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        String host = "localhost"; //host is "localhost" or "127.0.0.1"
        String port = "3306"; //port is where to communicate with the RDBM system
        String database = "trainmodelinc"; //database containing tables to be queried
        String cp = "utf8"; //Database codepage supporting danish (i.e. æøåÆØÅ)
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding" + cp; //Url for the database
        String user = "root";
        String password = "5548"; //TODO REMEMBER TO ADD YOUR PASSWORD
        boolean exit = true;
        while (exit) {        try {
            System.out.println("Connected to: " + database);
            // Import scanner
            Scanner scanner = new Scanner(System.in);

            // Connection to the Database
            Connection myConn = DriverManager.getConnection(url, user, password);

            // Create statement
            Statement myStmt = myConn.createStatement();

            // Execute SQL Query
            String choice;
            System.out.println("Do you wish to update, delete, insert or output Data?");
            System.out.println("          Type Exit to Close the program");
            choice = scanner.nextLine().toLowerCase();

            if (choice.equals("exit") || choice.equals("ex")) {
                exit = false;
            }

            else if (choice.equals("delete") || choice.equals("d")) {
                System.out.println("Deleting data from " + database);
                System.out.println("Type sql Table: "); //Any table in your database
                String sqlTable = scanner.nextLine();
                System.out.println("Deleting data from " + sqlTable);
                Output.Output(myStmt,sqlTable);
                System.out.println("Type the indentifier eg: fullname='John Doe': ");
                String sqlIdn = scanner.nextLine();
                String sql = "delete from " + sqlTable + " where " + sqlIdn;

                int rowsAffected = myStmt.executeUpdate(sql);

                System.out.println("Rows affected :" + rowsAffected);
                Output.Output(myStmt,sqlTable);
                System.out.println("Delete complete.");

                scanner.nextLine();
            }

            else if (choice.equals("update") || choice.equals("u")) {
                System.out.println("Updating data from " + database);
                System.out.println("Type sql Table: "); //Any table in your database
                String sqlTable = scanner.nextLine();
                System.out.println("Updating data from " + sqlTable);
                Output.Output(myStmt,sqlTable);
                System.out.println("Type the indentifier eg: fullname='John Doe': ");
                String sqlIdn = scanner.nextLine();
                System.out.println("Type what you wish to change eg: email='john@gmail.com' ");
                String sqlChange = scanner.nextLine();
                String sql = "update " + sqlTable + "  set " + sqlChange + " where " + sqlIdn;

                myStmt.executeUpdate(sql);

                Output.Output(myStmt,sqlTable);
                System.out.println("Update Complete.");

                scanner.nextLine();
            }

            else if (choice.equals("insert") || choice.equals("i")) {
                System.out.println("Inserting new data into " + database);
                System.out.println("Type sql Table: "); //Any table in your database
                String sqlTable = scanner.nextLine();
                System.out.println("Inserting new data into " + sqlTable);
                Output.Output(myStmt,sqlTable);
                System.out.println("Type sql Collumns. eg: firstname, lastname, age. or type nothing for all : "); //Any Column in your table
                String sqlColumn = scanner.nextLine();
                System.out.println("Type the new values you wish to insert. eg: 'Russell', 'Blair', 'everettblair@live.dk': "); //Add the values to the selceted Collumns
                String sqlValues = scanner.nextLine();
                String sql = "insert into " + sqlTable + "(" + sqlColumn + ")" + " values (" + sqlValues + ")";

                myStmt.executeUpdate(sql);


                Output.Output(myStmt,sqlTable);
                System.out.println("Insert complete.");

                scanner.nextLine();
            }

            else if (choice.equals("output") || choice.equals("o")) {
                String space = " ";
                System.out.println("Outputting data from " + database);
                System.out.println("Type sql Tabel: "); //Anything that will return a table
                String sqlQuery = scanner.nextLine();
                ResultSet myRs = myStmt.executeQuery("select * from " +sqlQuery);
                ResultSetMetaData resultSetMetaData = myRs.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();

                System.out.println("+" + String.join("", Collections.nCopies(6, "--------------------+")));
                for (int i = 1; i <= columnCount; i++) {
                    if (i == 1) System.out.print("|");
                    String columnName = String.valueOf(resultSetMetaData.getColumnName(i));
                    System.out.print(columnName + String.join("", Collections.nCopies(20 -columnName.length(), space))+ "|");
                }
                System.out.println("");
                System.out.println("+" + String.join("", Collections.nCopies(6, "--------------------+")));
                while (myRs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) System.out.print("|");
                        String columnValue = String.valueOf(myRs.getString(i));
                        System.out.print(columnValue + String.join("", Collections.nCopies(20 -columnValue.length(), space))+ "|");

                    }
                    System.out.println("");
                }
                System.out.println("+" + String.join("", Collections.nCopies(6, "--------------------+")));

                scanner.nextLine();
            }
            myStmt.close();
            myConn.close();
        }

        catch (SQLException e) {
            System.err.print("SQLException: ");
            System.err.println(e.getMessage());
            }

        }
    }
}
class PrintColumn {

    public static void PrintColumn(ResultSetMetaData rsmd)
            throws SQLException {
        int columns = rsmd.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            int jdbcType = rsmd.getColumnType(i);
            String name = rsmd.getColumnTypeName(i);
            System.out.print("Column " + i + " is JDBC type " + jdbcType);
            System.out.println(", which the DBMS calls " + name);
        }
    }
}

class Output {
    public static void Output(Statement myStmt,String sqlQuery) throws SQLException {
        String space = " ";
        ResultSet myRs = myStmt.executeQuery("select * from " +sqlQuery);
        ResultSetMetaData resultSetMetaData = myRs.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        System.out.println("+" + String.join("", Collections.nCopies(6, "--------------------+")));
        for (int i = 1; i <= columnCount; i++) {
            if (i == 1) System.out.print("|");
            String columnName = String.valueOf(resultSetMetaData.getColumnName(i));
            System.out.print(columnName + String.join("", Collections.nCopies(20 -columnName.length(), space))+ "|");
        }
        System.out.println("");
        System.out.println("+" + String.join("", Collections.nCopies(6, "--------------------+")));
        while (myRs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                if (i == 1) System.out.print("|");
                String columnValue = String.valueOf(myRs.getString(i));
                System.out.print(columnValue + String.join("", Collections.nCopies(20 -columnValue.length(), space))+ "|");

            }
            System.out.println("");
        }
        System.out.println("+" + String.join("", Collections.nCopies(6, "--------------------+")));
    }
}
