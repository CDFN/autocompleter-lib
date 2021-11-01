package dev.cdfn.autocompleterlib.base;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import dev.cdfn.autocompleterlib.Autocompleter;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinInjector implements Listener {

  private final ProtocolManager protocolManager;
  private final Autocompleter autocompleter;

  public JoinInjector(Autocompleter autocompleter) {
    this.protocolManager = Objects.requireNonNull(ProtocolLibrary.getProtocolManager(), "protocollib is not present (yet?)");
    this.autocompleter = autocompleter;
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    PacketContainer packetContainer = new PacketContainer(Server.PLAYER_INFO);
    packetContainer.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
    packetContainer.getPlayerInfoDataLists().write(0, autocompleter.completions()
        .stream()
        .map(completion -> new PlayerInfoData(
            new WrappedGameProfile(UUID.randomUUID(), completion),
            -1,
            // Spectators are last on tab list
            NativeGameMode.SPECTATOR,
            WrappedChatComponent.fromText("")
        ))
        .collect(Collectors.toList())
    );

    try {
      this.protocolManager.sendServerPacket(player, packetContainer);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(String.format("failed to send autocompletion packet for player %s", player.getName()), e);
    }
  }
}
