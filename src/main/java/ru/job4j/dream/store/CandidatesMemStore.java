package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CandidatesMemStore implements Store<Candidate> {
    private static final CandidatesMemStore INST = new CandidatesMemStore();
    private static final AtomicInteger ID = new AtomicInteger(4);

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private CandidatesMemStore() {
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static CandidatesMemStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public void delete(int id) {
        candidates.remove(id);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }
}
