package no.bibsys.microservices.metadata.external.resource;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static spark.Spark.stop;

public class Route_GET_metadata_dc_url_Test {

    @After
    public void tearDown() {
        stop();
    }

    @Test
    public void testMetadataDcRoute_real_url() {
        String doi_articles_3 = TestHelper.readTestResourceFile("/doi_article_3.txt");
        String testUrl = ExternalResourceMetadataService.getApi_Path() + "/metadata/dc/"
                + URLEncoder.encode("http://doi.org/10.3886/E101441V1", StandardCharsets.UTF_8);
        String res = TestHelper.request(testUrl);
        assertNotNull(res);
        assertThat(res, CoreMatchers.startsWith("200: OK"));
        assertThat(res, CoreMatchers.containsString(doi_articles_3));
    }

    @Test
    public void testMetadataDcRoute_faulty_url() {
        String testUrl = ExternalResourceMetadataService.getApi_Path() + "/metadata/dc/my_wrong_doi_url";
        String res = TestHelper.request(testUrl);
        assertNotNull(res);
        assertThat(res, CoreMatchers.containsString("400: Bad Request"));
    }
}
