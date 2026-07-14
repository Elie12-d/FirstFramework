package utils;

import java.util.Objects;

public class RouteKey {
    private String url;
    private String method;
    
    public RouteKey() {
    }
    
    public RouteKey(String url, String method) {
        this.url = url;
        this.method = method;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    // Méthodes equals et hashCode (IMPORTANT pour les Maps)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteKey routeKey = (RouteKey) o;
        return Objects.equals(url, routeKey.url) && 
               Objects.equals(method, routeKey.method);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(url, method);
    }
    
    @Override
    public String toString() {
        return method + " " + url;
    }
}