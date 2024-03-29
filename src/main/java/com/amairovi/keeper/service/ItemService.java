package com.amairovi.keeper.service;

import com.amairovi.keeper.exception.ItemDoesNotExistException;
import com.amairovi.keeper.model.Item;
import com.amairovi.keeper.repository.ItemRepository;
import com.amairovi.keeper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Item create(String name, String placeId) {
        Item item = new Item();
        item.setName(name);
        item.setPlaceId(placeId);
        String itemId = itemRepository.save(item);
        item.setId(itemId);
        return item;
    }

    public void update(String id, String name, String placeId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemDoesNotExistException("Item with id " + id + " does not exist."));

        item.setName(name);
        item.setPlaceId(placeId);

        itemRepository.update(item);
    }

    public void delete(String id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemDoesNotExistException("Item with id " + id + " does not exist."));

        itemRepository.delete(id);
        userRepository.removeRecentItemId(id);
    }

    public Item findById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemDoesNotExistException("Item with id " + id + " does not exist."));
    }

    public List<Item> getItemsForPlaces(Set<String> placeIds) {
        return placeIds.stream()
                .flatMap(placeId -> itemRepository.findByPlaceId(placeId).stream())
                .collect(toList());
    }

}
