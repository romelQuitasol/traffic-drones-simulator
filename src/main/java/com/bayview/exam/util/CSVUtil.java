package com.bayview.exam.util;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Created by romelquitasol on 22/08/2018.
 */
public class CSVUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtil.class);

  private static final String RESULT_DIRECTORY = "target/";

  /**
   * Get file from resources
   */
  public static File getFile(String fileName) throws IOException {
    Resource resource = new ClassPathResource(fileName);
    URL url = resource.getURL();
    File file = new File(url.getPath());
    return file;
  }


  /**
   * Get all files from folder
   */
  public static File[] getFiles(String folder) throws IOException {
    Resource resource = new ClassPathResource(folder);
    URL url = resource.getURL();
    File[] files = new File(url.getPath()).listFiles();
    return files;
  }

  /**
   * Parse csv data to bean
   */
  public static CsvToBean parseCSVToBean(String filePath, Class clazz) throws IOException {
    Reader reader = Files.newBufferedReader(Paths.get(filePath));
    CsvToBean csvToBean = new CsvToBeanBuilder(reader)
        .withType(clazz)
        .withIgnoreLeadingWhiteSpace(true)
        .build();
    return csvToBean;
  }

  /**
   * Generate csv files by arraylist
   */
  public static File generateCSVBy(List<?> modelList, String fileName)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
    File path = createTempCSVFile(fileName);
    FileOutputStream fos = new FileOutputStream(path);
    Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
    StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder<>(writer)
        .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
        .build();
    sbc.write(modelList);
    writer.close();
    return path;
  }

  /**
   * create temoporary csv file
   */
  public static File createTempCSVFile(String fileName) throws IOException {
    String directory = Files.exists(Paths.get(RESULT_DIRECTORY)) ? RESULT_DIRECTORY : "";
    Path path = Paths.get(directory);
    final File file = new File(path + "/" + fileName + ".csv");
    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }

}
