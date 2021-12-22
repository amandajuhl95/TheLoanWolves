package dk.lw.loantypesgateway;

import org.apache.camel.builder.RouteBuilder;

public class ContentTransform extends RouteBuilder
{
    private static final String SOURCE  = "loantypes/se_loantypes/";
    private static final String DESTINATION1  = "loantypes/dk_loantypes/";

    @Override
    public void configure() throws Exception
    {
                from("file:" + SOURCE + "?delete=false")
                .process(new ContentProcessor())
                .to("file:" + DESTINATION1);
    }
}


