package utils;

import java.util.List;
import java.util.Map;

import annotation.*;

import java.util.ArrayList;
import java.io.File;
import java.net.URL;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class UtilsFunctions {
    public List<Class<?>> getAllClassByPackage(String packageName) {
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        String path = packageName.replace('.', '/');
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL resource = classLoader.getResource(path);
            if (resource == null) {
                throw new RuntimeException("Package not found: " + packageName);
            }
            File directory = new File(resource.getFile());
            if (!directory.exists()) {
                throw new RuntimeException("Directory not found: " + directory);
            }
            for (String fileName : directory.list()) {
                if (fileName.endsWith(".class")) {
                    String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public void scanAllClassesWithAnnotationAndPutMap(List<Class<?>> classes,
            Class<? extends Annotation> annotationClass, String niveau, Map<RouteKey, RouteMapping> urlMappings) throws Exception {
        if (niveau.equals("class")) {
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(annotationClass)) {
                    // annotatedClasses.add(clazz);
                    for (Method method : clazz.getDeclaredMethods()) {
                        // pour le @GetMapping
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            GetMapping mapping = method.getAnnotation(GetMapping.class);
                            addMapping(clazz, method, mapping.value(), "GET", urlMappings);
                        }

                        // pour le @PostMapping
                        if (method.isAnnotationPresent(PostMapping.class)) {
                            PostMapping mapping = method.getAnnotation(PostMapping.class);
                            addMapping(clazz, method, mapping.value(), "POST", urlMappings);
                        }
                    }
                }
            }
        }
    }

    private void addMapping(Class<?> controllerClass, Method method, String url, String httpMethod,
            Map<RouteKey, RouteMapping> urlMappings)
            throws Exception {
        RouteKey key = new RouteKey(url, httpMethod);
        // verification doublon
        if (urlMappings.containsKey(key)) {
            RouteMapping existing = urlMappings.get(key);
            throw new Exception("Doublon detecte\n" + "URL: " + key + "\n"
                    + "methode existante: " + existing.getMethod().getName()
                    + "()\n" + "Nouvelle methode: " + method.getName() + "()\n");
        }
        // Ajout du mapping
        RouteMapping routeMapping = new RouteMapping();
        routeMapping.setController(controllerClass);
        routeMapping.setMethod(method);
        urlMappings.put(key, routeMapping);
    }
}