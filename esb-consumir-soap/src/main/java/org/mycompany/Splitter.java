package org.mycompany;

import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class Splitter {

	public List<Integer> split(Exchange ex) {
		SimpleInDto in = (SimpleInDto) ex.getIn().getBody();
		return in.getNums();
	}
}
