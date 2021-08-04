package calculatorTests;

import calculator.CalculatorApplication;
import calculator.CalculatorConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retrofit.CalculatorRestApi;
import retrofit.CalculatorRetrofitFactory;
import retrofit2.Call;
import retrofit2.Response;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class TestingRetrofitInterface {

    private static final String BASE_URI = "http://localhost:8080/calc/";
    private Client client;
    private CalculatorApplication app;
    private static final double serverTimeout = Duration.ofSeconds(10).toMillis();

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
    public void testRetrofitInterfacesPing() throws IOException {

        // ping endpoint test
        CalculatorRestApi service = CalculatorRetrofitFactory.build(BASE_URI);
        Response<String> response = service.ping().execute();
        System.out.println(response);
        assertEquals("Hello! World!",  response.body());

        // add endpoint test
    }

    @Test
    public void testRetrofitInterfacesAdd() throws IOException {

        // ping endpoint test
        CalculatorRestApi service = CalculatorRetrofitFactory.build(BASE_URI);
        Response<Integer> response = service.add(1, 2).execute();
        System.out.println(response);
        assertEquals(new Integer(3),  response.body());

        // add endpoint test
    }

}
