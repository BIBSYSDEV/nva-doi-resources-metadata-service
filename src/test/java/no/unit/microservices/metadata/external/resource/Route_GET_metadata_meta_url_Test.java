package no.unit.microservices.metadata.external.resource;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static spark.Spark.stop;

public class Route_GET_metadata_meta_url_Test {


    @After
    public void tearDown() {
        stop();
    }

    @Test
    public void testMetadataMetaRoute_faulty_url() {
        String testUrl = ExternalResourceMetadataService.getApi_Path() + "/metadata/meta/my_wrong_doi_url";
        String res = TestHelper.request(testUrl);
        assertNotNull(res);
        assertThat(res, CoreMatchers.containsString("400: Bad Request"));
    }

    @Test
    public void testMetadataMetaRoute_real_url() {
        String doi_articles_4 = TestHelper.readTestResourceFile("/doi_article_4.json");
        String testUrl = ExternalResourceMetadataService.getApi_Path() + "/metadata/meta/"
                + URLEncoder.encode("https://doi.org/10.1126/science.aau2582", StandardCharsets.UTF_8);
        String res = TestHelper.request(testUrl);
        assertNotNull(res);
        assertThat(res, CoreMatchers.startsWith("200: OK"));
        assertThat(res, CoreMatchers.containsString(doi_articles_4));
    }

}
