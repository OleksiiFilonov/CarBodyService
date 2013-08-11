package oleksii.filonov.gui;

/**
 * NOT Thread SAFE!!!
 */
public class ComponentsLocator {

    private static ComponentsLocator instanse;

    private ComponentsLocator() {
    }

    public static ComponentsLocator getInstanse() {
        if(instanse == null) {
            instanse = new ComponentsLocator();
        }
        return instanse;
    }

    private MainTable table;

    public MainTable getTable() {
        return this.table;
    }

    public void setTable(final MainTable table) {
        this.table = table;
    }

}
