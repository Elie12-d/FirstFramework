package main.java.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;

import main.java.annotation.ApiREST;
import main.java.http.HttpMethode;
import main.java.mapping.Mapping;
import main.java.mapping.UrlMethode;
import main.java.upload.UploadedFile;
import main.java.utils.Util;
import main.java.view.ModelAndView;
import main.java.upload.UploadConfig;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, Exception {
        ServletContext context = req.getServletContext();

        String prefix = context.getInitParameter("view-prefix");
        String sufix = context.getInitParameter("view-suffix");
        Object springContext = context.getAttribute("springContext");

        @SuppressWarnings("unchecked")
        HashMap<UrlMethode, Mapping> mapping = (HashMap<UrlMethode, Mapping>) context.getAttribute("mapping");
        // out.println(mapping);
        if (mapping == null) {
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().println("Mapping introuvab2le");
            return;
        }
        String askUrl = req.getRequestURI();
        String contextPath = req.getContextPath();

        askUrl = askUrl.substring(contextPath.length());
        // ici url: ceux qui sont derriere le nom du projet, exemple: /hello,
        // /api/etudiants, /save
        HttpMethode methode = HttpMethode.valueOf(req.getMethod());
        UrlMethode urlMethode = new UrlMethode(askUrl, methode);
        // le url methode , ex : /api/etudiants, GET

        Mapping map = mapping.get(urlMethode);
        if (map != null) {
            Class<?> class1 = map.getControllerClass();
            Method method = map.getMethod();
            Parameter[] a = method.getParameters();

            Class<?> returnType = method.getReturnType();
            if (returnType != ModelAndView.class && !method.isAnnotationPresent(ApiREST.class)) {
                throw new ServletException("La methode " + method + " n'as pas de type de retour valide");
            }
            try {
                Object obj = class1.getDeclaredConstructor().newInstance();

                Object[] arguments = new Object[a.length];
                for (int i = 0; i < a.length; i++) {
                    Parameter parameter = a[i];
                    // String parameterName = parameter.getName();
                    Class<?> parameterType = parameter.getType();
                    if (parameterType == UploadedFile.class) {
                        String parameterName = parameter.getName();
                        Part part = req.getPart(parameterName);
                        if (part == null
                                || part.getSubmittedFileName() == null
                                || part.getSubmittedFileName().isEmpty()) {
                            arguments[i] = null;
                        } else {
                            try (InputStream input = part.getInputStream()) {
                                byte[] bytes = input.readAllBytes();
                                // Lecture de application.properties
                                String uploadDirectory = UploadConfig.getUploadDirectory(context);
                                // Injection du fichier avec son dossier de stockage
                                arguments[i] = new UploadedFile(
                                        part.getSubmittedFileName(),
                                        part.getContentType(),
                                        bytes,
                                        uploadDirectory);
                            }
                        }
                    } else {
                        arguments[i] = this.createObjectFromRequest(parameterType, req);
                    }

                    // String value = req.getParameter(parameterName);
                    // if (value != null) {
                    // arguments[i] = value;
                    // } else {
                    // arguments[i] = null;
                    // }
                }
                Object resultRetour = method.invoke(obj, arguments);

                if (resultRetour != null) {
                    // out.println(resultRetour.toString());
                    if (returnType == ModelAndView.class) {

                        ModelAndView retour = (ModelAndView) resultRetour;
                        addArgToRequest(req, retour.getData());
                        String path = prefix + retour.getView() + "." + sufix;
                        RequestDispatcher dispat = req.getRequestDispatcher(path);
                        dispat.forward(req, resp);
                    } else {
                        resp.setContentType("application/json;charset=UTF-8");
                        if (resultRetour instanceof String) {
                            String retour = (String) resultRetour;
                            resp.getWriter().write(retour);
                        } else {
                            String json = Util.toJSON(resultRetour);
                            resp.getWriter().write(json);
                        }
                    }

                } else {
                    throw new ServletException("Le retour envoyé est null");

                }

            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause != null) {
                    cause.printStackTrace();
                    throw new ServletException(
                            "Erreur dans la méthode du développeur : "
                                    + cause.getMessage(),
                            cause);
                }
                throw new ServletException(e);
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | NoSuchMethodException e) {

                e.printStackTrace();
                throw new ServletException("Erreur : " + e.getMessage(), e);
            }
        } else {
            resp.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("Framework");

            out.println("Recherche :");
            out.println(urlMethode.getUrl());
            out.println(urlMethode.getMethode());
            out.println(urlMethode.hashCode());
            out.println("Url Introuvable, voici ceux qui existent :");
            for (UrlMethode url : mapping.keySet()) {
                Mapping nMap = mapping.get(url);

                out.println(
                        url.getUrl() + " --> " + nMap.getClass().getSimpleName() + " | " + nMap.getMethod().getName());

            }
        }
    }

    private void addArgToRequest(HttpServletRequest req, Map<String, Object> data) {
        for (String argument : data.keySet()) {
            Object value = data.get(argument);
            req.setAttribute(argument, value);
        }
    }

    private Object createObjectFromRequest(Class<?> clazz, HttpServletRequest req) throws Exception {

        Object object = clazz.getDeclaredConstructor().newInstance();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String value = req.getParameter(fieldName);
            if (value != null) {
                field.setAccessible(true);
                if (field.getType() == String.class) {
                    field.set(object, value);
                } else if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(object, Integer.parseInt(value));
                }
            }
        }
        return object;
    }

}