package me.mrscopes.beans.events.listeners

import me.mrscopes.beans.Beans
import me.mrscopes.beans.utilities.Utilities
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Arrow
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.atomic.AtomicLong

class BowListeners : Listener {
    @EventHandler
    fun onShoot(event: EntityShootBowEvent) {
        val shooter = event.entity as? Player ?: return
        /*
        Utilities.whileLoop({ !event.projectile.isDead }, {
            val location = event.projectile.location
            location.world.spawnParticle(Particle.CLOUD, location, 20, 0.5, 0.5, 0.5, 0.0)
        }, 1, 5)

         */
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val arrow = event.entity as? Arrow ?: return

        val location = arrow.location

        val x = AtomicLong(60)
        Utilities.whileLoop({ x.get() > 0 }, {

            location.world.spawnParticle(Particle.SOUL, location, 100, 1.0, 1.0, 1.0)

            val entities = location.getNearbyEntities(5.0, 5.0, 5.0)
            for (entity in entities) {
                if (entity is Player || entity is LivingEntity) {
                    val direction = location.toVector().subtract(entity.location.toVector()).normalize()
                    entity.velocity = direction.multiply(0.2)
                }
            }

            x.set(x.get() - 1)
        }, 1);

        val explosion = object : BukkitRunnable() {
            override fun run() {
                location.createExplosion(5f, false, false)
            }
        }
        explosion.runTaskLater(Beans.instance, 60)

        arrow.remove()

        val shooter = arrow.shooter as? Player ?: return
        val victim = event.hitEntity as? LivingEntity ?: return
    }
}