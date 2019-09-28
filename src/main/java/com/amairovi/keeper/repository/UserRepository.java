package com.amairovi.keeper.repository;

import com.amairovi.keeper.configuration.MongoConfiguration;
import com.amairovi.keeper.model.User;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepository {

    private final MongoConfiguration mongoConfiguration;

    public String save(User user) {
        log.debug("Save user: {}", user);
        MongoCollection<Document> users = mongoConfiguration.getUserCollection();
        Document document = new Document()
                .append("email", user.getEmail())
                .append("places", user.getPlaces());

        users.insertOne(document);

        return document.get("_id").toString();
    }

    public Optional<User> findById(String id) {
        log.debug("Find user by id: {}", id);

        MongoCollection<Document> users = mongoConfiguration.getUserCollection();

        Document found = users.find(eq("_id", new ObjectId(id)))
                .first();
        return Optional.ofNullable(found)
                .map(d -> {
                    User user = new User();
                    user.setId(found.getObjectId("_id").toString());
                    user.setEmail(found.getString("email"));
                    ArrayList<String> places = (ArrayList<String>) found.get("places");
                    user.setPlaces(new HashSet<>(places));
                    return user;
                });
    }

}
