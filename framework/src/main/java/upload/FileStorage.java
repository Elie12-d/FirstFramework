
package main.java.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileStorage {

    public static Path save(UploadedFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                "Impossible d'enregistrer un fichier vide"
            );
        }

        // Dossier configuré par le développeur
        String directory = file.getUploadDirectory();

        Path uploadPath = Paths.get(directory)
                .toAbsolutePath()
                .normalize();

        // Création du dossier si nécessaire
        Files.createDirectories(uploadPath);

        // Récupération du nom original
        String originalName = file.getOriginalName();

        String extension = "";

        if (originalName != null) {

            String name = originalName.replace("\\", "/");
            name = name.substring(name.lastIndexOf("/") + 1);

            int index = name.lastIndexOf(".");

            if (index > 0 && index < name.length() - 1) {
                extension = name.substring(index);
            }
        }

        // Génération d'un nom unique
        String filename = UUID.randomUUID() + extension;

        // Destination finale
        Path destination = uploadPath.resolve(filename);

        // Enregistrement physique
        Files.write(destination, file.getBytes());

        return destination;
    }
}
