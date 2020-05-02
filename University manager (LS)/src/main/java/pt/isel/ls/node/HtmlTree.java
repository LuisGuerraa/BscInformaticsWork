package pt.isel.ls.node;

public class HtmlTree {

    private Element html;
    private Element head;
    private Element body;

    public HtmlTree() {
        html = new Element("html");
        head = html.addElem("head");
        body = html.addElem("body");
    }

    public HtmlTree(String title) {
        this();
        addTitle(title);
    }

    public void addTitle(String title) {
        head.addText("title", title);
    }

    public void addHeader1WithLink(String link, String txt) {
        body.addElem("h1").addLink(link, txt);
    }

    public void addHeader2WithLink(String link, String txt) {
        body.addElem("h2").addLink(link, txt);
    }

    public void addHeader3WithLink(String link, String txt) {
        body.addElem("h3").addLink(link, txt);
    }

    public void addHeader1(String headerTitle) {
        body.addText("h1", headerTitle);
    }

    public Element addHeader2(String headerTitle) {
        return body.addText("h2", headerTitle);
    }

    public void addHeader3(String headerTitle) {
        body.addText("h3", headerTitle);
    }

    public Element getHtml() {
        return html;
    }

    public void addToTree(Node node) {
        body.addNode(node);
    }

}
