package nl.finalist.server.routes;


import nl.finalist.server.model.DTO.FileInfoInput;
import nl.finalist.server.service.FileInfoService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileRoutes extends RouteBuilder {

    private final Path path = Paths.get("uploads");
    private final Path library = Paths.get("library");

    @Autowired
    FileInfoService fileInfoService;

    @Override
    public void configure() throws Exception {
        from("file-watch:" + path + "?events=CREATE")
                .routeId("register-fileInfo")
                .log("File event: ${header.CamelFileEventType} occurred on file ${header.CamelFileName} at ${header.CamelFileLastModified}")
                .setHeader(Exchange.FILE_NAME, simple("${header.CamelFileName}.${header.CamelFileLastModified}"))
                .to("file:" + library)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        FileInfoInput fileInfoInput = FileInfoInput.builder()
                                .fileName((String) exchange.getIn().getHeader("CamelFileNameOnly"))
                                .fileDirectory((String) exchange.getIn().getHeader("CamelFileRelativePath"))
                                .savedDirectory((String) exchange.getIn().getHeader("CamelFileAbsolutePath"))
                                .lastEvent((String) exchange.getIn().getHeader("CamelFileEventType"))
                                .build();

                        fileInfoService.saveFileInfo(fileInfoInput);

                        exchange.getIn()
                                .setBody(fileInfoInput.toFileInfo());
                    }
                })
                .log("File id: ${exchange.getIn().getBody().getId()}");
        from("file-watch:" + path + "?events=MODIFY")
                .routeId("update-fileInfo")
                .log("File event: ${header.CamelFileEventType} occurred on file ${header.CamelFileName} at ${header.CamelFileLastModified}")
                .setHeader(Exchange.FILE_NAME, simple("${header.CamelFileName}.${header.CamelFileLastModified}"))
                .to("file:" + library)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        FileInfoInput fileInfoInput = FileInfoInput.builder()
                                .fileName((String) exchange.getIn().getHeader("CamelFileNameOnly"))
                                .fileDirectory((String) exchange.getIn().getHeader("CamelFileRelativePath"))
                                .savedDirectory((String) exchange.getIn().getHeader("CamelFileAbsolutePath"))
                                .lastEvent((String) exchange.getIn().getHeader("CamelFileEventType"))
                                .build();

                        fileInfoService.updateFile(fileInfoInput);

                        exchange.getIn()
                                .setBody(fileInfoInput.toFileInfo());
                    }
                })
                .log("File id: ${exchange.getIn().getBody().getId()}");
        from("file-watch:" + path + "?events=DELETE")
                .routeId("delete-fileInfo")
                .log("File event: ${header.CamelFileEventType} occurred on file ${header.CamelFileName} at ${header.CamelFileLastModified}")
                .setHeader(Exchange.FILE_NAME, simple("${header.CamelFileName}.${header.CamelFileLastModified}"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        FileInfoInput fileInfoInput = FileInfoInput.builder()
                                .fileName((String) exchange.getIn().getHeader("CamelFileNameOnly"))
                                .fileDirectory((String) exchange.getIn().getHeader("CamelFileRelativePath"))
                                .savedDirectory((String) exchange.getIn().getHeader("CamelFileAbsolutePath"))
                                .lastEvent((String) exchange.getIn().getHeader("CamelFileEventType"))
                                .build();

                        fileInfoService.deleteFile(fileInfoInput);

                        exchange.getIn()
                                .setBody(fileInfoInput.toFileInfo());
                    }
                })
                .log("File id: ${exchange.getIn().getBody().getId()}");
    }
}