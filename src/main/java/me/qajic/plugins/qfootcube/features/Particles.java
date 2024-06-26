package me.qajic.plugins.qfootcube.features;

import lombok.Getter;
import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.utils.Particle;
import me.qajic.plugins.qfootcube.utils.PlayerDataManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Particles implements Listener {
  private final Footcube plugin;

  public Particles(final Footcube instance) {
    this.plugin = instance;
  }

  public void cubeEffect() {
    Map<String, EnumParticle> particleMap = createParticleMap();

    for (Player player : getPlugin().getServer().getOnlinePlayers()) {
      UUID uuid = player.getUniqueId();
      PlayerDataManager playerData = new PlayerDataManager(getPlugin(), uuid);
      String effect = playerData.getString("particle");

      if (!effect.isEmpty() && particleMap.containsKey(effect)) {
        EnumParticle particleEnum = particleMap.get(effect);

        for (Slime cube : getPlugin().cubes) {
          if (!cube.isDead()) {
            Location loc = cube.getLocation();
            Particle particle = new Particle(particleEnum, loc, true, 0.0f, 0.0f, 0.0f, 100.0f, 0);
            particle.toPlayer(player);
          }
        }
      }
    }
  }

  private Map<String, EnumParticle> createParticleMap() {
    Map<String, EnumParticle> particleMap = new HashMap<>();

    particleMap.put("Hearts", EnumParticle.HEART);
    particleMap.put("Sparky", EnumParticle.FIREWORKS_SPARK);
    particleMap.put("Red", EnumParticle.REDSTONE);
    particleMap.put("Green", EnumParticle.VILLAGER_HAPPY);
    particleMap.put("Flames", EnumParticle.FLAME);
    particleMap.put("Portal", EnumParticle.PORTAL);
    particleMap.put("Spell", EnumParticle.CLOUD);
    particleMap.put("Cloud", EnumParticle.FIREWORKS_SPARK);
    particleMap.put("Flakes", EnumParticle.SNOWBALL);
    particleMap.put("Angry", EnumParticle.VILLAGER_ANGRY);
    particleMap.put("Notes", EnumParticle.NOTE);
    particleMap.put("Magic", EnumParticle.SPELL_WITCH);
    particleMap.put("Dizzy", EnumParticle.SPELL_MOB);

    return particleMap;
  }
}
