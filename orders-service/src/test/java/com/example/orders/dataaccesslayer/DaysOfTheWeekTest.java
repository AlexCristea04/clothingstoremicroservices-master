package com.example.orders.dataaccesslayer;

import com.example.orders.datalayer.DaysOfTheWeek;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DaysOfTheWeekTest {

    @Test
    public void testEnumValues() {
        // Get all the enum values
        DaysOfTheWeek[] values = DaysOfTheWeek.values();
        // Check if the length is equal to the number of combinations (2^7 combinations)
        assertEquals(127, values.length, "The enum should contain 128 combinations of days.");

        // Test individual enum values
        assertTrue(containsEnumValue(values, "MONDAY"));
        assertTrue(containsEnumValue(values, "TUESDAY"));
        assertTrue(containsEnumValue(values, "WEDNESDAY"));
        assertTrue(containsEnumValue(values, "THURSDAY"));
        assertTrue(containsEnumValue(values, "FRIDAY"));
        assertTrue(containsEnumValue(values, "SATURDAY"));
        assertTrue(containsEnumValue(values, "SUNDAY"));

        // Test for other combinations
        assertTrue(containsEnumValue(values, "MONDAY_TUESDAY_WEDNESDAY"));
        assertTrue(containsEnumValue(values, "MONDAY_TUESDAY_THURSDAY_FRIDAY_SATURDAY"));
        assertTrue(containsEnumValue(values, "MONDAY_WEDNESDAY_FRIDAY_SATURDAY_SUNDAY"));
        assertTrue(containsEnumValue(values, "ENTIRE_WEEK"));

        // Test a random invalid value (not in the enum)
        assertTrue(!containsEnumValue(values, "INVALID_VALUE"));
    }

    private boolean containsEnumValue(DaysOfTheWeek[] values, String value) {
        // Make the check case-insensitive by converting both inputs to uppercase
        for (DaysOfTheWeek day : values) {
            if (day.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
