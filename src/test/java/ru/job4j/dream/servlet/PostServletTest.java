package ru.job4j.dream.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PostsMemStore;
import ru.job4j.dream.store.PostsPsqlStore;
import ru.job4j.dream.store.Store;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.is;

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
}