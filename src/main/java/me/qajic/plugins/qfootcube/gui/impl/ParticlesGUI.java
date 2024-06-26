package me.qajic.plugins.qfootcube.gui.impl;

import lombok.Getter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.qajic.plugins.qfootcube.configuration.Lang;
import me.qajic.plugins.qfootcube.gui.InventoryButton;
import me.qajic.plugins.qfootcube.gui.InventoryGUI;
import me.qajic.plugins.qfootcube.managers.GUIManager;
import me.qajic.plugins.qfootcube.managers.UtilManager;
import me.qajic.plugins.qfootcube.utils.Logger;
import me.qajic.plugins.qfootcube.utils.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@Getter
public class ParticlesGUI extends InventoryGUI {
  private final GUIManager guiManager;
  private final UtilManager utilManager;
  private final Logger logger;

  public ParticlesGUI(final GUIManager guiManager, final UtilManager utilManager) {
    this.guiManager = guiManager;
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
  }

  @Override
  public Inventory createInventory() {
    return Bukkit.createInventory(null, 6 * 9, "Tragovi lopte");
  }

  @Override
  public void decorate(Player player) {
    for (int slot = 0; slot <= 53; slot++) {
      if (slot == 37) {
        this.addButton(slot, this.createHead("&fPomoćnik", "18154", getUtilManager().color("&eLevi klik &7za aktivaciju."), getUtilManager().color("&fTragove kupite @ &6/&ebuy&f.")).consumer(event -> {}));
      } else if (slot == 43) {
        this.addButton(slot, this.createHead("&cZatvorite", "3229")
            .consumer(event -> event.getWhoClicked().closeInventory()));
      } else if (slot == 42) {
        this.addButton(slot, this.createHead("&4Isključite", "9382")
            .consumer(event -> {
              Player target = (Player) event.getWhoClicked();
              PlayerDataManager playerData = new PlayerDataManager(getUtilManager().getPlugin(), target.getUniqueId());
              playerData.setString("particle", "Disable");
              getLogger().send(target, Lang.DISABLED_PARTICLES.getConfigValue(null));
              target.closeInventory();
              playerData.savePlayerData(target.getUniqueId());
            }));
      } else this.addButton(slot, this.createButton("&r", (byte) 7));
    }

    this.addButton(10, this.createParticleItem(new ItemStack(Material.EMERALD, 1), "&aZeleni", "green",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.green") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(11, this.createParticleItem(new ItemStack(Material.BLAZE_POWDER, 1), "&6Vatra", "flames",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.flames") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(12, this.createParticleItem(new ItemStack(Material.APPLE, 1), "&cSrca", "hearts",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.hearts") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(13, this.createParticleItem(new ItemStack(Material.BLAZE_POWDER, 1), "&fSvetlucav", "sparky",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.sparky") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(14, this.createParticleItem(new ItemStack(Material.REDSTONE, 1), "&4Crvenilo", "red",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.red") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(15, this.createParticleItem(new ItemStack(Material.SNOW_BALL, 1), "&7Pahuljice", "flakes",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.flakes") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(16, this.createParticleItem(new ItemStack(Material.ENDER_PORTAL_FRAME, 1), "&5Portal", "portal",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.portal") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(19, this.createParticleItem(new ItemStack(Material.ENCHANTED_BOOK, 1), "&dVradžbina", "spell",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.spell") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(20, this.createParticleItem(new ItemStack(Material.WOOL, 1), "&7Oblak", "cloud",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.cloud") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(21, this.createParticleItem(new ItemStack(Material.MOB_SPAWNER, 1), "&8Ljutnja", "anger",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.anger") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(22, this.createParticleItem(new ItemStack(Material.RECORD_8, 1), "&dNote", "notes",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.notes") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(23, this.createParticleItem(new ItemStack(Material.STICK, 1), "&dMagija", "magic",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.magic") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(24, this.createParticleItem(new ItemStack(Material.GLASS_BOTTLE, 1), "&eMutilica", "dizzy",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.particles.dizzy") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));

    super.decorate(player);
  }

  private InventoryButton createParticleItem(ItemStack itemStack, String title, String type, String... lore) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    itemMeta.setDisplayName(getUtilManager().color(title));
    itemMeta.setLore(Arrays.asList(lore));
    itemStack.setItemMeta(itemMeta);
    return new InventoryButton()
        .creator(player -> itemStack)
        .consumer(event -> {
          Player player = (Player) event.getWhoClicked();
          player.closeInventory();
          if (player.hasPermission("footcube.particles." + type)) {
            PlayerDataManager playerData = new PlayerDataManager(getUtilManager().getPlugin(), player.getUniqueId());
            String formattedType = type.substring(0, 1).toUpperCase() + type.substring(1);
            playerData.setString("particle", formattedType);
            getLogger().send(player, Lang.ACTIVATED_PARTICLE.getConfigValue(new String[]{formattedType}));
            playerData.savePlayerData(player.getUniqueId());
          }
        });
  }

  private InventoryButton createButton(String title, byte damage, String... lore) {
    ItemStack button = new ItemStack(Material.STAINED_GLASS_PANE, 1, damage);
    ItemMeta buttonMeta = button.getItemMeta();
    buttonMeta.setDisplayName(getUtilManager().color(title));
    buttonMeta.setLore(Arrays.asList(lore));
    button.setItemMeta(buttonMeta);
    return new InventoryButton()
        .creator(player -> button)
        .consumer(event -> {});
  }

  private InventoryButton createHead(String title, String headId, String... lore) {
    HeadDatabaseAPI headDatabaseAPI = new HeadDatabaseAPI();
    ItemStack head = null;
    try {
      head = headDatabaseAPI.getItemHead(headId);
      ItemMeta headMeta = head.getItemMeta();
      headMeta.setDisplayName(getUtilManager().color(title));
      headMeta.setLore(Arrays.asList(lore));
      head.setItemMeta(headMeta);
    } catch (NullPointerException exception) {
      getLogger().send("helper", "nemoguće pronaći glavu " + headId);
    }
    ItemStack finalHead = head;
    return new InventoryButton()
        .creator(player -> finalHead != null ? finalHead : new ItemStack(Material.BARRIER))
        .consumer(event -> {});
  }
}
