package ru.job4j.dream.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5
)
public class UploadServlet extends HttpServlet {
    private static final String IMAGE_FOLDER = "c:\\images\\";
    private static final String DEF_EXT = ".jpg";

    public static void writeFile(String id, Collection<Part> parts) throws IOException {
        File uploadDir = new File(IMAGE_FOLDER);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        for (Part part : parts) {
            if ("image/jpeg".equals(part.getContentType())) {
                part.write(uploadDir + File.separator + id + DEF_EXT);
            }
        }
    }

    public static void deleteFile(String id) {
        for (File file : Objects.requireNonNull(new File(IMAGE_FOLDER).listFiles())) {
            String fileName = id  + DEF_EXT;
            if (fileName.equals(file.getName())) {
                file.delete();
                break;
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        req.setAttribute("id", id);
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            deleteFile(id);
            resp.sendRedirect(req.getContextPath() + "/candidates.do");
            return;
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/photoUpload.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        writeFile(id, req.getParts());
        resp.sendRedirect(req.getContextPath() + "/upload?id=" + id);
    }
}
