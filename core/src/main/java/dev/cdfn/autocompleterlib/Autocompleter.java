package dev.cdfn.autocompleterlib;

import com.google.common.collect.Lists;
import dev.cdfn.autocompleterlib.base.JoinInjector;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

public record Autocompleter(List<String> completions) {

  public static Autocompleter of(String... completions) {
    return new Autocompleter(Lists.newArrayList(completions));
  }

  public void inject(JavaPlugin javaPlugin) {
    javaPlugin.getServer().getPluginManager().registerEvents(new JoinInjector(this), javaPlugin);
  }

  /**
   * @return copy of completions
   */
  @Override
  public List<String> completions() {
    return new ArrayList<>(this.completions);
  }

  /**
   * Adds completion to auto completion list
   *
   * @param completion completion
   */
  public void add(String completion) {
    this.completions.add(completion);
  }
}
