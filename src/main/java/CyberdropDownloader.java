import me.tongfei.progressbar.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.monoid.web.Resty;

import java.io.File;

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
        Elements imgUrls = doc.getElementsByClass("image");

        File saveDir = new File(path + "/" + title);
        if (!saveDir.exists()) {
            saveDir.mkdir();
            System.out.println("Directory [" + String.valueOf(saveDir.getAbsolutePath()) + "] created.");
        }

        downloadImagesAsync(imgUrls, String.valueOf(saveDir));
    }


    private static void downloadImagesAsync(Elements imgElements, String path) throws Exception {

        int urlCount = imgElements.size();

        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<File>> results = new ArrayList<>();
        for (final Element element : imgElements) {

            String imgUrl = element.attr("href").replace(" ", "%20");
            String imgTitle = element.attr("title");

            results.add(pool.submit(() -> new Resty()
                    .bytes(imgUrl)
                    .save(new File(path + "/" + imgTitle))));

        }

        ProgressBar pb = new ProgressBar("Downloading [" + title + "]", urlCount);

        for (Future<File> fr : results) {
            File file = fr.get();
            pb.step();
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
