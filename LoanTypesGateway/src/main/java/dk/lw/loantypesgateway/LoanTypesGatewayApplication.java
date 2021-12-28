package dk.lw.loantypesgateway;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoanTypesGatewayApplication {

    public static void main(String[] args) {
        CamelContext ctx = new DefaultCamelContext();
        ContentTransform routeBuilder = new ContentTransform();

        try {
            ctx.addRoutes(routeBuilder);
            ctx.start();
            Thread.sleep(5000);
            ctx.stop();

        } catch (Exception e) {}
        SpringApplication.run(LoanTypesGatewayApplication.class, args);
    }
}
