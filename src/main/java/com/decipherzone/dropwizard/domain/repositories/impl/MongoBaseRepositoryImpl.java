package com.decipherzone.dropwizard.domain.repositories.impl;

import com.decipherzone.dropwizard.domain.annotations.CollectionName;
import com.decipherzone.dropwizard.domain.models.db.MongoDb;
import com.decipherzone.dropwizard.domain.models.generic.BaseModel;
import com.decipherzone.dropwizard.domain.repositories.base.BaseRepository;
import com.decipherzone.dropwizard.exceptions.InvalidDataException;
import com.decipherzone.dropwizard.rest.result.DateSearchResult;
import com.decipherzone.dropwizard.rest.result.SearchResult;
import com.google.common.collect.Lists;
import com.mongodb.DB;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.HashMap;
import java.util.List;

public class MongoBaseRepositoryImpl<T extends BaseModel> implements BaseRepository<T> {
    final MongoDb mongoManager;

    protected final MongoCollection coll;
    protected final Class<T> entityClazz;
    protected CollectionName collectionName;

    public MongoBaseRepositoryImpl(Class<T> clazz, MongoDb mongoManager) throws Exception {
        this.mongoManager = mongoManager;
        collectionName = clazz.getAnnotation(CollectionName.class);
        coll = mongoManager.getJongoCollection(collectionName.name());
        entityClazz = clazz;

        // check for indexes
        String[] indexes = collectionName.indexes();
        for(String index : indexes) {
            coll.ensureIndex(index);
        }

        String[] uniqueIndexes = collectionName.uniqueIndexes();
        for(String index : uniqueIndexes) {
            coll.ensureIndex(index,"{unique:true}");
        }
    }

    public Jongo getJongo() {
        try {
            return mongoManager.getJongo();
        } catch (Exception e) {
            return null;
        }
    }

    protected DB getDB() {
        try {
            return mongoManager.getDB();
        } catch (Exception e) {
            return null;
        }
    }

    public T save(T pojo) {
        if (pojo == null) {
            throw new InvalidDataException("Mongo", "POJO should not be null.");
        }
        Object id = coll.save(pojo).getUpsertedId();
        if (id != null && id instanceof ObjectId) {
            pojo.setId(id.toString());
        }
        return pojo;
    }


    @Override
    public List<T> list() {
        Iterable<T> items = coll.find().as(entityClazz);
        return Lists.newArrayList(items);
    }

    @Override
    public List<T> listNonDeleted() {
        Iterable<T> items = coll.find("{deleted:false}").as(entityClazz);
        return Lists.newArrayList(items);
    }

    @Override
    public HashMap<String, T> listNonDeletedAsMap() {
        Iterable<T> items = listNonDeleted();
        return asMap(items);
    }

    @Override
    public Iterable<T> iterator() {
       return coll.find().as(entityClazz);

    }

    @Override
    public Iterable<T> getAllDeleted() {
        return coll.find("{deleted:true}").as(entityClazz);

    }


    @Override
    public HashMap<String, T> listAsMap() {
        Iterable<T> items = list();
        return asMap(items);
    }

    @Override
    public Iterable<T> findItemsIn(List<ObjectId> ids) {
        return coll.find("{_id:{$in:#}}",ids).as(entityClazz);
    }

    @Override
    public HashMap<String, T> findItemsInAsMap(List<ObjectId> ids) {
        return asMap(findItemsIn(ids));
    }

    protected HashMap<String,T> asMap(Iterable<T> items) {
        HashMap<String,T> map = new HashMap<>();
        for (T item : items) {
            map.put(item.getId(),item);
        }
        return map;
    }

    @Override
    public List<T> save(List<T> models) {
        if (models == null || models.size() == 0) {
            return models;
        }
        coll.insert(models.toArray());
        return models;
    }



    @Override
    public void removeById(String entityId) {
        if (entityId != null && ObjectId.isValid(entityId)) {
            // Fake Delete
            coll.update(new ObjectId(entityId)).with("{$set: {deleted:true,modified:#}}",DateTime.now().getMillis());
        }
    }

    @Override
    public void hardRemoveById(String entityId) {
        if (entityId != null && ObjectId.isValid(entityId)) {
            coll.remove(new ObjectId(entityId));
        }
    }

    public T update(String entityId, T pojo) {
        if (entityId == null || !ObjectId.isValid(entityId) || pojo == null) {
            throw new InvalidDataException("Mongo Repository","Entity or POJO should not be null.");
        }
        pojo.setModified(DateTime.now().getMillis());
        coll.update(new ObjectId(entityId)).with(pojo);
        return pojo;
    }


    public T getById(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return null;
        }
        return coll.findOne(new ObjectId(id)).as(entityClazz);
    }

    @Override
    public T getById(String entityId, String projection) {
        if (entityId == null || !ObjectId.isValid(entityId)) {
            return null;
        }
        return coll.findOne(new ObjectId(entityId)).projection(projection).as(entityClazz);
    }

    public boolean exist(String id) {
        if (id == null || !ObjectId.isValid(id)) {
            return false;
        }
        long count = coll.count("{_id:#}",new ObjectId(id));
        return count > 0;
    }

    @Override
    public Long count() {
        return coll.count();
    }

    @Override
    public Long countActive() {
        return coll.count("{deleted:false, active:true}");
    }


    @Override
    public <E extends T> DateSearchResult<E> findItemsWithDate(long afterDate, long beforeDate, Class<E> clazz) {
        if (afterDate < 0) afterDate = 0;
        if (beforeDate <= 0) beforeDate = DateTime.now().getMillis();
        Iterable<E> items = coll.find("{modified:{$lt:#, $gt:#}}",  beforeDate, afterDate).as(clazz);

        long count = coll.count("{modified:{$lt:#, $gt:#}}",beforeDate,afterDate);

        DateSearchResult<E> results = new DateSearchResult<>();
        results.setValues(Lists.newArrayList(items));
        results.setBeforeDate(beforeDate);
        results.setAfterDate(afterDate);
        results.setTotal(count);
        return results;
    }

    @Override
    public DateSearchResult<T> findItemsWithDate(long afterDate, long beforeDate, String projections) {
        if (afterDate < 0) afterDate = 0;
        if (beforeDate <= 0) beforeDate = DateTime.now().getMillis();
        Iterable<T> items = coll.find("{modified:{$lt:#, $gt:#}}",  beforeDate, afterDate).projection(projections).as(entityClazz);

        long count = coll.count("{modified:{$lt:#, $gt:#}}",beforeDate,afterDate);

        DateSearchResult<T> results = new DateSearchResult<>();
        results.setValues(Lists.newArrayList(items));
        results.setBeforeDate(beforeDate);
        results.setAfterDate(afterDate);
        results.setTotal(count);
        return results;
    }

    @Override
    public SearchResult<T> findItems(int skip, int limit) {
        if (skip < 0 || skip >= limit) {
            skip = 0;
        }
        if (limit > 200) {
            limit = 200;
        }


        Iterable<T> items = coll.find("{deleted:false}").skip(skip).limit(limit).as(entityClazz);

        long count = coll.count("{deleted:false}");

        SearchResult<T> results = new SearchResult<>();
        results.setValues(Lists.newArrayList(items));
        results.setSkip(skip);
        results.setLimit(limit);
        results.setTotal(count);
        return results;
    }
    @Override
    public SearchResult<T> findItems(String sort, int skip, int limit) {
        if (skip < 0 || skip >= limit) {
            skip = 0;
        }
        if (limit > 200) {
            limit = 200;
        }

        Iterable<T> items = coll.find("{deleted:false}").sort(sort).skip(skip).limit(limit).as(entityClazz);

        long count = coll.count("{deleted:false}");

        SearchResult<T> results = new SearchResult<>();
        results.setValues(Lists.newArrayList(items));
        results.setSkip(skip);
        results.setLimit(limit);
        results.setTotal(count);
        return results;
    }





    public void dropCollection() {
        coll.drop();
    }
}
