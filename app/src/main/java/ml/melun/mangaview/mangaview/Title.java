package ml.melun.mangaview.mangaview;
import java.util.ArrayList;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Title {
    public Title(String n, String t) {
        name = n;
        thumb = t;
    }
    public String getName() {
        return name;
    }
    public String getThumb() {
        return thumb;
    }
    public ArrayList<Manga> getEps(){
        return eps;
    }
    public void fetchEps() {
        //fetch episodes
        try {
            eps = new ArrayList<>();
            Document items = Jsoup.connect("https://mangashow.me/bbs/page.php?hid=manga_detail&manga_name="+name).get();
            for(Element e:items.select("div.slot")) {
                eps.add(0,new Manga(Integer.parseInt(e.attr("data-wrid")),e.selectFirst("div.title").text()));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    private String name;
    private String thumb;
    private ArrayList<Manga> eps;
}
