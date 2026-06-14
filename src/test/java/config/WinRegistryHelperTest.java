package config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WinRegistryHelperTest {

    @Test
    void testRegisterAutostart() {
        // Since we are catching IOException and InterruptedException inside the method,
        // it shouldn't throw any exception even if "reg.exe" is not found (e.g. on Linux CI)
        assertDoesNotThrow(WinRegistryHelper::registerAutostart);
    }

    @Test
    void testUnregisterAutostart() {
        assertDoesNotThrow(WinRegistryHelper::unregisterAutostart);
    }
}
