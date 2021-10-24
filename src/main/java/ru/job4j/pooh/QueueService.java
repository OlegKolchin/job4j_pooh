package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String queueName = req.getSourceName();
        if ("POST".equals(req.httpRequestType())) {
            map.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
            map.get(queueName).offer(req.getParam());
            return new Resp(req.getParam(), "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            ConcurrentLinkedQueue<String> queue = map.get(queueName);
            if (queue != null) {
                return new Resp(queue.poll(), "200");
            }
        }
        return new Resp(null, "204");
    }
}