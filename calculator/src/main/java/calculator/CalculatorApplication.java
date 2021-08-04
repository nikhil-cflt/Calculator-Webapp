package calculator;


import io.confluent.rest.Application;
import io.confluent.rest.RestConfig;

import org.eclipse.jetty.jaas.JAASLoginService;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.LoginAuthenticator;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Password;

import javax.ws.rs.core.Configurable;
import java.util.HashMap;



public class CalculatorApplication extends Application<CalculatorConfiguration> {

    public CalculatorApplication(CalculatorConfiguration config) {
        super(config);
    }

    @Override
    public void setupResources(Configurable<?> configurable, CalculatorConfiguration calculatorConfiguration) {
        final CalculatorResource resource = new CalculatorResource();
        final SwaggerFilesResource swaggerResource = new SwaggerFilesResource();
        final CalculatorException exception = new CalculatorException();
        configurable.register(resource);
        configurable.register(swaggerResource);
        configurable.register(exception);
    }

    @Override
    protected ConstraintMapping createGlobalAuthConstraint() {
        final Constraint constraint = new Constraint();
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"thinker"});

        final ConstraintMapping mapping = new ConstraintMapping();
        mapping.setConstraint(constraint);
        mapping.setMethod("*");
        mapping.setPathSpec("/calc/audit");
        return mapping;
    }

    @Override
    protected LoginService createLoginService() {
        return new ToyLoginService();
    }

    @Override
    protected IdentityService createIdentityService() {
        return null;
    }

    private static class ToyLoginService extends AbstractLoginService {

        @Override
        protected String[] loadRoleInfo(UserPrincipal user) {
            if (user.getName().equals("franz")) {
                return new String[]{"thinker"};
            }
            return new String[0];
        }

        @Override
        protected UserPrincipal loadUserInfo(String username) {
            if (username.equals("franz")) {
                return new UserPrincipal(username, new Password("opensesame"));
            }
            return null;
        }
    }




    public static void main(String[] args) throws Exception {
       
        HashMap<String, String> props = new HashMap<>();
        props.put("authentication.method", "BASIC");
        CalculatorConfiguration config = new CalculatorConfiguration(props);
        CalculatorApplication app = new CalculatorApplication(config);
        app.start();
        System.out.println("Server started, listening on " + app.server.getURI());
        System.out.println("Example query " + app.server.getURI() + "calc/add?first=1&second=2");
        System.out.println("SwaggerUI started, listening on " + app.server.getURI() + "openapi/swagger-ui/index.html");
        app.join();;
    }


}
