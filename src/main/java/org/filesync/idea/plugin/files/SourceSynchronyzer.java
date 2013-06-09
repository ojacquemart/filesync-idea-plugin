package org.filesync.idea.plugin.files;

import com.intellij.openapi.diagnostic.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class SourceSynchronyzer implements FileVisitor<Path> {

    private static final Logger LOGGER = Logger.getInstance(SourceSynchronyzer.class);
    private final Path source;
    private final Path target;

    public SourceSynchronyzer(Path source, Path target) {
        this.source = source;
        this.target = target;
    }

    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        Path newdir = target.resolve(source.relativize(dir));

        try {
            if (!Files.exists(newdir)) {
                LOGGER.debug("Creating new dir " + newdir);
                Files.copy(dir, newdir);
            }
        } catch (FileAlreadyExistsException e) {
            // Silence!
        } catch (IOException e) {
            LOGGER.error("Unable to create: " + newdir, e);

            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        Path targetFile = target.resolve(source.relativize(file));

        try {
            if (Files.exists(targetFile)) {
                LOGGER.debug("File already exists" + targetFile.toFile());

                // Check last modified time.
                FileTime targetLastModifiedTime = Files.getLastModifiedTime(targetFile);
                FileTime sourceLastModifiedTime = Files.getLastModifiedTime(file);
                if (sourceLastModifiedTime.compareTo(targetLastModifiedTime) > 0) {
                    LOGGER.debug("File has been recently modified" + file);

                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                LOGGER.debug("Copying new file " + file);
                Files.copy(file, targetFile);
            }
        } catch (IOException x) {
            LOGGER.error("Error during copying " + file);
        }

        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        // Fix up modification time.
        Path newdir = target.resolve(source.relativize(dir));
        try {
            FileTime time = Files.getLastModifiedTime(dir);
            Files.setLastModifiedTime(newdir, time);
        } catch (IOException e) {
            LOGGER.error("Unable to copy all attributes to " + newdir, e);
        }

        return FileVisitResult.CONTINUE;
    }

    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        if (exc instanceof FileSystemLoopException) {
            LOGGER.error("Cycle detected: " + file);
        } else {
            LOGGER.error("Unable to copy: " + file, exc);
        }
        return FileVisitResult.CONTINUE;
    }
}
