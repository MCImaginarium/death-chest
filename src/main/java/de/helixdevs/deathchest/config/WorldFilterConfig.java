package de.helixdevs.deathchest.config;

import de.helixdevs.deathchest.util.Filter;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public record WorldFilterConfig(Filter filter, Set<String> worlds) implements Predicate<World> {

    public static @NotNull WorldFilterConfig load(@Nullable ConfigurationSection section) {
        if (section == null)
            return new WorldFilterConfig(Filter.BLACKLIST, Collections.emptySet());

        String filterString = section.getString("filter");
        if (filterString != null) {
            try {
                Filter filter = Filter.valueOf(filterString.toUpperCase());
                List<String> worlds = section.getStringList("worlds");
                return new WorldFilterConfig(filter, new HashSet<>(worlds));
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown world filter in DeathChest/config.yml");
                e.printStackTrace();
            }
        }
        return new WorldFilterConfig(Filter.BLACKLIST, Collections.emptySet());
    }


    @Override
    public boolean test(World world) {
        return (filter() == Filter.WHITELIST && worlds().contains(world.getName())) || (filter() == Filter.BLACKLIST && !worlds().contains(world.getName()));
    }
}
