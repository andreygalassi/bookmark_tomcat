package br.com.agrego.sys.template;

import br.com.agrego.sys.exeption.MyException;

public interface MyBCInterface<T> {

	void save(T bean) throws MyException;
	
}
