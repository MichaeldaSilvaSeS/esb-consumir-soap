package org.mycompany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

@Component
public class AppRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		/*
		from("timer://foo?fixedRate=true&period=30s")
			.to("direct:callsoap");
		*/
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
			.to("direct:readbean")
		;
		
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
					.split()
						.method(Splitter.class, "split(${exchange})")
						.bean(SoapClientCalculadora.class,"pow")
						.log("Ola num ${in.body}")
						.aggregate(constant(true),(Exchange oldEx, Exchange newEx) -> {
							if(oldEx == null) {
								ArrayList<Integer> nums = new ArrayList<Integer>();
								
								Integer num = (Integer) newEx.getIn().getBody();
								nums.add(num);
								
								newEx.getIn().setHeader("LISTA", nums);
								return newEx;
							}
							
							List<Integer> nums = (List<Integer>) oldEx.getIn().getHeader("LISTA");
							Integer num = (Integer) newEx.getIn().getBody();
							nums.add(num);
							
							newEx.getIn().setHeader("LISTA", nums);
							newEx.getIn().setHeader(Exchange.AGGREGATION_COMPLETE_ALL_GROUPS_INCLUSIVE, true);
							return newEx;
						})
						.completionTimeout(500L)
						.process(ex -> {
							System.out.println("Process 0");
							System.out.println(ex.getIn().getHeader("LISTA"));
							System.out.println(ex.getIn().getBody());
							ex.getIn().setBody(ex.getIn().getHeader("LISTA"));
						})
					.end()
			.endChoice()
			.transform(simple("ok"));
		
		from("direct:readbean")
			.bean(ReadBean.class,"read");
	}

}
