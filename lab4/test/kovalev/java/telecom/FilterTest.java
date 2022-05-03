package telecom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

public class FilterTest
{
    private Tariff[] tariffs = {
        new Tariff("Foo(C)", "Дешёвый тариф", 200, 5, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
        new Tariff("Foo(C)", "Средний тариф", 400, 10, 300, 150),
        new Tariff("Foo(C)", "Дорогой тариф", 800, 50, 1200, 300),
        new Tariff("Foo(C)", "Безлимит", 1500, Double.POSITIVE_INFINITY, 1200, 300),
        new Tariff("Bar(C)", "Бит", 100, 5, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
        new Tariff("Bar(C)", "Байт", 400, 50, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
        new Tariff("Bar(C)", "Байт + звонки", 500, 50, 450, Double.NEGATIVE_INFINITY),
        new Tariff("Baz(C)", "Как при Брежневе", 50, 0, 150, 50),
        new Tariff("Baz(C)", "Народный", 250, 5, 150, 50),
        new Tariff("Baz(C)", "Царь-бомба", 500, 5, 1200, 600)
    };

    @Test
    public void mixedBoundaries()
    {
        FilterCtl fltr = new FilterCtl(tariffs);
        fltr.setCostLowerBound(100);
        fltr.setCostUpperBound(50);
        assertEquals(fltr.get().length, 0);
    }

    @Test
    public void unrealisticDesire()
    {
        FilterCtl fltr = new FilterCtl(tariffs);
        fltr.setCostUpperBound(100);
        fltr.setInternetLowerBound(50);
        assertEquals(fltr.get().length, 0);
    }

    @Test
    public void unlimitedInternetIsMyOnlyWish()
    {
        FilterCtl fltr = new FilterCtl(tariffs);
        fltr.setInternetLowerBound(9999);
        Tariff[] result = fltr.get();
        assertEquals(result.length, 1);
        assertTrue(result[0].getOperatorName().equals("Foo(C)"));
    }

    @Test
    public void theCheapestCalls()
    {
        FilterCtl fltr = new FilterCtl(tariffs);
        fltr.setCallsLowerBound(1000);
        Tariff[] result = fltr.get();
        assertEquals(result.length, 3);
        assertTrue(result[0].getOperatorName().equals("Baz(C)"));
        assertTrue(result[0].getMonthRub() == 500);
    }

    @Test
    public void havingPersonalLoveToOperator()
    {
        FilterCtl fltr = new FilterCtl(tariffs);
        String[] desirableOperators = {"Bar(C)", "Baz(C)"};
        fltr.setOperatorList(Arrays.asList(desirableOperators));
        fltr.setInternetLowerBound(15);
        Tariff[] result = fltr.get();
        assertEquals(result.length, 2);
        assertTrue(result[0].getMonthRub() == 400);
    }

    @Test
    public void orderBySms()
    {
        FilterCtl fltr = new FilterCtl(tariffs);
        Tariff[] result = fltr.get(Comparator.comparing(Tariff::getSms));
        assertEquals(result.length, 10);
        assertTrue(result[0].getSms() == Double.NEGATIVE_INFINITY);
        assertTrue(result[result.length - 1].getSms() == 600);
    }
}
