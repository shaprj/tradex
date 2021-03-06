package ru.shaprj.common.utils;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public class FilesHelper {

    private static Logger log = LogManager.getLogger(FilesHelper.class);

    /*
     * Save bytes to file via limited buffer
     *
     * @param data bytes to save
     * @param bufSize size of buffer
     * @param blockOffset file name postfix value
     * @param filePrefixe file name prefix value
     * */
    public static void saveData(byte[] data, int bufSize, int blockOffset, String filePrefix) {

        if (data.length < bufSize) {
            saveDataToFile(data, String.format("%s_%s", filePrefix, blockOffset));
        } else {
            saveDataToFile(Arrays.copyOfRange(data, 0, bufSize), String.format("%s_%s", filePrefix, blockOffset));
            saveData(Arrays.copyOfRange(data, bufSize, data.length), bufSize, blockOffset + 1, filePrefix);
        }

    }

    /*
     * Save bytes to file
     *
     * @param data bytes to save
     * @param fileName name of file
     * */
    public static void saveDataToFile(byte[] data, String fileName) {

        int bufferSize = 100 * 1024 * 1024;

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName, true), bufferSize)) {

            bos.write(data);

        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
    }

    /*
    *
    * Read file strings
    *
    * @param fileName name of file
    * @param stringsConsumer consumer of string
    *
    * */

    public static void readDataFromFile(String fileName, Consumer<String> stringConsumer){

        try (LineIterator it = FileUtils.lineIterator(new File(fileName), "UTF-8")) {

            while (it.hasNext()) {
                stringConsumer.accept(it.nextLine());

            }
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }

    }

}
