package no.unit.microservices.metadata.external.resource;

import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static spark.Spark.*;

class TestHelper {

    static String readTestResourceFile(String testFileName) throws NullPointerException {
        InputStream inputStream = MetadataExtractorTest.class.getResourceAsStream(testFileName);
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }


    static String request(String path) {
        setUpRoutes();
        String responseStr = "";
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("url", "https://doi.org/10.1093/afraf/ady029");
            connection.setDoOutput(true);
            connection.connect();
            responseStr += connection.getResponseCode();
            responseStr += ": " + connection.getResponseMessage();
            responseStr += " ; " + IOUtils.toString(connection.getInputStream());
        } catch (IOException e) {
            //ignore
        } finally {
            stop();
            awaitStop();
        }
        return responseStr;
    }

    private static void setUpRoutes() {
        String[] args = new String[]{};
        ExternalResourceMetadataService.main(args);
        awaitInitialization();
    }

}
