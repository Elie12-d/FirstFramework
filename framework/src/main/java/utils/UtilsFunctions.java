package utils;

import java.util.List;
import java.util.Map;

import annotation.GetMapping;

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

    public List<Class<?>> getAllClassesWithAnnotation(List<Class<?>> classes,
            Class<? extends Annotation> annotationClass, String niveau) {
        ArrayList<Class<?>> annotatedClasses = new ArrayList<Class<?>>();
        if (niveau.equals("class")) {
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(annotationClass)) {
                    annotatedClasses.add(clazz);
                }
            }
        } else if (niveau.equals("method")) {
            for (Class<?> clazz : classes) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(annotationClass)) {
                        annotatedClasses.add(clazz);
                        break;
                    }
                }
            }
        } else if (niveau.equals("attribute")) {
            for (Class<?> clazz : classes) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(annotationClass)) {
                        annotatedClasses.add(clazz);
                        break;
                    }
                }
            }
        }
        return annotatedClasses;
    }

    public void scanMappings(List<Class<?>> controllerClasses, Map<String, RouteMapping> urlMappings) {
        for (Class<?> controllerClass : controllerClasses) {
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping mapping = method.getAnnotation(GetMapping.class);
                    String url = mapping.value();

                    // if (url == null || url.isEmpty()) {
                    //     url = "/" + method.getName();
                    // }

                    // Créer le RouteMapping
                    RouteMapping routeMapping = new RouteMapping();
                    routeMapping.setController(controllerClass);
                    routeMapping.setMethod(method);

                    // Stocker dans la Map
                    urlMappings.put(url, routeMapping);
                }
            }
        }
    }
}
