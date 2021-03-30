package ru.job4j.dream.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class DownloadServlet extends HttpServlet {
    private static final String IMAGE_FOLDER = "c:\\images\\";
    private static final String DEF_EXT = ".jpg";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("id") + DEF_EXT;
        File downloadFile = null;
        for (File file : Objects.requireNonNull(new File(IMAGE_FOLDER).listFiles())) {
            if (name.equals(file.getName())) {
                downloadFile = file;
                break;
            }
        }
        if (downloadFile != null) {
            String fileName = downloadFile.getName();
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" +
                    URLEncoder.encode(fileName, StandardCharsets.UTF_8) +
                    "\"");
            try (FileInputStream stream = new FileInputStream(downloadFile)) {
                resp.getOutputStream().write(stream.readAllBytes());
            }
        }
    }
}
