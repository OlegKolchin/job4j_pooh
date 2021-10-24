package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] arr = content.split("/");
        String httpRequestType = arr[0].replaceAll(" ", "");
        String poohMode = arr[1];
        String sourceName = arr[2].split(" ")[0];
        String param = getParam(content);
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    private static String getParam(String content) {
        String[] arr = content.split("/");
        if (content.startsWith("GET") && arr[1].equals("topic")) {
            return arr[3].split(" ")[0];
        }
        if (content.startsWith("GET") && arr[1].equals("queue")) {
            return "";
        }
        return arr[6].split(System.lineSeparator())[2];
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}