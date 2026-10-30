package main.java.utils;

import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

public class ResponseHelper {
    public static void handleUnauthorized(HttpServletResponse resp, boolean api) throws IOException {

        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setCharacterEncoding("UTF-8");

        if (api) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(
                    "{\"error\":\"Unauthorized\",\"message\":\"Authentification requise\"}");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write(
                    "<!DOCTYPE html>" +
                            "<html lang=\"fr\">" +
                            "<head><meta charset=\"UTF-8\"><title>401 - Non authentifié</title></head>" +
                            "<body><h1>401 - Non authentifié</h1>" +
                            "<p>Vous devez vous connecter pour accéder à cette page.</p>" +
                            "</body></html>");
        }
    }

    public static void handleForbidden(
            HttpServletResponse resp,
            boolean api) throws IOException {

        resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        resp.setCharacterEncoding("UTF-8");

        if (api) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(
                    "{\"error\":\"Forbidden\",\"message\":\"Rôle insuffisant\"}");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write(
                    "<!DOCTYPE html>" +
                            "<html lang=\"fr\">" +
                            "<head><meta charset=\"UTF-8\"><title>403 - Accès interdit</title></head>" +
                            "<body><h1>403 - Accès interdit</h1>" +
                            "<p>Vous n'avez pas le rôle nécessaire pour accéder à cette page.</p>" +
                            "</body></html>");
        }
    }

}