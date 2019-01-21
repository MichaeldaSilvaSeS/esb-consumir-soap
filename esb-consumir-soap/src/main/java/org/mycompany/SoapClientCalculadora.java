package org.mycompany;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.soapclient.Calculator;
import br.com.soapclient.CalculatorSoap;

@Component
public class SoapClientCalculadora {

	private CalculatorSoap calculator;
	
	public SoapClientCalculadora() {
		this.calculator = new Calculator().getCalculatorSoap();
	}
	
	public Integer sum(List<Integer> nums) {
		return this.calculator.add(nums.get(0), nums.get(1));
	}
	
	public Integer pow(Integer num) {
		System.out.println("Chamou pow " + num);
		return this.calculator.multiply(num, num);
	}
}
