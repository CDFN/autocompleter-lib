package dev.cdfn.autocompleterlib;

import org.bukkit.plugin.java.JavaPlugin;

public final class AutocompleterPlugin extends JavaPlugin {
  @Override
  public void onEnable() {
    Autocompleter autocompleter = Autocompleter.of("test", "amogus", this.getDescription().getName());
    autocompleter.inject(this);
    autocompleter.add("added");
  }
}
