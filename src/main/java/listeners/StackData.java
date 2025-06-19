package listeners;

import lombok.Getter;
import lombok.Setter;

public class StackData {
    @Getter @Setter private Object[] obj;
    @Getter @Setter private Undo undo;
    public StackData(Object[] obj, Undo undo) {
        this.obj = obj;
        this.undo = undo;
    }

}
