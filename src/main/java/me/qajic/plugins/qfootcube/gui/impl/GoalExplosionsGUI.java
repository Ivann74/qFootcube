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
public class GoalExplosionsGUI extends InventoryGUI {
  private final GUIManager guiManager;
  private final UtilManager utilManager;
  private final Logger logger;

  public GoalExplosionsGUI(final GUIManager guiManager, final UtilManager utilManager) {
    this.guiManager = guiManager;
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
  }

  @Override
  public Inventory createInventory() {
    return Bukkit.createInventory(null, 6 * 9, "Gol eksplozije");
  }

  @Override
  public void decorate(Player player) {
    for (int slot = 0; slot <= 53; slot++) {
      if (slot == 37) {
        this.addButton(slot, this.createHead("&fPomoćnik", "18154", getUtilManager().color("&eLevi klik &7za aktivaciju."), getUtilManager().color("&fEksplozije kupite @ &6/&ebuy&f.")).consumer(event -> {}));
      } else if (slot == 43) {
        this.addButton(slot, this.createHead("&cZatvorite", "3229")
            .consumer(event -> event.getWhoClicked().closeInventory()));
      } else if (slot == 42) {
        this.addButton(slot, this.createHead("&4Isključite", "9382")
            .consumer(event -> {
              Player target = (Player) event.getWhoClicked();
              PlayerDataManager playerData = new PlayerDataManager(getUtilManager().getPlugin(), target.getUniqueId());
              playerData.setString("goal_explosion", "Disable");
              getLogger().send(target, Lang.DISABLED_GOALEXPLOSIONS.getConfigValue(null));
              target.closeInventory();
              playerData.savePlayerData(target.getUniqueId());
            }));
      } else this.addButton(slot, this.createButton("&r", (byte) 7));
    }

    this.addButton(10, this.createParticleItem(new ItemStack(Material.WOOL, 1, (byte) 11), "&9Običan", "default",
        getUtilManager().color("&fStatus: &a&ndostupno"),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(11, this.createParticleItem(new ItemStack(Material.WOOL, 1), "&fSpirala", "helix",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.goalexplosions.helix") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(12, this.createParticleItem(new ItemStack(Material.OBSIDIAN, 1), "&cMeteor", "meteor",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.goalexplosions.hearts") ? "&a&ndostupno" : "&c&nzaključano")),
        getUtilManager().color("&eKliknite&f da aktivirate!")));
    this.addButton(13, this.createParticleItem(new ItemStack(Material.WOOL, 1, (byte) 12), "&6Govno", "poo",
        getUtilManager().color("&fStatus: " + (player.hasPermission("footcube.goalexplosions.poo") ? "&a&ndostupno" : "&c&nzaključano")),
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
          if (player.hasPermission("footcube.goalexplosions." + type)) {
            PlayerDataManager playerData = new PlayerDataManager(getUtilManager().getPlugin(), player.getUniqueId());
            String formattedType = type.substring(0, 1).toUpperCase() + type.substring(1);
            playerData.setString("goal_explosion", formattedType);
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
