package com.amairovi.keeper.repository;

import com.amairovi.keeper.model.User;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.pull;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepository {

    @Qualifier("users")
    private final MongoCollection<Document> users;

    public String save(User user) {
        log.debug("Save user: {}", user);
        Document document = new Document()
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("places", user.getPlaces())
                .append("recentItems", user.getRecentItems());

        users.insertOne(document);

        return document.get("_id").toString();
    }

    public void update(User user) {
        log.debug("Update user: {}", user);
        Document document = new Document()
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("places", user.getPlaces())
                .append("recentItems", user.getRecentItems());

        users.replaceOne(eq("email", user.getEmail()), document);
    }

    public Optional<User> findById(String id) {
        log.debug("Find user by id: {}", id);

        Document found = users.find(eq("_id", new ObjectId(id)))
                .first();
        return Optional.ofNullable(found)
                .map(this::documentToUser);
    }

    public Optional<User> findByEmail(String email) {
        log.debug("Find user by email: {}", email);

        Document found = users.find(eq("email", email))
                .first();
        return Optional.ofNullable(found)
                .map(this::documentToUser);
    }

    public void removeRecentItemId(String itemId) {
        users.updateMany(
                new Document("recentItems", itemId),
                pull("recentItems", itemId)
        );
    }

    private User documentToUser(Document d) {
        User user = new User();
        user.setId(d.getObjectId("_id").toString());
        user.setEmail(d.getString("email"));
        user.setPassword(d.getString("password"));
        ArrayList<String> places = (ArrayList<String>) d.get("places");
        user.setPlaces(new HashSet<>(places));
        ArrayList<String> recentItems = (ArrayList<String>) d.get("recentItems");
        user.setRecentItems(new ArrayList<>(recentItems));
        return user;
    }
}
