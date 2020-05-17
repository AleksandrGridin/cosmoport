package com.space.service.checkIsValid;

import com.space.exception.BadRequestException;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Date;
import java.time.LocalDate;


public class CheckValidParam {

    public static void checkId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException();
        }
    }

    public static boolean checkName(String name) {
        if (name != null) {
            if (name.isEmpty() || name.length() > 50) {
                throw new BadRequestException();
            }
            return true;
        }
        return false;
    }

    public static boolean checkDate(Date date) {
        if (date != null) {
            if (date.toLocalDate().isBefore(LocalDate.of(2800, 1, 1))) {
                throw new BadRequestException();
            }
            if (date.toLocalDate().isAfter(LocalDate.of(3020, 1, 1))) {
                throw new BadRequestException();
            }
            return true;
        }
        return false;
    }

    public static boolean checkSpeed(Double speed) {
        if (speed != null) {
            if (speed < 0.01 || speed > 0.99) {
                throw new BadRequestException();
            }
            return true;
        }
        return false;
    }

    public static boolean checkCrewSize(Integer crewSize) {
        if (crewSize != null) {
            if (crewSize < 1 || crewSize > 9999) {
                throw new BadRequestException();
            }
            return true;
        }
        return false;
    }


}
