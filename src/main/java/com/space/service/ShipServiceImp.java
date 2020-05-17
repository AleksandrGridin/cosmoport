package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exception.BadRequestException;
import com.space.exception.NotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepo;
import com.space.service.checkIsValid.CheckValidParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShipServiceImp implements ShipService{

    private final ShipRepo shipRepo;

    @Autowired
    public ShipServiceImp(ShipRepo shipRepo) {
        this.shipRepo = shipRepo;
    }

    @Override
    public List<Ship> getOrderedAllShips(String name, String planet, ShipType shipType, Long after, Long before,
                                         Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                         Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder order,
                                         Integer pageNumber, Integer pageSize) {


        return filteredShips(getAllShips(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating),
                order, pageNumber, pageSize);
    }
    @Override
    public List<Ship> getAllShips(String name, String planet, ShipType shipType, Long after, Long before,
                                  Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                                  Integer maxCrewSize, Double minRating, Double maxRating) {
        List<Ship> listShips = shipRepo.findAll();

        if (name != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getName().contains(name))
                    .collect(Collectors.toList());
        }
        if (planet != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getPlanet().contains(planet))
                    .collect(Collectors.toList());
        }
        if (shipType != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getShipType().equals(shipType))
                    .collect(Collectors.toList());
        }
        if (after != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getProdDate().after(new Date(after)))
                    .collect(Collectors.toList());
        }
        if (before != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getProdDate().before(new Date(before)))
                    .collect(Collectors.toList());
        }
        if (isUsed != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getUsed().equals(isUsed))
                    .collect(Collectors.toList());
        }
        if (minSpeed != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getSpeed() >= minSpeed)
                    .collect(Collectors.toList());
        }
        if (maxSpeed != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getSpeed() <= maxSpeed)
                    .collect(Collectors.toList());
        }
        if (minCrewSize != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getCrewSize() >= minCrewSize)
                    .collect(Collectors.toList());
        }
        if (maxCrewSize != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getCrewSize() <= maxCrewSize)
                    .collect(Collectors.toList());
        }
        if (minRating != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getRating() >= minRating)
                    .collect(Collectors.toList());
        }
        if (maxRating != null) {
            listShips = listShips.stream()
                    .filter(ship -> ship.getRating() <= maxRating)
                    .collect(Collectors.toList());
        }
        return listShips;
    }

    @Override
    public Ship create(Ship ship) {
        Ship createShip;
        try {
            CheckValidParam.checkName(ship.getName());
            CheckValidParam.checkName(ship.getPlanet());
            Objects.requireNonNull(ship.getShipType());
            CheckValidParam.checkDate(ship.getProdDate());
            CheckValidParam.checkSpeed(ship.getSpeed());
            CheckValidParam.checkCrewSize(ship.getCrewSize());

            ship.setSpeed((double) Math.round(ship.getSpeed() * 100) / 100);
            if (ship.getUsed() == null) {
                ship.setUsed(false);
            }
            ship.setRating(rating(ship));
            createShip = shipRepo.save(ship);
        } catch (Exception e) {
            throw new BadRequestException();
        }
        return createShip;
    }

    @Override
    public void deleteShip(Long id) {
        if (!shipRepo.existsById(id)) {
            throw new NotFoundException();
        }
        shipRepo.deleteById(id);
    }

    @Override
    public Ship edit(Ship ship, Long id) {
        Ship editShip = get(id);

        if (ship == null) {
            throw new BadRequestException();
        }
        if (CheckValidParam.checkName(ship.getName())) {
            editShip.setName(ship.getName());
        }
        if (CheckValidParam.checkName(ship.getPlanet())) {
            editShip.setPlanet(ship.getPlanet());
        }
        if (ship.getShipType() != null) {
            editShip.setShipType(ship.getShipType());
        }
        if (CheckValidParam.checkDate(ship.getProdDate())) {
            editShip.setProdDate(ship.getProdDate());
        }
        if (ship.getUsed() != null) {
            editShip.setUsed(ship.getUsed());
        }
        if (CheckValidParam.checkSpeed(ship.getSpeed())) {
            editShip.setSpeed(ship.getSpeed());
        }
        if (CheckValidParam.checkCrewSize(ship.getCrewSize())) {
            editShip.setCrewSize(ship.getCrewSize());
        }
        editShip.setRating(rating(editShip));
        return shipRepo.save(editShip);
    }

    @Override
    public Ship get(Long id) {
        if (!shipRepo.existsById(id)) {
            throw new NotFoundException();
        }
        return shipRepo.findById(id).orElse(null);
    }

    public List<Ship> filteredShips(final List<Ship> shipList, ShipOrder order, Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber == null ? 0 : pageNumber;
        pageSize = pageSize == null ? 3 : pageSize;

        return shipList.stream()
                .sorted(getComparator(order))
                .skip(pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    private Comparator<Ship> getComparator(ShipOrder order) {
        if (order == null) {
            return Comparator.comparing(Ship::getId);
        }
        Comparator<Ship> comparator = null;
        switch (order.getFieldName()) {
            case "id":
                comparator = Comparator.comparing(Ship::getId);
                break;
            case "speed":
                comparator = Comparator.comparing(Ship::getSpeed);
                break;
            case "prodDate":
                comparator = Comparator.comparing(Ship::getProdDate);
                break;
            case "rating":
                comparator = Comparator.comparing(Ship::getRating);
        }

        return comparator;
    }

    private Double rating(Ship ship) {
        Double rating = 0.0;
        if (Objects.nonNull(ship)) {
            if (ship.getUsed()) {
                rating = (80 * ship.getSpeed() * 0.5) / ((3019 - ship.getProdDate().toLocalDate().getYear()) + 1);
            } else {
                rating = (80 * ship.getSpeed() * 1) / ((3019 - ship.getProdDate().toLocalDate().getYear()) + 1);
            }
        }
        return (double) Math.round(rating * 100) / 100;
    }
}
