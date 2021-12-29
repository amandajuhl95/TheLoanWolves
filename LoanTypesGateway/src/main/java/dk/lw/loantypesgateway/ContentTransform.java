package dk.lw.loantypesgateway;

import org.apache.camel.builder.RouteBuilder;

public class ContentTransform extends RouteBuilder
{


    @Override
    public void configure() throws Exception
    {
                from("file:" + AppSettings.source + "?delete=false")
                .process(new ContentProcessor())
                .to("file:" + AppSettings.destination);
    }
}


