package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenPostNonExistentTopic() {
        TopicService topicService = new TopicService();
        Resp rsl = topicService.process(
                new Req("POST", "topic", "weather", "temperature=18")
        );
        assertThat(rsl.status(), is("204"));
    }

    @Test
    public void whenDoublePost() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        topicService.process(
                new Req("POST", "topic", "weather", "temperature=30")
        );
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp rsl = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        assertThat(rsl.text(), is("temperature=30"));
    }

    @Test
    public void whenTest() {
        TopicService topicService = new TopicService();
        String paramForSubscriber2 = "client6565";
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result2.text(), is(""));
    }
}
