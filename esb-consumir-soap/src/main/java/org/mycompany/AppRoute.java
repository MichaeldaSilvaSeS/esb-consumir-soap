package org.mycompany;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class AppRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("timer://foo?fixedRate=true&period=30s")
			.to("direct:callsoap");
		
		/*
		from("timer://foo?fixedRate=true&period=30s")
			.setBody(constant(Arrays.<Integer>asList(1,2)))
			.bean(SoapClientCalculadora.class,"sum")
			.log("${body[0]}");
		*/
		
		restConfiguration()
			.port(8500)
			.enableCORS(true)
			.contextPath("/api")
			.dataFormatProperty("prettyPrint", "true")
			.bindingMode(RestBindingMode.json);
		
		rest()
			.get("/callsoap")
				.consumes("application/json")
				.produces("text/plain")
			.to("direct:callsoap")
			.post("/get/{id}")
				.consumes("application/json")
				.produces("text/plain")
				.type(SimpleInDto.class)
				.outType(SimpleOutDto.class)
			.to("direct:callbean")
			.post("/read")
				.consumes("application/json")
				.produces("text/plain")
				.type(SimpleInDto.class)
				.outType(SimpleOutDto.class)
			.to("direct:readbean");
		
		from("direct:callsoap")
			.setBody(constant(Arrays.<Integer>asList(1,2)))
				.bean(SoapClientCalculadora.class,"sum")
				.transform(simple("ok"))
			.log("${body[0]}");
		
		from("direct:callbean")
			.log("${body[0]}")
			.choice()
				.when()
					.simple("${in.body.nome} == 'Michael'")
						.setHeader(Exchange.HTTP_RESPONSE_CODE,constant(500))
						.transform(simple("erro"))
					.otherwise()
						.bean(SimpleBean.class,"processar(${header.id})")
						.transform(simple("ok"));
		
		from("direct:readbean")
			.bean(ReadBean.class,"read");
	}

}
