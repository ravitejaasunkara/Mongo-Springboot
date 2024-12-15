package com.ravi.mongo.Service;

import com.ravi.mongo.Controller.PersonController;
import com.ravi.mongo.Documents.Person;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;


    public List<Document> getOldestPersonByCity() {
        UnwindOperation unwindOperation = Aggregation.unwind("addresses");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC,"age");
        GroupOperation groupOperation = Aggregation.group("addresses.city")
                .first(Aggregation.ROOT)
                .as("oldPerson");
        Aggregation aggregation = Aggregation.newAggregation(unwindOperation,sortOperation,groupOperation);
        return mongoTemplate.aggregate(aggregation, Person.class,Document.class).getMappedResults();
    }
}
