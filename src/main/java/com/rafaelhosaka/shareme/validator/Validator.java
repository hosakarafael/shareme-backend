package com.rafaelhosaka.shareme.validator;

import java.time.LocalDate;
import java.time.Year;

public class Validator {
    public boolean isValidDate(LocalDate date){
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();

        if(year < 0){
            return false;
        }

        switch (month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if(day < 1 || day > 31){
                    return false;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if(day < 1 || day > 30){
                    return false;
                }
                break;
            case 2:
                if(Year.isLeap(year)){
                    if(day < 1 || day > 29){
                        return false;
                    }else if(day < 1 || day > 28){
                        return false;
                    }
                }
                break;
            default:
                return false;
        }

        return true;
    }
}
