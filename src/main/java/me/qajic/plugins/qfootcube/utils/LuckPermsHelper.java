package me.qajic.plugins.qfootcube.utils;

import me.qajic.plugins.qfootcube.Footcube;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckPermsHelper {
    private final Footcube plugin;
    public final UserManager userManager;
    public final GroupManager groupManager;
    public final String[] permissions = new String[]{"chatcontrol.channel.{team}",
            "chatcontrol.channel.send.{team}",
            "chatcontrol.channel.join.{team}",
            "chatcontrol.channel.join.{team}.write",
            "chatcontrol.channel.join.{team}.read",
            "chatcontrol.channel.autojoin.{team}",
            "chatcontrol.channel.autojoin.{team}.read",
            "chatcontrol.channel.leave.{team}",
            "tab.group.{team}"};

    final private LuckPerms luckPermsAPI;

    public LuckPermsHelper(final Footcube plugin) {
        this.plugin = plugin;
        this.luckPermsAPI = plugin.luckPermsAPI;
        this.userManager = this.luckPermsAPI.getUserManager();
        this.groupManager = this.luckPermsAPI.getGroupManager();
    }

    public User getPlayer(final UUID uniqueId) {
        final CompletableFuture<User> userFuture = this.userManager.loadUser(uniqueId);
        return userFuture.join();
    }

    public boolean playerInGroup(final UUID uniqueId, final String groupName) {
        final User user = getPlayer(uniqueId);
        final Group group = getGroup(groupName);
        return user.getInheritedGroups(user.getQueryOptions()).contains(group);
    }

    public void playerRemoveTeams(final UUID uniqueId) {
        final User user = getPlayer(uniqueId);
        for (final Group group : user.getInheritedGroups(user.getQueryOptions())) {
            final int weight = 100, groupWeight = group.getWeight().isPresent() ? group.getWeight().getAsInt() : 0;
            if (groupWeight == weight) playerRemoveGroup(uniqueId, group.getName());
        }
    }

    public void playerAddGroup(final UUID uniqueId, final String groupName) {
        final InheritanceNode inheritanceNode = InheritanceNode.builder(groupName).build();
        this.userManager.modifyUser(uniqueId, user -> user.data().add(inheritanceNode));
    }

    public void playerRemoveGroup(final UUID uniqueId, final String groupName) {
        final InheritanceNode inheritanceNode = InheritanceNode.builder(groupName).build();
        this.userManager.modifyUser(uniqueId, user -> user.data().remove(inheritanceNode));
    }

    public void playerAddPermission(final UUID uniqueId, final String permission) {
        final PermissionNode permissionNode = PermissionNode.builder(permission).build();
        this.userManager.modifyUser(uniqueId, user -> user.data().add(permissionNode));
    }

    public void playerRemovePermission(final UUID uniqueId, final String permission) {
        final PermissionNode permissionNode = PermissionNode.builder(permission).build();
        this.userManager.modifyUser(uniqueId, user -> user.data().remove(permissionNode));
    }

    public Group getGroup(final String groupName) {
        return this.groupManager.getGroup(groupName);
    }

    public boolean groupHasPermission(final String groupName, final String permission) {
        final Group group = getGroup(groupName);
        return group.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

    public void groupAddPermission(final String groupName, final String permission, final boolean toggle) {
        final PermissionNode permissionNode = PermissionNode.builder(permission).value(toggle).build();
        this.groupManager.modifyGroup(groupName, group -> group.data().add(permissionNode));
    }

    public void groupRemovePermission(final String groupName, final String permission) {
        final PermissionNode permissionNode = PermissionNode.builder(permission).build();
        this.groupManager.modifyGroup(groupName, group -> group.data().remove(permissionNode));
    }

    public boolean groupExists(final String groupName) {
        return getGroup(groupName) != null;
    }
}
