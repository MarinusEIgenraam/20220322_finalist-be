package nl.finalist.server.routes;


import nl.finalist.server.model.Message;
import nl.finalist.server.service.MessageServices;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileRoutes extends RouteBuilder {

    private final Path path = Paths.get("uploads");
    private final Path library = Paths.get("library");

    @Override
    public void configure() throws Exception {
        from("file-watch:" + path)
                .routeId("register-fileInfo")
                .log("File event: ${header.CamelFileEventType} occurred on file ${header.CamelFileName} at ${header.CamelFileLastModified}")
                .setHeader(Exchange.FILE_NAME, simple("${header.CamelFileName}.${header.CamelFileLastModified}"))
                .to("file:" + library);

    }
}