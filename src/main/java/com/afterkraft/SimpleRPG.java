package com.afterkraft;

import org.slf4j.Logger;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.projectile.fireball.LargeFireball;
import org.spongepowered.api.entity.projectile.fireball.SmallFireball;
import org.spongepowered.api.event.entity.living.player.PlayerInteractEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.util.event.Order;
import org.spongepowered.api.util.event.Subscribe;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.google.inject.Inject;

@Plugin(id = "com.afterkraft.SimpleFireball",
        name = "SimpleFireball",
        version = "1.0")
public class SimpleRPG {

    @Inject
    private Logger logger;

    @Subscribe(order = Order.POST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Optional<ItemStack> option = player.getItemInHand();

        logger.info("[SF] Going through the if statement!");
        if (option.isPresent()) {
            ItemType itemType = option.get().getItem();
            if (itemType.equals(ItemTypes.STICK)) {
                spawnFireball(player);
            } else if (itemType.equals(ItemTypes.BLAZE_ROD)) {
                spawnLargeFireball(player);
            }
        }
    }

    private void spawnFireball(Player player) {
        World world = player.getWorld();
        Optional<Entity> optional = world.createEntity(EntityTypes.SMALL_FIREBALL,
                player.getLocation().getPosition());

        logger.info("[SF] Attempting to spawn a SMALL_FIREBALL");
        if (optional.isPresent()) {
            optional.get().setVelocity(getVelocity(player, 1.5D));
            SmallFireball fireball = (SmallFireball) optional.get();
            fireball.setShooter(player);
            world.spawnEntity(fireball);
            logger.info("Spawned a SMALL_FIREBALL!");
        }
    }

    private void spawnLargeFireball(Player player)  {
        World world = player.getWorld();
        Optional<Entity> optional = world.createEntity(EntityTypes.FIREBALL,
                player.getLocation().getPosition());

        logger.info("[SF] Attempting to spawn a LARGE_FIREBALL");
        if (optional.isPresent()) {
            optional.get().setVelocity(getVelocity(player, 1));
            LargeFireball fireball = (LargeFireball) optional.get();
            fireball.setShooter(player);
            fireball.setExplosionPower(20);
            world.spawnEntity(fireball);
            logger.info("Spawned a LARGE_FIREBALL!");
        }
    }

    private static Vector3d getVelocity(Player player, double multiplier) {
        double yaw = ((player.getRotation().getX() + 90) % 360);
        double pitch  = ((player.getRotation().getY()) * -1);
        double rotYCos = Math.cos(Math.toRadians(pitch));
        double rotYSin = Math.sin(Math.toRadians(pitch));
        double rotXCos = Math.cos(Math.toRadians(yaw));
        double rotXSin = Math.sin(Math.toRadians(yaw));
        Vector3d velocity = new Vector3d((multiplier * rotYCos) * rotXCos, multiplier * rotYSin, (multiplier * rotYCos) * rotXSin);
        return velocity;
    }

}