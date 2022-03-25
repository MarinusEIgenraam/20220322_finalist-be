package nl.finalist.server.routes;

import nl.finalist.server.model.Message;
import nl.finalist.server.service.MessageServices;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
class RestApi extends RouteBuilder {

    @Value("${server.port}")
    String serverPort;

    @Value("${finalist.api.path}")
    String contextPath;

    @Override
    public void configure() {

        CamelContext context = new DefaultCamelContext();

        restConfiguration().contextPath(contextPath)
                .port(serverPort)
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiProperty("cors", "true")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        rest("/api/").description("Teste REST Service")
                .id("api-route")
                .post("/message")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .bindingMode(RestBindingMode.auto)
                .type(Message.class)
                .enableCORS(true)

                .to("direct:remoteService");

        from("direct:remoteService")
                .routeId("direct-route")
                .tracing()
                .log(">>> ${body.id}")
                .log(">>> ${body.name}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message bodyIn = (Message) exchange.getIn()
                                .getBody();

                        MessageServices.example(bodyIn);

                        exchange.getIn()
                                .setBody(bodyIn);
                    }
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));
    }
}
