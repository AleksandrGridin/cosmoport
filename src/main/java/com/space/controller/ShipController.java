package com.space.controller;

import com.space.exception.BadRequestException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.service.ShipServiceImp;
import com.space.service.checkIsValid.CheckValidParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@ResponseBody
public class ShipController {


    private ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @RequestMapping(value = "/rest/ships", method = RequestMethod.GET)
    public List<Ship> all(@RequestParam(required = false) String name,
                          @RequestParam(required = false) String planet,
                          @RequestParam(required = false) ShipType shipType,
                          @RequestParam(required = false) Long after,
                          @RequestParam(required = false) Long before,
                          @RequestParam(required = false) Boolean isUsed,
                          @RequestParam(required = false) Double minSpeed,
                          @RequestParam(required = false) Double maxSpeed,
                          @RequestParam(required = false) Integer minCrewSize,
                          @RequestParam(required = false) Integer maxCrewSize,
                          @RequestParam(required = false) Double minRating,
                          @RequestParam(required = false) Double maxRating,
                          @RequestParam(required = false) ShipOrder order,
                          @RequestParam(required = false) Integer pageNumber,
                          @RequestParam(required = false) Integer pageSize) {
        List<Ship> ships = shipService.getOrderedAllShips(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize,
                                        maxCrewSize, minRating, maxRating, order, pageNumber, pageSize);

        return ships;
    }
    @RequestMapping(value = "/rest/ships/count", method = RequestMethod.GET)
    public int count(@RequestParam(required = false) String name,
                     @RequestParam(required = false) String planet,
                     @RequestParam(required = false) ShipType shipType,
                     @RequestParam(required = false) Long after,
                     @RequestParam(required = false) Long before,
                     @RequestParam(required = false) Boolean isUsed,
                     @RequestParam(required = false) Double minSpeed,
                     @RequestParam(required = false) Double maxSpeed,
                     @RequestParam(required = false) Integer minCrewSize,
                     @RequestParam(required = false) Integer maxCrewSize,
                     @RequestParam(required = false) Double minRating,
                     @RequestParam(required = false) Double maxRating) {
        return shipService.getAllShips(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating).size();
    }
    @RequestMapping(value = "/rest/ships/{id}", method = RequestMethod.GET)
    public Ship get(@PathVariable Long id) {
        if (Objects.nonNull(id) && id > 0) {
            return shipService.get(id);
        } else {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(value = "/rest/ships/{id}")
    public void delete(@PathVariable Long id) {
        CheckValidParam.checkId(id);
        shipService.deleteShip(id);

    }

    @PostMapping(value = "/rest/ships")
    public Ship create(@RequestBody Ship ship) {
        return shipService.create(ship);
    }

    @PostMapping(value = "/rest/ships/{id}")
    public Ship edit(@RequestBody Ship ship, @PathVariable Long id) {
        CheckValidParam.checkId(id);
        return shipService.edit(ship, id);
    }
}
