package detect.object;


public class AssertURL extends Action {
    String expectedUrl;
    public AssertURL(String expectedUrl) {
        this.expectedUrl = expectedUrl;
    }

    public String getExpectedUrl() {
        return expectedUrl;
    }
}
