package geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class GeoServiceImplTest {

    @ParameterizedTest
    @MethodSource("byIpTestArguments")
    public void byIpTest(String ip, Location expected) {
        GeoService geoService = new GeoServiceImpl();

        Location result = geoService.byIp(ip);

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void byCoordinatesTest() {
        double latitude = -1;
        double longitude = 1;
        GeoService geoService = new GeoServiceImpl();
        Class<RuntimeException> expected = RuntimeException.class;

        Executable executable = () -> geoService.byCoordinates(latitude, longitude);

        Assertions.assertThrowsExactly(expected, executable);
    }

    public static Stream<Arguments> byIpTestArguments() {
        return Stream.of(
                Arguments.of("127.0.0.1", new Location(null, null, null, 0)),
                Arguments.of("172.0.32.11", new Location("Moscow", Country.RUSSIA, "Lenina", 15)),
                Arguments.of("96.44.183.149", new Location("New York", Country.USA, " 10th Avenue", 32)),
                Arguments.of("172.00100", new Location("Moscow", Country.RUSSIA, null, 0)),
                Arguments.of("96.0000", new Location("New York", Country.USA, null, 0)),
                Arguments.of("0", null)
        );
    }

}
