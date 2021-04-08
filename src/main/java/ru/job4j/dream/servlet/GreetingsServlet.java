package ru.job4j.dream.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringJoiner;

public class GreetingsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println("Nice to meet you, " + name);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        StringJoiner joiner = new StringJoiner("");
        String line;
        try (BufferedReader br = req.getReader()) {
            while ((line = br.readLine()) != null) {
                joiner.add(line);
            }
        }
        JSONObject jo = new JSONObject(joiner.toString());
        String text = jo.getJSONObject("data").getString("text");
        JSONObject data = new JSONObject();
        data.put("text", text);
        JSONObject joOut = new JSONObject();
        joOut.put("data", data);
        try (PrintWriter pw = resp.getWriter()) {
            pw.println(joOut);
            pw.flush();
        }
    }
}
