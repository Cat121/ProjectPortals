package com.gmail.trentech.pjp.commands.portal;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.Resource;
import com.gmail.trentech.pjp.portals.CuboidBuilder;
import com.gmail.trentech.pjp.portals.LocationType;

public class CMDCube implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!(src instanceof Player)){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Must be a player"));
			return CommandResult.empty();
		}
		Player player = (Player) src;
		
		if(!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/portal button <world> [x] [y] [z]"));
			return CommandResult.empty();
		}
		String worldName = Resource.getBaseName(args.<String>getOne("name").get());

		if(worldName.equalsIgnoreCase("remove")){
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Right click the Cuboid to remove"));
			CuboidBuilder.getActiveBuilders().put((Player) src, new CuboidBuilder());
			return CommandResult.success();
		}
		
		if(!Main.getGame().getServer().getWorld(worldName).isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, Resource.getPrettyName(worldName), " does not exist"));
			return CommandResult.empty();
		}
		World world = Main.getGame().getServer().getWorld(worldName).get();

		Location<World> location = null;
		LocationType locationType;
		
		if(!args.hasAny("coords")) {
			locationType = LocationType.SPAWN;
		}else{
			String coords = args.<String>getOne("coords").get();
			if(coords.equalsIgnoreCase("random")){
				locationType = LocationType.RANDOM;
			}else{
				locationType = LocationType.NORMAL;
				location = Resource.getLocation(world, coords);
				if(location == null){
					src.sendMessage(Text.of(TextColors.YELLOW, "/portal cube <world> [x] [y] [z]"));
					return CommandResult.empty();
				}
			}
		}

		CuboidBuilder builder = new CuboidBuilder(world, location, locationType);

		CuboidBuilder.getActiveBuilders().put(player, builder);

		player.sendMessage(Text.of(TextColors.DARK_GREEN, "Right click starting point"));

		return CommandResult.success();
	}

}
