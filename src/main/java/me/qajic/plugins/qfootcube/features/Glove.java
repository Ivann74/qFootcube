package me.qajic.plugins.qfootcube.features;

import me.qajic.plugins.qfootcube.Footcube;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Glove implements Listener {
    private final Footcube pl;
    private Boolean sneaking;
    public Glove(final Footcube pl) {
        this.pl = pl;
        this.sneaking = false;
    }

    @EventHandler
    public void onGloveUse(final PlayerInteractEntityEvent e) {
        final Player p = e.getPlayer();
        final Entity entity = e.getRightClicked();
        if(!p.getItemInHand().getType().equals(Material.AIR)) {
            final ItemStack item = p.getInventory().getItemInHand();
            final String itemName = item.getItemMeta().getDisplayName();
            final Material mat = item.getType();
            if (entity instanceof Slime && mat == Material.BLAZE_ROD && itemName.equals("GLOVE") && this.sneaking && p.getPassenger() == null && this.pl.cubes.contains(entity) && e.getPlayer().getGameMode() != GameMode.SPECTATOR && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                final Slime slime = (Slime) entity;
                double distance = p.getLocation().distance(slime.getLocation());
                if (distance < 5.0) {
                    p.setPassenger(slime);
                    this.pl.pickedCubes.add(slime);
                }
            }
        }
    }

    @EventHandler
    public void onDropBall(final EntityDamageByEntityEvent e) {
        final Player p = (Player)e.getDamager();
        if (e.getEntity() instanceof Slime && p.getPassenger() == e.getEntity() && this.pl.cubes.contains(e.getEntity()) && e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            p.eject();
            new BukkitRunnable() {
                @Override
                public void run() {
                    Glove.this.pl.pickedCubes.remove((Slime)e.getEntity());
                }
            }.runTaskLater(this.pl, 10);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onSneak(final PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {
            this.sneaking=true;
        }
        else {
            this.sneaking=false;
        }
    }

}
