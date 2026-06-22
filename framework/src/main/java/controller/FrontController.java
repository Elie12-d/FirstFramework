package controller;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import utils.*;
import annotation.*;
import java.util.List;

public class FrontController extends HttpServlet {

    private List<Class<?>> controllerClasses;

    public void init() throws ServletException {
        String packageName = this.getInitParameter("package");
        UtilsFunctions utils = new UtilsFunctions();
        List<Class<?>> classes = utils.getAllClassByPackage(packageName);
        controllerClasses = utils.getAllClassesWithAnnotation(classes, Controllerako.class, "class");

        for (Class<?> clazz : controllerClasses) {
            System.out.println("   - " + clazz.getName());
        }
    }

    protected String processRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String url = req.getRequestURI();
        return url;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // String url = processRequest(req, res);
        displayControllers(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // String url = processRequest(req, res);
        displayControllers(req, res);
    }

    private void displayControllers(HttpServletRequest req, HttpServletResponse res) throws IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html lang=\"en\">");
    out.println("<head>");
    out.println("    <meta charset=\"UTF-8\">");
    out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
    out.println("    <title>Liste des controller</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("    <h1>Liste controller</h1>");
    out.println("    <ul>");
    for (Class<?> clazz : controllerClasses) {
        out.println("        <li>" + clazz.getSimpleName() + "</li>");
    }
    out.println("    </ul>");
    out.println("</body>");
    out.println("</html>");
}
}