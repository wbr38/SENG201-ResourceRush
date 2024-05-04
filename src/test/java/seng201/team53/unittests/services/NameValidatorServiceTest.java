package seng201.team53.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import seng201.team53.service.NameValidatorService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NameValidatorServiceTest {
    private NameValidatorService nameValidatorService;

    @BeforeAll
    public void beforeAllTests() {
        nameValidatorService = new NameValidatorService();
    }

    @Test
    void testNameTooShort() {
        for (int i = 0; i < NameValidatorService.MIN_NAME_LENGTH; i++) {
            assertFalse(nameValidatorService.isValid("a".repeat(i)));
        }
    }

    @Test
    void testNameTooLong() {
        for (int i = 1; i <= 5; i++) {
            assertFalse(nameValidatorService.isValid("b".repeat(NameValidatorService.MAX_NAME_LENGTH + i)));
        }
    }

    @Test
    void testContainsSpecialCharacters() {
        assertFalse(nameValidatorService.isValid("5C#80"));
        assertFalse(nameValidatorService.isValid("dB&90"));
        assertFalse(nameValidatorService.isValid("[~#@^"));
        assertFalse(nameValidatorService.isValid("0P8(Uj6Wy\"l'"));
        assertFalse(nameValidatorService.isValid("Zw/\"R]$+64+0"));
        assertFalse(nameValidatorService.isValid("'&_£;£_^+\";;"));
        assertFalse(nameValidatorService.isValid("$$$$$$"));
    }

    @Test
    void testEdgeCases() {
        assertTrue(nameValidatorService.isValid("a".repeat(NameValidatorService.MIN_NAME_LENGTH)));
        assertTrue(nameValidatorService.isValid("x".repeat(NameValidatorService.MAX_NAME_LENGTH)));
    }

    @Test
    void testValidShortNames() {
        assertTrue(nameValidatorService.isValid("John"));
        assertTrue(nameValidatorService.isValid("Kim123"));
        assertTrue(nameValidatorService.isValid("xX1Kim1Xx"));
    }

    @Test
    void testValidLongNames() {
        assertTrue(nameValidatorService.isValid("AliceSmith"));
        assertTrue(nameValidatorService.isValid("David50John"));
        assertTrue(nameValidatorService.isValid("EmilyBrown12"));
    }
}
