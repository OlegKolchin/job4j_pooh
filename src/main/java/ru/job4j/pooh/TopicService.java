package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> map = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String type = req.httpRequestType();
        if ("POST".equals(type)) {
            return postProcess(req);
        }
        if ("GET".equals(type)) {
            return getProcess(req);
        }
        return new Resp(null, "204");
    }

    private Resp postProcess(Req req) {
        String topic = req.getSourceName();
        if (map.get(topic) != null) {
            for (ConcurrentLinkedQueue<String> queue : map.get(topic).values()) {
                queue.offer(req.getParam());
            }
            return new Resp("", "200");
        }
        return new Resp("", "204");
    }

    private Resp getProcess(Req req) {
        String topic = req.getSourceName();
        String param = req.getParam();
        map.putIfAbsent(topic, new ConcurrentHashMap<>());
        map.get(topic).putIfAbsent(param, new ConcurrentLinkedQueue<>());
        return map.get(topic).get(param).isEmpty()
                ? new Resp("", "200")
                        : new Resp(map.get(topic).get(param).poll(), "200");
    }
}