package com.rossotti.basketball.jpa.service;

import java.util.List;

public interface CrudService<T> {
	List<?> listAll();

	T getById(Long id);

	T saveOrUpdate(T domainObject);

	void delete(Long id);
}
