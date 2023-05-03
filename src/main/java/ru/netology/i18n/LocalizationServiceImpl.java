package ru.netology.i18n;

import ru.netology.entity.Country;

public class LocalizationServiceImpl implements LocalizationService {

    public String locale(Country country) {
        if (country != null) {
            switch (country) {
                case RUSSIA:
                    return "Добро пожаловать";
            }
        }
        return null;
    }
}
