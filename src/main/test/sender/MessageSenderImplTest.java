package sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.entity.Country.RUSSIA;

public class MessageSenderImplTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"172.0.32.11", "178.0000", "96.44.183.149", "96.0", "127.0.0.1", "172.0", "Hello world!"})

    public void sendTest(String ipAddress) {
        String expectedRus = "Добро пожаловать", expectedEng = "Welcome";
        GeoService geoService = geoServiceMock();
        LocalizationService localizationService = localizationServiceMock();
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        boolean rusLocale = geoService.byIp(ipAddress).getCountry().equals(RUSSIA);
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);

        String resultStr = messageSender.send(headers);

        Assertions.assertEquals(rusLocale, resultStr.equals(expectedRus));
        Assertions.assertNotEquals(rusLocale, resultStr.equals(expectedEng));
    }

    private GeoService geoServiceMock() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.anyString()))
                .thenReturn(new Location(null, Country.GERMANY, null, 0));
        Mockito.when(geoService.byIp(Mockito.startsWith("172.")))
                .thenReturn(new Location(null, Country.RUSSIA, null, 0));
        Mockito.when(geoService.byIp(Mockito.startsWith("96.")))
                .thenReturn(new Location(null, Country.USA, null, 0));
        Mockito.when(geoService.byIp(Mockito.startsWith("178.")))
                .thenReturn(new Location(null, Country.GERMANY, null, 0));
        Mockito.when(geoService.byIp(null))
                .thenReturn(new Location(null, Country.GERMANY, null, 0));
        return geoService;
    }

    private LocalizationService localizationServiceMock() {
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Mockito.any(Country.class)))
                .thenReturn("Welcome");
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn("Добро пожаловать");
        Mockito.when(localizationService.locale(null))
                .thenReturn("Welcome");
        return localizationService;
    }

}
