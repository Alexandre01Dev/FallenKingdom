package be.alexandre01.fk.teams.waiting.commands;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import be.alexandre01.fk.teams.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ForceTeam extends Command {
    private WaitingSession session;
    public ForceTeam(String name, WaitingSession waitingSession) {
        super(name);
        this.session = waitingSession;
    }

    @Override
    public boolean execute(CommandSender sender, String msg, String[] args) {
        if(!sender.isOp()) return false;
        if(args.length < 3){
            sender.sendMessage("Ota tu es méchant tu as oublié la commande, tu as donc fait une erreur");
            sender.sendMessage("/forceTeam <team> <OfflinePlayer>");
        }

        String teamName = args[1];
        String playerName = args[2];

        Team team = FKPlugin.instance.getTeams().getTeamsByName().get(teamName);
        if(team == null){
            sender.sendMessage("Cette équipe n'existe pas");
            return false;
        }
        session.getToOverride().put(playerName, team);

        sender.sendMessage("Le joueur " + playerName + " a été forcé dans l'équipe " + teamName);


        return true;
    }
}
