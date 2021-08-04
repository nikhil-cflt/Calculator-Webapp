package calculator;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/openapi/")
public class SwaggerFilesResource {

    @GET
    @Path("{path:.*}")
    public Response staticResources(@PathParam("path") final String path) {
        System.out.println(path);
        final InputStream resource = this.getClass().getResourceAsStream("/WEB-INF/openapi/" + path);
        System.out.println(path);
        System.out.println(resource);

        return null == resource
                ? Response.status(NOT_FOUND).build()
                : Response.ok().entity(resource).build();
    }
}
