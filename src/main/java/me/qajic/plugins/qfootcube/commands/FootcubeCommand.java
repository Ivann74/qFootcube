package me.qajic.plugins.qfootcube.commands;

import me.qajic.plugins.qfootcube.Footcube;
import me.qajic.plugins.qfootcube.configuration.MessagesConfig;
import me.qajic.plugins.qfootcube.core.Match;
import me.qajic.plugins.qfootcube.utils.*;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.WeightNode;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class FootcubeCommand implements CommandExecutor {
    final private Footcube plugin;
    public String pluginString;
    private Banners banners;
    private Leaderboard leaderboard;

    public FootcubeCommand(final Footcube instance) {
        this.plugin = instance;
        this.pluginString = ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("prefix"));
        this.banners = new Banners();
    }
    public void createStack(final Inventory inv, final int slot, final Material material, final String name, final List lore, final byte data) {
        final ItemStack item = new ItemStack(material,1, data);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        inv.setItem(slot, item);
    }
    public void explosionInventory(final Player p) {
        final Inventory inv = Bukkit.getServer().createInventory((InventoryHolder)null, 54, ChatColor.translateAlternateColorCodes('&',"&6&lGoal &e&lExplosions &fMenu"));
        String hasPermision;

        hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        this.createStack(inv, 11, Material.GRASS, " §aDefault", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this goal explosion.")), (byte) 0);

        this.createStack(inv, 49, Material.BARRIER, " §cDisable", Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to disable"), ""), (byte) 0);
        //helix
        if(p.hasPermission("footcube.goalexplosions.helix") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 12, Material.WOOL, " §7Helix", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //meteor
        if(p.hasPermission("footcube.goalexplosions.meteor") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 13, Material.OBSIDIAN, " §cMeteor", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //poo
        if(p.hasPermission("footcube.goalexplosions.poo") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 14, Material.WOOL, " §6Poo", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 12);
        //serbia
        if(p.hasPermission("footcube.goalexplosions.serbia") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.banners.Serbia(inv, 15, " §cSerbia", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")));
        //spain
        if(p.hasPermission("footcube.goalexplosions.spain") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.banners.Spain(inv, 20, " §6Spain", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")));
        //kok
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "kok") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 21, Material.WOOL, " §fK§1O§fK", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //skl
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "skl") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 22, Material.WOOL, " §bS§eK§cL", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //kel
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "kel") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 23, Material.WOOL, " §eK§0E§eL", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //fls
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "fls") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 24, Material.WOOL, " §9F§fL§9S", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //fks
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "fks") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 29, Material.WOOL, " §4F§1K§4S", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //fkk
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "fkk") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 30, Material.WOOL, " §1F§fK§6K", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //mku
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "mku") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 31, Material.WOOL, " §6M§8K§6U", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        //spa
        if(this.plugin.lpHelper.playerInGroup(p.getUniqueId(), "spa") || p.hasPermission("footcube.goalexplosions.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 32, Material.WOOL, " §9S§cP§fA", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        p.openInventory(inv);
    }

    public void particleInventory(final Player p) {
        final Inventory inv = Bukkit.getServer().createInventory((InventoryHolder)null, 54, ChatColor.translateAlternateColorCodes('&',"&3&lPar&b&ltic&f&lles &fMenu"));
        String hasPermision;

        if(p.hasPermission("footcube.particles.green") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";

        this.createStack(inv, 11, Material.EMERALD, " §aGreen", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        if(p.hasPermission("footcube.particles.flames") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";

        this.createStack(inv, 12, Material.BLAZE_POWDER, " §6Flames", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        this.createStack(inv, 49, Material.BARRIER, " §cDisable", Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to disable"), ""), (byte) 0);
        if(p.hasPermission("footcube.particles.hearts") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 13, Material.APPLE, " §cHearts", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        if(p.hasPermission("footcube.particles.sparky") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 14, Material.FEATHER, " §fSparky", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        if(p.hasPermission("footcube.particles.red") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 15, Material.REDSTONE, " §4Red", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);
        if(p.hasPermission("footcube.particles.flakes") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 20, Material.SNOW_BALL, " §7Flakes", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        if(p.hasPermission("footcube.particles.portal") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 21, Material.ENDER_PORTAL_FRAME, " §5Portal", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        if(p.hasPermission("footcube.particles.spell") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 22, Material.ENCHANTED_BOOK, " §dSpell", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        if(p.hasPermission("footcube.particles.cloud") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 23, Material.WOOL, " §7Cloud", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        if(p.hasPermission("footcube.particles.angry") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 24, Material.MOB_SPAWNER, " §8Angry", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        if(p.hasPermission("footcube.particles.notes") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 29, Material.RECORD_8, " §dNotes", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        if(p.hasPermission("footcube.particles.magic") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 30, Material.STICK, " §5Magic", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        if(p.hasPermission("footcube.particles.dizzy") || p.hasPermission("footcube.particles.all"))
            hasPermision = "&8 ▪ &7Status: &a&nOWNED";
        else
            hasPermision = "&8 ▪ &7Status: &c&mNOT OWNED";
        this.createStack(inv, 31, Material.POTION, " §eDizzy", Arrays.asList(ChatColor.translateAlternateColorCodes('&', hasPermision), "", ChatColor.translateAlternateColorCodes('&', "&8 ▪ &f&oClick to activate this particle.")), (byte) 0);

        p.openInventory(inv);
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String c, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Command cannot be executed through console.");
        } else {
            final Player p = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("tc")) {
                if(this.plugin.organization.playingPlayers.contains(p.getName())) {
                    if(args.length<1)
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("tcUsage")));
                    else {
                        String message = "";
                        for (final String s : args) {
                            message = message + s + " ";
                        }

                        int arena = this.plugin.organization.findArena(p);
                        Match m = null;

                        if (this.plugin.organization.matches2v2[arena].isRed.containsKey(p))
                            m = this.plugin.organization.matches2v2[arena];
                        else if (this.plugin.organization.matches3v3[arena].isRed.containsKey(p))
                            m = this.plugin.organization.matches3v3[arena];
                        else if (this.plugin.organization.matches4v4[arena].isRed.containsKey(p))
                            m = this.plugin.organization.matches4v4[arena];
                        else if (this.plugin.organization.matches5v5[arena].isRed.containsKey(p))
                            m = this.plugin.organization.matches5v5[arena];

                        if (m == null) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("notIngame")));
                        }
                        else
                            m.teamchat(p, message);
                    }
                } else {
                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("notIngame")));
                }
            }
            if(p.hasPermission("footcube.banned") && !p.hasPermission("footcube.admin")) {
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("banned")));
            } else if(!this.plugin.state && !p.hasPermission("footcube.admin")) {
                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("disabled")));
            } else {
                if (cmd.getName().equalsIgnoreCase("footcube")) {
                    boolean success = true;
                    if (args.length < 1) {
                        success = false;
                    } else if ((args[0].equalsIgnoreCase("goalexplosions") || args[0].equalsIgnoreCase("ge")) && p.hasPermission("footcube.goalexplosions.gui")) {
                        this.explosionInventory(p);
                    } else if (args[0].equalsIgnoreCase("particles") || args[0].equalsIgnoreCase("p")) {
                        this.particleInventory(p);
                    } else if ((args[0].equalsIgnoreCase("commandDisabler") || args[0].equalsIgnoreCase("cd")) && p.hasPermission("footcube.admin")) {
                        if (args.length < 2) {
                            p.sendMessage(this.pluginString + "Command list:");
                            p.sendMessage("/cd add [command]");
                            p.sendMessage("/cd remove [command]");
                            p.sendMessage("/cd list");
                        }
                        else {
                            final FileConfiguration cfg = this.plugin.getConfig();
                            if (args[1].equalsIgnoreCase("add")) {
                                if (args.length < 3) {
                                    p.sendMessage(pluginString + "/cd add [command]");
                                }
                                else if (this.plugin.organization.disableCommands.commands.contains(args[2])) {
                                    p.sendMessage(this.pluginString + "Command is already added.");
                                }
                                else {
                                    this.plugin.organization.disableCommands.commands.add(args[2]);
                                    String cfgString = "";
                                    for (final String s : this.plugin.organization.disableCommands.commands) {
                                        cfgString = cfgString + s + " ";
                                    }
                                    cfg.set("enabledCommands", (Object)cfgString);
                                    this.plugin.saveConfig();
                                    p.sendMessage(this.pluginString + ChatColor.GOLD + "/" + ChatColor.YELLOW + args[2] + ChatColor.GRAY + " added to the list.");
                                }
                            }
                            else if (args[1].equalsIgnoreCase("remove")) {
                                if (args.length < 3) {
                                    p.sendMessage(this.pluginString + "/cd remove [command]");
                                }
                                else if (this.plugin.organization.disableCommands.commands.contains(args[2])) {
                                    this.plugin.organization.disableCommands.commands.remove(args[2]);
                                    String cfgString = "";
                                    for (final String s : this.plugin.organization.disableCommands.commands) {
                                        cfgString = cfgString + s + " ";
                                    }
                                    cfg.set("enabledCommands", (Object)cfgString);
                                    this.plugin.saveConfig();
                                    p.sendMessage(this.pluginString + ChatColor.GOLD + "/" + ChatColor.YELLOW + args[2] + ChatColor.GRAY + " removed from the list.");
                                }
                                else {
                                    p.sendMessage(this.pluginString + "ERROR!");
                                }
                            }
                            else if (args[1].equalsIgnoreCase("list")) {
                                p.sendMessage(this.pluginString + "List of commands:");
                                for (final String s2 : this.plugin.organization.disableCommands.commands) {
                                    p.sendMessage(ChatColor.WHITE + s2);
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("reload") && p.hasPermission("footcube.admin")) {
                        MessagesConfig.reload();
                        this.plugin.reloadConfig();
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("reload")));
                    } else if (args[0].equalsIgnoreCase("disable") && p.hasPermission("footcube.admin")) {
                        this.plugin.state = false;
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("sDisabled")));
                    } else if (args[0].equalsIgnoreCase("enable") && p.hasPermission("footcube.admin")) {
                        this.plugin.state = true;
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("sEnabled")));
                    }
                    else if ((args[0].equalsIgnoreCase("setteam") || args[0].equalsIgnoreCase("st")) && (p.hasPermission("footcube.manager") || p.hasPermission("footcube.admin"))) {
                        if (args.length <= 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setTeamUsage"))));
                        } else if (args.length == 3) {
                            final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            final String name = args[2], nameUppercase = name.toUpperCase();

                            if (target == null || !target.hasPlayedBefore()) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("noUser"))));
                            } else if (args[1].equalsIgnoreCase(target.getName())) {
                                if (this.plugin.lpHelper.groupExists(name.toLowerCase())) {
                                    if (!this.plugin.lpHelper.playerInGroup(target.getUniqueId(), name.toLowerCase())) {
                                        this.plugin.lpHelper.userManager.modifyUser(target.getUniqueId(), user -> {
                                            for (final Group group : user.getInheritedGroups(user.getQueryOptions())) {
                                                final int weight = 100, groupWeight = group.getWeight().isPresent() ? group.getWeight().getAsInt() : 0;
                                                if (groupWeight == weight) user.data().remove(InheritanceNode.builder(group.getName()).build());
                                            }

                                            user.data().add(InheritanceNode.builder(name.toLowerCase()).build());
                                        }).whenComplete((v, th) -> p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userAddedToTeam"))).replace("{player}", target.getName()).replace("{team}", name.toUpperCase())));
                                    } else
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userAlreadyInTeam"))).replace("{player}", target.getName()).replace("{team}", name.toUpperCase()));
                                } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamNotExisting"))).replace("{team}", name.toUpperCase()));
                            } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setTeamUsage"))));
                        }
                    }
                    else if ((args[0].equalsIgnoreCase("removeteam") || args[0].equalsIgnoreCase("rt")) && (p.hasPermission("footcube.manager") || p.hasPermission("footcube.admin"))) {
                        if (args.length <= 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("removeTeamUsage"))));
                        } else if (args.length == 3) {
                            final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            final String name = args[2], nameUppercase = name.toUpperCase();

                            if (target == null || !target.hasPlayedBefore()) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("noUser"))));
                            } else if (args[1].equalsIgnoreCase(target.getName())) {
                                if (this.plugin.lpHelper.groupExists(name.toLowerCase())) {
                                    this.plugin.lpHelper.userManager.modifyUser(target.getUniqueId(), user -> {
                                        final DataMutateResult result = user.data().remove(InheritanceNode.builder(name.toLowerCase()).build());
                                        if (result.wasSuccessful())
                                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userRemovedFromTeam"))).replace("{player}", target.getName()).replace("{team}", name.toUpperCase()));
                                        else
                                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userNotInTeam"))).replace("{player}", target.getName()).replace("{team}", name.toUpperCase()));
                                    });
                                } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamNotExisting"))).replace("{team}", name.toUpperCase()));
                            } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("removeTeamUsage"))));
                        }
                    }
                    else if ((args[0].equalsIgnoreCase("setteammanager") || args[0].equalsIgnoreCase("stm")) && (p.hasPermission("footcube.manager") || p.hasPermission("footcube.admin"))) {
                        if (args.length <= 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setTeamManagerUsage"))));
                        } else if (args.length == 3) {
                            final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            final String name = args[2], nameUppercase = name.toUpperCase(),
                                    permission = "tab.group." + name.toLowerCase() + "-director";

                            if (target == null || !target.hasPlayedBefore()) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("noUser"))));
                            } else if (args[1].equalsIgnoreCase(target.getName())) {
                                if (this.plugin.lpHelper.groupExists("director")) {
                                    if (!this.plugin.lpHelper.playerInGroup(target.getUniqueId(), "director")) {
                                        this.plugin.lpHelper.playerAddPermission(target.getUniqueId(), permission);
                                        this.plugin.lpHelper.playerAddGroup(target.getUniqueId(), "director");
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userSetManager"))).replace("{player}", target.getName()).replace("{team}", name.toUpperCase()));
                                    } else
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userAlreadyManager"))).replace("{player}", target.getName()));
                                } else
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamNotExisting"))).replace("{team}", name.toUpperCase()));
                            } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setTeamManagerUsage"))));
                        } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setTeamManagerUsage"))));
                    }
                    else if ((args[0].equalsIgnoreCase("removeteammanager") || args[0].equalsIgnoreCase("rtm")) && (p.hasPermission("footcube.manager") || p.hasPermission("footcube.admin"))) {
                        if (args.length <= 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("removeTeamManagerUsage"))));
                        } else if (args.length == 3) {
                            final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            final String name = args[2], nameUppercase = name.toUpperCase(),
                                    permission = "tab.group." + name.toLowerCase() + "-director";

                            if (target == null || !target.hasPlayedBefore()) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("noUser"))));
                            } else if (args[1].equalsIgnoreCase(target.getName())) {
                                if (this.plugin.lpHelper.groupExists("director")) {
                                    if (this.plugin.lpHelper.playerInGroup(target.getUniqueId(), "director")) {
                                        this.plugin.lpHelper.playerRemovePermission(target.getUniqueId(), permission);
                                        this.plugin.lpHelper.playerRemoveGroup(target.getUniqueId(), "director");
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userRemoveManager"))).replace("{player}", target.getName()).replace("{team}", name.toUpperCase()));
                                    } else
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("userNotManager"))).replace("{player}", target.getName()));
                                } else
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamNotExisting"))).replace("{team}", name.toUpperCase()));
                            } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("removeTeamManagerUsage"))));
                        } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("removeTeamManagerUsage"))));
                    }
                    else if ((args[0].equalsIgnoreCase("createteam") || args[0].equalsIgnoreCase("ct")) && (p.hasPermission("footcube.manager") || p.hasPermission("footcube.admin"))) {
                        if (args.length <= 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("createTeamUsage"))));
                        } else if (args.length == 3) {
                            final String name = args[1], tag = args[2], nameUppercase = name.toUpperCase();
                            final GroupManager groupManager = this.plugin.luckPermsAPI.getGroupManager();

                            if (!groupManager.isLoaded(name.toLowerCase())) {
                                groupManager.createAndLoadGroup(name).thenApplyAsync(group -> {
                                    group.data().add(WeightNode.builder(100).build());
                                    group.data().add(MetaNode.builder("team", tag).build());

                                    for (String permission : this.plugin.lpHelper.permissions) {
                                        permission = permission.replace("{team}", name.toLowerCase());
                                        group.data().add(PermissionNode.builder(permission).build());
                                    }

                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamCreated"))).replace("{team}", name.toUpperCase()));
                                    return group;
                                }).thenCompose(groupManager::saveGroup);
                            } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamAlreadyExists"))).replace("{team}", name.toUpperCase()));
                        } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("createTeamUsage"))));
                    }
                    else if ((args[0].equalsIgnoreCase("deleteteam") || args[0].equalsIgnoreCase("dt")) && (p.hasPermission("footcube.manager") || p.hasPermission("footcube.admin"))) {
                        if (args.length <= 1) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("deleteTeamUsage"))));
                        } else if (args.length == 2) {
                            final String name = args[1], nameUppercase = name.toUpperCase();
                            final GroupManager groupManager = this.plugin.luckPermsAPI.getGroupManager();

                            if (groupManager.isLoaded(name.toLowerCase())) {
                                final Group group = this.plugin.lpHelper.getGroup(name.toLowerCase());
                                groupManager.deleteGroup(group);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamDeleted"))).replace("{team}", name.toUpperCase()));
                            } else
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("teamNotExisting"))).replace("{team}", name.toUpperCase()));
                        } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("deleteTeamUsage"))));
                    }
                    else if (args[0].equalsIgnoreCase("ban") && p.hasPermission("footcube.admin")) {
                        if (args.length <= 1) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banUsage"))));
                        } else if (args.length >= 3) {
                            final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            final Time time = Time.parseString(args[2]);

                            final String permission = "footcube.banned";

                            if (target == null || !target.hasPlayedBefore()) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("noUser"))));
                            }
                            else if (args[1].equalsIgnoreCase(target.getName())) {
                                final PermissionNode node = PermissionNode.builder(permission).value(true).expiry(time.toMilliseconds(), TimeUnit.MILLISECONDS).build();

                                this.plugin.lpHelper.userManager.modifyUser(target.getUniqueId(), user -> {
                                    final DataMutateResult result = user.data().add(node);

                                    if (result.wasSuccessful()) {
                                        if (args.length == 3) {
                                            if (target.isOnline()) {
                                                target.getPlayer().sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banNoReason"))).replace("{time}", time.toString()));
                                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banSuccessNoReason"))).replace("{player}", target.getName()).replace("{time}", time.toString()));
                                            }
                                        } else {
                                            final String reason = StringUtils.join(args, ' ', 3, args.length);
                                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banSuccess"))).replace("{player}", target.getName()).replace("{time}", time.toString()).replace("{reason}", reason));
                                            if (target.isOnline())
                                                target.getPlayer().sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banReason"))).replace("{time}", time.toString()).replace("{reason}", reason));
                                        }
                                    } else
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banAlready"))).replace("{player}", target.getName()));
                                });
                            } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banUsage"))));
                        } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("banUsage"))));
                    } else if (args[0].equalsIgnoreCase("unban") && p.hasPermission("footcube.admin")) {
                        if (args.length <= 1) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("unBanUsage"))));
                        } else if (args.length >= 2) {
                            final OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            final String permission = "footcube.banned";

                            if (target == null || !target.hasPlayedBefore()) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("noUser"))));
                            } else if (args[1].equalsIgnoreCase(target.getName())) {
                                final User user = this.plugin.lpHelper.getPlayer(target.getUniqueId());
                                final Node node = user.getCachedData().getPermissionData().queryPermission(permission).node();

                                if (node != null && node.hasExpiry()) {
                                    this.plugin.lpHelper.userManager.modifyUser(target.getUniqueId(), user1 -> {
                                        final DataMutateResult result = user1.data().remove(node);
                                        if (result.wasSuccessful()) {
                                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("unBanSuccess"))).replace("{player}", target.getName()));
                                            if (target.isOnline())
                                                target.getPlayer().sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("unBanned"))));
                                        } else
                                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("notBanned"))).replace("{player}", target.getName()));
                                    });
                                } else
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("notBanned"))).replace("{player}", target.getName()));
                            } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("unBanUsage"))));
                        } else p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("unBanUsage"))));
                    }
                    else if ((args[0].equalsIgnoreCase("setunchargedkickpower") || args[0].equalsIgnoreCase("setukp")) && p.hasPermission("footcube.admin")) {
                        final FileConfiguration cfg = this.plugin.getConfig();
                        Boolean valid = true;
                        if (args.length < 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setukpError"))));
                        } else {
                            try {
                                Double.parseDouble(args[1]);
                            } catch (NumberFormatException nfe) {
                                valid = false;
                            }
                            if (valid) {
                                Double kp = Double.parseDouble(args[1]);
                                cfg.set("uc_kickpower", kp);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setukpSuccess"))).replace("{kickpower}", "" + kp));
                                this.plugin.saveConfig();
                                this.plugin.reloadConfig();
                            } else {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setukpError"))));
                            }
                        }
                    }
                    else if ((args[0].equalsIgnoreCase("setchargespeed") || args[0].equalsIgnoreCase("setch")) && p.hasPermission("footcube.admin")) {
                        final FileConfiguration cfg = this.plugin.getConfig();
                        Boolean valid = true;
                        if (args.length < 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setchError"))));
                        } else {
                            try {
                                Double.parseDouble(args[1]);
                            } catch (NumberFormatException nfe) {
                                valid = false;
                            }
                            if (valid) {
                                Double ch = Double.parseDouble(args[1]);
                                cfg.set("charge", ch);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setchSuccess"))).replace("{charge}", "" + ch));
                                this.plugin.saveConfig();
                                this.plugin.reloadConfig();
                            } else {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setchError"))));
                            }
                        }
                    }
                    else if ((args[0].equalsIgnoreCase("setkickpower") || args[0].equalsIgnoreCase("setkp")) && p.hasPermission("footcube.admin")) {
                        final FileConfiguration cfg = this.plugin.getConfig();
                        Boolean valid = true;
                        if (args.length < 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setkpError"))));
                        } else {
                            try {
                                Double.parseDouble(args[1]);
                            } catch (NumberFormatException nfe) {
                                valid = false;
                            }
                            if (valid) {
                                Double kp = Double.parseDouble(args[1]);
                                cfg.set("kickpower", kp);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setkpSuccess"))).replace("{kickpower}", "" + kp));
                                this.plugin.saveConfig();
                                this.plugin.reloadConfig();
                            } else {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("setkpError"))));
                            }
                        }
                    } else if ((args[0].equalsIgnoreCase("setscoremessage") || args[0].equalsIgnoreCase("setsm")) && p.hasPermission("footcube.donator")) {
                        final String message = StringUtils.join(args, ' ', 1, args.length);
                        if(args.length < 2 || message.length() <= 0) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("messageNotSpecified"))));
                        } else {
                            if(message.length() > 30) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("messageTooLong"))));
                            } else {
                                PlayerDataManager playerData = new PlayerDataManager(this.plugin, this.plugin.organization.uuidConverter.get(p.getName()));
                                playerData.setString("custom_score_message", message);
                                playerData.savePlayerData(this.plugin.organization.uuidConverter.get(p.getName()));
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("messageSuccess"))));
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("forcestart") && p.hasPermission("footcube.admin")) {
                        if (args.length < 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString(("arenaNotSpecified"))));
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("forceStartUsage")));
                        } else if (args[1].equalsIgnoreCase("1v1")) {
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            this.plugin.organization.matches1v1[this.plugin.organization.lobby1v1].join(p);
                            this.plugin.organization.waitingPlayers.put(p.getName(), 1);
                            this.plugin.organization.removeTeam(p);
                        } else if (args[1].equalsIgnoreCase("2v2")) {
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            this.plugin.organization.matches2v2[this.plugin.organization.lobby2v2].join(p);
                            this.plugin.organization.waitingPlayers.put(p.getName(), 2);
                            this.plugin.organization.removeTeam(p);
                        } else if (args[1].equalsIgnoreCase("3v3")) {
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            this.plugin.organization.matches3v3[this.plugin.organization.lobby3v3].join(p);
                            this.plugin.organization.waitingPlayers.put(p.getName(), 3);
                            this.plugin.organization.removeTeam(p);
                        } else if (args[1].equalsIgnoreCase("4v4")) {
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            this.plugin.organization.matches4v4[this.plugin.organization.lobby4v4].join(p);
                            this.plugin.organization.waitingPlayers.put(p.getName(), 4);
                            this.plugin.organization.removeTeam(p);
                        } else if (args[1].equalsIgnoreCase("5v5")) {
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            this.plugin.organization.matches5v5[this.plugin.organization.lobby5v5].join(p);
                            this.plugin.organization.waitingPlayers.put(p.getName(), 5);
                            this.plugin.organization.removeTeam(p);
                        } else {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("invalidArenaTypeFS")));
                        }
                    } else if (args[0].equalsIgnoreCase("setLobby") && p.hasPermission("footcube.admin")) {
                        final FileConfiguration cfg = this.plugin.getConfig();
                        Location loc = p.getLocation();
                        cfg.set("afterMatchRespawn.1", loc.getX());
                        cfg.set("afterMatchRespawn.2", loc.getY());
                        cfg.set("afterMatchRespawn.3", loc.getZ());
                        this.plugin.saveConfig();
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("setLobby")));
                    } else if (args[0].equalsIgnoreCase("join")) {
                        final FileConfiguration cfg = this.plugin.getConfig();
                        if (this.plugin.organization.waitingPlayers.containsKey(p.getName()) || this.plugin.organization.playingPlayers.contains(p.getName()) || this.plugin.organization.waitingTeamPlayers.contains(p)) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("alreadyIngame")));
                        } else if (args.length < 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("specifyArena")));
                        } else if (args[1].equalsIgnoreCase("1v1")) {
                            if (this.plugin.getConfig().getInt("arenas.1v1.amount") == 0) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("no1v1")));
                            } else {
                                p.setFlying(false);
                                this.plugin.organization.matches1v1[this.plugin.organization.lobby1v1].join(p);
                                this.plugin.organization.waitingPlayers.put(p.getName(), 1);
                                this.plugin.organization.removeTeam(p);
                            }
                        } else if (args[1].equalsIgnoreCase("2v2")) {
                            if (this.plugin.getConfig().getInt("arenas.2v2.amount") == 0) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("no2v2")));
                            } else {
                                p.setFlying(false);
                                this.plugin.organization.matches2v2[this.plugin.organization.lobby2v2].join(p);
                                this.plugin.organization.waitingPlayers.put(p.getName(), 2);
                                this.plugin.organization.removeTeam(p);
                            }
                        } else if (args[1].equalsIgnoreCase("3v3")) {
                            if (this.plugin.getConfig().getInt("arenas.3v3.amount") == 0) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("no3v3")));
                            } else {
                                p.setFlying(false);
                                this.plugin.organization.matches3v3[this.plugin.organization.lobby3v3].join(p);
                                this.plugin.organization.waitingPlayers.put(p.getName(), 3);
                                this.plugin.organization.removeTeam(p);
                            }
                        } else if (args[1].equalsIgnoreCase("4v4")) {
                            if (this.plugin.getConfig().getInt("arenas.4v4.amount") == 0) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("no4v4")));
                            } else {
                                p.setFlying(false);
                                this.plugin.organization.matches4v4[this.plugin.organization.lobby4v4].join(p);
                                this.plugin.organization.waitingPlayers.put(p.getName(), 4);
                                this.plugin.organization.removeTeam(p);
                            }
                        } else if (args[1].equalsIgnoreCase("5v5")) {
                            if (this.plugin.getConfig().getInt("arenas.5v5.amount") == 0) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("no5v5")));
                            } else {
                                p.setFlying(false);
                                this.plugin.organization.matches5v5[this.plugin.organization.lobby5v5].join(p);
                                this.plugin.organization.waitingPlayers.put(p.getName(), 5);
                                this.plugin.organization.removeTeam(p);
                            }
                        } else {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("specifyArena")));
                        }
                    } else if (args[0].equalsIgnoreCase("best")) {
                        List<String> highscore = MessagesConfig.get().getStringList("highscore");
                        this.leaderboard = new Leaderboard(this.plugin,"plugins" + File.separator + "qFootcube" + File.separator + "playerdata");

                        List<Footballer> goals = this.leaderboard.getLeaderboard("goals");
                        List<Footballer> assists = this.leaderboard.getLeaderboard("assists");
                        List<Footballer> wins = this.leaderboard.getLeaderboard("wins");
                        List<Footballer> winstreak = this.leaderboard.getLeaderboard("best_win_streak");
                        for (String e : highscore) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', e)
                                    .replace("{first_goals}", goals.get(0).getUsername())
                                    .replace("{first_goals_count}", ""+(int)goals.get(0).getGoals())
                                    .replace("{second_goals}", goals.get(1).getUsername())
                                    .replace("{second_goals_count}", ""+(int)goals.get(1).getGoals())
                                    .replace("{third_goals}", goals.get(2).getUsername())
                                    .replace("{third_goals_count}", ""+(int)goals.get(2).getGoals())

                                    .replace("{first_assists}", assists.get(0).getUsername())
                                    .replace("{first_assists_count}", ""+(int)assists.get(0).getAssists())
                                    .replace("{second_assists}", assists.get(1).getUsername())
                                    .replace("{second_assists_count}", ""+(int)assists.get(1).getAssists())
                                    .replace("{third_assists}", assists.get(2).getUsername())
                                    .replace("{third_assists_count}", ""+(int)assists.get(2).getAssists())

                                    .replace("{first_wins}", wins.get(0).getUsername())
                                    .replace("{first_wins_count}", ""+(int)wins.get(0).getWins())
                                    .replace("{second_wins}", wins.get(1).getUsername())
                                    .replace("{second_wins_count}", ""+(int)wins.get(1).getWins())
                                    .replace("{third_wins}", wins.get(2).getUsername())
                                    .replace("{third_wins_count}", ""+(int)wins.get(2).getWins())

                                    .replace("{first_win_streak}", winstreak.get(0).getUsername())
                                    .replace("{first_win_streak_count}", ""+(int)winstreak.get(0).getBestWinStreak())
                                    .replace("{second_win_streak}", winstreak.get(1).getUsername())
                                    .replace("{second_win_streak_count}", ""+(int)winstreak.get(1).getBestWinStreak())
                                    .replace("{third_win_streak}", winstreak.get(2).getUsername())
                                    .replace("{third_win_streak_count}", ""+(int)winstreak.get(2).getBestWinStreak())
                            );
                        }
                    } else if (args[0].equalsIgnoreCase("team")) {
                        if (args.length < 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamNoArgs")));
                        } else if (args[1].equalsIgnoreCase("1v1") || args[1].equalsIgnoreCase("2v2") || args[1].equalsIgnoreCase("3v3") || args[1].equalsIgnoreCase("4v4")) {
                            if (this.plugin.organization.waitingPlayers.containsKey(p.getName()) || this.plugin.organization.playingPlayers.contains(p.getName())) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("ingameTeam")));
                            } else if (this.plugin.organization.waitingTeamPlayers.contains(p)) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("alreadyIngame")));
                            } else if (this.plugin.organization.team.containsKey(p)) {
                                final String matchType = this.plugin.organization.teamType.get(this.plugin.organization.team.get(p)) + "v" + this.plugin.organization.teamType.get(this.plugin.organization.team.get(p));
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("alreadyHaveRequest").replace("{player}", this.plugin.organization.team.get(p).getName()).replace("{matchType}", "matchType")));
                            } else if (this.plugin.organization.teamReverse.containsKey(p)) {
                                final String matchType = this.plugin.organization.teamType.get(p) + "v" + this.plugin.organization.teamType.get(p);
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("alreadyHaveRequest").replace("{player}", this.plugin.organization.team.get(p).getName()).replace("{matchType}", "matchType")));
                            } else if (args.length < 3) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamNoArgs")));
                            } else if (this.plugin.organization.isOnlinePlayer(args[2])) {
                                final Player player = this.plugin.getServer().getPlayer(args[2]);
                                if (this.plugin.organization.waitingTeamPlayers.contains(player)) {
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("inTeamDeny").replace("{player}", args[2])));
                                } else if (this.plugin.organization.waitingPlayers.containsKey(player.getName()) || this.plugin.organization.playingPlayers.contains(player.getName())) {
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("inGameDeny").replace("{player}", args[2])));
                                } else if (this.plugin.organization.team.containsKey(player)) {
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("playerAlreadyGotRequest").replace("{player}", args[2])));
                                } else if (this.plugin.organization.teamReverse.containsKey(player)) {
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("playerAlreadySentRequest").replace("{player}", args[2])));
                                } else if (p.getName() == player.getName()) {
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamYourself")));
                                } else {
                                    int matchType2 = 2;
                                    if (args[1].equalsIgnoreCase("3v3")) {
                                        matchType2 = 3;
                                    }
                                    if (args[1].equalsIgnoreCase("4v4")) {
                                        matchType2 = 4;
                                    }
                                    if (this.plugin.getConfig().getInt("arenas." + matchType2 + "v" + matchType2 + ".amount") == 0) {
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("no" + matchType2 + "v" + matchType2)));
                                    } else {
                                        this.plugin.organization.team.put(player, p);
                                        this.plugin.organization.teamReverse.put(p, player);
                                        this.plugin.organization.teamType.put(p, matchType2);
                                        player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("wannaBeTeam").replace("{player}", p.getName()).replace("{matchType}", matchType2 + "v" + matchType2)));
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamResponse")));
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamRequestSent").replace("{player}", player.getName()).replace("{matchType}", matchType2 + "v" + matchType2)));
                                    }
                                }
                            } else {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("playerOffline").replace("{player}", args[2])));
                            }
                        } else if (args[1].equalsIgnoreCase("cancel")) {
                            if (this.plugin.organization.teamReverse.containsKey(p)) {
                                final Player player = this.plugin.organization.teamReverse.get(p);
                                player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("requestCanceled").replace("{player}", p.getName())));
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamCancel")));
                                this.plugin.organization.teamType.remove(p);
                                this.plugin.organization.teamReverse.remove(p);
                                this.plugin.organization.team.remove(player);
                            } else {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("RequestNotSent")));
                            }
                        } else if (args[1].equalsIgnoreCase("accept")) {
                            if (this.plugin.organization.team.containsKey(p)) {
                                final Player player = this.plugin.organization.team.get(p);
                                if (this.plugin.organization.teamType.get(player) == 2) {
                                    this.plugin.organization.waitingPlayers.put(p.getName(), 2);
                                    this.plugin.organization.waitingPlayers.put(player.getName(), 2);
                                    if (!this.plugin.organization.matches2v2[this.plugin.organization.lobby2v2].team(p, player)) {
                                        this.plugin.organization.waitingPlayers.remove(p.getName());
                                        this.plugin.organization.waitingPlayers.remove(player.getName());
                                        this.plugin.organization.waitingTeams = this.plugin.organization.extendArray(this.plugin.organization.waitingTeams, new Player[]{p, player});
                                        this.plugin.organization.waitingTeamPlayers.add(p);
                                        this.plugin.organization.waitingTeamPlayers.add(player);
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", player.getName())));
                                        player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", p.getName())));
                                    }
                                } else if (this.plugin.organization.teamType.get(player) == 3) {
                                    this.plugin.organization.waitingPlayers.put(p.getName(), 3);
                                    this.plugin.organization.waitingPlayers.put(player.getName(), 3);
                                    if (!this.plugin.organization.matches3v3[this.plugin.organization.lobby3v3].team(p, player)) {
                                        this.plugin.organization.waitingPlayers.remove(p.getName());
                                        this.plugin.organization.waitingPlayers.remove(player.getName());
                                        this.plugin.organization.waitingTeams = this.plugin.organization.extendArray(this.plugin.organization.waitingTeams, new Player[]{p, player});
                                        this.plugin.organization.waitingTeamPlayers.add(p);
                                        this.plugin.organization.waitingTeamPlayers.add(player);
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", player.getName())));
                                        player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", p.getName())));
                                    }
                                } else if (this.plugin.organization.teamType.get(player) == 4) {
                                    this.plugin.organization.waitingPlayers.put(p.getName(), 4);
                                    this.plugin.organization.waitingPlayers.put(player.getName(), 4);
                                    if (!this.plugin.organization.matches4v4[this.plugin.organization.lobby4v4].team(p, player)) {
                                        this.plugin.organization.waitingPlayers.remove(p.getName());
                                        this.plugin.organization.waitingPlayers.remove(player.getName());
                                        this.plugin.organization.waitingTeams = this.plugin.organization.extendArray(this.plugin.organization.waitingTeams, new Player[]{p, player});
                                        this.plugin.organization.waitingTeamPlayers.add(p);
                                        this.plugin.organization.waitingTeamPlayers.add(player);
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", player.getName())));
                                        player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", p.getName())));
                                    }
                                } else {
                                    this.plugin.organization.waitingPlayers.put(p.getName(), 5);
                                    this.plugin.organization.waitingPlayers.put(player.getName(), 5);
                                    if (!this.plugin.organization.matches5v5[this.plugin.organization.lobby5v5].team(p, player)) {
                                        this.plugin.organization.waitingPlayers.remove(p.getName());
                                        this.plugin.organization.waitingPlayers.remove(player.getName());
                                        this.plugin.organization.waitingTeams = this.plugin.organization.extendArray(this.plugin.organization.waitingTeams, new Player[]{p, player});
                                        this.plugin.organization.waitingTeamPlayers.add(p);
                                        this.plugin.organization.waitingTeamPlayers.add(player);
                                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", player.getName())));
                                        player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamed").replace("{player}", p.getName())));
                                    }
                                }
                                this.plugin.organization.team.remove(p);
                                this.plugin.organization.teamReverse.remove(player);
                                this.plugin.organization.teamType.remove(player);
                            } else {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("noRequest")));
                            }
                        } else if (args[1].equalsIgnoreCase("decline")) {
                            if (this.plugin.organization.team.containsKey(p)) {
                                final Player player = this.plugin.organization.team.get(p);
                                player.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("requestDeny").replace("{player}", p.getName())));
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("requestDenySuccess")));
                                this.plugin.organization.teamType.remove(player);
                                this.plugin.organization.teamReverse.remove(player);
                                this.plugin.organization.team.remove(p);
                            } else {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("noRequestToDeny")));
                            }
                        } else {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("teamNoArgs")));
                        }
                    } else if (args[0].equalsIgnoreCase("takeplace")) {
                        if (this.plugin.organization.leftMatches.length > 0) {
                            if (this.plugin.organization.waitingPlayers.containsKey(p.getName()) || this.plugin.organization.playingPlayers.contains(p.getName())) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("alreadyIngame")));
                            } else {
                                this.plugin.organization.leftMatches[0].takePlace(p);
                                this.plugin.organization.playingPlayers.add(p.getName());
                                final Match[] newL = new Match[this.plugin.organization.leftMatches.length - 1];
                                final boolean[] newB = new boolean[this.plugin.organization.leftMatches.length - 1];
                                for (int i2 = 0; i2 < newL.length; ++i2) {
                                    newL[i2] = this.plugin.organization.leftMatches[i2 + 1];
                                    newB[i2] = this.plugin.organization.leftPlayerIsRed[i2 + 1];
                                }
                                this.plugin.organization.leftMatches = newL;
                                this.plugin.organization.leftPlayerIsRed = newB;
                            }
                        } else {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("noTkp")));
                        }
                    } else if (args[0].equalsIgnoreCase("stats")) {
                        if (args.length>1) {
                            String username = (args[1].length()<=0 ? p.getName() : args[1]);
                            this.plugin.organization.checkStats(username, p);
                        } else {
                            this.plugin.organization.checkStats(p.getName(), p);
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (this.plugin.organization.waitingPlayers.containsKey(p.getName())) {
                            if (this.plugin.organization.waitingPlayers.get(p.getName()) == 1) {
                                this.plugin.organization.matches1v1[this.plugin.organization.lobby1v1].leave(p);
                                this.plugin.organization.waitingPlayers.remove(p.getName());
                                int team = -1;
                                for (int j2 = 0; j2 < this.plugin.organization.waitingTeams.length; ++j2) {
                                    if (this.plugin.organization.waitingTeams[j2].length > 0) {
                                        team = j2;
                                        break;
                                    }
                                }
                                if (team > -1 && this.plugin.organization.matches1v1[this.plugin.organization.lobby1v1].team(this.plugin.organization.waitingTeams[team][0], this.plugin.organization.waitingTeams[team][1])) {
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][0]);
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][1]);
                                    this.plugin.organization.reduceArray(this.plugin.organization.waitingTeams, this.plugin.organization.waitingTeams[team][0]);
                                }
                            } else if (this.plugin.organization.waitingPlayers.get(p.getName()) == 2) {
                                this.plugin.organization.matches2v2[this.plugin.organization.lobby2v2].leave(p);
                                this.plugin.organization.waitingPlayers.remove(p.getName());
                                for (int j3 = 0; j3 < this.plugin.organization.waitingTeams.length && this.plugin.organization.waitingTeams[j3].length <= 1; ++j3) {
                                }
                            } else if (this.plugin.organization.waitingPlayers.get(p.getName()) == 3) {
                                this.plugin.organization.matches3v3[this.plugin.organization.lobby3v3].leave(p);
                                this.plugin.organization.waitingPlayers.remove(p.getName());
                                int team = -1;
                                for (int j2 = 0; j2 < this.plugin.organization.waitingTeams.length; ++j2) {
                                    if (this.plugin.organization.waitingTeams[j2].length > 2) {
                                        team = j2;
                                        break;
                                    }
                                }
                                if (team > -1 && this.plugin.organization.matches3v3[this.plugin.organization.lobby3v3].team(this.plugin.organization.waitingTeams[team][0], this.plugin.organization.waitingTeams[team][1])) {
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][0]);
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][1]);
                                    this.plugin.organization.reduceArray(this.plugin.organization.waitingTeams, this.plugin.organization.waitingTeams[team][0]);
                                }
                            } else if (this.plugin.organization.waitingPlayers.get(p.getName()) == 4) {
                                if (this.plugin.organization.lobby4v4 < this.plugin.organization.matches4v4.length) {
                                    this.plugin.organization.matches4v4[this.plugin.organization.lobby4v4].leave(p);
                                }
                                this.plugin.organization.waitingPlayers.remove(p.getName());
                                int team = -1;
                                for (int j2 = 0; j2 < this.plugin.organization.waitingTeams.length; ++j2) {
                                    if (this.plugin.organization.waitingTeams[j2].length < 3) {
                                        team = j2;
                                        break;
                                    }
                                }
                                if (team > -1 && this.plugin.organization.matches4v4[this.plugin.organization.lobby4v4].team(this.plugin.organization.waitingTeams[team][0], this.plugin.organization.waitingTeams[team][1])) {
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][0]);
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][1]);
                                    this.plugin.organization.reduceArray(this.plugin.organization.waitingTeams, this.plugin.organization.waitingTeams[team][0]);
                                }
                            } else {
                                if (this.plugin.organization.lobby5v5 < this.plugin.organization.matches5v5.length) {
                                    this.plugin.organization.matches5v5[this.plugin.organization.lobby5v5].leave(p);
                                }
                                this.plugin.organization.waitingPlayers.remove(p.getName());
                                int team = -1;
                                for (int j2 = 0; j2 < this.plugin.organization.waitingTeams.length; ++j2) {
                                    if (this.plugin.organization.waitingTeams[j2].length < 3) {
                                        team = j2;
                                        break;
                                    }
                                }
                                if (team > -1 && this.plugin.organization.matches5v5[this.plugin.organization.lobby5v5].team(this.plugin.organization.waitingTeams[team][0], this.plugin.organization.waitingTeams[team][1])) {
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][0]);
                                    this.plugin.organization.waitingTeamPlayers.remove(this.plugin.organization.waitingTeams[team][1]);
                                    this.plugin.organization.reduceArray(this.plugin.organization.waitingTeams, this.plugin.organization.waitingTeams[team][0]);
                                }
                            }
                        } else if (!this.plugin.organization.playingPlayers.contains(p.getName())) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("notIngame")));
                        } else if (this.plugin.organization.playingPlayers.contains(p.getName())) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("cantLeave")));
                        }
                    } else if (args[0].equalsIgnoreCase("undo") && this.plugin.organization.setupGuy == p.getName()) {
                        this.plugin.organization.setupGuy = null;
                        this.plugin.organization.setupType = 0;
                        this.plugin.organization.setupLoc = null;
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("undo")));
                    } else if (args[0].equalsIgnoreCase("setuparena") && p.hasPermission("footcube.admin")) {
                        if (this.plugin.organization.setupGuy == null) {
                            if (args.length < 1) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("arenaNotSpecified")));
                            } else {
                                if (args[1].equalsIgnoreCase("1v1")) {
                                    this.plugin.organization.setupType = 1;
                                } else if (args[1].equalsIgnoreCase("2v2")) {
                                    this.plugin.organization.setupType = 2;
                                } else if (args[1].equalsIgnoreCase("3v3")) {
                                    this.plugin.organization.setupType = 3;
                                } else if (args[1].equalsIgnoreCase("4v4")) {
                                    this.plugin.organization.setupType = 4;
                                } else if (args[1].equalsIgnoreCase("5v5")) {
                                    this.plugin.organization.setupType = 5;
                                } else {
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("invalidArenaType")));
                                }
                                if (this.plugin.organization.setupType > 0) {
                                    this.plugin.organization.setupGuy = p.getName();
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("arenaCreation")));
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("arenaCreation1")));
                                    p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("arenaCreation2")));
                                }
                            }
                        } else {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("alreadyInSetup")));
                        }
                    } else if (args[0].equalsIgnoreCase("glove") && p.hasPermission("footcube.glove")) {
/*                        ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName("GLOVE");
                        item.setItemMeta(meta);
                        p.getInventory().addItem(item);*/
                    } else if (args[0].equalsIgnoreCase("cleararenas") && p.hasPermission("footcube.admin")) {
                        final FileConfiguration cfg = this.plugin.getConfig();
                        cfg.set("arenas", (Object) null);
                        cfg.addDefault("arenas.1v1.amount", (Object) 0);
                        cfg.addDefault("arenas.2v2.amount", (Object) 0);
                        cfg.addDefault("arenas.3v3.amount", (Object) 0);
                        cfg.addDefault("arenas.4v4.amount", (Object) 0);
                        cfg.options().copyDefaults(true);
                        this.plugin.saveConfig();
                        this.plugin.organization.matches1v1 = new Match[0];
                        this.plugin.organization.matches2v2 = new Match[0];
                        this.plugin.organization.matches3v3 = new Match[0];
                        this.plugin.organization.matches4v4 = new Match[0];
                        this.plugin.organization.matches5v5 = new Match[0];
                        p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("arenaClearSuccess")));
                    } else if (args[0].equalsIgnoreCase("autohitter") && p.hasPermission("footcube.admin")) {
                        if (args.length < 2) {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("autoHitterUsage")));
                        } else if (args[1].equalsIgnoreCase("on")) {
                            if (this.plugin.autohitter.contains(p.getName())) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("autoAlreadyHitterOn")));
                            }
                            this.plugin.autohitter.add(p.getName());
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("autoHitterOn")));
                        } else if (args[1].equalsIgnoreCase("off")) {
                            if (!this.plugin.autohitter.contains(p.getName())) {
                                p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("autoHitterAlreadyOff")));
                            }
                            this.plugin.autohitter.remove(p.getName());
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("autoHitterOff")));
                        } else {
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("autoHitterUsage")));
                        }
                    } else if(args[0].equalsIgnoreCase("help")) {
                        if(args.length<2) {
                            List<String> help = MessagesConfig.get().getStringList("help");
                            for (String e : help) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', e));
                            }
                        } else {
                            if (args[1].equalsIgnoreCase("admin")) {
                                if (p.hasPermission("footcube.admin")) {
                                    List<String> adminHelp = MessagesConfig.get().getStringList("adminHelp");
                                    for (String e : adminHelp) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', e));
                                    }
                                }
                            } else if (args[1].equalsIgnoreCase("manager")) {
                                if (p.hasPermission("footcube.manager")) {
                                    List<String> adminHelp = MessagesConfig.get().getStringList("managerHelp");
                                    for (String e : adminHelp) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', e));
                                    }
                                }
                            } else {
                                List<String> help = MessagesConfig.get().getStringList("help");
                                for (String e : help) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', e));
                                }
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("set") && this.plugin.organization.setupGuy == p.getName()) {
                        if (this.plugin.organization.setupLoc == null) {
                            this.plugin.organization.setupLoc = p.getLocation();
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("firstLocation")));
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("secondLocation")));
                        } else {
                            final FileConfiguration cfg = this.plugin.getConfig();
                            final String v = this.plugin.organization.setupType + "v" + this.plugin.organization.setupType;
                            final int arena = cfg.getInt("arenas." + v + ".amount") + 1;
                            final String blue = "arenas." + v + "." + arena + ".blue.";
                            final String red = "arenas." + v + "." + arena + ".red.";
                            final Location b2 = this.plugin.organization.setupLoc;
                            final Location r = p.getLocation();
                            cfg.set("arenas." + v + ".amount", (Object) arena);
                            cfg.set("arenas.world", (Object) p.getWorld().getName());
                            cfg.set(blue + "x", (Object) b2.getX());
                            cfg.set(blue + "y", (Object) b2.getY());
                            cfg.set(blue + "z", (Object) b2.getZ());
                            cfg.set(blue + "pitch", (Object) b2.getPitch());
                            cfg.set(blue + "yaw", (Object) b2.getYaw());
                            cfg.set(red + "x", (Object) r.getX());
                            cfg.set(red + "y", (Object) r.getY());
                            cfg.set(red + "z", (Object) r.getZ());
                            cfg.set(red + "pitch", (Object) r.getPitch());
                            cfg.set(red + "yaw", (Object) r.getYaw());
                            cfg.set("arenas." + v + "." + arena + ".name", "null");
                            this.plugin.saveConfig();
                            this.plugin.organization.addArena(this.plugin.organization.setupType, b2, r);
                            this.plugin.organization.setupGuy = null;
                            this.plugin.organization.setupType = 0;
                            this.plugin.organization.setupLoc = null;
                            p.sendMessage(this.pluginString + ChatColor.translateAlternateColorCodes('&', MessagesConfig.get().getString("arenaSetupSuccess")));
                        }
                    } else {
                        success = false;
                    }
                    if (!success) {
                        p.sendMessage("");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bq&3Footcube &7version &b0.2.7-beta"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Created by: &bqajic"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Credits to: &bSmileyCraft"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Contact: &bcontact@ivangajic.com"));
                        p.sendMessage("");
                    }
                }
            }
        }
        return false;
    }
}
