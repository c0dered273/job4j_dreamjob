package ru.job4j.dream.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.CitiesPsqlStore;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class CitiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("getList".equals(action)) {
            retJsonList(req, resp);
        }
    }

    private void retJsonList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Collection<City> cities = CitiesPsqlStore.instOf().findAll();
        JSONObject jsonCities = new JSONObject();
        cities.forEach(e -> jsonCities.put(String.valueOf(e.getId()), e.getName()));
        JSONObject jsonOut = new JSONObject().put("cities", jsonCities);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = resp.getWriter()) {
            writer.println(jsonOut);
            writer.flush();
        }
    }
}
