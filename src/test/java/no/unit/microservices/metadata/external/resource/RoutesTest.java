package no.unit.microservices.metadata.external.resource;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static spark.Spark.stop;

public class RoutesTest {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @After
    public void tearDown() {
        stop();
    }

    @Test
    public void testEnvRoute() {
        environmentVariables.set("name", "value");
        assertEquals("value", System.getenv("name"));
        String testUrl = "/env";
        String res = TestHelper.request(testUrl);
        assertNotNull(res);
        assertThat(res, CoreMatchers.containsString("200: OK"));
        assertThat(res, CoreMatchers.containsString("\"name\":\"value\""));
    }

    @Test
    public void testPropertiesRoute() {
        String testUrl = "/properties";
        String res = TestHelper.request(testUrl);
        assertNotNull(res);
        assertThat(res, CoreMatchers.containsString("200: OK"));
    }

    @Test
    public void testMetadataRoute_faulty_url() {
        String testUrl = ExternalResourceMetadataService.getApi_Path() + "/metadata/my_wrong_doi_url";
        String res = TestHelper.request(testUrl);
        assertNotNull(res);
        assertThat(res, CoreMatchers.containsString("404: Not Found"));
    }

}
