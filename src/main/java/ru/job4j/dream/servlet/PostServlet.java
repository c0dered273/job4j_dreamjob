package ru.job4j.dream.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PostsPsqlStore;

import java.io.IOException;

public class PostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("user", req.getSession().getAttribute("user"));
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            req.getRequestDispatcher("/post/edit.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            deletePost(req);
        }
        defaultViewPosts(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PostsPsqlStore.instOf().save(
                new Post(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }

    private void deletePost(HttpServletRequest req) {
        String id = req.getParameter("id");
        PostsPsqlStore.instOf().delete(Integer.parseInt(id));
    }

    private void defaultViewPosts (HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        req.setAttribute("posts", PostsPsqlStore.instOf().findAll());
        req.getRequestDispatcher("/posts.jsp").forward(req, resp);
    }
}
