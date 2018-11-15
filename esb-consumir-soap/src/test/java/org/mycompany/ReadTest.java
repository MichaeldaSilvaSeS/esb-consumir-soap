package org.mycompany;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ReadTest extends CamelTestSupport{
	
    @EndpointInject(uri="direct:readbean")
    private ProducerTemplate producerTemplate;
    
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
    	return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:readbean")
					.bean(ReadBean.class,"read")
				.to("mock:result");
			}
		};
    }
    
    @Test
    public void testRead() throws Exception {
    	super.startCamelContext();
    	final SimpleInDto in = new SimpleInDto();
    	in.setNome("Michael");
    	
    	final SimpleOutDto out = new SimpleOutDto();
    	out.setMensagem("Msg:Michael");
    	
    	resultEndpoint.expectedMessageCount(1);
    	producerTemplate.sendBody(in);
    	
    	resultEndpoint
    		.getExchanges()
    		.forEach(ex -> {
    			assertEquals(out,ex.getIn().getBody());
    		});
    	
    	resultEndpoint.assertIsSatisfied();
    	super.stopCamelContext();
    }	

}
