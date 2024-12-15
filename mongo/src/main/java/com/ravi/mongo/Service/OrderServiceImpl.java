package com.ravi.mongo.Service;

import com.ravi.mongo.Controller.PersonController;
import com.ravi.mongo.Documents.User;
import com.ravi.mongo.Dto.OrderDto;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl {

    @Autowired
    MongoTemplate mongoTemplate;


    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    public List<OrderDto> getOrdersWithDetails() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.lookup("customers", "customer_id", "_id", "customer_info"), // Join with customers
                Aggregation.unwind("customer_info"), // Flatten customer info
                Aggregation.lookup("products", "items.product_id", "_id", "products_info"), // Join with products
                Aggregation.unwind("products_info"), // Flatten products info
                Aggregation.lookup("payments", "order_id", "_id", "payment_info"), // Join with payments
                Aggregation.unwind("payment_info"), // Flatten payment info
                Aggregation.project("order_id", "amount", "order_date")
                        .and("customer_info.name").as("customer_name")
                        .and("customer_info.email").as("customer_email")
                        .and("products_info.name").as("product_name")
                        .and("payment_info.payment_method").as("payment_method")
                        .and("payment_info.amount_paid").as("payment_amount")
        );

        AggregationResults<OrderDto> results = mongoTemplate.aggregate(aggregation, "orders", OrderDto.class);
        return results.getMappedResults();
    }

    public List<User> getListOfUsers(String name) {
        Query query = new Query();
//        Query query = new Query(Criteria.where("name").is(name));
//        Update update = new Update();
//        update.set("email","sunkara.pujitha@rps.com");
//        mongoTemplate.updateFirst(query,update,User.class);
//        return mongoTemplate.find(query,User.class);

//        Query query = new Query();
//        query.addCriteria(Criteria.where("name").is(name).and("age").gte(25));


//        Query query = new Query(new Criteria().orOperator(
//                Criteria.where("name").is(name),
//                Criteria.where("age").gte(20),
//                Criteria.where("address.state").is("OH")
//        ));

//        query.addCriteria(Criteria.where("age").gt(25).lt(40));

//        query.addCriteria(Criteria.where("name").regex(name,"i"));

//        query.addCriteria(Criteria.where("name").exists(false));

        query.addCriteria(Criteria.where("age").gte(35).andOperator(
                new Criteria().orOperator(
                        Criteria.where("name").regex("j","i"),
                        Criteria.where("address.state").regex("OH","i")
                )
        ));


        return mongoTemplate.find(query,User.class);
    }

    public Page<User> getUsersByNameAndAge(String name, int age, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criterias = new ArrayList<>();
        criterias.add(Criteria.where("name").regex(name,"i"));
        criterias.add(Criteria.where("age").gte(age));
        query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[0])));
        long total = mongoTemplate.count(query,User.class);
        List<User> users = mongoTemplate.find(query, User.class);
        return new PageImpl<>(users,pageable,total);
    }

    public void groupUsersByAge() {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("age").gte(30));
        GroupOperation groupOperation = Aggregation.group("age").count().as("count");
        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC,"age"));
        ProjectionOperation projectionOperation = Aggregation
                .project("count")
                .and("_id")
                .as("age")
                .andExclude("_id");
        Aggregation aggregation = Aggregation.newAggregation(matchOperation,groupOperation,sortOperation,projectionOperation);
        List<Document> mappedResults = mongoTemplate.aggregate(aggregation, User.class, Document.class).getMappedResults();
        logger.info("grouped results:{}",mappedResults);
    }

}
