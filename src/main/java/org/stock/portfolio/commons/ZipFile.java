package org.stock.portfolio.commons;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ZipFile {

    private final String filePath;
    private final String destination;

    public ZipFile(String fileDir, String fileName) {
        this.filePath = joinPaths(fileDir, fileName);
        this.destination = createDestination(fileDir, fileName);
    }

    public Collection<String> unzip() throws ZipFileException {
        extractAll(destination, filePath);
        FileVisitor visitor = visit(destination);
        return visitor.paths;
    }

    private static FileVisitor visit(String destination) {
        Path root = Paths.get(destination);
        FileVisitor visitor = new FileVisitor();
        try {
            Files.walkFileTree(root, visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return visitor;
    }

    private static void extractAll(String destination, String filePath) throws ZipFileException {
        try {
            net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(filePath);
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            throw new ZipFileException();
        }
    }

    private static String joinPaths(String pathA, String pathB) {
        return pathA.endsWith(File.separator) ? pathA + pathB : pathA + File.separator + pathB;
    }

    private static String removeExtension(String fileName) {
        String[] segments = fileName.split("\\.");
        if (segments.length < 2) throw new RuntimeException("Could not find extension from file!");
        return segments[segments.length - 1];
    }

    private static String createDestination(String fileDir, String fileName) {
        File destination = new File(fileDir + removeExtension(fileName));
        if (!destination.exists()) {
            boolean success = destination.mkdirs();
            if (!success) throw new RuntimeException(String.format("Could not create directory: %s", destination.getAbsolutePath()));
        }
        return destination.getAbsolutePath();
    }


    static class FileVisitor extends SimpleFileVisitor<Path> {

        final List<String> paths = new ArrayList<>();

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!attrs.isDirectory()) paths.add(file.toString());
            return FileVisitResult.CONTINUE;
        }
    }
}
