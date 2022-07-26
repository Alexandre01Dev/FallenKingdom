package be.alexandre01.fk.sessions.waiting.commands;

import be.alexandre01.fk.FKPlugin;
import be.alexandre01.fk.sessions.waiting.WaitingSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends Command {

    boolean used = false;
    public StartCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String msg, String[] args) {
        if(!sender.isOp())  return false;
        if(used) return false;

        if(args.length < 1){
                WaitingSession waitingSession = FKPlugin.instance.getWaitingSession();
                waitingSession.doStartTimer();
                used = true;
        }else {
            if(args[0].equalsIgnoreCase("force")){
                WaitingSession waitingSession = FKPlugin.instance.getWaitingSession();
                waitingSession.finish();
                used = true;
            }
        }
        return false;
    }
}
