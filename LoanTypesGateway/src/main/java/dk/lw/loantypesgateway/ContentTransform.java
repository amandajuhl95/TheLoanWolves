package dk.lw.loantypesgateway;

import org.apache.camel.builder.RouteBuilder;

public class ContentTransform extends RouteBuilder
{
    private static final String SOURCE  = "C:/Users/benja/OneDrive/Skrivebord/SI Big Project/camel/Test_se_csvfile/";
    private static final String DESTINATION1  = "C:/Users/benja/OneDrive/Skrivebord/SI Big Project/camel/Test_dk_csvfile/";

    @Override
    public void configure() throws Exception
    {
                from("file:" + SOURCE + "?delete=false")
                .process(new ContentProcessor())
                .to("file:" + DESTINATION1);
    }
}


