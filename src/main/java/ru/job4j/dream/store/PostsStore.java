package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostsStore {
    private static final PostsStore INST = new PostsStore();
    private static final AtomicInteger ID = new AtomicInteger(4);

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostsStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Junior should suffer", "23-03-2021"));
        posts.put(2, new Post(2, "Middle Java Job", "Works the most", "01-01-2014"));
        posts.put(3, new Post(3, "Senior Java Job", "400kk/sec", "12-01-2020"));
    }

    public static PostsStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

}
