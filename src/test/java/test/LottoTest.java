package test;

import gh.funthomas424242.annotations.numbers.Cardinal;
import gh.funthomas424242.annotations.numbers.Ungerade;

public class LottoTest {

    @Cardinal(max = 20, min = 6)
    private final int counter = 0;
 
    @Ungerade
    final int tipp = 8;

    @Cardinal(min = 0, max = 49)
    public int tipp(@Cardinal(min = 8, max = 49) final int zahl) {

        @Cardinal(max = -20, min = 6)
        final int counter = 0;

        return counter + tipp;
    }

}
