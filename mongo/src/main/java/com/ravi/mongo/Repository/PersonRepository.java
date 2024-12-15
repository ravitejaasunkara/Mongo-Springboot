package com.ravi.mongo.Repository;

import com.ravi.mongo.Documents.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends MongoRepository<Person,String> {

}
