package org.klodnicki.service.generic;

import org.klodnicki.DTO.ResponseDTO;
import org.klodnicki.exception.NotFoundInDatabaseException;

import java.util.List;

/**
 * This interface defines basic CRUD (Create, Read, Update, Delete) operations
 * for a generic type T. It provides methods to create, find, update, and delete
 * objects of type T.
 *
 * @param <T>  The type of the entity.
 * @param <K>  The type of the DTO or input object used for create and update operations.
 * @param <ID> The type of the identifier of the entity.
 */
public interface BasicCrudOperations<T, K, ID> {

    T create(K object);
    T findById(ID id) throws NotFoundInDatabaseException;
    List<T> findAll();
    T update(ID id, K object) throws NotFoundInDatabaseException;
    ResponseDTO delete(ID id) throws NotFoundInDatabaseException;
}
