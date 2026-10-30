package main.java.session;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import jakarta.servlet.http.HttpSession;

public class Session {

    private String id;

    private Map<String, Object> attributes;

    private Set<String> initialKeys;

    public Session() {
        this.attributes = new HashMap<>();
        this.initialKeys = new HashSet<>();
    }

    public static Session from(HttpSession httpSession) {
        Session session = new Session();
        session.id = httpSession.getId();
        httpSession.getAttributeNames()
                .asIterator()
                .forEachRemaining(name -> {
                    Object value = httpSession.getAttribute(name);
                    session.attributes.put(name, value);
                    session.initialKeys.add(name);
                });
        return session;
    }

    public void flushTo(HttpSession httpSession) {
        // 1. Supprimer les attributs retirés de Session
        for (String key : initialKeys) {
            if (!attributes.containsKey(key)) {
                httpSession.removeAttribute(key);
            }
        }
        // 2. Ajouter ou mettre à jour les attributs
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            httpSession.setAttribute(key, value);
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }
}