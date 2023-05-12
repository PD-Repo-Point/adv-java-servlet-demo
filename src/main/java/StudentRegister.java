import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utility.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class StudentRegister extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        Random random = new Random();

        int id = random.nextInt(5000);
        String name = req.getParameter("name");
        String fname=req.getParameter("father_name");
        String mname =req.getParameter("mother_name");
        String addr = req.getParameter("address");
        double marks = Double.parseDouble(req.getParameter("marks"));
        //------- date = -------------
        // DOB ===>


        Properties prop = new Properties();

        prop.load(StudentRegister.class.getClassLoader().
                getResourceAsStream("db-connection.properties"));

        Connection connection = null;

        connection = DBConnection.getConnection(
                prop.getProperty("db_driver"),
                prop.getProperty("db_url"),
                prop.getProperty("db_user"),
                prop.getProperty("db_pwd"));

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                  "insert into student_details values(?,?,?,?,?,?)"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, fname);
            preparedStatement.setString(4, mname);
            preparedStatement.setString(5, addr);
            preparedStatement.setBigDecimal(6, BigDecimal.valueOf(marks));
            // store it in the database

            int num = preparedStatement.executeUpdate();

            if(num > 0)
                out.print("Data Entered Successfully ... ");

            if(preparedStatement != null)
                preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(connection!=null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
