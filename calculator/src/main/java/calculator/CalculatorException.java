package calculator;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CalculatorException extends Exception implements ExceptionMapper<ClientErrorException> {


    @Override
    public Response toResponse(ClientErrorException exception) {
        return Response.status(500).entity(exception.getMessage()).type("text/plain").build();
    }

}
