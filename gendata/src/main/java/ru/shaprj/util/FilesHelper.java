package ru.shaprj.util;
/*
 * Created by O.Shalaevsky on 24.04.2020
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;

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
    public static void saveData(byte[] data, int bufSize, int blockOffset, String filePrefix){

        if(data.length < bufSize){
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
    public static void saveDataToFile(byte[] data, String fileName){

        try(FileOutputStream fos = new FileOutputStream(fileName, true)){

            fos.write(data);

        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
    }

}
