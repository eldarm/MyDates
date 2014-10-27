package com.eldar.mydates;

import java.util.GregorianCalendar;

class SpecialDateTest {

    private static class TesterSpecialDate extends SpecialDate {
        public TesterSpecialDate(String label, String dateString) {
            super(label, dateString);
        }

        public TesterSpecialDate setNow(GregorianCalendar now) {
            this.now = now;
            computeAnniversary();
            return this;
        }
    }

    private static void ExpectEqual(String label, String actual, String expected) {
        if (actual.equals(expected)) {
            System.out.println(label + ": Passed.");
        } else {
            System.out.println(label + ": Expected '" + expected + "' got '" + actual + "'");
        }
    }

    private static void Test(String testName, String dateString, String since, String till) {
        System.out.println("Testing " + testName + " '" + dateString + "'");
        // 2014/10/09 12:00:00
        GregorianCalendar now = new GregorianCalendar(2014, 9, 9, 12, 0, 0);
        SpecialDate date = new TesterSpecialDate(testName, dateString).setNow(now);
        ExpectEqual(testName, date.getLabel(), testName);
        ExpectEqual(testName, date.toString(), dateString);
        ExpectEqual(testName, date.timeSince(), since);
        ExpectEqual(testName, date.timeTillAnniversary(), till);
    }

    public static void main(String[] args) {
        Test("N", "2013/10/09 13:00:00", "364 days 23 hours 0 minutes 0 seconds",
                "0 days 1 hours 0 minutes 0 seconds");
        Test("G", "2010/09/27 09:00:00", "4 years 12 days 3 hours 0 minutes 0 seconds",
                "352 days 21 hours 0 minutes 0 seconds");
        Test("Z", "2012/10/01 10:00:00", "2 years 8 days 2 hours 0 minutes 0 seconds",
                "356 days 22 hours 0 minutes 0 seconds");
        Test("M", "2000/04/28 08:00:00", "14 years 164 days 4 hours 0 minutes 0 seconds",
                "200 days 20 hours 0 minutes 0 seconds");
        Test("X", "1996/12/31 23:55:00", "17 years 281 days 11 hours 5 minutes 0 seconds",
                "83 days 12 hours 55 minutes 0 seconds");
    }

}