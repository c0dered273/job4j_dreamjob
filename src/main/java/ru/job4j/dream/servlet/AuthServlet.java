package ru.job4j.dream.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.UserStore;
import ru.job4j.dream.store.UsersPsqlStore;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AuthServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        UserStore<User> users = UsersPsqlStore.instOf();
        User user = new User(-1, "", email, password);
        if (users.checkPass(user)) {
            user = users.findByEmail(email);
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            logger.info("Успешный вход в систему: {}", email);
            resp.sendRedirect(req.getContextPath());
        } else {
            logger.info("Не верный email или пароль: {}, {}", email, password);
            req.setAttribute("error", "Не верный email или пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}