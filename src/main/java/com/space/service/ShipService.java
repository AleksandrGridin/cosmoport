package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.List;

public interface ShipService {

    List<Ship> getOrderedAllShips(String name, String planet, ShipType shipType, Long after, Long before,
                                  Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                  Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order,
                                  Integer pageNumber, Integer pageSize);

    List<Ship> getAllShips(String name, String planet, ShipType shipType, Long after, Long before,
                           Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                           Integer maxCrewSize, Double minRating, Double maxRating);

    Ship create(Ship ship);
    void deleteShip(Long id);
    Ship edit(Ship ship, Long id);
    Ship get(Long id);


}
