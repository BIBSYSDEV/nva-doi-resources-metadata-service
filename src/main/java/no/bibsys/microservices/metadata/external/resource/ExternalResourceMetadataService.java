package no.bibsys.microservices.metadata.external.resource;

import no.bibsys.dlr.microservices.sparkjava.common.Route_env;
import no.bibsys.dlr.microservices.sparkjava.common.Route_properties;
import no.bibsys.dlr.microservices.sparkjava.common.SparkJavaMicroServiceRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

/**
 * This class implements /nva-doi-resource-metadata-service/v1
 */
public class ExternalResourceMetadataService extends SparkJavaMicroServiceRest {

    private static final Logger logger = LoggerFactory.getLogger(ExternalResourceMetadataService.class);

    static {
        api_path = "/nva-doi-resource-metadata-service/v1";
        api_verbose = true;
    }

    static String getApi_Path() {
        return api_path;
    }

    public static void main(String[] args) {
        initFromEnvAndArgs(args);
        if (api_port != null) port(api_port);

        before(new MDC_api_filter(), new MDC_artifact_filter(), new MDC_request_filter(), new MDC_git_filter());

        path(api_path, () -> path("/metadata", () -> {
            path("/dc", () -> get("/:url", new Route_GET_metadata_dc_url()));
            path("/doi", () -> get("/:url", new Route_GET_metadata_doi_url()));
            path("/meta", () -> get("/:url", new Route_GET_metadata_meta_url()));
        }));

        get("/properties", new Route_properties(clazz()));
        get("/env", new Route_env());

        after(new MDC_request_response_filter(), new Log_request_response_filter(logger, api_path + "/ping"));
        logger.info("Service is initialised.");
    }

}
