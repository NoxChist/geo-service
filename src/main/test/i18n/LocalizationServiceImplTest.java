package i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

import static ru.netology.entity.Country.RUSSIA;

public class LocalizationServiceImplTest {

    @ParameterizedTest
    @EnumSource(Country.class)
    public void localeTest(Country country) {
        LocalizationService locale = new LocalizationServiceImpl();
        String expectedRus = "Добро пожаловать", expectedEng = "Welcome";
        boolean rusLocale = country.equals(RUSSIA);

        String resultStr = locale.locale(country);

        Assertions.assertEquals(rusLocale, resultStr.equals(expectedRus));
        Assertions.assertNotEquals(rusLocale, resultStr.equals(expectedEng));
    }

    @Test
    public void localeNullCountryTest() {
        LocalizationService locale = new LocalizationServiceImpl();
        String expected = null;

        String resultStr = locale.locale(null);

        Assertions.assertTrue(resultStr == null);
    }
}
