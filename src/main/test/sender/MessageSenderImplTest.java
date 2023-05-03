package sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static ru.netology.entity.Country.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MessageSenderImplTest {

    @ParameterizedTest
    @MethodSource("sendTestArguments")
    public void sendTest(String ipAddress, Location location, String message, String messageExpectation, int callLocaleExpectations) {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.doReturn(location).when(geoService).byIp(argThat(str -> (str == null) || (str.getClass() == String.class)));

        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(location.getCountry())).thenReturn(message);
        Mockito.when(localizationService.locale(USA)).thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ipAddress);


        String resultStr = messageSender.send(headers);


        Assertions.assertEquals(messageExpectation, resultStr);
        Mockito.verify(localizationService, Mockito.times(callLocaleExpectations)).locale(any(Country.class));
    }

    public static Stream<Arguments> sendTestArguments() {
        String messageRus = "Добро пожаловать", messageEng = "Welcome";

        return Stream.of(
                Arguments.of("127.0.0.1", new Location(null, null, null, 0), null, messageEng, 1),

                Arguments.of(null, new Location(null, null, null, 0), null, messageEng, 1),

                Arguments.of("172.0.32.11", new Location(null, RUSSIA, null, 0), messageRus, messageRus, 2),

                Arguments.of("96.44.183.149", new Location(null, USA, null, 0), messageEng, messageEng, 2),

                Arguments.of("178.00000", new Location(null, GERMANY, null, 0), messageEng, messageEng, 2));
    }
}
