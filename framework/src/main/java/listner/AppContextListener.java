package listener;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import utils.RouteKey;
import utils.RouteMapping;
import java.util.List;
import annotation.Controllerako;
import utils.UtilsFunctions;

public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String packageName = context.getInitParameter("package");
        Map<RouteKey, RouteMapping> mappings = new HashMap<>();
        UtilsFunctions utils = new UtilsFunctions();
        List<Class<?>> classes = utils.getAllClassByPackage(packageName);
        try {
            utils.scanAllClassesWithAnnotationAndPutMap(classes,Controllerako.class, "class", mappings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.setAttribute("urlMappings", mappings);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}