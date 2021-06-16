package com.github.ecstasyawesome.warehouse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ResourceBackupManager {

  private final Map<Path, Path> paths = new HashMap<>();
  private final Path[] resources;
  private boolean state = false;

  public ResourceBackupManager(Path... resources) {
    this.resources = resources;
  }

  public static void deleteAllFiles(Path resource) throws IOException {
    try {
      Files.walk(resource)
          .sorted(Comparator.reverseOrder())
          .forEach(path -> {
            try {
              Files.deleteIfExists(path);
            } catch (IOException ignored) {
            }
          });
    } catch (NoSuchFileException ignored) {
    }
  }

  public void backup() throws IOException {
    if (!state) {
      checkResources();
      try {
        for (var resource : paths.entrySet()) {
          Files.move(resource.getKey(), resource.getValue(), StandardCopyOption.REPLACE_EXISTING);
        }
        state = true;
      } catch (NoSuchFileException ignored) {
      }
    }
  }

  public void restore() throws IOException {
    if (state) {
      for (var resource : paths.entrySet()) {
        var destinationPath = resource.getKey();
        deleteAllFiles(destinationPath);
        Files.move(resource.getValue(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
      }
      state = false;
    }
  }

  private void checkResources() throws IOException {
    paths.clear();
    for (var resource : resources) {
      Objects.requireNonNull(resource);
      if (Files.exists(resource)) {
        paths.put(resource, Files.createTempDirectory(Path.of("."), "tmp_"));
      }
    }
  }
}
