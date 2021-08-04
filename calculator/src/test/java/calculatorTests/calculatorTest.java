package calculatorTests;


import calculator.CalculatorApplication;
import calculator.CalculatorConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;


public class calculatorTest {
    private static final String BASE_URI = "http://localhost:8080/calc/";
    private static final String SWAGGER_URI = "http://localhost:8080/openapi/";
    private static final double serverTimeout = Duration.ofSeconds(10).toMillis();
    private Thread thread;
    private Client client;
    private CalculatorApplication app;

    private String getValidBasicAuth() {
        return "Basic " + Base64.getEncoder().encodeToString("franz:opensesame".getBytes());
    }

    private String getInvalidBasicAuth() {
        return "Basic " + Base64.getEncoder().encodeToString("franz:rejectionhurts".getBytes());
    }


    @Before
    public void setUp() throws Exception {

        HashMap<String, String> props = new HashMap<>();
        props.put("authentication.method", "BASIC");
        CalculatorConfiguration config = new CalculatorConfiguration(props);
        app = new CalculatorApplication(config);
        app.start();

        long startTime = System.currentTimeMillis();
        client = ClientBuilder.newClient();
        while(true) {
            try {
                if (client.target(BASE_URI).path("live").request().get().getStatus() == 200) {
                    break;
                }
            }
            catch (Exception ignored) {
            }
            if (System.currentTimeMillis() - startTime > serverTimeout) {
                throw new RuntimeException("timed out starting server");
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        // This is ok in testing. In production the process will wait for OS Signal
        app.stop();
    }

    @Test
    public void testDivideByZero() {
        Response response = client.target(BASE_URI)
                .path("div")
                .queryParam("first", 2)
                .queryParam("second", 0)
                .request()
                .get();

        assertEquals(500, response.getStatus());
    }

    @Test
    public void testMultiply() {
        assertEquals(new Integer(10),
                client.target(BASE_URI)
                        .path("mul")
                        .queryParam("first", 2)
                        .queryParam("second", 5)
                        .request()
                        .get(Integer.class));
    }

    @Test
    public void testAdd() {
        assertEquals(new Integer(37), client.target(BASE_URI)
                .path("add")
                .queryParam("first", 13)
                .queryParam("second", 24)
                .request()
                .get(Integer.class)

        );
    }

    @Test
    public void testAudit() throws Exception {
        // put a function in the history
        assertEquals(new Integer(10),
                client.target(BASE_URI)
                        .path("mul")
                        .queryParam("first", 2)
                        .queryParam("second", 5)
                        .request()
                        .get(Integer.class));

        // Should require creds
        assertEquals(401, client.target(BASE_URI).path("audit").request().get().getStatus());

        // Should reject bad creds
        assertEquals(401,
                client.target(BASE_URI)
                        .path("audit")
                        .request()
                        .header(HttpHeaders.AUTHORIZATION, getInvalidBasicAuth())
                        .get()
                        .getStatus());

        // Should give history with valid creds
        assertEquals("[{\"operationType\":\"mul\",\"firstNumber\":2,\"secondNumber\":5,\"operationResult\":10}]",
                client.target(BASE_URI)
                        .path("audit")
                        .request()
                        .header(HttpHeaders.AUTHORIZATION, getValidBasicAuth())
                        .get(String.class));
    }

    @Test
    public void testPing() throws Exception {
        assertEquals(200,
                client.target(BASE_URI)
                    .path("ping")
                    .request()
                    .get()
                    .getStatus());
    }

    @Test
    public void testSwaggerUi() throws Exception {
        assertEquals(200,
                client.target(SWAGGER_URI)
                        .path("swagger-ui/index.html")
                        .request()
                        .get()
                        .getStatus());
    }

}
