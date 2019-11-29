package no.bibsys.microservices.metadata.external.resource;

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

class Route_GET_metadata_doi_url implements Route {

    private static final Logger logger = LoggerFactory.getLogger(Route_GET_metadata_doi_url.class);
    private MetadataExtractor metadataExtractor = new MetadataExtractor();

    @Override
    public Object handle(Request request, Response response) {
        String json = "";
        try {
            final String url = metadataExtractor.validUrl(validRouteParam(request, "url", 1024));
            json = metadataExtractor.getDoiMetadata_json(url);
            response.type(APPLICATION_JSON.asString());
        } catch (Validate.BadRequestParamException e) {
            logger.warn(e.getMessage(), e);
            response.status(BAD_REQUEST_400);
            response.body(e.getLocalizedMessage());
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
