package no.bibsys.microservices.metadata.external.resource;

import com.google.gson.Gson;
import no.bibsys.dlr.microservices.sparkjava.common.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;

import static no.bibsys.dlr.microservices.sparkjava.common.Validate.validRouteParam;
import static org.eclipse.jetty.http.HttpStatus.*;
import static org.eclipse.jetty.http.MimeTypes.Type.APPLICATION_JSON;

class Route_GET_metadata_dc_url implements Route {

    private static final Logger logger = LoggerFactory.getLogger(Route_GET_metadata_doi_url.class);
    private MetadataExtractor metadataExtractor;

    @Override
    public Object handle(Request request, Response response) {
        String json = "";
        try {
            metadataExtractor = new MetadataExtractor();
            final String url = metadataExtractor.validUrl(validRouteParam(request, "url", 1024));
            final MetadataMap metadata = metadataExtractor.extract_metadata_from_resource_content(url);
            response.type(APPLICATION_JSON.asString());
            json = new Gson().toJson(metadata.getMap());
        } catch (Validate.BadRequestParamException e) {
            logger.warn(e.getMessage(), e);
            response.status(BAD_REQUEST_400);
            return e.getMessage();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            response.status(SERVICE_UNAVAILABLE_503);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.status(INTERNAL_SERVER_ERROR_500);
        }
        return json;
    }
}
