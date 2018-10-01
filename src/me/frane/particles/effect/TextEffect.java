package me.frane.particles.effect;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.frane.particles.util.ImageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Frane on 01-10-2018.
 */
public class TextEffect
{
    private JavaPlugin plugin;
    private Location loc;
    private BufferedImage img;

    private Set<Player> targets;

    private final int stepX = 1, stepY = 1;
    private final float size = 1.0f/5.0f;

    public TextEffect(JavaPlugin plugin, Location loc, String text, Font font)
    {
        this.plugin = plugin;
        this.loc = loc;
        img = ImageUtil.stringToBufferedImage(text, font);
        targets = new HashSet<>();

        run();
    }

    public void addPlayer(Player player)
    {
        targets.add(player);
    }

    public void removePlayer(Player player)
    {
        targets.remove(player);
    }

    public void run()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for(int y = 0 ; y < img.getHeight(); y += stepY)
                {
                    for(int x = 0; x < img.getWidth(); x += stepX)
                    {
                        if(Color.BLACK.getRGB() != img.getRGB(x, y))
                            continue;

                        Vector v = new Vector((float) img.getWidth() / 2 - x,
                                (float) img.getHeight() / 2 - y, 0).multiply(size);
                        loc.add(v);

                        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                        packet.getParticles().write(0, EnumWrappers.Particle.FLAME);
                        packet.getFloat().write(0, (float)loc.getX());
                        packet.getFloat().write(1, (float)loc.getY());
                        packet.getFloat().write(2, (float)loc.getZ());
                        packet.getFloat().write(3, 0.0f);
                        packet.getFloat().write(4, 0.0f);
                        packet.getFloat().write(5, 0.0f);
                        packet.getFloat().write(6, 0.0f);
                        packet.getIntegers().write(0, 1);
                        packet.getBooleans().write(0, true);

                        for(Player p : targets)
                        {
                            try {
                                ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }

                        loc.subtract(v);
                    }
                }


            }
        }.runTaskTimer(plugin, 0, 4);
    }
}
