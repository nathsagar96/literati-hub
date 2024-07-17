package dev.sagar.literatihub.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FileUtils {

  public static byte[] readFileFromLocation(String fileUrl) {
    if (StringUtils.isBlank(fileUrl)) {
      return null;
    }
    try {
      Path filePath = new File(fileUrl).toPath();
      return Files.readAllBytes(filePath);
    } catch (IOException exception) {
      log.warn("No file found in the path {}", fileUrl);
    }
    return null;
  }
}
