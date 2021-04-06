package ru.job4j.dream.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PostsMemStore;
import ru.job4j.dream.store.PostsPsqlStore;
import ru.job4j.dream.store.Store;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PostServletTest {
    @Test
    public void whenCreatePostAndStore() throws ServletException, IOException {
        try (MockedStatic<PostsPsqlStore> storeMockedStatic = Mockito.mockStatic(PostsPsqlStore.class)) {
            String newUserName = "Test User Name";
            Store<Post> memStore = PostsMemStore.instOf();
            storeMockedStatic.when(PostsPsqlStore::instOf).thenReturn(memStore);
            HttpServletRequest mockReq = Mockito.mock(HttpServletRequest.class);
            HttpServletResponse mockResp = Mockito.mock(HttpServletResponse.class);
            Mockito.when(mockReq.getParameter("id")).thenReturn("1");
            Mockito.when(mockReq.getParameter("name")).thenReturn(newUserName);
            new PostServlet().doPost(mockReq, mockResp);
            assertThat(memStore.findAll().iterator().next().getName(), is(newUserName));
        }
    }

    @Test
    public void whenEditPost() throws ServletException, IOException {
        String page = "/post/edit.jsp";
        HttpServletRequest reqMocked = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse respMocked = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcherMocked = Mockito.mock(RequestDispatcher.class);
        HttpSession sessionMocked = Mockito.mock(HttpSession.class);
        Mockito.when(reqMocked.getParameter("action")).thenReturn("edit");
        Mockito.when(reqMocked.getRequestDispatcher(page)).thenReturn(dispatcherMocked);
        Mockito.when(reqMocked.getSession()).thenReturn(sessionMocked);
        Mockito.when(sessionMocked.getAttribute(Mockito.any(String.class))).thenReturn("");
        new PostServlet().doGet(reqMocked, respMocked);
        Mockito.verify(reqMocked, Mockito.times(1)).getRequestDispatcher(page);
        Mockito.verify(dispatcherMocked).forward(reqMocked, respMocked);
    }

    @Test
    public void whenDeletePost() throws ServletException, IOException {
        try (MockedStatic<PostsPsqlStore> storeMocked = Mockito.mockStatic(PostsPsqlStore.class)) {
            String page = "/posts.jsp";
            Store<Post> memStore = PostsMemStore.instOf();
            Post post = new Post(0, "NewPost", "Desc", "timestamp");
            memStore.save(post);
            storeMocked.when(PostsPsqlStore::instOf).thenReturn(memStore);
            HttpServletRequest reqMocked = Mockito.mock(HttpServletRequest.class);
            HttpServletResponse respMocked = Mockito.mock(HttpServletResponse.class);
            RequestDispatcher dispatcherMocked = Mockito.mock(RequestDispatcher.class);
            HttpSession sessionMocked = Mockito.mock(HttpSession.class);
            Mockito.when(reqMocked.getParameter("id")).thenReturn(String.valueOf(post.getId()));
            Mockito.when(reqMocked.getParameter("action")).thenReturn("delete");
            Mockito.when(reqMocked.getRequestDispatcher(page)).thenReturn(dispatcherMocked);
            Mockito.when(reqMocked.getSession()).thenReturn(sessionMocked);
            Mockito.when(sessionMocked.getAttribute(Mockito.any(String.class))).thenReturn("");
            new PostServlet().doGet(reqMocked, respMocked);
            assertThat(memStore.findById(post.getId()), nullValue());
            Mockito.verify(reqMocked, Mockito.times(1)).getRequestDispatcher(page);
            Mockito.verify(dispatcherMocked).forward(reqMocked, respMocked);
        }
    }
}