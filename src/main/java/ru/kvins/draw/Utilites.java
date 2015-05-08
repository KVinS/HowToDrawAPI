/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kvins.draw;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author KVinS
 */
public class Utilites {

    public enum SortType {

        NEW, OLD, VIEWS, RATING
    }

    public static void writeImageToResponse(String img, HttpServletResponse response) {
        try {
            InputStream is = null;
            if (img.contains("http")) {
                is = new URL(img).openStream();
            } else {
                is = new FileInputStream(img);
            }

            response.setIntHeader("Content-Length", is.available());
            response.setContentType("image/png");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();
        } catch (IOException ex) {
            //log.info("Error writing file to output stream. Filename was '{}'", fileName, ex);
            throw new RuntimeException("IOError reading '" + img + "' to output stream: " + ex.toString());
        }
    }
}
