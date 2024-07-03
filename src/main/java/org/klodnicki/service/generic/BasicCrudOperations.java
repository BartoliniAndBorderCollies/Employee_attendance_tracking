package org.klodnicki.service.generic;

import org.klodnicki.DTO.ResponseDTO;
import org.klodnicki.exception.NotFoundInDatabaseException;

import java.util.List;

public interface BasicCrudOperations<T, K, ID> {

    T create(K object);
    T findById(ID id) throws NotFoundInDatabaseException;
    List<T> findAll();
    T update(ID id, K object);
    ResponseDTO delete(ID id) throws NotFoundInDatabaseException;
}
