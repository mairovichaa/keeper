package com.amairovi.keeper.web;


import com.amairovi.keeper.dto.CreatePlace;
import com.amairovi.keeper.dto.ItemDto;
import com.amairovi.keeper.dto.UpdatePlace;
import com.amairovi.keeper.model.Place;
import com.amairovi.keeper.model.User;
import com.amairovi.keeper.service.PlaceService;
import com.amairovi.keeper.service.UserPlaceService;
import com.amairovi.keeper.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceService placeService;
    private final UserService userService;
    private final UserPlaceService userPlaceService;

    @PostMapping
    public String create(@RequestBody CreatePlace request) {

        String placeName = request.getPlaceName();
        String parentPlaceId = request.getParentPlaceId();

        Place place = placeService.create(placeName, parentPlaceId);
        User user = userService.findById(request.getUserId());

        userPlaceService.save(place, user);

        return place.getId();
    }

    @PutMapping("/{placeId}")
    public void update(@PathVariable String placeId, @RequestBody UpdatePlace request) {
        log.debug("Update place {}: {}.", placeId, request);

        String name = request.getPlaceName();
        String parentPlaceId = request.getParentPlaceId();

        placeService.update(placeId, name, parentPlaceId);
    }

    @DeleteMapping("/{placeId}")
    public void delete(@PathVariable String placeId) {
        log.debug("Delete place {}.", placeId);

        placeService.delete(placeId);
    }

    @GetMapping("/{placeId}/items")
    public List<ItemDto> getAllItems(@PathVariable String placeId) {
        log.debug("Get all items for place with id {}.", placeId);


        return placeService.findItemsFor(placeId)
                .stream()
                .map(item -> {
                    ItemDto dto = new ItemDto();
                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setPlaceId(placeId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
