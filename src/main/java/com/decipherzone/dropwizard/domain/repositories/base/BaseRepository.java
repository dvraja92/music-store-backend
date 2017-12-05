package com.decipherzone.dropwizard.domain.repositories.base;

import com.decipherzone.dropwizard.domain.models.generic.BaseModel;
import com.decipherzone.dropwizard.rest.result.DateSearchResult;
import com.decipherzone.dropwizard.rest.result.SearchResult;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public interface BaseRepository<T extends BaseModel> {
    Iterable<T> iterator();
    Iterable<T> getAllDeleted();
    List<T> list();
    List<T> listNonDeleted();
    HashMap<String,T> listAsMap();
    HashMap<String,T> listNonDeletedAsMap();
    Iterable<T> findItemsIn(List<ObjectId> ids);
    HashMap<String,T> findItemsInAsMap(List<ObjectId> ids);
    T getById(String entityId);
    T getById(String entityId, String projection);
    T save(T model);
    T update(String entityId, T model);
    boolean exist(String entityId);
    List<T> save(List<T> models);
    void removeById(String entityId);
    void hardRemoveById(String entityId);
    Long count();
    Long countActive();
    <E extends T> DateSearchResult<E> findItemsWithDate(long afterDate, long beforeDate, Class<E> clazz);
    DateSearchResult<T> findItemsWithDate(long afterDate, long beforeDate, String projections);
    SearchResult<T> findItems(int skip, int limit);
    SearchResult<T> findItems(String sort, int skip, int limit);
}
