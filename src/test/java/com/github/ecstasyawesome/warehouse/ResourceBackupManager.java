package com.github.ecstasyawesome.warehouse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public final class ResourceBackupManager {

  private final Map<Path, Path> paths = new HashMap<>();
  private boolean state = false;

  public ResourceBackupManager(Path... resources) {
    IntStream.range(0, resources.length).forEach(index -> {
      var path = resources[index];
      try {
        if (Files.exists(path)) {
          paths.put(path, Files.createTempDirectory("warehouse_tmp_"));
        }
      } catch (IOException exception) {
        throw new NullPointerException();
      }
    });

  }

  public void backup() throws IOException {
    if (!state) {
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
    }
  }

  private void deleteAllFiles(Path resource) throws IOException {
    Files.walk(resource)
        .sorted(Comparator.reverseOrder())
        .forEach(path -> {
          try {
            Files.deleteIfExists(path);
          } catch (IOException ignored) {
          }
        });
  }
}
