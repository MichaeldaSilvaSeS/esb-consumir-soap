package org.mycompany;

public class ReadBean {

	public SimpleOutDto read(SimpleInDto in) {
		System.out.println("=================");
		System.out.println(in.getNome());
		System.out.println("=================");
		
		SimpleOutDto out = new SimpleOutDto();
		out.setMensagem("Msg:"+in.getNome());
		return out;
	}
}
