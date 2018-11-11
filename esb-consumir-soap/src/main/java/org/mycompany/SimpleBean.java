package org.mycompany;

import org.springframework.stereotype.Component;

@Component
public class SimpleBean {

	public void processar(String id) {
		System.out.println("===============================");
		System.out.println(id);
		System.out.println("===============================");
	}
}
