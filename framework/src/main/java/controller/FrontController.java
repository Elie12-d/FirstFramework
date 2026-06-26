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

    private List<Class<?>> controllerClasses;
    private Map<String, RouteMapping> urlMappings = new HashMap<>();
    private Map<String, Object> controllerInstances = new HashMap<>();

    public void init() throws ServletException {
        String packageName = this.getInitParameter("package");
        UtilsFunctions utils = new UtilsFunctions();
        List<Class<?>> classes = utils.getAllClassByPackage(packageName);
        controllerClasses = utils.getAllClassesWithAnnotation(classes, Controllerako.class, "class");

        for (Class<?> controllerClass : controllerClasses) {
            try {
                Object instance = controllerClass.getDeclaredConstructor().newInstance();
                controllerInstances.put(controllerClass.getName(), instance);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        // Pour sprint 2
        utils.scanMappings(controllerClasses, urlMappings);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null)
            path = "/";

        RouteMapping mapping = urlMappings.get(path);
        if (mapping != null) {
            try {
                // // Récupérer l'instance du contrôleur
                // String controllerName = mapping.getController().getName();
                // Object controllerInstance = controllerInstances.get(controllerName);

                // // Exécuter la méthode
                // Method method = mapping.getMethod();
                // method.invoke(controllerInstance, req, res);
                displayMethods(req, res);

            } catch (Exception e) {
                // TODO: handle exception
            }
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

    // ✅ Ajouter cette méthode
    private void handleNotFound(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<html><body>");
        out.println("<h1>404 - URL Not Found</h1>");
        out.println("<p>URL demandée: <strong>" + req.getPathInfo() + "</strong></p>");
        out.println("<h2>URLs disponibles:</h2>");
        out.println("<ul>");

        for (String url : urlMappings.keySet()) {
            RouteMapping rm = urlMappings.get(url);
            out.println("<li><strong>" + url + "</strong> → "
                    + rm.getController().getSimpleName()
                    + "." + rm.getMethod().getName() + "()</li>");
        }

        out.println("</ul>");
        out.println("</body></html>");
    }

    private void displayMethods(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"fr\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("    <title>Liste des Contrôleurs et URLs</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("        h1 { color: #333; }");
        out.println("        .controller { background: #f0f0f0; padding: 10px; margin: 10px 0; border-radius: 5px; }");
        out.println("        .controller-name { color: #0066cc; font-weight: bold; font-size: 18px; }");
        out.println("        .method-list { margin-left: 20px; }");
        out.println(
                "        .method-item { padding: 5px; margin: 5px 0; background: white; border-left: 3px solid #0066cc; }");
        out.println("        .url { color: #28a745; font-weight: bold; }");
        out.println("        .method-name { color: #dc3545; }");
        out.println("        .no-mapping { color: #999; font-style: italic; }");
        out.println("        .stats { background: #e9ecef; padding: 10px; border-radius: 5px; margin-bottom: 20px; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        // En-tête
        out.println("    <h1>📋 Liste des Contrôleurs et URLs</h1>");

        // Statistiques
        int totalControllers = controllerClasses != null ? controllerClasses.size() : 0;
        int totalUrls = urlMappings != null ? urlMappings.size() : 0;

        out.println("    <div class='stats'>");
        out.println("        <p>📊 <strong>Statistiques :</strong></p>");
        out.println("        <ul>");
        out.println("            <li>Nombre de contrôleurs : <strong>" + totalControllers + "</strong></li>");
        out.println("            <li>Nombre d'URLs mappées : <strong>" + totalUrls + "</strong></li>");
        out.println("        </ul>");
        out.println("    </div>");

        if (controllerClasses == null || controllerClasses.isEmpty()) {
            out.println("    <p class='no-mapping'>⚠️ Aucun contrôleur trouvé.</p>");
        } else {
            // Parcourir chaque contrôleur
            for (Class<?> controllerClass : controllerClasses) {
                out.println("    <div class='controller'>");
                out.println("        <div class='controller-name'>📦 " + controllerClass.getSimpleName() + "</div>");
                out.println("        <div class='method-list'>");

                // Vérifier si le contrôleur a des mappings
                boolean hasMappings = false;

                // Parcourir les méthodes du contrôleur
                for (Method method : controllerClass.getDeclaredMethods()) {
                    // Vérifier si la méthode a @GetMapping
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        hasMappings = true;

                        // Récupérer l'URL
                        GetMapping mapping = method.getAnnotation(GetMapping.class);
                        String url = mapping.value();

                        // Si URL vide, utiliser le nom de la méthode
                        if (url == null || url.isEmpty()) {
                            url = "/" + method.getName();
                        }

                        // Récupérer le type de retour
                        String returnType = method.getReturnType().getSimpleName();

                        out.println("            <div class='method-item'>");
                        out.println("                <span class='url'>🔗 " + url + "</span>");
                        out.println("                → <span class='method-name'>" + method.getName() + "()</span>");
                        out.println("                <span style='color: #666; font-size: 12px;'> (retour: "
                                + returnType + ")</span>");
                        out.println("            </div>");
                    }
                }

                if (!hasMappings) {
                    out.println("            <div class='no-mapping'>ℹ️ Aucune méthode mappée avec @GetMapping</div>");
                }

                out.println("        </div>");
                out.println("    </div>");
            }
        }

        // Pied de page
        out.println("    <hr>");
        out.println("    <p style='color: #666; font-size: 12px;'>");
        out.println("        Framework personnalisé - Sprint 2<br>");
        out.println("        Total: " + totalControllers + " contrôleurs, " + totalUrls + " URLs");
        out.println("    </p>");

        out.println("</body>");
        out.println("</html>");
    }
}