package com.amairovi.keeper.dto;

import lombok.Value;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Value
public class UserPlace {
    private final String id;
    private final String name;
    private final List<UserPlace> children;

    public Set<String> getHierarchyIds() {
        HashSet<String> ids = new HashSet<>();

        children.stream()
                .map(UserPlace::getHierarchyIds)
                .forEach(ids::addAll);

        ids.add(id);
        return ids;
    }
}