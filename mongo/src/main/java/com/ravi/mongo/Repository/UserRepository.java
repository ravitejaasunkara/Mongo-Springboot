package com.ravi.mongo.Repository;

import com.ravi.mongo.Documents.Address;
import com.ravi.mongo.Documents.User;
import com.ravi.mongo.Dto.StreetLike;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.temporal.ValueRange;
import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{ 'age' :{ $gte:?0 } }", fields = "{ 'age':1,'_id':0}")
    List<Document> getAge(int age);

    @Query(value = "{ 'age':?0}")
    Document getByAge(int age);

    //@Query(value = "{ 'address.city':?0 }")
    List<Document> findByAddressCity(String city);

    List<Document> findByAddressStreet(String street);

    @Query(value = "{ 'address.street': { $regex:?0, $options:'i' } }",fields = "{'address.street':1,'_id':0 }")
    List<StreetLike> findByAddressStreetLike(String street);

    List<Document> findByEmail(String email);

    @Query(value = "{ 'email': { $regex:?0, $options:'i' }}")
    List<Document> findByEmailLike(String email);

    @Query(value = "{ 'email': { $regex:?0, $options:'i' }}",fields = "{ 'email':1,'_id':1}")
    List<Document> findByEmailLikeProjection(String email);


    @Query(value = "{ 'age':?0,'name': { $regex:?1, $options:'i'}}",fields = "{'name':1,'age':1}")
    List<Document> findByAgeAndName(int age,String name);

    @Query(value = "{ $or: [ {'age':?0},{'name': { $regex:?1, $options:'i'}}] }",fields = "{'name':1,'age':1}")
    List<Document> findByAgeOrName(int age,String name);


    @Query(value = "{ 'name': { $regex:?0, $options:'i' }}",count = true)
    long checkNameExists(String email);

    void deleteByAge(int age);

    @Query(value = "{ 'address.city' : { $regex:?0, $options: 'i' } }", fields = "{ 'address': 1, '_id': 0 }")
    Address getAddressByCity(String city);





}
