package net.vvxzv.tfc_farm_charm.common.fluid;

import net.dries007.tfc.util.climate.KoppenClimateClassification;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Beers {
    BEER_BARLEY(-3957193),
    BEER_HALEY(-6222592),
    BEER_HOPS(-857121),
    BEER_WHEAT(-5198286),
    BEER_NETTLE(-4396),
    BEER_OAT(-1785750);

    public static final Beers[] VALUES = values();
    public static final KoppenClimateClassification[] KOPPEN_VALUES = KoppenClimateClassification.values();
    private final String name;
    private final int color;

    Beers(int color){
        String name = this.name();
        this.name = name.toLowerCase(Locale.ROOT);
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public @NotNull String getSerializedName() {
        return this.name;
    }

}
