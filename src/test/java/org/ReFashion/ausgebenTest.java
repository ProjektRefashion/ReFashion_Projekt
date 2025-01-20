package org.ReFashion;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ausgebenTest {

    KorbEintrag testKorb = new KorbEintrag("T-Shirt", 3, 8.00);

    @Test
    public void ausgebenTest(){
        assertEquals("Name:" + "T-Shirt"+ " Anzahl:" + 3 + " Preis:" + 8.00, testKorb.ausgeben());
    }

}