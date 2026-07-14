package controller;

import java.io.*;
import java.lang.reflect.Method;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import utils.*;
import annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontController extends HttpServlet {
    private Map<RouteKey, RouteMapping> urlMappings = new HashMap<>();

    public void init() throws ServletException {
        String packageName = this.getInitParameter("package");
        UtilsFunctions utils = new UtilsFunctions();
        List<Class<?>> classes = utils.getAllClassByPackage(packageName);
        try {
            utils.scanAllClassesWithAnnotationAndPutMap(classes, Controllerako.class, "class", urlMappings);
        } catch (Exception e) {
            throw new ServletException("Configuration des mappings invalide: " + e.getMessage(), e);
        }
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null)
            path = "/";

        String httpMethod = req.getMethod();
        RouteKey key = new RouteKey(path, httpMethod);

        if (urlMappings.containsKey(key)) {
            try {
                RouteMapping mapping = urlMappings.get(key);
                Class<?> controllerClass = mapping.getController();
                Object controller = controllerClass.getDeclaredConstructor().newInstance();
                Method method = mapping.getMethod();
                Object result = method.invoke(controller);
                displayResult(req, res, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //displayMapping(req, res, path, mapping);
        } else {
            handleNotFound(req, res);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
        // displayControllers(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
        // displayControllers(req, res);
    }

    private void handleNotFound(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String path = req.getPathInfo();
        if (path == null)
            path = "/";
        String method = req.getMethod();

        out.println("<html><body>");
        out.println("<h1>404 - URL Not Found</h1>");
        out.println("<p>URL demandee: <strong>" + path + "</strong></p>");
        out.println("<p>Methode HTTP: <strong>" + method + "</strong></p>");
        out.println("<h2>URLs disponibles:</h2>");
        out.println("<ul>");

        for (RouteKey key : urlMappings.keySet()) {
            RouteMapping mapping = urlMappings.get(key);
            out.println("<li><strong>" + key.getMethod() + " " + key.getUrl() + "</strong> -> "
                    + mapping.getController().getSimpleName()
                    + "." + mapping.getMethod().getName() + "()</li>");
        }

        out.println("</ul>");
        out.println("</body></html>");
    }

    private void displayMapping(HttpServletRequest req, HttpServletResponse res, String url, RouteMapping mapping)
            throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Mapping trouve</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Mapping trouve</h1>");
        out.println("    <p><strong>URL :</strong> " + url + "</p>");
        out.println("    <p><strong>Methode HTTP :</strong> " + req.getMethod() + "</p>");
        out.println("    <p><strong>Controleur :</strong> " + mapping.getController().getSimpleName() + "</p>");
        out.println("    <p><strong>Methode :</strong> " + mapping.getMethod().getName() + "()</p>");
        out.println("</body>");
        out.println("</html>");
    }

    private void displayResult(HttpServletRequest req, HttpServletResponse res, Object result)
            throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Resultat du controleur</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Resultat du controleur</h1>");
        out.println("    <p><strong>URL :</strong> " + req.getPathInfo() + "</p>");
        out.println("    <p><strong>Methode HTTP :</strong> " + req.getMethod() + "</p>");
        out.println("    <p><strong>Resultat :</strong> " + result + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
}