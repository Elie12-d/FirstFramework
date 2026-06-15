package controller;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ToFormController extends HttpServlet {
    RequestDispatcher dispatcher;
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        dispatcher = req.getRequestDispatcher("/form.jsp");
        dispatcher.forward(req, res);
    }
}
