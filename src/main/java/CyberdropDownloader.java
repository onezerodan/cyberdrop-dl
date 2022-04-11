import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.monoid.web.Resty;

import java.io.File;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CyberdropDownloader {

    private static String title;

    public static String getHelp() {
        return new String(
                "Usage:\n" +
                        "Run program with these arguments:\n" +
                        "1. Specify url to download\n" +
                        "2. (Optional) Specify destination directory\n" +
                        "\ne.g. java -jar cyberdrop-dl***.jar <url> <directory>"
        );
    }


    public static void download(String url, String path) throws Exception {

        Document doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5").referrer("http://www.google.com").get();

        title = doc.getElementsByAttributeValue("class", "title has-text-centered").text();
        Elements elemts = doc.getElementsByClass("image");

        File saveDir = new File(path + "/" + title);
        if (!saveDir.exists()) {
            saveDir.mkdir();
            System.out.println("Directory [" + saveDir.getAbsolutePath() + "] created.");
        }

        downloadAlbumAsync(elemts, String.valueOf(saveDir));
    }


    private static void downloadAlbumAsync(Elements elements, String path) throws Exception {

        int urlCount = elements.size();

        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<File>> results = new ArrayList<>(urlCount);
        for (final Element element : elements) {

            // These two lines remove illegal characters from url
            URL url = new URL(element.attr("href"));
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

            String elementUrl =  uri.toString();
            String imgTitle = element.attr("title");

            results.add(pool.submit(() -> new Resty()
                    .bytes(elementUrl)
                    .save(new File(path + "/" + imgTitle))));

        }

        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setStyle(ProgressBarStyle.ASCII);

        for (Future<File> file : ProgressBar.wrap(results, pbb)){
            file.get();
        }


        pool.awaitTermination(800, TimeUnit.MILLISECONDS);

        pool.shutdown();
        try {
            if (!pool.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }
        System.out.println("Complete.");
    }

}
