
package main.java.upload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import jakarta.servlet.ServletContext;

public class UploadConfig {
    public static String getUploadDirectory(ServletContext context) {
        Properties properties = new Properties();
        try (InputStream input = context.getResourceAsStream(
                "/WEB-INF/classes/application.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                    "application.properties introuvable"
                );
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(
                "Erreur de lecture de application.properties", e
            );
        }
        String projectDirectory = properties.getProperty("project.directory");
        String uploadDirectory = properties.getProperty("upload.directory");
        if (projectDirectory == null || projectDirectory.isBlank()) {
            throw new IllegalStateException(
                "La propriété project.directory est obligatoire"
            );
        }

        if (uploadDirectory == null || uploadDirectory.isBlank()) {
            throw new IllegalStateException(
                "La propriété upload.directory est obligatoire"
            );
        }
        // Construire le chemin complet
        Path projectPath = Paths.get(projectDirectory.trim())
                .toAbsolutePath()
                .normalize();

        Path uploadRelativePath = Paths.get(uploadDirectory.trim());

        if (uploadRelativePath.isAbsolute()) {
            throw new IllegalArgumentException(
                "upload.directory doit être un chemin relatif"
            );
        }

        Path uploadPath = projectPath
                .resolve(uploadRelativePath)
                .normalize();

        // Empêcher de sortir du dossier du projet
        if (!uploadPath.startsWith(projectPath)) {
            throw new IllegalArgumentException(
                "Le dossier upload doit rester dans le projet"
            );
        }
        return uploadPath.toString();
    }
}