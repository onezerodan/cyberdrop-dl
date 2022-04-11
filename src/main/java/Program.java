import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;


public class Program {

    public static void main(String[] args) throws Exception {


        String path = null;
        String url = null;


        if ((args.length == 1 && args[0].equals("-h")) || args.length == 0) {
            System.out.println(CyberdropDownloader.getHelp());
            System.exit(0);
        }
        else if (args.length == 1) {
            UrlValidator urlValidator = new UrlValidator();
            if (!urlValidator.isValid(args[0])){
                System.out.println("Invalid url.");
                System.exit(0);
            }
            url = args[0];
            path = ".";

        }

        if (args.length ==  2) {
            if (!checkUrl(args[0])){
                System.out.println("Invalid url.");
                System.exit(0);
            }
            if (!checkPath(args[1])){
                System.out.println("Invalid directory.");
                System.exit(0);
            }

            url = args[0];
            path = args[1];

        }



        CyberdropDownloader.download(url, path);
    }
    private static boolean checkPath(String path) {
        File checkPath = new File(path);
        if (!checkPath.exists()) {
            return false;
        }
        return true;

    }
    private static boolean checkUrl(String url){
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(url)){
            return false;
        }
        return true;
    }
}
