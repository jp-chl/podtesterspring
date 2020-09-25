package jpvr.com.podtesterspring.controller.templates;

public class HelloWorldResponseTemplate {
    private String message;
    private String hostname;
    private int count;

    public HelloWorldResponseTemplate() {
    }

    public HelloWorldResponseTemplate(String message, String hostname, int count) {
        this.message = message;
        this.hostname = hostname;
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
} // class HelloWorldResponseTemplate
