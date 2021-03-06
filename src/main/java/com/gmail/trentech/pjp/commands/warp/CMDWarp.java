package com.gmail.trentech.pjp.commands.warp;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.pjp.portal.Portal;
import com.gmail.trentech.pjp.portal.PortalService;

public class CMDWarp implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("warp").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		if (args.hasAny("name")) {
			if (!(src instanceof Player)) {
				throw new CommandException(Text.of(TextColors.RED, "Must be a player"));
			}
			Player player = ((Player) src);

			Portal portal = args.<Portal>getOne("name").get();

			if (!player.hasPermission("pjp.warps." + portal.getName())) {
				throw new CommandException(Text.of(TextColors.RED, "you do not have permission to warp here"));
			}

			if (args.hasAny("player")) {
				if (!src.hasPermission("pjp.cmd.warp.others")) {
					throw new CommandException(Text.of(TextColors.RED, "you do not have permission to warp others"));
				}

				player = args.<Player>getOne("player").get();
			}

			Sponge.getServiceManager().provide(PortalService.class).get().execute(player, portal);

			return CommandResult.success();
		}

		src.sendMessage(Text.of(TextColors.YELLOW, " /warp <name> [player]"));

		Help.executeList(src, Help.get("warp").get().getChildren());
		src.sendMessage(Text.of(TextColors.YELLOW, "warp <command> --help"));
		
		return CommandResult.success();
	}

}
