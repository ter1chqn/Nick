package me.teri;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import net.minecraft.util.org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;


public class Main
        extends JavaPlugin
        implements Listener
{
    public static Main plugin;

    public static Main getPlugin()
    {
        return plugin;
    }

    public ArrayList<String> skins = new ArrayList<>();
    public ArrayList<Player> nicked = new ArrayList<>();
    public ArrayList<String> random_nicked = new ArrayList<>();
    public Map<String,String> a = new HashMap<>();

    public void onEnable()
    {
        getCommand("nick").setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);

        this.skins.add("9cc5da5557ef47109179dc53eb92cc04");
        this.skins.add("c322d99c6900489d8c676a98e12bbc1b");
        this.skins.add("48285d2b45c046cb946688ecc8ebeb3f");
        this.skins.add("2fb8797b74124d7a80f2eb7d64999031");
        this.skins.add("069a79f444e94726a5befca90e38aaf5");
        System.out.println("[Nick] Plugin Enabled");
    }

    public void onDisable()
    {
        System.out.println("[Nick] Plugin Disabled");
    }

    public static void setNameTag(String name, Player player)
    {
        try
        {
            Method getHandle = player.getClass().getMethod("getHandle");
            try
            {
                Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
            }
            catch (ClassNotFoundException e)
            {
                Bukkit.broadcastMessage("§c1.7以外で使うな!");
                return;
            }
            Object profile = getHandle.invoke(player, new Object[0]).getClass()
                    .getMethod("getProfile", new Class[0])
                    .invoke(getHandle.invoke(player, new Object[0]), new Object[0]);
            Field ff = profile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(profile, name);
            for (Player players : Bukkit.getServer().getOnlinePlayers())
            {
                players.hidePlayer(player);
                players.showPlayer(player);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if ((sender instanceof Player)) {
            if (p.getPlayer().hasPermission("nick.use")) {
                Skin skin;
                if (args.length == 0) {
                    Random r = new Random();
                    String randomNick = "";
                    randomNick = RandomStringUtils.random(8,"0123456789abcdefghijklmnopqrstuvwxyz");;

                    String nick = randomNick;

                    a.put(p.getName(), randomNick);

                    p.setCustomName(p.getName());

                    setNameTag(nick, p);

                    GameProfile gp = ((CraftPlayer) p).getProfile();
                    gp.getProperties().clear();

                    Collections.shuffle(this.skins);

                    skin = new Skin(this.skins.get(0));
                    if (skin.getSkinName() != null) {
                        gp.getProperties().put(skin.getSkinName(),
                                new Property(skin.getSkinName(), skin
                                        .getSkinValue(), skin
                                        .getSkinSignatur()));
                    }
                    for (Player a : Bukkit.getServer().getOnlinePlayers()) {
                        a.hidePlayer(p);
                    }
                    for (Player a : Bukkit.getServer().getOnlinePlayers()) {
                        a.showPlayer(p);
                    }

                    p.setDisplayName(nick);
                    p.setPlayerListName(nick);

                    p.sendMessage("Name seted " + randomNick);
                }
                if (args.length == 1) {
                    if (!p.getPlayer().hasPermission("nick.other")) {
                        return false;
                    }

                    String nick = args[0];

                    if(this.nicked.contains(nick)) {
                        return true;
                    }

                    skin = new Skin(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString().replaceAll("-",""));

                    a.put(p.getName(), nick);

                    p.setCustomName(p.getName());

                    setNameTag(nick, p);

                    GameProfile gp = ((CraftPlayer) p).getProfile();
                    gp.getProperties().clear();

                    Collections.shuffle(this.skins);

                    if (skin.getSkinName() != null) {
                        gp.getProperties().put(skin.getSkinName(),
                                new Property(skin.getSkinName(), skin
                                        .getSkinValue(), skin
                                        .getSkinSignatur()));
                    }


                    for (Player a : Bukkit.getServer().getOnlinePlayers()) {
                        a.hidePlayer(p);
                    }
                    for (Player a : Bukkit.getServer().getOnlinePlayers()) {
                        a.showPlayer(p);
                    }

                    p.setDisplayName(nick);
                    p.setPlayerListName(nick);

                    p.sendMessage("Name seted " + args[0]);
                }
            }
        }
        return true;
    }
}