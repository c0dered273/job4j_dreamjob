package ru.job4j.dream.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.CandidatesPsqlStore;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("user", req.getSession().getAttribute("user"));
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            req.getRequestDispatcher("/candidate/edit.jsp").forward(req, resp);
            return;
        }
        if ("delete".equals(action)) {
            deleteCandidate(req);
        }
        defaultViewCandidates(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        CandidatesPsqlStore.instOf().save(
                new Candidate(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

    private void deleteCandidate(HttpServletRequest req) {
        String id = req.getParameter("id");
        CandidatesPsqlStore.instOf().delete(Integer.parseInt(id));
        UploadServlet.deleteFile(id);
    }

    private void defaultViewCandidates(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        req.setAttribute("candidates", CandidatesPsqlStore.instOf().findAll());
        req.getRequestDispatcher("/candidates.jsp").forward(req, resp);
    }
}
