package calculator;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/calc")
@Produces(MediaType.APPLICATION_JSON)
public class CalculatorResource {

    private final List<ProblemFormat> auditLogs;

    public CalculatorResource() {
        this.auditLogs = new ArrayList<>();
    }

    @GET
    @Path("live")
    public boolean live() {
        return true;
    }

    @GET
    @Path("add")
    public int add(@QueryParam("first") int first, @QueryParam("second") int second){
        // http://localhost:8080/calc/add?first=2&second=3
        int result = first + second;
        auditLogs.add(new ProblemFormat("add", first, second, result));
        return result;
    }

    @GET
    @Path("sub")
    public int sub(@QueryParam("first") int first, @QueryParam("second") int second){
        int result = first - second;
        auditLogs.add(new ProblemFormat("sub", first, second, result));
        return result;
    }

    @GET
    @Path("mul")
    public int mul(@QueryParam("first") int first, @QueryParam("second") int second){
        int result = first*second;
        auditLogs.add(new ProblemFormat("mul", first, second, result));
        return result;
    }

    @GET
    @Path("div")
    public int div(@QueryParam("first") int first, @QueryParam("second") int second) {
        if (second == 0) {
            throw new ClientErrorException("divide by 0 is not allowed", 400);
        }
        int result = first/second;
        auditLogs.add(new ProblemFormat("div", first, second, result));
        return result;
    }

    @GET
    @Path("audit")
    public List<ProblemFormat> audit() {
        return auditLogs;
    }

    @GET
    @Path("ping")
    public String ping(){
        return "Hello! World!";
    }
}

