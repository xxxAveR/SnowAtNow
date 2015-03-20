package ru.xAveR.SnowAtNow;


import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class SnowAtNowMain extends JavaPlugin implements Listener{
    WorldGuardPlugin wg;
	@Override
    public void onEnable()
    {
    getServer().getPluginManager().registerEvents(this, this);
    
    wg = (WorldGuardPlugin)getServer().getPluginManager().getPlugin("WorldGuard");
    
    ItemStack ice = new ItemStack(Material.ICE);
    ShapedRecipe rIce = new ShapedRecipe(ice);
    rIce.shape(new String[] { "SSS", "SBS", "SSS" });
    rIce.setIngredient('B', Material.WATER_BUCKET);
    rIce.setIngredient('S', Material.SNOW_BLOCK);
    getServer().addRecipe(rIce);
    
    saveDefaultConfig();
  }
	@Override
  public void onDisable() {
		
	}  
  @EventHandler
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
    if ((getConfig().getBoolean("Snowball Damage")) && ((e.getDamager() instanceof Snowball))){
      e.setDamage(1.0);
      if (e.getEntity().getFireTicks() > 0) {
        e.getEntity().setFireTicks(0);
      }
    }
  }
@EventHandler
  public void ProjectileHitEvent(ProjectileHitEvent e){
	
	  Entity s = e.getEntity();
	  Player sh = (Player) e.getEntity().getShooter();
	  if(s instanceof Snowball){
		  BlockIterator i = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
		   Block hB = null;
		  while(i.hasNext()){
			  hB = i.next();
			  if(!(wg.canBuild(sh, hB))){
			  	s.remove();
			  } else {
			  if(hB.getType() != Material.AIR){
				  if(hB.getRelative(BlockFace.UP).getType() != Material.AIR || hB.getType() == Material.SNOW){
					  break;
				  }
				  hB.getRelative(BlockFace.UP).setType(Material.SNOW);
				  break;
			  }
		  }
		  if(hB.getType() == Material.FIRE){
			  hB.setType(Material.AIR);
		  }
		  if(hB.getType() == Material.WATER){
			  hB.setType(Material.ICE);
		  		}
		  	}
		  	} 
	  } 
	@EventHandler
	public void SnowFormEvent(EntityBlockFormEvent e){
		final Block b = e.getBlock();
		Entity en = e.getEntity();
		if(en instanceof Snowman){
		if(b.getType() == Material.SNOW || b.getType() == Material.ICE){
			if(b.getBiome() != Biome.TAIGA ||b.getBiome() != Biome.TAIGA_HILLS||b.getBiome() != Biome.ICE_MOUNTAINS||b.getBiome() != Biome.ICE_PLAINS||b.getBiome() != Biome.FROZEN_OCEAN||b.getBiome() != Biome.FROZEN_RIVER){
				e.setCancelled(true);
				}
			}
		}
	}

}
