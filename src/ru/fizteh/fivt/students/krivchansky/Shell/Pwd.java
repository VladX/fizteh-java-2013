
public class Pwd implements Commands {
    
    public String getCommandName() {
        return "pwd";
    }

    public int getArgumentQuantity() {
        return 0;
    }
    
    public void implement(String[] args, Shell.ShellState state) {
        System.out.println(state.getCurDir());
    }
}
