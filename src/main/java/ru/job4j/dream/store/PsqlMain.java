package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

public class PsqlMain {
    private void start() {
        Store<Post> store = PostPsqlStore.instOf();
        store.save(new Post(0, "Zero ID"));
        store.save(new Post(3, "Middle Java Job edited"));
        System.out.println("*** findById() ***");
        Post rsl = store.findById(2);
        System.out.printf("Id: %d, Name: %s\n", rsl.getId(), rsl.getName());
        System.out.println("*** findAll() ***");
        store.findAll().forEach(p -> System.out.printf("Id: %d, Name: %s\n", p.getId(), p.getName()));

    }

    public static void main(String[] args) {
        PsqlMain psqlMain = new PsqlMain();
        psqlMain.start();
    }
}
